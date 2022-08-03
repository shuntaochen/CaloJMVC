package org.caloch.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.caloch.core.Entity;
import org.caloch.core.Satisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BaseSatisfact<T extends Entity> extends Satisfact {
    public BaseSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    protected void setBeanName(String beanName) {
        this.beanName = "org.caloch.beans." + beanName;
    }

    protected String beanName;

    public T single() throws SQLException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, JsonProcessingException {
        T bean = (T) Class.forName(beanName).getDeclaredConstructor().newInstance();
        bean = inflate(bean);
        return mysqlDbContext.single(bean);
    }

    public ArrayList<T> getAll() throws SQLException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, JsonProcessingException {
        T bean = (T) Class.forName(beanName).getDeclaredConstructor().newInstance();
        bean = inflate(bean);
        return mysqlDbContext.select(bean);
    }

    public int delete() throws SQLException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, JsonProcessingException {
        T bean = (T) Class.forName(beanName).getDeclaredConstructor().newInstance();
        bean = inflate(bean);
        return mysqlDbContext.delete(bean);
    }

    public T insert() throws SQLException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, JsonProcessingException {
        T bean = (T) Class.forName(beanName).getDeclaredConstructor().newInstance();
        bean = inflate(bean);
        return mysqlDbContext.insert(bean);
    }

    public int update() throws SQLException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, JsonProcessingException {
        T bean = (T) Class.forName(beanName).getDeclaredConstructor().newInstance();
        bean = inflate(bean);
        return mysqlDbContext.update(bean);
    }

}
