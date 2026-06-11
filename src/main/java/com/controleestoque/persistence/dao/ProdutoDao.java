package com.controleestoque.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Produto;
import com.controleestoque.model.entity.Relatorio;

public class ProdutoDao extends AbstractDao<Produto> {

    private final RowMapper<Produto> PRODUTO_MAPPER = rs -> {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setValor(rs.getFloat("valor"));
        p.setQuantidade(rs.getInt("quantidade"));
        return p;
    };

    public void inserir(Produto produto) throws SQLException, ClassNotFoundException {
        executeInsert(
            "INSERT INTO produto (nome, valor, quantidade) VALUES (?, ?, ?)",
            ps -> {
                ps.setString(1, produto.getNome());
                ps.setFloat(2, produto.getValor());
                ps.setInt(3, produto.getQuantidade());
            });
        adicionarRelatorio(produto);
    }

    public void atualizar(Produto produto) throws SQLException, ClassNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE produto SET ");
        List<String> sets = new java.util.ArrayList<>();
        List<Object> params = new java.util.ArrayList<>();

        if (produto.getNome() != null && !produto.getNome().isEmpty()) {
            sets.add("nome = ?");
            params.add(produto.getNome());
        }
        if (produto.getValor() > 0) {
            sets.add("valor = ?");
            params.add(produto.getValor());
        }
        if (produto.getQuantidade() > 0) {
            sets.add("quantidade = ?");
            params.add(produto.getQuantidade());
        }

        if (!sets.isEmpty()) {
            sql.append(String.join(", ", sets));
            sql.append(" WHERE id = ?");
            params.add(produto.getId());

            executeUpdate(sql.toString(), ps -> {
                for (int i = 0; i < params.size(); i++) {
                    Object param = params.get(i);
                    if (param instanceof String) {
                        ps.setString(i + 1, (String) param);
                    } else if (param instanceof Float) {
                        ps.setFloat(i + 1, (Float) param);
                    } else if (param instanceof Integer) {
                        ps.setInt(i + 1, (Integer) param);
                    }
                }
            });
        }
    }

    public void excluir(Produto produto) throws SQLException, ClassNotFoundException {
        executeUpdate(
            "DELETE FROM produto WHERE id = ?",
            ps -> ps.setInt(1, produto.getId()));
    }

    public List<Produto> listar() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT id, nome, valor, quantidade FROM produto",
            null, PRODUTO_MAPPER);
    }

    public Produto consultar(Produto produto) throws SQLException, ClassNotFoundException {
        return executeSingleResult(
            "SELECT id, nome, valor, quantidade FROM produto WHERE nome = ?",
            ps -> ps.setString(1, produto.getNome()),
            PRODUTO_MAPPER).orElse(null);
    }

    public void adicionarQuantidade(Produto produto, int quantidade)
            throws ClassNotFoundException, SQLException {
        executeUpdate(
            "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?",
            ps -> {
                ps.setInt(1, quantidade);
                ps.setInt(2, produto.getId());
            });
        adicionarRelatorio(produto, quantidade);
    }

    public void removerQuantidade(Produto produto, int quantidade)
            throws ClassNotFoundException, SQLException {
        adicionarQuantidade(produto, -quantidade);
    }

    public List<Produto> listarProdutosComQuantidadeVendida()
            throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT p.id, p.nome, p.valor, SUM(vp.quantidade) AS total_vendida "
                + "FROM produto p "
                + "JOIN venda_produto vp ON p.id = vp.produtoID "
                + "GROUP BY p.id, p.nome, p.valor",
            null, PRODUTO_MAPPER);
    }

    public List<Produto> listarProdutosComEstoqueDisponivel()
            throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT p.id, p.nome, p.valor, e.quantidade "
                + "FROM produto p "
                + "JOIN estoque e ON p.id = e.id "
                + "WHERE e.quantidade > 0",
            null, PRODUTO_MAPPER);
    }

    private void adicionarRelatorio(Produto produto)
            throws ClassNotFoundException, SQLException {
        Relatorio relatorio = new Relatorio();
        relatorio.setDescricao("Produto cadastrado: " + produto.getNome()
                + ", valor R$" + produto.getValor()
                + ", quantidade: " + produto.getQuantidade());
        new RelatorioDao().inserir(relatorio);
    }

    private void adicionarRelatorio(Produto produto, int quantidade)
            throws ClassNotFoundException, SQLException {
        Relatorio relatorio = new Relatorio();
        String descricao;
        if (quantidade > 0) {
            descricao = "Entrada de " + quantidade
                    + " unidades do produto #ID " + produto.getId();
        } else {
            descricao = "Saída de " + (-quantidade)
                    + " unidades do produto #ID " + produto.getId();
        }
        relatorio.setDescricao(descricao);
        new RelatorioDao().inserir(relatorio);
    }
}
