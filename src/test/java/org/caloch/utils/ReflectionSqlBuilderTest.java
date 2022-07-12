package org.caloch.utils;

import org.caloch.beans.BaseTypeBean2;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ReflectionSqlBuilderTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getPresentFields() throws InvocationTargetException, IllegalAccessException {
        assert Integer.class.equals(((Object) 5).getClass());
        assert Character.class.equals(((Object) 'c').getClass());

        ReflectionSqlBuilder reflectionSqlBuilder = new ReflectionSqlBuilder();
        BaseTypeBean2 b = new BaseTypeBean2();
        char x = b.getId4();
        b.setName("calo");
//        b.setId(5);
        String se = String.join(" and ", new ArrayList<String>());
        assert se.equals("");
    }

    @Test
    public void UnitedTest() throws InvocationTargetException, IllegalAccessException {
        ReflectionSqlBuilder reflectionSqlBuilder = new ReflectionSqlBuilder();
        BaseTypeBean2 b = new BaseTypeBean2();
        char x = b.getId4();
        b.setId(2);
        b.setName("calo");
        b.setId7(false);
        String sql = reflectionSqlBuilder.createSelectSql(b);
        String sql1 = reflectionSqlBuilder.createUpdateSql(b);
        String sql2 = reflectionSqlBuilder.createInsertSql(b);
        String sql3 = reflectionSqlBuilder.createDeleteSql(b);
        assert sql.equals("select name,id7 from basetypebean2 where 1=1  and name='calo' and id7='false'");
    }
}