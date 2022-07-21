package org.caloch.utils;

import org.caloch.beans.News;
import org.caloch.beans.Roles;
import org.caloch.beans.User;

import java.sql.SQLException;

public class Migrator {

    private String dbUrl;
    private String user;
    private String password;

    public Migrator(String db, String user, String password) {
        this.dbUrl = db;
        this.user = user;
        this.password = password;
    }

    public void run()  {
        MySqlDbContext db=new MySqlDbContext(dbUrl,user,password);
        db.connect();
        try {
//            db.createTable(new Roles());
//            db.createTable(new News());
            db.createTable(new User());
            db.commit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
