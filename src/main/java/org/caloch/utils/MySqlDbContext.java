package org.caloch.utils;

import java.sql.ResultSet;

import org.caloch.core.Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.cj.jdbc.Driver;

public class MySqlDbContext {


    public MySqlDbContext() {

    }

    public Connection conn;

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public void connect() {
        String dbURL = "jdbc:mysql://localhost:3306/mycms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = "root";
        String password = "cst";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conne = DriverManager.getConnection(dbURL, username, password);
            conne.setAutoCommit(false);
            conn = conne;
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public Object executeSql(String sql) throws SQLException {
        Statement st = conn.createStatement();
        Object ret = st.execute(sql);
        conn.commit();
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
        return ResultSetToBeanConverter.getBeans(result, bean.getClass());
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

}
