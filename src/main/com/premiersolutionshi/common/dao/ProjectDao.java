package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.Customer;
import com.premiersolutionshi.common.domain.Project;

public interface ProjectDao extends BaseDao<Project> {
    /**
     * Get Customers with their corresponding projects.
     * @return List of Customers.
     */
    ArrayList<Customer> getCustomers();

    /**
     * Gets all projects by User FK.
     * @param userFk
     * @return List of Projects.
     */
    ArrayList<Project> getByUserFk(int userFk);
}
