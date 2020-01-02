package com.premiersolutionshi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.premiersolutionshi.old.util.CommonMethods;

public class ConfigUtils {
    private static Logger logger = Logger.getLogger(ConfigUtils.class.getSimpleName());
    private static final String CONFIG_DIR = "C:/cloaked/pshi/WEB-INF/config/";
    private static final String CONFIG_PROPERTIES = CONFIG_DIR + "config.properties";
    private static final String BUILD_PROPERTIES = CONFIG_DIR + "build.properties";

    public static Integer getBuildNumber() {
        Properties buildProperties = getPropertiesFromFile(BUILD_PROPERTIES);
        return StringUtils.parseInt(buildProperties.get("build.number").toString());
    }

    public static Properties getConfigProperties() {
        return getPropertiesFromFile(CONFIG_PROPERTIES);
    }

    public static boolean isDevEnv() {
        Properties properties = getConfigProperties();
        String isProdProp = properties.getProperty("IS_PROD");
        return StringUtils.isEmpty(isProdProp) || isProdProp.equals("false");
    }

    private static Properties getPropertiesFromFile(String filePath) {
        InputStream input = null;
        Properties prop = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("ERROR: file '" + filePath + "' not found.");
                return null;
            }
            input = new FileInputStream(file);
            prop = new Properties();
            prop.load(input);
        }
        catch (IOException ex) {
            logError("Error getting config properties.", ex);
            prop = null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    logError("Error closing inputStream.", e);
                }
            }
        }
        return prop;
    }

    protected static void logError(String message, Exception e) {
        if (e == null) {
            logger.error(message);
        }
        else {
            logger.error(message, e);
            e.printStackTrace();
        }
    }

    protected static void logInfo(String message) {
        logger.info(message);
    }

    public static void printAllProperties() {
        Properties prop = getConfigProperties();
        if (prop == null) {
            return;
        }
        //Map<String, String> propMap = new HashMap<>();
        Enumeration<?> e = prop.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = prop.getProperty(key);
            System.out.println("Key : " + key + ",\tValue : " + value);
        }
        String doesntExist = prop.getProperty("doesnt exist");
        System.out.println("doesntExist=" + doesntExist);
    }

    public static void main(String[] args) {
        printAllProperties();
        System.out.println("\n\nCommonMethods.isDevEnv()=" + CommonMethods.isDevEnv());
        System.out.println("\n\nConfigUtils.getBuildNumber()=" + getBuildNumber());
    }
}
