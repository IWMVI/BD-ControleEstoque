package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDao {
	private GenericDao gDao;

	public ClienteDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	public void inserir(Cliente cliente) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO cliente (nome, qtdCompras) VALUES (?, ?)";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cliente.getNome());
		ps.setInt(2, cliente.getQtdCompras());

		ps.execute();

		ps.close();
		c.close();
	}

	public List<Cliente> listar() throws ClassNotFoundException, SQLException {
		List<Cliente> clientes = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT * FROM cliente";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Cliente cliente = new Cliente();
			cliente.setId(rs.getInt("id"));
			cliente.setNome(rs.getString("nome"));
			cliente.setQtdCompras(rs.getInt("qtdCompras"));
			clientes.add(cliente);
		}

		rs.close();
		ps.close();
		c.close();

		return clientes;
	}

	public void atualizar(Cliente cliente) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE cliente SET nome = ?, qtdCompras = ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cliente.getNome());
		ps.setInt(2, cliente.getQtdCompras());
		ps.setInt(3, cliente.getId());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public int contarClientes() throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS total FROM cliente";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		int total = 0;
		if (rs.next()) {
			total = rs.getInt("total");
		}

		rs.close();
		ps.close();
		c.close();

		return total;
	}

	public List<Cliente> listarClientesComTotalCompras() throws ClassNotFoundException, SQLException {
		List<Cliente> clientes = new ArrayList<>();
		Connection c = gDao.getConnection();
	    String sql = "SELECT " +
                "c.id, c.nome, " +
                "SUM(p.valor * vp.quantidade) AS totalCompras " +
                "FROM cliente c " +
                "LEFT JOIN venda v ON c.id = v.clienteID " +
                "LEFT JOIN venda_produto vp ON v.id = vp.vendaID " +
                "LEFT JOIN produto p ON vp.produtoID = p.id " +
                "GROUP BY c.id, c.nome";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Cliente cliente = new Cliente();
			cliente.setId(rs.getInt("id"));
			cliente.setNome(rs.getString("nome"));
			cliente.setTotalCompras(rs.getFloat("totalCompras"));
			clientes.add(cliente);
		}

		rs.close();
		ps.close();
		c.close();

		return clientes;
	}
}
