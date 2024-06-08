package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Funcionario;
import persistence.FuncionarioDao;
import persistence.GenericDao;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		String erro = "";
		String saida = "";

		GenericDao gDao = new GenericDao();
		FuncionarioDao funcionarioDao = new FuncionarioDao(gDao);

		Funcionario funcionario = new Funcionario();
		funcionario.setUserName(username);
		funcionario.setSenha(password);

		try {
			Funcionario authenticatedFuncionario = funcionarioDao.autenticar(funcionario);
			if (authenticatedFuncionario != null) {
				HttpSession session = request.getSession();
				session.setAttribute("funcionario", authenticatedFuncionario);
				response.sendRedirect("produto.jsp");
				return;
			} else {
				erro = ("Usuário e/ou senha incorretos");
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = "Erro ao tentar autenticar usuário.";
		} finally {

			request.setAttribute("erro", erro);
			request.setAttribute("saida", saida);
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		}
	}
}