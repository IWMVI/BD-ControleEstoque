package controller;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cliente;
import persistence.ClienteDao;
import persistence.GenericDao;

public class ClienteCommandCadastrar implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {

		String nome = request.getParameter("nome");
		String saida = "";

		if (nome != null && !nome.isEmpty()) {
			Cliente c = new Cliente();
			c.setNome(nome);
			GenericDao gDao = new GenericDao();
			ClienteDao cDao = new ClienteDao(gDao);
			cDao.inserir(c);
		} else {
			saida = "Informe um nome para cadastrar";
		}

		request.setAttribute("saida", saida);
	}
}
