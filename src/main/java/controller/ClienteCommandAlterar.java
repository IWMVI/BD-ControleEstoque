package controller;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cliente;
import persistence.ClienteDao;
import persistence.GenericDao;

public class ClienteCommandAlterar implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String saida = "";

		if (id != null && !id.isEmpty() && nome != null && !nome.isEmpty()) {
			Cliente c = new Cliente();
			c.setId(Integer.parseInt(id));
			c.setNome(nome);
			GenericDao gDao = new GenericDao();
			ClienteDao cDao = new ClienteDao(gDao);
			cDao.atualizar(c);
			saida = "Cliente alterado com sucesso!";
		} else {
			saida = "Campos insuficientes para atualização.";
		}
		
		request.setAttribute("saida", saida);
	}
}
