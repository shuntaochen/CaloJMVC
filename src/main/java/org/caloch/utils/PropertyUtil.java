package org.caloch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    /**
     * properties
     */
    private Properties properties = null;

    /**
     * 根据key获取value值
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        if (properties == null) {
            properties = loadConfProperties();
        }
        String value = properties.getProperty(key);
        System.out.println("从配置文件读取参数： " + key + " -->> " + value);
        return value;
    }

    public String getMailAccount(){
        return getValue("mailaccount");
    }
    public String getMailKey(){
        return getValue("mailkey");
    }

    public String getDbUrl(){
        return getValue("db");
    }
    public String getDbUser(){
        return getValue("dbuser");
    }
    public String getDbPassword(){
        return getValue("dbpassword");
    }
    /**
     * 初始化propertiies
     *
     * @return
     */
    public Properties loadConfProperties() {
        Properties properties = new Properties();
        InputStream in = null;

        // 优先从项目路径获取连接信息
        String confPath = System.getProperty("user.dir");
        confPath = confPath + File.separator + "calo.properties";
        File file = new File(confPath);
        if (file.exists()) {
            System.out.println("配置文件路径---->>" + confPath);
            try {
                in = new FileInputStream(new File(confPath));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // 未传入路径时，读取classpath路径
        else {
            System.out.println("项目路径[" + confPath + "]下无连接信息，从classpath路径下加载");
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("calo.properties");
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}