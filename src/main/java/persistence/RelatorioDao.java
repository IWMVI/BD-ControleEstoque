package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Relatorio;

public class RelatorioDao implements ICrudRelatorio<Relatorio> {

	private GenericDao gDao;

	public RelatorioDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Relatorio relatorio) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO relatorio (descricao, vendaID) VALUES (?, ?)";
		PreparedStatement ps = c.prepareStatement(sql);

		ps.setString(1, relatorio.getDescricao());
		ps.setInt(2, relatorio.getVendaID());

		ps.execute();

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			int id = rs.getInt(1);
			relatorio.setId(id);
		}

		rs.close();
		ps.close();
		c.close();
	}

	@Override
	public List<Relatorio> listar() throws SQLException, ClassNotFoundException {
		List<Relatorio> relatorios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, descricao, vendaID FROM relatorio";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Relatorio relatorio = new Relatorio();
			relatorio.setId(rs.getInt("id"));
			relatorio.setDescricao(rs.getString("descricao"));
			relatorio.setVendaID(rs.getInt("vendaID"));
			relatorios.add(relatorio);
		}

		rs.close();
		ps.close();
		c.close();

		return relatorios;
	}
	
	public int somarVendasCliente(int clienteID) throws SQLException, ClassNotFoundException {
	    Connection c = gDao.getConnection();
	    String sql = "SELECT SUM(valor) AS total_vendas FROM venda WHERE clienteID = ?";
	    
	    PreparedStatement ps = c.prepareStatement(sql);
	    ps.setInt(1, clienteID);
	    ResultSet rs = ps.executeQuery();
	    
	    int totalVendas = 0;
	    if (rs.next()) {
	        totalVendas = rs.getInt("total_vendas");
	    }
	    
	    rs.close();
	    ps.close();
	    c.close();
	    
	    return totalVendas;
	}
}
