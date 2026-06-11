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

import com.controleestoque.model.dto.VendaResumoDTO;
import com.controleestoque.model.entity.ItemVenda;
import com.controleestoque.model.entity.Venda;
import com.controleestoque.service.VendaService;
import com.controleestoque.util.EntityContext;

@WebServlet("/venda")
public class VendaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final VendaService vendaService = new VendaService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<VendaResumoDTO> vendas = vendaService.listar();
			request.setAttribute("vendas", vendas);
			RequestDispatcher rd = request.getRequestDispatcher("views/venda/listar.jsp");
			rd.forward(request, response);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		String cmd = ctx.getParam("enviar");
		String erro = "";
		String saida = "";

		if ("Registrar Venda".equals(cmd)) {
			try {
				int clienteId = ctx.getIntParam("clienteId");
				int funcionarioId = ctx.getIntParam("funcionarioId");

				String[] produtoIds = ctx.getParamValues("produtoId");
				String[] quantidades = ctx.getParamValues("quantidade");

				if (produtoIds != null && quantidades != null
						&& produtoIds.length == quantidades.length) {
					List<ItemVenda> itens = new ArrayList<>();
					for (int i = 0; i < produtoIds.length; i++) {
						ItemVenda item = new ItemVenda();
						item.setProdutoID(Integer.parseInt(produtoIds[i]));
						item.setQuantidade(Integer.parseInt(quantidades[i]));
						itens.add(item);
					}

					Venda venda = new Venda();
					venda.setClienteID(clienteId);
					venda.setFuncionarioID(funcionarioId);
					venda.setItens(itens);
					vendaService.registrar(venda);
					saida = "Venda registrada com sucesso!";
				} else {
					erro = "Dados incompletos para registrar a venda.";
				}
			} catch (Exception e) {
				erro = e.getMessage();
			}
		}

		request.setAttribute("saida", saida);
		request.setAttribute("erro", erro);
		doGet(request, response);
	}
}
