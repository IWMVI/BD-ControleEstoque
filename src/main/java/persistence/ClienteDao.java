package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDao implements ICrud<Cliente> {

	private GenericDao gDao;

	public ClienteDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Cliente cliente) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO cliente (nome) VALUES (?)";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cliente.getNome());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void atualizar(Cliente cliente) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE cliente SET nome = ? WHERE id = ?";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, cliente.getNome());
		ps.setInt(2, cliente.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void excluir(Cliente cliente) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE cliente WHERE id = ?";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, cliente.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Cliente consultar(Cliente cliente) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome FROM cliente WHERE id = ?";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, cliente.getId());
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			cliente.setId(rs.getInt("id"));
			cliente.setNome(rs.getString("nome"));
		
		}
		
		rs.close();
		ps.close();
		c.close();

		return cliente;
	}

	@Override
	public List<Cliente> listar() throws SQLException, ClassNotFoundException {
		List<Cliente> cliente = new ArrayList<>();
		Connection c= gDao.getConnection();
		String sql = "SELECT id, nome FROM cliente";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Cliente cli = new Cliente();
			cli.setId(rs.getInt("id"));
			cli.setNome(rs.getString("nome"));
			cliente.add(cli);
		}
		
		rs.close();
		ps.close();
		c.close();
		
		return cliente;
	}
	
	public int contarClientes() throws SQLException, ClassNotFoundException {
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
}
