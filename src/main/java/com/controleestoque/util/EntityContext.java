package com.controleestoque.util;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class EntityContext {

    private final HttpServletRequest request;

    public EntityContext(HttpServletRequest request) {
        this.request = request;
    }

    public String getParam(String name) {
        return request.getParameter(name);
    }

    public Optional<String> getOptionalParam(String name) {
        String val = request.getParameter(name);
        return (val != null && !val.isEmpty()) ? Optional.of(val) : Optional.empty();
    }

    public int getIntParam(String name) {
        return Integer.parseInt(request.getParameter(name));
    }

    public int getIntParam(String name, int defaultValue) {
        String val = request.getParameter(name);
        if (val == null || val.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(val);
    }

    public float getFloatParam(String name) {
        return Float.parseFloat(request.getParameter(name));
    }

    public float getFloatParam(String name, float defaultValue) {
        String val = request.getParameter(name);
        if (val == null || val.isEmpty()) {
            return defaultValue;
        }
        return Float.parseFloat(val);
    }

    public String[] getParamValues(String name) {
        return request.getParameterValues(name);
    }
}
