package com.premiersolutionshi.common.util;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.Domain;

public class DomainUtil {

    public static <T extends Domain> T findOnListById(ArrayList<T> list, Integer id) {
        if (id == null || list == null || list.isEmpty()) {
            return null;
        }
        for (T domain : list) {
            Integer domainId = domain.getId();
            if (domainId != null && domainId.equals(id)) {
                return domain;
            }
        }
        return null;
    }
}
