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
import model.Funcionario;
import persistence.FuncionarioDao;
import persistence.GenericDao;

@WebServlet("/funcionario")
public class FuncionarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FuncionarioServlet() {
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
		String username = request.getParameter("username");
		String senha = request.getParameter("password");

		String saida = "";
		String erro = "";

		Funcionario f = new Funcionario();
		List<Funcionario> funcionarios = new ArrayList<>();

		try {
			if (nome != null && !nome.isEmpty() && username != null && !username.isEmpty() && senha != null
					&& !senha.isEmpty()) {
				if (senha.length() >= 8) {
					f.setNome(nome);
					f.setUserName(username);
					f.setSenha(senha);
					cadastrarFuncionario(f);
					saida = "Funcionário cadastrado com sucesso!";
				} else {
					saida = "Senha deve possuir no mínimo 8 caracteres!";
				}
			} else {
				saida = "Informações insuficientes para o cadastro de um funcionário.";
			}
			if (cmd.equals("Alterar")) {
				if (id != null && !id.isEmpty()) {
					f.setId(Integer.parseInt(id));
					f.setNome(nome);
					alterarFuncionario(f);
					saida = "Funcionário alterado com sucesso!";
				} else {
					saida = "É necessário informar um ID para alteração do funcionário!";
				}
			}
			if (cmd.equals("Listar")) {
				funcionarios = listarFuncionarios();
			}
			if (cmd.equals("Buscar")) {
				if (id != null && !id.isEmpty()) {
					int funcionarioId = Integer.parseInt(id);
					Funcionario funcionarioEncontrado = buscarFuncionario(funcionarioId);
					funcionarios.add(funcionarioEncontrado);
				} else {
					saida = "É necessário informar um ID para busca.";
				}
			}
			if (cmd.equals("Excluir"))
				if (id != null && !id.isEmpty()) {
					f.setId(Integer.parseInt(id));
					excluirFuncionario(f);
					saida = "Funcionário excluído com sucesso!";
				} else {
					saida = "É necessário informar um ID para exclusão de um funcionário.";
				}
			if (cmd.equals("Valor em vendas")) {
				if (id != null && !id.isEmpty()) {
					f.setId(Integer.parseInt(id));
					float valor = valorVendas(f);
					saida = "Valor em vendas R$: " + valor;
				} else {
					saida = "Informe um ID para verificar o total em vendas.";
				}
			}
			if (cmd.equals("Quantidade em vendas")) {
				if (id != null && !id.isEmpty()) {
					f.setId(Integer.parseInt(id));
					int qtd = qtdVendas(f);
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

			RequestDispatcher rd = request.getRequestDispatcher("funcionario.jsp");
			rd.forward(request, response);
		}
	}

	private void cadastrarFuncionario(Funcionario f) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao pDao = new FuncionarioDao(gDao);
		pDao.inserir(f);
	}

	private void alterarFuncionario(Funcionario f) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao pDao = new FuncionarioDao(gDao);
		pDao.atualizar(f);
	}

	private List<Funcionario> listarFuncionarios() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao pDao = new FuncionarioDao(gDao);
		List<Funcionario> funcionarios = pDao.listar();

		return funcionarios;
	}

	private Funcionario buscarFuncionario(int id) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao pDao = new FuncionarioDao(gDao);
		Funcionario funcionario = new Funcionario();
		funcionario.setId(id);

		return pDao.consultar(funcionario);
	}

	private void excluirFuncionario(Funcionario f) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao pDao = new FuncionarioDao(gDao);
		pDao.excluir(f);
	}

	private int qtdVendas(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);
		int qtd = fDao.qtdVendas(funcionario);

		return qtd;
	}

	private float valorVendas(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		float valor = fDao.valorVenda(funcionario);

		return valor;
	}

}