package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import persistence.GenericDao;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, ICommand> commands;

	public ClienteServlet() {
		super();
		GenericDao gDao = new GenericDao();
		commands = new HashMap<>();
		commands.put("Cadastrar", new ClienteCommandCadastrar());
		commands.put("Listar", new ClienteCommandListar());
		commands.put("Atualizar", new ClienteCommandAtualizar());
		commands.put("Contar", new ClienteCommandContar(gDao));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		ICommand command = commands.get(cmd);

		if(command != null) {
			try {
				command.execute(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				request.setAttribute("erro", e.getMessage());
			}
		} else {
			request.setAttribute("saida", "Comando não reconhecido.");
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
		rd.forward(request, response);
	}
}
