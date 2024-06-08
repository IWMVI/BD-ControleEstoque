package controller;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produto;
import persistence.GenericDao;
import persistence.ProdutoDao;

public class ListarProdutoCommand implements IProdutoCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ProdutoDao pDao = new ProdutoDao(gDao);
        List<Produto> produtos = pDao.listar();

        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        for (Produto produto : produtos) {
            produto.setValorFormatado(formatadorMoeda.format(produto.getValor()));
        }

        request.setAttribute("produtos", produtos);
    }
}
