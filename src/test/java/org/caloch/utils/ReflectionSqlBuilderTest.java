package org.caloch.utils;

import org.caloch.beans.BaseTypeBean2;
import org.caloch.beans.Roles;
import org.junit.Test;

import javax.management.relation.Role;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @org.junit.Test
    public void getPresentFields() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        assert Integer.class.equals(((Object) 5).getClass());
        assert Character.class.equals(((Object) 'c').getClass());

        ReflectionSqlBuilder reflectionSqlBuilder = new ReflectionSqlBuilder();
        BaseTypeBean2 b = new BaseTypeBean2();
        Constructor t = b.getClass().getDeclaredConstructor();
        char x = b.getId4();
        b.setName("calo");
//        b.setId(5);
        String se = String.join(" and ", new ArrayList<String>());
        assert se.equals("");
    }

    @Test
    public void UnitedTest() {
        BaseTypeBean2 b = new BaseTypeBean2();
        b.setId5(5.3f);

        BeanDbParser beanDbParser = new BeanDbParser(b, "id7", "id3");
        beanDbParser.parse();

        String tps = "";
        Field[] fields = b.getClass().getDeclaredFields();
        for (Field field : fields) {
            String type = field.getType().getSimpleName();
            String t = type;
            tps += type;
        }
        assert tps.equals("Stringintbyteshortlongcharfloatdoublebooleanbyte");

        HashMap<String, Map.Entry<String, Object>> h = beanDbParser.getPresentFieldsInfoWithoutId(b);
        for (Map.Entry<String, Map.Entry<String, Object>> it : h.entrySet()) {
            String name = it.getKey();
            Map.Entry<String, Object> valo = it.getValue();
            String type = valo.getKey();
            Object val = valo.getValue();
            if (type.equals("float")) {
                float f = (float) val;
            }
        }


        char x = b.getId4();
        b.setId(2);
        b.setId4('c');
        short a1 = 4;
        b.setId2(a1);
        b.setName("calo");
        b.setId7(false);
        String sql = beanDbParser.buildSelectSqlTemplate();
        String sql1 = beanDbParser.buildInsertSqlTemplate();
        String sql2 = beanDbParser.buildDeleteSqlTemplate();
        String sql3 = beanDbParser.buildUpdateSqlTemplate();

        assert true;
    }

    @Test
    public void testDb() throws SQLException {
        MySqlDbContext ctx = new MySqlDbContext(dbURL, username, password);
        ctx.connect();
        Roles r = new Roles();
        r.setName("chen");
        r = ctx.insert(r);
        ctx.commit();
    }

    @Test
    public void updateTest() throws SQLException {

        MySqlDbContext ctx = new MySqlDbContext(dbURL, username, password);
        ctx.connect();
        Roles r = new Roles();
        r.setId(1);
        r.setName("chen.tao");
        r = ctx.single(r);
        ctx.delete(r);

        ctx.commit();
    }
}