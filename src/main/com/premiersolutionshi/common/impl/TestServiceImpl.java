package com.premiersolutionshi.common.impl;

import org.springframework.stereotype.Service;

import com.premiersolutionshi.common.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Override
    public String getString() {
        return "STRING";
    }

}
