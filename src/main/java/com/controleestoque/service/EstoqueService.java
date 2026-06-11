package com.controleestoque.service;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Estoque;
import com.controleestoque.model.entity.Produto;
import com.controleestoque.persistence.dao.EstoqueDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class EstoqueService {

    private final EstoqueDao estoqueDao;

    public EstoqueService() {
        this.estoqueDao = DaoFactory.createEstoqueDao();
    }

    public List<Estoque> listar() throws SQLException, ClassNotFoundException {
        return estoqueDao.listar();
    }

    public void adicionarQuantidade(int produtoId, int qtd)
            throws SQLException, ClassNotFoundException {
        estoqueDao.adicionarQuantidade(produtoId, qtd);
    }

    public void removerQuantidade(int produtoId, int qtd)
            throws ClassNotFoundException, SQLException {
        estoqueDao.removerQuantidade(produtoId, qtd);
    }

    public List<Produto> listarSemEstoque() throws SQLException, ClassNotFoundException {
        return estoqueDao.listarProdutosSemEstoque();
    }

    public int calcularValorTotal() throws SQLException, ClassNotFoundException {
        return estoqueDao.calcularValorTotalEstoque();
    }
}
