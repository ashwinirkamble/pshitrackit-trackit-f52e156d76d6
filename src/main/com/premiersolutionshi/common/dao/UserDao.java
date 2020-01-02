package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.User;

public interface UserDao extends BaseDao<User> {

    ArrayList<String> getUsernames();

    User getByUsername(String username);
}
