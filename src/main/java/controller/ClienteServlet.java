package controller;

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
import model.Cliente;
import persistence.ClienteDao;
import persistence.GenericDao;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClienteServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		String cmd = request.getParameter("botao");
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");

		String erro = "";
		String saida = "";

		Cliente cliente = new Cliente();
		List<Cliente> clientes = new ArrayList<>();

		try {
			if (cmd.contains("Cadastrar")) {
				if (nome != null && !nome.isEmpty()) {
					cliente.setNome(nome);
					cadastrarCliente(cliente);
					saida = "Cliente cadastrado com sucesso.";
				} else {
					saida = "Informe um nome para cadastrar o cliente";
				}
			}
			if (cmd.contains("Listar")) {
				clientes = listarClientes();
			}
			if (cmd.contains("Atualizar")) {
				if (id != null && !id.isEmpty() && nome != null && !nome.isEmpty()) {
					cliente.setId(Integer.parseInt(id));
					cliente.setNome(nome);
					atualizarCliente(cliente);
					saida = "Cliente atualizado com sucesso,";
				} else {
					saida = "Preencha todos os campos para atualizar os dados do cliente";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {

			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("cliente", cliente);
			request.setAttribute("clientes", clientes);

			RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
			rd.forward(request, response);
		}
	}

	private void cadastrarCliente(Cliente cliente) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		cDao.inserir(cliente);
	}

	private List<Cliente> listarClientes() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		List<Cliente> clientes = cDao.listar();
		return clientes;
	}

	private void atualizarCliente(Cliente cliente) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		cDao.atualizar(cliente);
	}
}
