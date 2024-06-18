package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ItemVenda;
import model.Venda;

public class VendaDao {
    private GenericDao gDao;
    private EstoqueDao estoqueDao;

    public VendaDao(GenericDao gDao) {
        this.gDao = gDao;
        this.estoqueDao = new EstoqueDao(gDao);
    }

    public void registrar(Venda venda) throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement psVenda = null;
        PreparedStatement psItem = null;
        ResultSet rsVenda = null;
        try {
            c = gDao.getConnection();
            c.setAutoCommit(false); // Inicia transação

            // Inserindo a venda e obtendo o ID gerado
            String sqlVenda = "INSERT INTO venda (clienteID, funcionarioID, dataVenda, total) VALUES (?, ?, GETDATE(), 0)";
            psVenda = c.prepareStatement(sqlVenda, PreparedStatement.RETURN_GENERATED_KEYS);
            psVenda.setInt(1, venda.getClienteID());
            psVenda.setInt(2, venda.getFuncionarioID());
            psVenda.executeUpdate();

            // Obtendo o ID da venda gerado
            rsVenda = psVenda.getGeneratedKeys();
            int vendaId = 0;
            if (rsVenda.next()) {
                vendaId = rsVenda.getInt(1);
            } else {
                throw new SQLException("Erro ao obter o ID da venda gerado.");
            }

            // Calculando o total da venda
            float total = 0;
            for (ItemVenda item : venda.getItens()) {
                String sqlItem = "INSERT INTO venda_produto (vendaID, produtoID, quantidade) VALUES (?, ?, ?)";
                psItem = c.prepareStatement(sqlItem);
                psItem.setInt(1, vendaId);
                psItem.setInt(2, item.getProdutoID());
                psItem.setInt(3, item.getQuantidade());
                psItem.executeUpdate();
                psItem.close();

                // Calculando o total parcial da venda
                float valorProduto = getValorProduto(item.getProdutoID());
                total += valorProduto * item.getQuantidade();

                // Atualizando o estoque
                estoqueDao.removerQuantidade(item.getProdutoID(), -item.getQuantidade());
            }

            // Atualizando o total da venda
            String sqlUpdateVenda = "UPDATE venda SET total = ? WHERE id = ?";
            PreparedStatement psUpdate = c.prepareStatement(sqlUpdateVenda);
            psUpdate.setFloat(1, total);
            psUpdate.setInt(2, vendaId);
            psUpdate.executeUpdate();
            psUpdate.close();

            c.commit(); // Confirma a transação
        } catch (SQLException e) {
            if (c != null) {
                try {
                    c.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rsVenda != null) {
                rsVenda.close();
            }
            if (psVenda != null) {
                psVenda.close();
            }
            if (c != null) {
                c.close();
            }
        }
    }

    public List<Venda> listar() throws SQLException, ClassNotFoundException {
        List<Venda> vendas = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT v.id, v.dataVenda, v.total, c.nome as clienteNome, f.nome as funcionarioNome, "
                + "vp.produtoID, vp.quantidade " + "FROM venda v " + "JOIN cliente c ON v.clienteID = c.id "
                + "JOIN funcionario f ON v.funcionarioID = f.id " + "JOIN venda_produto vp ON v.id = vp.vendaID";
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int vendaId = rs.getInt("id");
            Venda venda = vendas.stream().filter(v -> v.getId() == vendaId).findFirst().orElse(null);
            if (venda == null) {
                venda = new Venda();
                venda.setId(vendaId);
                venda.setDataVenda(rs.getDate("dataVenda"));
                venda.setTotal(rs.getFloat("total"));
                venda.setClienteNome(rs.getString("clienteNome"));
                venda.setFuncionarioNome(rs.getString("funcionarioNome"));
                venda.setItens(new ArrayList<>());
                vendas.add(venda);
            }
            ItemVenda item = new ItemVenda();
            item.setProdutoID(rs.getInt("produtoID"));
            item.setQuantidade(rs.getInt("quantidade"));
            venda.getItens().add(item);
        }
        rs.close();
        ps.close();
        c.close();
        return vendas;
    }

    private float getValorProduto(int produtoId) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT valor FROM produto WHERE id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, produtoId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        float valor = rs.getFloat("valor");
        rs.close();
        ps.close();
        c.close();
        return valor;
    }
}
