package org.caloch.utils;

import org.caloch.beans.Feature;
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

    public void run() {
        MySqlDbContext db = new MySqlDbContext(dbUrl, user, password);
        db.connect();
        try {
//            db.createTable(new Roles());
//            db.createTable(new News());
            db.createTable(new User(), false);

            db.createTable(new Feature(), true);
            seedFeatures(db);

            db.commit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void seedFeatures(MySqlDbContext db) throws SQLException {
        {
            Feature f1 = new Feature();
            f1.name = "SiteComposer";
            f1.code = 1;
            f1.isEnabled = true;
            db.insert(f1);
        }
        {
            int maxCode = (int) db.executeScalar("select max(code) from Feature;");
            Feature f1 = new Feature();
            f1.name = "HomePage";
            f1.code = (maxCode + 1);
            f1.isEnabled = true;
            db.insert(f1);
        }

    }
}
