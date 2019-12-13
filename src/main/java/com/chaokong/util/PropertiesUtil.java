package com.chaokong.util;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 从类路径下获取资源文件并进行读写
 *
 * @author dell
 *
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Map<String, Properties> propsMap = Maps.newConcurrentMap();
    /**
     * 初始化Properties实例
     *
     * @param propertyName
     * @throws Exception
     */
    public synchronized static void initProperty(String propertyName) throws Exception {
        if(propsMap.containsKey(propertyName)) return;
        Properties prop = new Properties();
        InputStream inputstream = null;
        ClassLoader cl = PropertiesUtil.class.getClassLoader();
        if (cl != null) {
            inputstream = cl.getResourceAsStream(propertyName);
        } else {
            inputstream = ClassLoader.getSystemResourceAsStream(propertyName);
        }

        if (inputstream == null) {
            throw new Exception("inputstream " + propertyName + " open null");
        }
        prop.load(inputstream);
        propsMap.put(propertyName, prop);
        inputstream.close();
        inputstream = null;
    }

    /**
     * 读取数据
     *
     * @param propertyName
     * @param key
     * @return
     */
    public static String getValueByKey(String propertyName, String key) {
        return getValueByKey(propertyName, key, "");
    }

    /**
     * 读取数据
     *
     * @param propertyName
     * @param key
     * @param defaultValue 当key不在 properyName 对应的.properties 中时，默认的返回值
     * @return
     */
    public static String getValueByKey(String propertyName, String key, String defaultValue) {
        String result = defaultValue;
        try {
            initProperty(propertyName);
            Properties prop = propsMap.get(propertyName);
            if(prop != null) {
                result = prop.getProperty(key);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("from '" + propertyName + ".properties' get key '" + key + "' has error.", e);
            return result;
        }
    }

    public static void  main(String[] args) {
        System.out.println(PropertiesUtil.getValueByKey("kafka.properties", "kafka.url"));
    }
}