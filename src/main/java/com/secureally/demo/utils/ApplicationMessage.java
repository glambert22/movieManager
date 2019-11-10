package com.secureally.demo.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMessage {

    private final MessageSourceAccessor accessor;

    public ApplicationMessage(MessageSource messageSource) {
        this.accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String get(String code, Object... args) {
        return accessor.getMessage(code, args);
    }
}
