package com.premiersolutionshi.support.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.support.domain.Ship;

public interface ShipDao extends BaseDao<Ship> {

    /**
     * Gets list of Homeports existing on the ship data table.
     * @return
     */
    ArrayList<String> getHomeportList();
}
