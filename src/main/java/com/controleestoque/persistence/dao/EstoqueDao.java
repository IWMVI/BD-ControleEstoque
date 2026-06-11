package com.controleestoque.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Estoque;
import com.controleestoque.model.entity.Produto;

public class EstoqueDao extends AbstractDao<Estoque> {

    private final RowMapper<Estoque> ESTOQUE_MAPPER = rs -> {
        Estoque e = new Estoque();
        e.setId(rs.getInt("id"));
        e.setQuantidade(rs.getInt("quantidade"));
        return e;
    };

    private final RowMapper<Produto> PRODUTO_SEM_ESTOQUE_MAPPER = rs -> {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setValor(rs.getFloat("valor"));
        return p;
    };

    private void upsertQuantidade(int produtoId, int quantidade)
            throws SQLException, ClassNotFoundException {
        String update = "UPDATE estoque SET quantidade = quantidade + ? WHERE id = ?";
        int rows = executeUpdate(update, ps -> {
            ps.setInt(1, quantidade);
            ps.setInt(2, produtoId);
        });
        if (rows == 0) {
            String insert = "INSERT INTO estoque (id, quantidade) VALUES (?, ?)";
            executeUpdate(insert, ps -> {
                ps.setInt(1, produtoId);
                ps.setInt(2, quantidade);
            });
        }
    }

    public void inserir(Produto p, int quantidade)
            throws SQLException, ClassNotFoundException {
        upsertQuantidade(p.getId(), quantidade);
    }

    public void atualizar(Estoque estoque) throws SQLException, ClassNotFoundException {
        executeUpdate(
            "UPDATE estoque SET quantidade = ? WHERE id = ?",
            ps -> {
                ps.setInt(1, estoque.getQuantidade());
                ps.setInt(2, estoque.getId());
            });
    }

    public void excluir(Estoque estoque) throws SQLException, ClassNotFoundException {
        executeUpdate(
            "DELETE FROM estoque WHERE id = ?",
            ps -> ps.setInt(1, estoque.getId()));
    }

    public List<Estoque> listar() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT id, quantidade FROM estoque",
            null, ESTOQUE_MAPPER);
    }

    public Estoque consultarPorId(int id) throws SQLException, ClassNotFoundException {
        return executeSingleResult(
            "SELECT id, quantidade FROM estoque WHERE id = ?",
            ps -> ps.setInt(1, id),
            ESTOQUE_MAPPER).orElse(null);
    }

    public void adicionarQuantidade(int produtoId, int quantidade)
            throws SQLException, ClassNotFoundException {
        upsertQuantidade(produtoId, quantidade);
    }

    public List<Produto> listarProdutosSemEstoque() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT p.id, p.nome, p.valor "
                    + "FROM produto p "
                    + "LEFT JOIN estoque e ON p.id = e.id "
                    + "WHERE e.quantidade <= 0 OR e.quantidade IS NULL",
            null, PRODUTO_SEM_ESTOQUE_MAPPER);
    }

    public int calcularValorTotalEstoque() throws SQLException, ClassNotFoundException {
        return executeAggregate(
            "SELECT SUM(p.valor * e.quantidade) AS total_estoque "
                    + "FROM produto p JOIN estoque e ON p.id = e.id",
            null, "total_estoque");
    }

    public void removerQuantidade(int produtoId, int quantidade)
            throws ClassNotFoundException, SQLException {
        upsertQuantidade(produtoId, -quantidade);
    }
}
