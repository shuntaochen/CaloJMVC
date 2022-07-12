package org.caloch.utils;

import org.caloch.beans.BaseTypeBean2;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ReflectionHelperTest {

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

        ReflectionHelper reflectionHelper = new ReflectionHelper();
        BaseTypeBean2 b = new BaseTypeBean2();
        char x = b.getId4();
        b.setName("calo");
//        b.setId(5);
        String s1 = String.join(" and ", reflectionHelper.buildPresentFields(b));
        String se = String.join(" and ", new ArrayList<String>());
        assert se.equals("");
        assert s1.equals("name='calo'");
    }

    @Test
    public void UnitedTest() throws InvocationTargetException, IllegalAccessException {
        ReflectionHelper reflectionHelper = new ReflectionHelper();
        BaseTypeBean2 b = new BaseTypeBean2();
        char x = b.getId4();
        b.setId(2);
        b.setName("calo");
        b.setId7(false);
        String sql = reflectionHelper.createSelectSql(b);
        String sql1 = reflectionHelper.createUpdateSql(b);
        String sql2 = reflectionHelper.createInsertSql(b);
        String sql3 = reflectionHelper.createDeleteSql(b);
        assert sql.equals("select name,id7 from basetypebean2 where 1=1  and name='calo' and id7='false'");
    }
}