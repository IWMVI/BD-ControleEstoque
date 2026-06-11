package com.controleestoque.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.controleestoque.model.dto.ClienteResumoDTO;
import com.controleestoque.model.entity.Cliente;
import com.controleestoque.service.ClienteService;
import com.controleestoque.util.EntityContext;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ClienteService clienteService = new ClienteService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		String cmd = ctx.getParam("botao");
		String erro = "";
		String saida = "";

		Cliente cliente = new Cliente();
		List<ClienteResumoDTO> clientes = null;

		try {
			if ("Cadastrar".equals(cmd)) {
				String nome = ctx.getParam("nome");
				if (nome != null && !nome.isEmpty()) {
					cliente.setNome(nome);
					clienteService.cadastrar(cliente);
					saida = "Cliente cadastrado com sucesso!";
				} else {
					erro = "Não é possível cadastrar um cliente sem nome";
				}
			}
			if ("Listar".equals(cmd)) {
				clientes = clienteService.listarComTotalCompras();
			}
			if ("Atualizar".equals(cmd)) {
				int id = ctx.getIntParam("id", 0);
				String nome = ctx.getParam("nome");
				if (id > 0 && nome != null && !nome.isEmpty()) {
					cliente.setId(id);
					cliente.setNome(nome);
					clienteService.atualizar(cliente);
					saida = "Cliente atualizado com sucesso!";
				}
			}
			if ("Contar".equals(cmd)) {
				int qtd = clienteService.contar();
				request.setAttribute("qtdClientes", qtd);
				saida = "Quantidade de clientes cadastrados: " + qtd;
			}

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("cliente", cliente);
			request.setAttribute("clientes", clientes);

			RequestDispatcher rd = request.getRequestDispatcher("views/cliente/listar.jsp");
			rd.forward(request, response);
		}
	}
}
