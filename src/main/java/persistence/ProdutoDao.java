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
	public void inserir(Produto produto) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO produto (nome, valor, quantidade) VALUES (?, ?, ?)";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, produto.getNome());
		ps.setFloat(2, produto.getValor());
		ps.setInt(3, produto.getQuantidade());

		ps.execute();
		ps.close();
		c.close();

		adicionarRelatorio(produto);
	}

	@Override
	public void atualizar(Produto produto) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "";
		PreparedStatement ps = null;

		if (produto.getNome() != null && !produto.getNome().isEmpty()) {
			sql = "UPDATE produto SET nome = ? WHERE id = ?";
			ps = c.prepareStatement(sql);
			ps.setString(1, produto.getNome());
			ps.setInt(2, produto.getId());
			ps.execute();
		}
		if (produto.getValor() > 0) {
			sql = "UPDATE produto SET valor = ? WHERE id = ?";
			ps = c.prepareStatement(sql);
			ps.setFloat(1, produto.getValor());
			ps.setInt(2, produto.getId());
			ps.execute();
		}
		if (produto.getQuantidade() > 0) {
			sql = "UPDATE produto SET quantidade = ? WHERE id = ?";
			ps = c.prepareStatement(sql);
			ps.setInt(1, produto.getQuantidade());
			ps.setInt(2, produto.getId());
			ps.execute();
		}

		ps.close();
		c.close();
	}

	@Override
	public void excluir(Produto produto) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE FROM produto WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, produto.getId());

		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public List<Produto> listar() throws SQLException, ClassNotFoundException {
		List<Produto> produtos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, valor, quantidade FROM produto";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Produto produto = new Produto();
			produto.setId(rs.getInt("id"));
			produto.setNome(rs.getString("nome"));
			produto.setValor(rs.getFloat("valor"));
			produto.setQuantidade(rs.getInt("quantidade"));
			produtos.add(produto);
		}

		rs.close();
		ps.close();
		c.close();

		return produtos;
	}

	@Override
	public Produto consultar(Produto produto) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT id, nome, valor, quantidade FROM produto WHERE nome = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, produto.getNome());

		ResultSet rs = ps.executeQuery();
		Produto pCons = null;
		if (rs.next()) {
			pCons = new Produto();
			pCons.setId(rs.getInt("id"));
			pCons.setNome(rs.getString("nome"));
			pCons.setValor(rs.getFloat("valor"));
			pCons.setQuantidade(rs.getInt("quantidade"));
		}

		rs.close();
		ps.close();
		c.close();

		return pCons;
	}

	public void adicionarQuantidade(Produto produto, int quantidade) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, quantidade);
		ps.setInt(2, produto.getId());

		ps.execute();
		ps.close();
		c.close();

		adicionarRelatorio(produto, quantidade);
	}

	public void removerQuantidade(Produto produto, int quantidade) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";

		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, quantidade);
		ps.setInt(2, produto.getId());

		ps.execute();
		ps.close();
		c.close();

		adicionarRelatorio(produto, -quantidade);
	}

	public List<Produto> listarProdutosComQuantidadeVendida() throws SQLException, ClassNotFoundException {
		List<Produto> produtos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT p.id, p.nome, p.valor, SUM(vp.quantidade) AS total_vendida " + "FROM produto p "
				+ "JOIN venda_produto vp ON p.id = vp.produtoID " + "GROUP BY p.id, p.nome, p.valor";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Produto produto = new Produto();
			produto.setId(rs.getInt("id"));
			produto.setNome(rs.getString("nome"));
			produto.setValor(rs.getFloat("valor"));
			produto.setQuantidade(rs.getInt("total_vendida"));
			produtos.add(produto);
		}

		rs.close();
		ps.close();
		c.close();

		return produtos;
	}

	private void adicionarRelatorio(Produto produto) throws ClassNotFoundException, SQLException {
		Relatorio relatorio = new Relatorio();
		relatorio.setDescricao("Produto cadastrado: " + produto.getNome() + ", valor R$" + produto.getValor()
				+ ", quantidade: " + produto.getQuantidade());

		RelatorioDao rDao = new RelatorioDao(gDao);
		rDao.inserir(relatorio);
	}

	private void adicionarRelatorio(Produto produto, int quantidade) throws ClassNotFoundException, SQLException {
		Relatorio relatorio = new Relatorio();
		String descricaoProduto = "";

		if (quantidade > 0) {
			descricaoProduto = ("Entrada de " + quantidade + " unidades do produto #ID " + produto.getId());
		}
		if (quantidade < 0) {
			descricaoProduto = ("Saída de " + (-quantidade) + " unidades do produto #ID " + produto.getId());
		}

		relatorio.setId(1);
		relatorio.setDescricao(descricaoProduto);

		RelatorioDao rDao = new RelatorioDao(gDao);
		rDao.inserir(relatorio);
	}

	public List<Produto> listarProdutosComEstoqueDisponivel() throws SQLException, ClassNotFoundException {
		List<Produto> produtos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT p.id, p.nome, p.valor, e.quantidade " + "FROM produto p "
				+ "JOIN estoque e ON p.id = e.produtoID " + "WHERE e.quantidade > 0";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Produto produto = new Produto();
			produto.setId(rs.getInt("id"));
			produto.setNome(rs.getString("nome"));
			produto.setValor(rs.getFloat("valor"));
			produto.setQuantidade(rs.getInt("quantidade"));
			produtos.add(produto);
		}

		rs.close();
		ps.close();
		c.close();

		return produtos;
	}

}
