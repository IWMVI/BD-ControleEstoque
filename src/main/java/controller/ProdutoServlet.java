package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;

@WebServlet("/produto")
public class ProdutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProdutoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("enviar");
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String valor = request.getParameter("valor");
		String quantidade = request.getParameter("quantidade");

		String saida = "";
		String erro = "";

		Produto p = new Produto();
		List<Produto> produtos = new ArrayList<>();

		try {
			if (cmd.equals("Cadastrar")) {
				if (nome != null && !nome.isEmpty()) {
					p.setNome(nome);
					if (valor != null && !valor.isEmpty()) {
						p.setValor(Float.parseFloat(valor));
					}
					cadastrarProduto(p);
					saida = "Produto cadastrado com sucesso!";
				} else {
					saida = "Informe um nome para cadastrar";
				}
			}
			if (cmd.equals("Listar")) {
				produtos = listarProdutos(p);
			}
			if (cmd.equals("Alterar")) {
				if (id != null && !id.isEmpty()) {
					p.setId(Integer.parseInt(id));
					if (valor != null && !valor.isEmpty()) {
						p.setValor(Float.parseFloat(valor));
					}
					if (nome != null && !nome.isEmpty()) {
						p.setNome(nome);
					}
					alterarProduto(p);
				} else {
					saida = "ID do produto não especificado.";
				}
			}
			if (cmd.equals("Buscar")) {
				Produto pCons = null;
				if (nome != null && !nome.isEmpty()) {
					p.setNome(nome);
					pCons = consultar(p);
				} else {
					saida = "Informe um nome para a busca.";
				}
				if (pCons != null) {
					produtos.add(pCons);
				} else {
					saida = "Produto não encontrado.";
				}
			}
			if (cmd.equals("Entrada/Saída")) {
				try {
					if (id != null && !id.isEmpty() && quantidade != null && !quantidade.isEmpty()) {
						p.setId(Integer.parseInt(id));
						int qtd = Integer.parseInt(quantidade);
						if (qtd > 0) {
							adicionarQuantidade(p, qtd);
						} else if (qtd < 0) {
							removerQuantidade(p, -qtd);
						} else {
							saida = "Quantidade mantida.";
						}
					} else {
						saida = "ID e/ou quantidade não especificados.";
					}
				} catch (Exception e) {
					saida = e.getMessage();
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("produto", p);
			request.setAttribute("produtos", produtos);

			RequestDispatcher rd = request.getRequestDispatcher("produto.jsp");
			rd.forward(request, response);
		}
	}

	private void cadastrarProduto(Produto p) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);

		pDao.inserir(p);
	}

	private List<Produto> listarProdutos(Produto p) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);
		List<Produto> produtos = pDao.listar();

		NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		for (Produto produto : produtos) {
			produto.setValorFormatado(formatadorMoeda.format(produto.getValor()));
		}

		return produtos;
	}

	private void alterarProduto(Produto p) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);

		pDao.atualizar(p);
	}

	private Produto consultar(Produto p) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);

		Produto pCons = pDao.consultar(p);

		if (pCons != null) {
			return pCons;
		} else {
			return null;
		}
	}

	private void adicionarQuantidade(Produto p, int quantidade) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);
		pDao.adicionarQuantidade(p, quantidade);
	}

	private void removerQuantidade(Produto p, int quantidade) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		ProdutoDao pDao = new ProdutoDao(gDao);
		pDao.removerQuantidade(p, quantidade);
	}
}
