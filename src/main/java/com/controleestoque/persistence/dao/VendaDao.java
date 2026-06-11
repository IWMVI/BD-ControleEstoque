package com.controleestoque.persistence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.controleestoque.model.entity.ItemVenda;
import com.controleestoque.model.entity.Venda;

public class VendaDao extends AbstractDao<Venda> {
    private EstoqueDao estoqueDao;

    public VendaDao() {
        this.estoqueDao = new EstoqueDao();
    }

    public void registrar(Venda venda) throws SQLException, ClassNotFoundException {
        executeTransaction(c -> {
            String sqlVenda = "INSERT INTO venda (clienteID, funcionarioID, dataVenda, total) "
                    + "VALUES (?, ?, ?, 0)";
            int vendaId;

            try (PreparedStatement psVenda = c.prepareStatement(sqlVenda,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                psVenda.setInt(1, venda.getClienteID());
                psVenda.setInt(2, venda.getFuncionarioID());
                psVenda.setDate(3, Date.valueOf(LocalDate.now()));
                psVenda.executeUpdate();

                try (var rs = psVenda.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Erro ao obter o ID da venda gerado.");
                    }
                    vendaId = rs.getInt(1);
                }
            }

            float total = 0;
            for (ItemVenda item : venda.getItens()) {
                try (PreparedStatement psItem = c.prepareStatement(
                        "INSERT INTO venda_produto (vendaID, produtoID, quantidade) "
                                + "VALUES (?, ?, ?)")) {
                    psItem.setInt(1, vendaId);
                    psItem.setInt(2, item.getProdutoID());
                    psItem.setInt(3, item.getQuantidade());
                    psItem.executeUpdate();
                }

                float valorProduto = getValorProduto(item.getProdutoID());
                total += valorProduto * item.getQuantidade();

                estoqueDao.removerQuantidade(item.getProdutoID(), item.getQuantidade());
            }

            try (PreparedStatement psUpdate = c.prepareStatement(
                    "UPDATE venda SET total = ? WHERE id = ?")) {
                psUpdate.setFloat(1, total);
                psUpdate.setInt(2, vendaId);
                psUpdate.executeUpdate();
            }

            return vendaId;
        });
    }

    public List<Venda> listar() throws SQLException, ClassNotFoundException {
        java.util.Map<Integer, Venda> vendaMap = new java.util.LinkedHashMap<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT v.id, v.dataVenda, v.total, "
                             + "c.nome as clienteNome, f.nome as funcionarioNome, "
                             + "vp.produtoID, vp.quantidade "
                             + "FROM venda v "
                             + "JOIN cliente c ON v.clienteID = c.id "
                             + "JOIN funcionario f ON v.funcionarioID = f.id "
                             + "JOIN venda_produto vp ON v.id = vp.vendaID");
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int vendaId = rs.getInt("id");
                Venda venda = vendaMap.get(vendaId);
                if (venda == null) {
                    venda = new Venda();
                    venda.setId(vendaId);
                    venda.setDataVenda(rs.getDate("dataVenda"));
                    venda.setTotal(rs.getFloat("total"));
                    venda.setClienteNome(rs.getString("clienteNome"));
                    venda.setFuncionarioNome(rs.getString("funcionarioNome"));
                    venda.setItens(new ArrayList<>());
                    vendaMap.put(vendaId, venda);
                }
                ItemVenda item = new ItemVenda();
                item.setProdutoID(rs.getInt("produtoID"));
                item.setQuantidade(rs.getInt("quantidade"));
                venda.getItens().add(item);
            }
        }

        return new ArrayList<>(vendaMap.values());
    }

    private float getValorProduto(int produtoId) throws SQLException, ClassNotFoundException {
        return executeAggregateFloat(
            "SELECT valor FROM produto WHERE id = ?",
            ps -> ps.setInt(1, produtoId),
            "valor");
    }
}
