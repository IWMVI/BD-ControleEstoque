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

import com.controleestoque.model.dto.FuncionarioResumoDTO;
import com.controleestoque.model.entity.Funcionario;
import com.controleestoque.service.FuncionarioService;
import com.controleestoque.util.EntityContext;

@WebServlet("/funcionario")
public class FuncionarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final FuncionarioService funcionarioService = new FuncionarioService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		String cmd = ctx.getParam("botao");
		String saida = "";
		String erro = "";

		Funcionario f = new Funcionario();
		List<FuncionarioResumoDTO> funcionarios = new ArrayList<>();

		try {
			if (cmd.equals("Cadastrar")) {
				String nome = ctx.getParam("nome");
				String username = ctx.getParam("username");
				String senha = ctx.getParam("password");

				if (nome != null && !nome.isEmpty()
						&& username != null && !username.isEmpty()
						&& senha != null && !senha.isEmpty()) {
					if (senha.length() >= 8) {
						f.setNome(nome);
						f.setUserName(username);
						f.setSenha(senha);
						funcionarioService.cadastrar(f);
						saida = "Funcionário cadastrado com sucesso!";
					} else {
						saida = "Senha deve possuir no mínimo 8 caracteres!";
					}
				} else {
					saida = "Informações insuficientes para o cadastro de um funcionário.";
				}
			}
			if (cmd.equals("Alterar")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					f.setId(id);
					f.setNome(ctx.getParam("nome"));
					funcionarioService.alterar(f);
					saida = "Funcionário alterado com sucesso!";
				} else {
					saida = "É necessário informar um ID para alteração do funcionário!";
				}
			}
			if (cmd.equals("Listar")) {
				funcionarios = funcionarioService.listar();
			}
			if (cmd.equals("Buscar")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					Funcionario func = funcionarioService.buscarPorId(id);
					if (func != null) {
						funcionarios.add(new FuncionarioResumoDTO(func.getId(), func.getNome(),
								func.getUserName(), func.getTotalVendas()));
					}
				} else {
					saida = "É necessário informar um ID para busca.";
				}
			}
			if (cmd.equals("Excluir")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					f.setId(id);
					funcionarioService.excluir(f);
					saida = "Funcionário excluído com sucesso!";
				} else {
					saida = "É necessário informar um ID para exclusão de um funcionário.";
				}
			}
			if (cmd.equals("Valor em vendas")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					f.setId(id);
					float valor = funcionarioService.valorVendas(f);
					saida = "Valor em vendas R$: " + valor;
				} else {
					saida = "Informe um ID para verificar o total em vendas.";
				}
			}
			if (cmd.equals("Quantidade em vendas")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					f.setId(id);
					int qtd = funcionarioService.quantidadeVendas(f);
					saida = "Quantidade de vendas realizadas: " + qtd;
				} else {
					saida = "Informe um ID para verificar as vendas.";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("funcionario", f);
			request.setAttribute("funcionarios", funcionarios);

			RequestDispatcher rd = request.getRequestDispatcher("views/funcionario/listar.jsp");
			rd.forward(request, response);
		}
	}
}
