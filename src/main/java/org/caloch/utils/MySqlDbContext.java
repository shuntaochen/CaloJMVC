package org.caloch.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.caloch.core.Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MySqlDbContext {

    static Logger logger = Logger.getLogger(MySqlDbContext.class);
    private String dbURL;
    private String username;
    private String password;

    public MySqlDbContext(String dbURL, String username, String password) {
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
    }

    public <T extends Entity> void createTable(T bean) throws SQLException {
        createTable(bean, true);
    }

    public <T extends Entity> void createTable(T bean, boolean dropOld) {
        try {
            BeanDbParser b = new BeanDbParser(bean);
            b.parse();
            if (dropOld) {
                String dropSql = b.createBuildDropTableSql();
                executeSql(dropSql);
            }
            String sql = b.createBuildCreateTableSql();
            executeSql(sql);
        } catch (SQLException e) {
            System.out.println("Table for " + bean.getClass().getTypeName() + " might exist;");
        }
    }

    private ArrayList<String[]> getTableInfo(String tableName, ArrayList<String[]> rows) {
        ArrayList<String[]> ret = new ArrayList<>();
        for (String[] row : rows) {
            if (row[0].equals(tableName)) {
                ret.add(row);
            }
        }
        return ret;
    }

    public void createBeansForDb(String dbName) throws SQLException, IOException {
        String dbInfoSql = "select TABLE_NAME,column_name,DATA_TYPE,column_comment from information_schema.COLUMNS where TABLE_SCHEMA='" + dbName + "'";
        ResultSet rs = (ResultSet) this.executeSql(dbInfoSql);
        HashMap<String, ArrayList<String[]>> tableInfo = new HashMap<>();
        ArrayList<String[]> rows = new ArrayList<>();
        ArrayList<String> tableNames = new ArrayList<>();
        while (rs.next()) {
            String[] row = new String[4];
            row[0] = rs.getString(1);
            if (!tableNames.contains(row[0])) {
                tableNames.add(row[0]);
            }
            row[1] = rs.getString(2);
            row[2] = rs.getString(3);
            row[3] = rs.getString(4);
            rows.add(row);
        }
        for (String tableName : tableNames){
            tableInfo.put(tableName,getTableInfo(tableName,rows));
        }

        DbBeanTemplateBuilder beanTemplateBuilder =new DbBeanTemplateBuilder();
        for(Map.Entry<String,ArrayList<String[]>> tbl:tableInfo.entrySet()){
            StringBuilder sb=new StringBuilder();
            sb.append(beanTemplateBuilder.header);
            sb.append(beanTemplateBuilder.getClassOpener(tbl.getKey()));
            for(String[] row:tbl.getValue()){
                sb.append(beanTemplateBuilder.getFieldString(row[1],row[2]));
            }
            sb.append(beanTemplateBuilder.classCloser);
            String outputDir=System.getProperty("user.dir")+ File.separator+"generated"+File.separator+"beans";
            File dir=new File(outputDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String fileName=outputDir+File.separator+tbl.getKey()+".java";
            File src=new File(fileName);
            if(src.exists()){
                src.delete();
            }
            src.createNewFile();
            FileWriter fileWriter=new FileWriter(src);
            fileWriter.write(sb.toString());
            System.out.println(fileName);
            fileWriter.flush();
            fileWriter.close();
        }

    }

    public Connection conn;

    public void doCommit() {
        commit(true);
    }

    public void commit(boolean returnConnection) {
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            rollback();
            throw new RuntimeException(e);
        } finally {
            if (returnConnection) {
                try {
                    pool.returnConnection(conn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                pool.returnConnection(conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    MysqlConnectionPool pool;

    public void connect() {
        pool = new MysqlConnectionPool(
                dbURL,
                username, password, 5);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conne = pool.getConnection();
            conne.setAutoCommit(false);
            conn = conne;
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public <T> Object executeSql(String sql) throws SQLException {
        return executeSql(sql, null);
    }


    public <T extends Entity> Object max(String colName, T bean) throws SQLException {
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String sql = b.buildMaxSql(colName);
        Object ret = executeScalar(sql);
        return ret;
    }

    public <T extends Entity> Object count(String colName, T bean) throws SQLException {
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String sql = b.buildCountSql(colName);
        Object ret = executeScalar(sql);
        return ret;
    }

    public <T extends Entity> Object sum(String colName, T bean) throws SQLException {
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String sql = b.buildSumSql(colName);
        Object ret = executeScalar(sql);
        return ret;
    }

    public <T extends Entity> Object min(String colName, T bean) throws SQLException {
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String sql = b.buildMinSql(colName);
        Object ret = executeScalar(sql);
        return ret;
    }

    public <T extends Entity> Object avg(String colName, T bean) throws SQLException {
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String sql = b.buildAvgSql(colName);
        Object ret = executeScalar(sql);
        return ret;
    }


    public Object executeScalar(String sqlTemplate, Object... params) throws SQLException {
        ResultSet rs = (ResultSet) executeQuery(sqlTemplate, null, params);
        rs.next();
        Object ret;
        try {
            ret = rs.getInt(1);

        } catch (Exception e) {
            ret = rs.getString(1);
        }
        closeStatementSet(rs.getStatement(), rs);
        return ret;
    }


    public <T> Object executeQuery(String sqlTemplate, Class dtoClass, Object... params) throws SQLException {
        String sql = String.format(sqlTemplate, params);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (dtoClass != null) {
            ArrayList<T> r = ResultSetToBeanConverter.getBeans(rs, dtoClass);
            closeStatementSet(st, rs);
            return r;
        }
        return rs;
    }

    /*
    Returns: has result set,then convert resultset to arraylist<T>; resultset not closed; updatecount;
     */
    public <T> Object executeSql(String sqlTemplate, Class dtoClass, Object... params) throws SQLException {
        String sql = String.format(sqlTemplate, params);
        Statement st = conn.createStatement();
        boolean success = st.execute(sql);
        if (success) {
            ResultSet r1 = st.getResultSet();
            if (r1 != null) {
                if (dtoClass != null) {
                    ArrayList<T> r = ResultSetToBeanConverter.getBeans(r1, dtoClass);
                    closeStatementSet(st, r1);
                    return r;
                } else {
                    return r1;
                }
            }
        } else {
            int r2 = st.getUpdateCount();
            return r2;
        }
        return null;
    }

    public <T extends Entity> T single(T bean, String... forceInclude) throws SQLException {
        ArrayList<T> ret = select(bean, forceInclude);
        if (!ret.isEmpty()) return ret.get(0);
        return null;
    }

    public <T extends Entity> ArrayList<T> select(T bean, String... forceInclude) throws SQLException {
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean, forceInclude);
        sqlParser.parse();
        String sql = sqlParser.buildSelectSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0) statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, sqlParser.beanInfo);
        }
        ResultSet result = statement.executeQuery();
        ArrayList<T> ret = ResultSetToBeanConverter.getBeans(result, bean.getClass());
        closeStatementSet(statement, result);
        return ret;
    }


    public <T extends Entity> ArrayList<T> selectPageAsc(T bean, String orderCol, int limit, int pageSize, String... forceInclude) throws SQLException {
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean, forceInclude);
        sqlParser.setAscByLimit(orderCol, limit, pageSize);
        sqlParser.parse();
        String sql = sqlParser.buildSelectPageAscSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0) statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, sqlParser.beanInfo);
        }
        ResultSet result = statement.executeQuery();
        ArrayList<T> ret = ResultSetToBeanConverter.getBeans(result, bean.getClass());
        closeStatementSet(statement, result);
        return ret;
    }

    public <T extends Entity> ArrayList<T> selectPageDesc(T bean, String orderCol, int limit, int pageSize, String... forceInclude) throws SQLException {
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean, forceInclude);
        sqlParser.setDescByLimit(orderCol, limit, pageSize);
        sqlParser.parse();
        String sql = sqlParser.buildSelectPageDescSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0) statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, sqlParser.beanInfo);
        }
        ResultSet result = statement.executeQuery();
        ArrayList<T> ret = ResultSetToBeanConverter.getBeans(result, bean.getClass());
        closeStatementSet(statement, result);
        return ret;
    }


    public <T extends Entity> T insert(T bean, String... forceInclude) throws SQLException {
        bean.insertedOn = new java.util.Date().getTime();
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean, forceInclude);
        sqlParser.parse();
        String sql = sqlParser.buildInsertSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparePreparedStatement(statement, sqlParser.beanInfo);
        int rowsInserted = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs != null && rs.next()) {
            bean.setId(rs.getInt(1));
        }
        if (rowsInserted > 0) {
            System.out.println("A new bean was inserted successfully!");
        }
        closeStatementSet(statement, rs);
        return bean;
    }

    public <T extends Entity> int update(T bean, String... forceInclude) throws SQLException {
        bean.updatedOn = new Date().getTime();
        if (bean.getId() == 0) throw new IllegalArgumentException("id for update mandatory.");
        BeanDbParser parser = new BeanDbParser(bean, forceInclude);
        parser.parse();
        String sql = parser.buildUpdateSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        int index = preparePreparedStatement(statement, parser.beanInfo);
        statement.setInt(index, bean.getId());
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("An existing bean was updated successfully!");
        }
        closeStatementSet(statement, null);
        return rowsUpdated;
    }

    public <T extends Entity> int delete(T bean, String... forceInclude) throws SQLException {
        BeanDbParser parser = new BeanDbParser(bean, forceInclude);
        parser.parse();
        String sql = parser.buildDeleteSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0) statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, parser.beanInfo);
        }
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("A bean was deleted successfully!");
        }
        closeStatementSet(statement, null);
        return rowsDeleted;
    }

    private int preparePreparedStatement(PreparedStatement statement, HashMap<String, Map.Entry<String, Object>> fieldInfoNoId) throws SQLException {
        int index = 1;
        for (Map.Entry<String, Map.Entry<String, Object>> it : fieldInfoNoId.entrySet()) {
            Map.Entry<String, Object> valo = it.getValue();
            String type = valo.getKey();
            Object val = valo.getValue();
            if (type.equals("int")) {
                statement.setInt(index, (int) val);
            } else if (type.equals("short")) {
                statement.setShort(index, (short) val);
            } else if (type.equals("String") || type.equals("char")) {
                statement.setString(index, val.toString());
            } else if (type.equals("long")) {
                statement.setLong(index, (long) val);
            } else if (type.equals("boolean")) {
                statement.setBoolean(index, (boolean) val);
            } else if (type.equals("double")) {
                statement.setDouble(index, (double) val);
            } else if (type.equals("float")) {
                statement.setFloat(index, (float) val);
            }
            index++;
        }
        return index;
    }

    private void closeStatementSet(Statement st, ResultSet rs) throws SQLException {
        if (st != null) st.close();
        if (rs != null) rs.close();
    }

}
