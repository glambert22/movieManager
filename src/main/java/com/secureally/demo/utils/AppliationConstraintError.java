package com.secureally.demo.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AppliationConstraintError {
    private final String field;
    private final String message;
}
