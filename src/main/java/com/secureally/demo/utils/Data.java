package com.secureally.demo.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data<T>
{
    @JsonInclude( JsonInclude.Include.NON_NULL )
    private List<T> entities;

    @JsonInclude( JsonInclude.Include.NON_NULL )
    private Object entity;
}
