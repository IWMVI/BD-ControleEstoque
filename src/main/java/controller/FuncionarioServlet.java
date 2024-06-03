package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Funcionario;
import persistence.FuncionarioDao;
import persistence.GenericDao;

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
		doGet(request, response);

		String cmd = request.getParameter("botao");
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String username = request.getParameter("username");
		String senha = request.getParameter("senha");

		String saida = "";
		String erro = "";

		Funcionario funcionario = new Funcionario();
		List<Funcionario> funcionarios = new ArrayList<>();

		try {
			if (cmd.equals("Cadastrar")) {
				if (nome != null && !nome.isEmpty() && username != null && !username.isEmpty() && senha != null
						&& !senha.isEmpty()) {
					if (senha.length() >= 8) {
						funcionario.setNome(nome);
						funcionario.setUserName(username);
						funcionario.setSenha(senha);
						cadastrarFuncionario(funcionario);
						saida = "Funcionário cadastrado com sucesso!";
					} else {
						saida = "Senha necessita de no mínimo 8 caracteres.";
					}
				} else {
					saida = "Dados insuficientes para realizar o cadastro.";
				}
			}
			if (cmd.equals("Alterar")) {
				if (nome != null && !nome.isEmpty() && senha != null && !senha.isEmpty()) {
					if (senha.length() >= 8) {
						funcionario.setId(Integer.parseInt(id));
						funcionario.setNome(nome);
						funcionario.setSenha(senha);
						alterarFuncionario(funcionario);
						saida = "Dados alterados com sucesso.";
					} else {
						saida = "Senha necessita de no mínimo 8 caracteres.";
					}
				} else {
					saida = "Dados insuficientes para alteração do cadastro";
				}
			}
			if (cmd.equals("Listar")) {
				funcionarios = listarFuncionarios();
			}
			if (cmd.equals("Buscar")) {
				Funcionario fCon = null;
				if (id != null && !id.isEmpty()) {
					funcionario.setId(Integer.parseInt(id));
					if (nome != null && !nome.isEmpty()) {
						funcionario.setNome(nome);
					}
					fCon = buscarFuncionario(funcionario);
				}
				if (fCon != null) {
					funcionarios.add(fCon);
				} else {
					saida = "Funcionário não encontrado.";
				}
			}
			if (cmd.equals("Excluir")) {
				funcionario.setId(Integer.parseInt(id));
				excluirFuncionario(funcionario);
				saida = "Funcionário excluído com sucesso.";
			}
		} catch (ClassNotFoundException |

				SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("funcionario", funcionario);
			request.setAttribute("funcionarios", funcionarios);

			RequestDispatcher rd = request.getRequestDispatcher("funcionario.jsp");
			rd.forward(request, response);
		}
	}

	private void cadastrarFuncionario(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		fDao.inserir(funcionario);
	}

	private void alterarFuncionario(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		fDao.atualizar(funcionario);
	}

	private Funcionario buscarFuncionario(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		Funcionario fCon = fDao.consultar(funcionario);

		if (fCon != null) {
			return fCon;
		} else {
			return null;
		}
	}

	private List<Funcionario> listarFuncionarios() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		List<Funcionario> funcionarios = fDao.listar();

		return funcionarios;
	}

	private void excluirFuncionario(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		FuncionarioDao fDao = new FuncionarioDao(gDao);

		fDao.excluir(funcionario);
	}
}
