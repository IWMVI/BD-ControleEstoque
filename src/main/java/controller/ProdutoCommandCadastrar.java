package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;
import java.sql.SQLException;

public class ProdutoCommandCadastrar implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		String nome = request.getParameter("nome");
		String valor = request.getParameter("valor");
		String saida = "";

		if (nome != null && !nome.isEmpty()) {
			Produto p = new Produto();
			p.setNome(nome);
			if (valor != null && !valor.isEmpty()) {
				p.setValor(Float.parseFloat(valor));
			}
			GenericDao gDao = new GenericDao();
			ProdutoDao pDao = new ProdutoDao(gDao);
			pDao.inserir(p);
			saida = "Produto cadastrado com sucesso!";
		} else {
			saida = "Informe um nome para cadastrar.";
		}

		request.setAttribute("saida", saida);
	}
}
