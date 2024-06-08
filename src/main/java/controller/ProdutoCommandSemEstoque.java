package controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.EstoqueDao;
import persistence.GenericDao;

public class ProdutoCommandSemEstoque implements IProdutoCommand {

	private EstoqueDao estoqueDao;

	public ProdutoCommandSemEstoque() {
		this.estoqueDao = new EstoqueDao(new GenericDao());
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		List<Produto> produtos = estoqueDao.listarProdutosSemEstoque();
		request.setAttribute("produtos", produtos);
	}
}
