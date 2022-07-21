package org.caloch.utils;

import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.caloch.core.Entity;

import java.sql.*;
import java.util.ArrayList;
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
        BeanDbParser b = new BeanDbParser(bean);
        b.parse();
        String dropSql = b.createBuildDropTableSql();
        executeSql(dropSql);
        String sql = b.createBuildCreateTableSql();
        executeSql(sql);
    }

    public Connection conn;

    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            rollback();
            throw new RuntimeException(e);
        }
    }

    public void commit(boolean closeConn) {
        try {
            conn.commit();
            if (closeConn)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            rollback();
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conne = DriverManager.getConnection(dbURL, username, password);
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
        Statement st = conn.createStatement();
        Object ret = st.execute(sql);
        st.close();
//        if (ret.getClass().isAssignableFrom(ResultSet.class)) ((ResultSet) ret).close();
        return ret;
    }

    public <T extends Entity> T single(T bean, String... forceInclude) throws SQLException {
        return select(bean, forceInclude).get(0);
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
