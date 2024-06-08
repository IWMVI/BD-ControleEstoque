package controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cliente;
import persistence.ClienteDao;
import persistence.GenericDao;

public class ClienteCommandListar implements ICommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		List<Cliente> clientes = cDao.listar();
		
		request.setAttribute("clientes", clientes);
	}
}
