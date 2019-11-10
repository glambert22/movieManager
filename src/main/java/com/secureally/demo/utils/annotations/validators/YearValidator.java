package com.secureally.demo.utils.annotations.validators;

import com.secureally.demo.utils.annotations.Year;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<Year, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(YearValidator.class);

    @Override
    public void initialize(Year constraint) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return null != value || (value >= 1900 && value <= LocalDate.now().getYear());
    }
}
