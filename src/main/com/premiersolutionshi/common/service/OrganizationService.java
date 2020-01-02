package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.dao.OrganizationDao;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.util.StringUtils;

public class OrganizationService extends ModifiedService<Organization> {
    public OrganizationService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, OrganizationDao.class, userService);
    }

    @Override
    protected OrganizationDao getDao() {
        return (OrganizationDao) super.getDao();
    }

    public ArrayList<Organization> getByProjectFk(Integer projectFk) {
        String baseErrMsg = "Could not getByProjectFk. ";
        if (projectFk == null) {
            logError(baseErrMsg + "There was no projectFk provided.");
        }
        try {
            return getDao().getByProjectFk(projectFk);
        }
        catch (Exception e) {
            logError(baseErrMsg + "projectFk=" + projectFk, e);
        }
        return new ArrayList<>();
    }

    @Override
    protected void beforeSave(Organization domain) {
        if (domain == null) {
            return;
        }
        super.beforeSave(domain);
        //sanitize data with "escapeHtml", "removeXss", or "sanitizeUrl"
        domain.setName(StringUtils.removeXss(domain.getName()));
        domain.setAddress1(StringUtils.removeXss(domain.getAddress1()));
        domain.setAddress2(StringUtils.removeXss(domain.getAddress2()));
        domain.setZip(StringUtils.removeAllButDashesAndNumbers(domain.getZip()));
        domain.setStateProvince(StringUtils.removeXss(domain.getStateProvince()));
        domain.setCountry(StringUtils.removeXss(domain.getCountry()));
        domain.setEmail(StringUtils.sanitizeUrl(domain.getEmail()));
        domain.setUrl(StringUtils.sanitizeUrl(domain.getUrl()));
        domain.setPhone(StringUtils.removeXss(domain.getPhone()));
    }
}
