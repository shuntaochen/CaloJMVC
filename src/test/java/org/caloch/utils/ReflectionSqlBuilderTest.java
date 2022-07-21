package org.caloch.utils;

import org.caloch.beans.Roles;
import org.junit.Test;

import java.sql.SQLException;

public class ReflectionSqlBuilderTest {

    String dbURL = "jdbc:mysql://localhost:3306/mycms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    String username = "root";
    String password = "cst";

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }


    @Test
    public void testtbcreate() throws SQLException {
        new Migration(dbURL,username,password).run();

    }
}