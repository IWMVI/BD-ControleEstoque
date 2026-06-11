package com.controleestoque.config;

import java.io.IOException;

import com.controleestoque.persistence.connection.DatabaseConnection;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;

public class WebConfig extends HttpFilter {
    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (request instanceof HttpServletRequest) {
            request.setAttribute("h2Fallback", DatabaseConnection.isUsingFallback());
        }

        chain.doFilter(request, response);
    }
}
