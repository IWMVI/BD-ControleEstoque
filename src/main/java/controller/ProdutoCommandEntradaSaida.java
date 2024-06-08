package controller;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;

public class ProdutoCommandEntradaSaida implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		String id = request.getParameter("id");
		String quantidade = request.getParameter("quantidade");
		String saida = "";

		try {
			if (id != null && !id.isEmpty() && quantidade != null && !quantidade.isEmpty()) {
				Produto p = new Produto();
				p.setId(Integer.parseInt(id));
				int qtd = Integer.parseInt(quantidade);
				GenericDao gDao = new GenericDao();
				ProdutoDao pDao = new ProdutoDao(gDao);

				if (qtd > 0) {
					pDao.adicionarQuantidade(p, qtd);
					saida = "Quantidade adicionada com sucesso!";
				} else if (qtd < 0) {
					pDao.removerQuantidade(p, -qtd);
					saida = "Quantidade removida com sucesso!";
				} else {
					saida = "Quantidade mantida.";
				}
			} else {
				saida = "ID e/ou quantidade não especificados.";
			}
		} catch (Exception e) {
			saida = e.getMessage();
		}

		request.setAttribute("saida", saida);
	}
}
