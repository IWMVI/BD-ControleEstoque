package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;
import model.Relatorio;

public class ProdutoDao implements ICrud<Produto> {

	private GenericDao gDao;

	public ProdutoDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Produto t) throws SQLException, ClassNotFoundException {
	    Connection c = gDao.getConnection();
	    
	    String sql = "INSERT INTO produto (nome, valor) VALUES (?, ?)";

	    
	    PreparedStatement ps = c.prepareStatement(sql);
	    ps.setString(1, t.getNome());
	    ps.setFloat(2, t.getValor());

	    ps.execute();
	    ps.close();
	    c.close();

	    adicionarRelatorio(t);
	}


	@Override
	public void atualizar(Produto p) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE produto SET nome = ?, valor = ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, p.getNome());
		ps.setFloat(2, p.getValor());
		ps.setInt(3, p.getId());
		ps.execute();

		ps.close();
		c.close();
	}

	@Override
	public void excluir(Produto t) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM produto WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, t.getId());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public List<Produto> listar() throws SQLException, ClassNotFoundException {
		List<Produto> produtos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, valor FROM produto";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Produto p = new Produto();
			p.setId(rs.getInt("id"));
			p.setNome(rs.getString("nome"));
			p.setValor(rs.getFloat("valor"));
			produtos.add(p);
		}

		rs.close();
		ps.close();
		c.close();

		return produtos;
	}

	@Override
	public Produto consultar(Produto t) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, valor FROM produto WHERE nome LIKE ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		Produto produto = null;
		if (rs.next()) {
			produto = new Produto();
			produto.setId(rs.getInt("id"));
			produto.setNome(rs.getString("nome"));
			produto.setValor(rs.getFloat("valor"));
		}

		rs.close();
		ps.close();
		c.close();

		return produto;
	}

	public void adicionarQuantidade(Produto p, int quantidade) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, quantidade);
		ps.setInt(2, p.getId());

		ps.execute();
		ps.close();
		c.close();

		adicionarRelatorio(p, quantidade);
	}

	public void removerQuantidade(Produto p, int quantidade) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, quantidade);
		ps.setInt(2, p.getId());

		ps.execute();
		ps.close();
		c.close();

		adicionarRelatorio(p, quantidade);
	}

	private void adicionarRelatorio(Produto produto) throws ClassNotFoundException, SQLException {
		Relatorio relatorio = new Relatorio();
		relatorio.setDescricao("Produto cadastrado: " + produto.getNome() + ", valor R$" + produto.getValor());

		RelatorioDao rDao = new RelatorioDao(gDao);
		rDao.inserir(relatorio);
	}

	public void adicionarRelatorio(Produto produto, int quantidade) throws ClassNotFoundException, SQLException {
		Relatorio relatorio = new Relatorio();
		String descricao = "";

		if (quantidade > 0) {
			descricao = "Entrada de " + quantidade + " itens do produto #ID " + produto.getId();
		}
		if (quantidade < 0) {
			descricao = "Saída de " + (quantidade * (-1)) + " itens do produto #ID " + produto.getId();
		}

		relatorio.setId(1);
		relatorio.setDescricao(descricao);

		RelatorioDao rDao = new RelatorioDao(gDao);
		rDao.inserir(relatorio);
	}
}
