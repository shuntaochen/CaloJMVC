package org.caloch.utils;

import org.caloch.beans.*;

import java.sql.SQLException;

public class Migrator {

    private String dbUrl;
    private String user;
    private String password;

    public Migrator(PropertyUtil propertyUtil) {
        this.dbUrl = propertyUtil.getDbUrl();
        this.user = propertyUtil.getDbUser();
        this.password = propertyUtil.getDbPassword();
    }

    public void run() {
        MySqlDbContext db = new MySqlDbContext(dbUrl, user, password);
        db.connect();
        try {
            db.createTable(new Roles(),false);
            db.createTable(new News(),false);
            db.createTable(new User(), false);
            db.createTable(new Registration(), false);

            db.createTable(new Feature(), true);
            seedFeatures(db);

            db.doCommit();
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
