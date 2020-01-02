package com.premiersolutionshi.support.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.dao.PocDao;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PocService extends ModifiedService<Poc> {
    public PocService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, PocDao.class, userService);
    }

    @Override
    protected PocDao getDao() {
        return (PocDao) super.getDao();
    }

    public ArrayList<Poc> getByProjectFk(Integer projectFk) {
        if (projectFk == null) {
            logError("getByProjectFk | There was no projectFk provided.");
        }
        try {
            return getDao().getByProjectFk(projectFk);
        }
        catch (Exception e) {
            logError("Could not getByProjectFk. projectFk=" + projectFk, e);
        }
        return new ArrayList<>();
    }

    public ArrayList<Poc> getByShipFk(Integer shipFk) {
        if (shipFk == null) {
            logError("getByShipFk | There was no shipFk provided.");
        }
        try {
            return getDao().getByShipFk(shipFk);
        }
        catch (Exception e) {
            logError("Could not getByShipFk. shipFk=" + shipFk, e);
        }
        return new ArrayList<>();
    }

    public ArrayList<Poc> search(String searchText) {
        //sanitize the search
        searchText = StringUtils.removeXss(searchText);
        if (!StringUtils.isEmpty(searchText)) {
            try {
                @SuppressWarnings("unchecked")
                ArrayList<String> searchTerms = new ArrayList<>(Arrays.asList(searchText.split("\\s+")));
                return getDao().search(searchTerms);
            }
            catch (Exception e) {
                logError("Could not search. searchText=" + searchText, e);
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<Poc> getByOrganizationFk(Integer organizationFk) {
        if (organizationFk == null) {
            logError("getByOrganizationFk | There was no organizationFk provided.");
        }
        try {
            return getDao().getByOrganizationFk(organizationFk);
        }
        catch (Exception e) {
            logError("Could not getByOrganizationFk. organizationFk=" + organizationFk, e);
        }
        return new ArrayList<>();
    }

    @Override
    protected void beforeSave(Poc domain) {
        if (domain == null) {
            return;
        }
        super.beforeSave(domain);
        //sanitize data with "escapeHtml", "removeXss", or "sanitizeUrl"
        domain.setLastName(StringUtils.removeXss(domain.getLastName()));
        domain.setFirstName(StringUtils.removeXss(domain.getFirstName()));
        domain.setTitle(StringUtils.removeXss(domain.getTitle()));
        domain.setRank(StringUtils.removeXss(domain.getRank()));
        domain.setEmail(StringUtils.removeXss(domain.getEmail()));
        domain.setWorkNumber(StringUtils.removeXss(domain.getWorkNumber()));
        domain.setFaxNumber(StringUtils.removeXss(domain.getFaxNumber()));
        domain.setCellNumber(StringUtils.removeXss(domain.getCellNumber()));
        domain.setNotes(StringUtils.sanitizeHtml(domain.getNotes()));
        domain.setLastUpdatedBy(StringUtils.removeXss(domain.getLastUpdatedBy()));
        Integer workNumberExt = domain.getWorkNumberExt();
        if (workNumberExt != null && workNumberExt <= 0) {
            domain.setWorkNumberExt(null);
        }
    }
}

