package com.controleestoque.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Cliente;

public class ClienteDao extends AbstractDao<Cliente> {

    private final RowMapper<Cliente> CLIENTE_MAPPER = rs -> {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setQtdCompras(rs.getInt("qtdCompras"));
        return c;
    };

    private final RowMapper<Cliente> CLIENTE_COM_TOTAL_MAPPER = rs -> {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setTotalCompras(rs.getFloat("totalCompras"));
        return c;
    };

    public void inserir(Cliente cliente) throws ClassNotFoundException, SQLException {
        executeInsert(
            "INSERT INTO cliente (nome, qtdCompras) VALUES (?, ?)",
            ps -> {
                ps.setString(1, cliente.getNome());
                ps.setInt(2, cliente.getQtdCompras());
            });
    }

    public List<Cliente> listar() throws ClassNotFoundException, SQLException {
        return executeQuery(
            "SELECT * FROM cliente",
            null, CLIENTE_MAPPER);
    }

    public void atualizar(Cliente cliente) throws ClassNotFoundException, SQLException {
        executeUpdate(
            "UPDATE cliente SET nome = ?, qtdCompras = ? WHERE id = ?",
            ps -> {
                ps.setString(1, cliente.getNome());
                ps.setInt(2, cliente.getQtdCompras());
                ps.setInt(3, cliente.getId());
            });
    }

    public int contarClientes() throws ClassNotFoundException, SQLException {
        return executeAggregate(
            "SELECT COUNT(*) AS total FROM cliente",
            null, "total");
    }

    public List<Cliente> listarClientesComTotalCompras() throws ClassNotFoundException, SQLException {
        return executeQuery(
            "SELECT c.id, c.nome, "
                + "SUM(p.valor * vp.quantidade) AS totalCompras "
                + "FROM cliente c "
                + "LEFT JOIN venda v ON c.id = v.clienteID "
                + "LEFT JOIN venda_produto vp ON v.id = vp.vendaID "
                + "LEFT JOIN produto p ON vp.produtoID = p.id "
                + "GROUP BY c.id, c.nome",
            null, CLIENTE_COM_TOTAL_MAPPER);
    }
}
