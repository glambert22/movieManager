package com.secureally.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureally.demo.utils.ApplicationError;
import com.secureally.demo.utils.ApplicationMessage;
import com.secureally.demo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

    @Autowired
    private ObjectMapper MAPPER;

    @Autowired
    private ApplicationMessage CATALOG_MESSAGE;

    @PostConstruct
    public void init() {
        ApplicationError.setStaticMembers(MAPPER, CATALOG_MESSAGE);
        JsonUtils.setStaticMembers(MAPPER);
    }
}
