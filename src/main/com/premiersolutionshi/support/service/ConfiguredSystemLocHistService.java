package com.premiersolutionshi.support.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.support.dao.ConfiguredSystemLocHistDao;
import com.premiersolutionshi.support.domain.ConfiguredSystemLocHist;

@Component("configuredSystemLocHistService")
public class ConfiguredSystemLocHistService extends ModifiedService<ConfiguredSystemLocHist> {
    public ConfiguredSystemLocHistService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, ConfiguredSystemLocHistDao.class, userService);
    }

    @Override
    protected ConfiguredSystemLocHistDao getDao() {
        return (ConfiguredSystemLocHistDao) super.getDao();
    }
}
