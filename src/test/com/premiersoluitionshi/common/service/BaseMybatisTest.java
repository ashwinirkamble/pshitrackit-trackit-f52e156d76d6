package com.premiersoluitionshi.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class BaseMybatisTest {
    private SqlSession sqlSession;

    private static final String MYBATIS_CONFIG_XML = "C:/cloaked/pshi/WEB-INF/config/mybatis-config.xml";

    public SqlSession getSqlSession() {
        if (sqlSession == null) {
            File file = new File(MYBATIS_CONFIG_XML);
            if (!file.exists()) {
                System.out.println("ERROR: file '" + MYBATIS_CONFIG_XML + "' not found.");
                return null;
            }
            InputStream inputStream;
            SqlSessionFactory sqlSessionFactory = null;
            try {
                // inputStream = Resources.getResourceAsStream(resource);
                inputStream = new FileInputStream(file);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (sqlSessionFactory != null) {
                sqlSession = sqlSessionFactory.openSession();
            }
        }
        return sqlSession;
    }
}
