package ua.bolt.twitterbot.prop;

import twitter4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ackiybolt on 17.02.15.
 */
public enum PropertyHolder {

    INSTANCE;

    private static Logger LOG = Logger.getLogger(PropertyHolder.class);

    private Properties prop;

    public PropertyHolder load() {

        if (prop != null) return INSTANCE;

        prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            if (inputStream != null) {
                    prop.load(inputStream);
            } else {
                String msg = "Property file '" + propFileName + "' not found in the classpath";
                LOG.error(msg);
                throw new FileNotFoundException(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return INSTANCE;
    }

    public String getStr(String name) {
        return prop.getProperty(name);
    }

    public Integer getInt(String name) {
        return Integer.valueOf(prop.getProperty(name));
    }

    public Boolean getBool(String name) {
        return Boolean.valueOf(prop.getProperty(name));
    }
}