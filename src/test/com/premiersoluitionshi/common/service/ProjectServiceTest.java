package com.premiersoluitionshi.common.service;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.Customer;
import com.premiersolutionshi.common.domain.Project;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.service.ProjectService;
import com.premiersolutionshi.common.service.UserService;

public class ProjectServiceTest extends BaseServiceTest<Project> {
    public ProjectServiceTest() {
        super(ProjectServiceTest.class);
    }

    private UserService userService;
    private ProjectService projectService;

    public static void main(String[] args) {
        ProjectServiceTest test = new ProjectServiceTest();
        test.runTests();
        test.runBaseTests();
    }
    private void runTests() {
        ArrayList<Customer> customers = projectService.getCustomers();
        for (Customer customer : customers) {
            System.out.println("====================== " + customer.getCustomerName());
            ArrayList<Project> projects = customer.getProjects();
            for (Project project : projects) {
                logInfo("- id=" + project.getId() + ", projectName=" + project.getProjectName() + ", createdBy=" + project.getCreatedBy());
            }
        }
        //System.out.println(customers);
    }

    @Override
    protected void init() {
        userService = new UserService(getSqlSession());
        projectService = new ProjectService(getSqlSession(), userService);
    }

    @Override
    public BaseService<Project> getService() {
        return projectService;
    }

    @Override
    public Project createInstance() {
        Project project = new Project();
        applyRandomData(project);
        return project;
    }

    @Override
    public void applyRandomData(Project domain) {
       
        
    }

    @Override
    public boolean compare(Project domain1, Project domain2) {
       
        return true;
    }

}
