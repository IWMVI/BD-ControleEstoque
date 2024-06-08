package controller;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;

public class ProdutoCommandBuscar implements IProdutoCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		String nome = request.getParameter("nome");
		String saida = "";
		Produto pCons = null;

		if (nome != null && !nome.isEmpty()) {
			Produto p = new Produto();
			p.setNome(nome);
			GenericDao gDao = new GenericDao();
			ProdutoDao pDao = new ProdutoDao(gDao);
			pCons = pDao.consultar(p);
		} else {
			saida = "Informe um nome para a busca.";
		}

		if (pCons != null) {
			request.setAttribute("produto", pCons);
		} else {
			saida = "Produto não encontrado.";
		}

		request.setAttribute("saida", saida);
	}
}
