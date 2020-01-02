package com.premiersolutionshi.support.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.support.dao.ShipDao;
import com.premiersolutionshi.support.domain.Ship;

public class ShipService extends BaseService<Ship> {
    public ShipService(SqlSession sqlSession) {
        super(sqlSession, ShipDao.class);
    }

    @Override
    protected ShipDao getDao() {
        return (ShipDao) super.getDao();
    }

    public ArrayList<Ship> getAllWithPocs() {
        ArrayList<Ship> shipsWithPocs = new ArrayList<>();
        ArrayList<Ship> all = getAll();
        if (all != null) {
            for (Ship ship : all) {
                ArrayList<Poc> pocList = ship.getPocList();
                if (pocList != null && pocList.size() > 0) {
                    shipsWithPocs.add(ship);
                }
            }
        }
        return shipsWithPocs;
    }

    public ArrayList<String> getHomeportList() {
        try {
            return getDao().getHomeportList();
        }
        catch (Exception e) {
            logError("Could not getHomeportList.", e);
        }
        return new ArrayList<>();
    }
}
