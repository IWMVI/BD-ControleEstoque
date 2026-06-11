package com.controleestoque.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.controleestoque.model.entity.Relatorio;
import com.controleestoque.service.RelatorioService;
import com.controleestoque.util.EntityContext;

@WebServlet("/relatorio")
public class RelatorioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final RelatorioService relatorioService = new RelatorioService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		List<Relatorio> relatorios = new ArrayList<>();
		String cmd = ctx.getParam("botao");
		String saida = "";
		String erro = "";

		try {
			if ("Listar".equals(cmd)) {
				relatorios = relatorioService.listar();
			}
			if ("Total".equals(cmd)) {
				float qtd = relatorioService.somarVendas();
				saida = "Total em vendas: " + qtd;
			}
			if ("Total vendas".equals(cmd)) {
				int qtd = relatorioService.contarVendas();
				saida = "Total de vendas: " + qtd;
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("relatorios", relatorios);

			RequestDispatcher rd = request.getRequestDispatcher("views/relatorio/listar.jsp");
			rd.forward(request, response);
		}
	}
}
