package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.Organization;

public interface OrganizationDao extends BaseDao<Organization> {

    ArrayList<Organization> getByProjectFk(Integer projectFk);
}
