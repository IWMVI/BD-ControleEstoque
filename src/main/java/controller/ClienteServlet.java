package controller;

import java.io.IOException;
import java.sql.SQLException;
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
		String cmd = request.getParameter("botao");
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String totalCompras = request.getParameter("totalCompras");

		String erro = "";
		String saida = "";

		Cliente cliente = new Cliente();
		List<Cliente> clientes = null;

		try {
			if ("Cadastrar".equals(cmd)) {
				if (nome != null && !nome.isEmpty()) {
					cliente.setNome(nome);
					if (totalCompras != null && !totalCompras.isEmpty()) {
						cliente.setTotalCompras(Float.parseFloat(totalCompras));
					}
					cadastrarCliente(cliente);
					saida = "Cliente cadastrado com sucesso!";
				} else {
					erro = "Não é possível cadastrar um cliente sem nome";
				}
			}
			if ("Listar".equals(cmd)) {
				clientes = listarClientesComTotalCompras();
			}
			if ("Atualizar".equals(cmd)) {
				if (id != null && !id.isEmpty() && nome != null && !nome.isEmpty()) {
					cliente.setId(Integer.parseInt(id));
					cliente.setNome(nome);
					if (totalCompras != null && !totalCompras.isEmpty()) {
						cliente.setTotalCompras(Float.parseFloat(totalCompras));
					}
					atualizarCliente(cliente);
					saida = "Cliente atualizado com sucesso!";
				}
			}
			if ("Contar".equals(cmd)) {
				int qtdClientes = contarClientes();
				saida = "Quantidade de clientes cadastrados: " + qtdClientes;
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

	private void atualizarCliente(Cliente cliente) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		cDao.atualizar(cliente);
	}

	private int contarClientes() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		int quantidade = cDao.contarClientes();

		return quantidade;
	}

	private List<Cliente> listarClientesComTotalCompras() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		List<Cliente> clientes = cDao.listarClientesComTotalCompras();

		return clientes;
	}
}
