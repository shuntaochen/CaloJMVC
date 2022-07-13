package org.caloch.utils;

import com.mysql.jdbc.ResultSet;
import org.caloch.core.Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MysqlObjectHelper {


    public MysqlObjectHelper() {

    }

    private Connection conn;

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public void connect() {
        String dbURL = "jdbc:mysql://localhost:3306/sampledb";
        String username = "root";
        String password = "cst";
        try {
            Connection conne = DriverManager.getConnection(dbURL, username, password);
            conne.setAutoCommit(false);
            conn = conne;
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public <T extends Entity> ArrayList<T> select(T bean) throws SQLException, IllegalAccessException {
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean);
        sqlParser.parse();
        String sql = sqlParser.buildSelectSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        preparePreparedStatement(statement, sqlParser.beanInfo);
        ResultSet result = (ResultSet) statement.executeQuery(sql);
        return ResultSetToBeanConverter.getBeans(result, bean.getClass());
    }

    public <T extends Entity> T insert(T bean) throws SQLException {
        BeanDbParser<T> sqlParser = new BeanDbParser<>(bean);
        sqlParser.parse();
        String sql = sqlParser.buildInsertSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        preparePreparedStatement(statement, sqlParser.beanInfo);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new bean was inserted successfully!");
        }
        return bean;
    }

    public <T extends Entity> void update(T bean) throws SQLException {//update

        BeanDbParser parser = new BeanDbParser(bean);
        parser.parse();
        String sql = parser.buildDeleteSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0)
            statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, parser.beanInfo);
        }
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("An existing bean was updated successfully!");
        }
    }

    public <T extends Entity> int delete(T bean) throws SQLException {//delete
        BeanDbParser parser = new BeanDbParser(bean);
        parser.parse();
        String sql = parser.buildDeleteSqlTemplate();
        PreparedStatement statement = conn.prepareStatement(sql);
        if (bean.getId() != 0)
            statement.setInt(1, bean.getId());
        else {
            preparePreparedStatement(statement, parser.beanInfo);
        }
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("A bean was deleted successfully!");
        }
        return rowsDeleted;
    }

    private void preparePreparedStatement(PreparedStatement statement, HashMap<String, Map.Entry<String, Object>> fieldInfoNoId) throws SQLException {
        for (Map.Entry<String, Map.Entry<String, Object>> it : fieldInfoNoId.entrySet()) {
            int index = 1;
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
    }

}
