package com.controleestoque.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.controleestoque.model.entity.Funcionario;
import com.controleestoque.service.FuncionarioService;
import com.controleestoque.util.EntityContext;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final FuncionarioService service = new FuncionarioService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		String action = ctx.getParam("action");

		if ("logout".equals(action)) {
			request.getSession().invalidate();
			response.sendRedirect(request.getContextPath() + "/views/login.jsp");
		} else if ("register".equals(action)) {
			handleRegister(request, response, ctx);
		} else {
			handleLogin(request, response, ctx);
		}
	}

	private void handleLogin(HttpServletRequest request, HttpServletResponse response, EntityContext ctx)
			throws ServletException, IOException {
		String username = ctx.getParam("username");
		String password = ctx.getParam("password");
		String erro = "";

		Funcionario funcionario = new Funcionario();
		funcionario.setUserName(username);
		funcionario.setSenha(password);

		try {
			Funcionario authenticated = service.autenticar(funcionario);
			if (authenticated != null) {
				HttpSession session = request.getSession();
				session.setAttribute("funcionario", authenticated);
				response.sendRedirect(request.getContextPath() + "/views/dashboard.jsp");
				return;
			} else {
				erro = "Usuário e/ou senha incorretos";
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = "Erro ao tentar autenticar usuário.";
		}

		request.setAttribute("erro", erro);
		RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
		rd.forward(request, response);
	}

	private void handleRegister(HttpServletRequest request, HttpServletResponse response, EntityContext ctx)
			throws ServletException, IOException {
		String nome = ctx.getParam("nome");
		String username = ctx.getParam("username");
		String password = ctx.getParam("password");
		String confirmPassword = ctx.getParam("confirmPassword");
		String erro = "";
		String saida = "";

		if (nome == null || nome.isEmpty()
				|| username == null || username.isEmpty()
				|| password == null || password.isEmpty()) {
			erro = "Preencha todos os campos obrigatórios.";
		} else if (password.length() < 8) {
			erro = "A senha deve possuir no mínimo 8 caracteres.";
		} else if (!password.equals(confirmPassword)) {
			erro = "As senhas não coincidem.";
		} else {
			Funcionario f = new Funcionario();
			f.setNome(nome);
			f.setUserName(username);
			f.setSenha(password);
			try {
				service.cadastrar(f);
				saida = "Conta criada com sucesso! Faça login.";
			} catch (ClassNotFoundException | SQLException e) {
				String msg = e.getMessage();
				if (msg != null && msg.contains("UNIQUE")) {
					erro = "Este nome de usuário já está em uso.";
				} else {
					erro = "Erro ao criar conta: " + msg;
				}
			}
		}

		request.setAttribute("erro", erro);
		request.setAttribute("saida", saida);
		request.setAttribute("showRegister", true);
		RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
		rd.forward(request, response);
	}
}
