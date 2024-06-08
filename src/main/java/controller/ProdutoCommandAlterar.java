package controller;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;

public class ProdutoCommandAlterar implements IProdutoCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String valor = request.getParameter("valor");
		String saida = "";

		if (id != null && !id.isEmpty()) {
			Produto p = new Produto();
			p.setId(Integer.parseInt(id));
			if (valor != null && !valor.isEmpty()) {
				p.setValor(Float.parseFloat(valor));
			}
			if (nome != null && !nome.isEmpty()) {
				p.setNome(nome);
			}
			GenericDao gDao = new GenericDao();
			ProdutoDao pDao = new ProdutoDao(gDao);
			pDao.atualizar(p);
			saida = "Produto alterado com sucesso!";
		} else {
			saida = "ID do produto não especificado.";
		}

		request.setAttribute("saida", saida);
	}
}
