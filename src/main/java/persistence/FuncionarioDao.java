package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Funcionario;

public class FuncionarioDao implements ICrud<Funcionario> {

	private GenericDao gDao;

	public FuncionarioDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Funcionario funcionario) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO funcionario (nome, username, senha) VALUES (?, ?, ?)";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, funcionario.getNome());
		ps.setString(2, funcionario.getUserName());
		ps.setString(3, funcionario.getSenha());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void atualizar(Funcionario funcionario) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		PreparedStatement ps = null;

		try {
			if (funcionario.getNome() != null && !funcionario.getNome().isEmpty()) {
				String sql = "UPDATE funcionario SET nome = ? WHERE id = ?";
				ps = c.prepareStatement(sql);
				ps.setString(1, funcionario.getNome());
				ps.setInt(2, funcionario.getId());
				ps.execute();
				ps.close();
			}

			if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
				String sql = "UPDATE funcionario SET senha = ? WHERE id = ?";
				ps = c.prepareStatement(sql);
				ps.setString(1, funcionario.getSenha());
				ps.setInt(2, funcionario.getId());
				ps.execute();
				ps.close();
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			c.close();
		}
	}

	@Override
	public void excluir(Funcionario funcionario) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM funcionario WHERE id = ? OR nome = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, funcionario.getId());
		ps.setString(2, funcionario.getNome());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Funcionario consultar(Funcionario funcionario) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, username FROM funcionario WHERE id = ? OR nome = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, funcionario.getId());
		ps.setString(2, funcionario.getNome());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			funcionario.setId(rs.getInt("id"));
			funcionario.setNome(rs.getString("nome"));
			funcionario.setUserName(rs.getString("username"));
		}
		rs.close();
		ps.close();
		c.close();

		return funcionario;
	}

	@Override
	public List<Funcionario> listar() throws SQLException, ClassNotFoundException {
		List<Funcionario> funcionarios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, username FROM funcionario";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Funcionario fun = new Funcionario();
			fun.setId(rs.getInt("id"));
			fun.setNome(rs.getString("nome"));
			fun.setUserName(rs.getString("username"));
			funcionarios.add(fun);
		}

		rs.close();
		ps.close();
		c.close();

		return funcionarios;
	}

	public Funcionario autenticar(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, username FROM funcionario WHERE username = ? AND senha = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, funcionario.getUserName());
		ps.setString(2, funcionario.getSenha());
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			funcionario.setId(rs.getInt("id"));
			funcionario.setNome(rs.getString("nome"));
			funcionario.setUserName(rs.getString("username"));
		} else {
			funcionario = null;
		}
		rs.close();
		ps.close();
		c.close();

		return funcionario;
	}

	public int qtdVendas(Funcionario funcionario) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT COUNT(*) AS total FROM venda WHERE funcionarioID = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, funcionario.getId());
		ResultSet rs = ps.executeQuery();

		int qtdVendas = 0;
		if (rs.next()) {
			qtdVendas = rs.getInt("total");
		}

		rs.close();
		ps.close();
		c.close();

		return qtdVendas;
	}

	public List<Funcionario> listarFuncionariosComVendas() throws SQLException, ClassNotFoundException {
		List<Funcionario> funcionarios = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT f.id, f.nome, COUNT(v.id) AS total_vendas " + "FROM funcionario f "
				+ "LEFT JOIN venda v ON f.id = v.funcionarioID " + "GROUP BY f.id, f.nome";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Funcionario funcionario = new Funcionario();
			funcionario.setId(rs.getInt("id"));
			funcionario.setNome(rs.getString("nome"));
			funcionario.setTotalVendas(rs.getInt("total_vendas"));
			funcionarios.add(funcionario);
		}

		rs.close();
		ps.close();
		c.close();

		return funcionarios;
	}

	public float valorVenda(Funcionario funcionario) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT SUM(total) as total FROM venda WHERE funcionarioID = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, funcionario.getId());
		ResultSet rs = ps.executeQuery();

		float valorVendas = 0;
		if (rs.next()) {
			valorVendas = rs.getFloat(1);
		}

		rs.close();
		ps.close();
		c.close();

		return valorVendas;
	}

	public boolean existe(int id) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id FROM funcionario WHERE id = ?";
		boolean existe = false;

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			existe = true;
		}

		rs.close();
		ps.close();
		c.close();

		return existe;
	}
}