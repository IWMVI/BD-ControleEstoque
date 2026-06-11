package com.controleestoque.service;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.controleestoque.model.dto.ProdutoListagemDTO;
import com.controleestoque.model.entity.Produto;
import com.controleestoque.persistence.dao.ProdutoDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class ProdutoService {

    private final ProdutoDao produtoDao;

    public ProdutoService() {
        this.produtoDao = DaoFactory.createProdutoDao();
    }

    public void cadastrar(Produto p) throws ClassNotFoundException, SQLException {
        produtoDao.inserir(p);
    }

    public void alterar(Produto p) throws ClassNotFoundException, SQLException {
        produtoDao.atualizar(p);
    }

    public List<ProdutoListagemDTO> listar() throws ClassNotFoundException, SQLException {
        List<Produto> produtos = produtoDao.listar();
        return toDTOList(produtos);
    }

    public List<ProdutoListagemDTO> consultarPorNome(String nome) throws ClassNotFoundException, SQLException {
        Produto p = new Produto();
        p.setNome(nome);
        Produto resultado = produtoDao.consultar(p);
        List<ProdutoListagemDTO> dtoList = new ArrayList<>();
        if (resultado != null) {
            dtoList.add(toDTO(resultado));
        }
        return dtoList;
    }

    public void adicionarQuantidade(Produto p, int qtd) throws ClassNotFoundException, SQLException {
        produtoDao.adicionarQuantidade(p, qtd);
    }

    public void removerQuantidade(Produto p, int qtd) throws ClassNotFoundException, SQLException {
        produtoDao.removerQuantidade(p, qtd);
    }

    public List<ProdutoListagemDTO> listarVendidos() throws ClassNotFoundException, SQLException {
        List<Produto> produtos = produtoDao.listarProdutosComQuantidadeVendida();
        return toDTOList(produtos);
    }

    private ProdutoListagemDTO toDTO(Produto p) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return new ProdutoListagemDTO(p.getId(), p.getNome(), p.getValor(),
                fmt.format(p.getValor()), p.getQuantidade());
    }

    private List<ProdutoListagemDTO> toDTOList(List<Produto> produtos) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        List<ProdutoListagemDTO> dtoList = new ArrayList<>();
        for (Produto p : produtos) {
            dtoList.add(new ProdutoListagemDTO(p.getId(), p.getNome(), p.getValor(),
                    fmt.format(p.getValor()), p.getQuantidade()));
        }
        return dtoList;
    }
}
