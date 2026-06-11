package com.controleestoque.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.controleestoque.util.EntityContext;

public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected abstract String getJspPath();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response,
                                  RequestProcessor processor) throws ServletException, IOException {
        String saida = "";
        String erro = "";

        try {
            saida = processor.execute(new EntityContext(request));
        } catch (Exception e) {
            erro = e.getMessage();
        } finally {
            request.setAttribute("saida", saida);
            request.setAttribute("erro", erro);
            RequestDispatcher rd = request.getRequestDispatcher(getJspPath());
            rd.forward(request, response);
        }
    }

    @FunctionalInterface
    public interface RequestProcessor {
        String execute(EntityContext ctx) throws Exception;
    }
}
