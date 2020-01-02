package com.premiersolutionshi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.premiersolutionshi.old.model.ProjectModel;

/**
 * This will have utility methods specific to MyBatis.
 */
public class SqlUtils {
    private static Logger logger = Logger.getLogger(ProjectModel.class.getSimpleName());
    private static final String MYBATIS_CONFIG_XML = "C:/cloaked/pshi/WEB-INF/config/mybatis-config.xml";

    /**
     * MyBatis setup.
     * @return
     */
    public static SqlSession getMybatisSession() {
        File file = new File(MYBATIS_CONFIG_XML);
        if (!file.exists()) {
            System.out.println("ERROR: file '" + MYBATIS_CONFIG_XML + "' not found.");
            return null;
        }
        InputStream inputStream;
        SqlSessionFactory sqlSessionFactory = null;
        try {
            inputStream = new FileInputStream(file);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            return sqlSessionFactory.openSession();
        }
        catch (IOException e) {
            logger.error("Error setting up Mybatis.", e);
        }
        return null;
    }
}
