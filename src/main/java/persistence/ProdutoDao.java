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
        String sql = "INSERT INTO produto (nome, valor, quantidade, estoqueID) VALUES (?, ?, ?, ?)";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, t.getNome());
        ps.setDouble(2, t.getValor());

        adicionarRelatorio(t);

        ps.executeUpdate();
        ps.close();
        c.close();
    }
    
    @Override
    public void atualizar(Produto p) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql;
        PreparedStatement ps;

        if (p.getNome() != null && !p.getNome().isEmpty()) {
            sql = "UPDATE produto SET nome = ?, valor = ? WHERE id = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getValor());
            ps.setInt(3, p.getId());
        } else {
            sql = "UPDATE produto SET valor = ? WHERE id = ?";
            ps = c.prepareStatement(sql);
            ps.setDouble(1, p.getValor());
            ps.setInt(2, p.getId());
        }

        ps.executeUpdate();
        ps.close();
        c.close();
    }

    @Override
    public void excluir(Produto t) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "DELETE FROM produto WHERE id = ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, t.getId());
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    @Override
    public List<Produto> listar() throws SQLException, ClassNotFoundException {
        List<Produto> produtos = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT id, nome, valor, quantidade FROM produto WHERE nome LIKE ?";
        
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
        String sql = "SELECT id, nome, valor, quantidade FROM produto WHERE nome LIKE ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, "%" + t.getNome() + "%");
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

    public void adicionarQuantidade(Produto produto, int quantidade) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, produto.getId());

        ps.executeUpdate();
        ps.close();
        c.close();

        adicionarRelatorio(produto, quantidade);
    }

    public void removerQuantidade(Produto produto, int quantidade) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";
       
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, produto.getId());

        ps.executeUpdate();
        ps.close();
        c.close();

        adicionarRelatorio(produto, -quantidade);
    }

    private void adicionarRelatorio(Produto produto) throws ClassNotFoundException, SQLException {
        Relatorio relatorio = new Relatorio();
        relatorio.setDescricao("Produto cadastrado: " + produto.getNome().toString() + ", valor R$" + produto.getValor());

        RelatorioDao rDao = new RelatorioDao(gDao);
        rDao.inserir(relatorio);
    }

    private void adicionarRelatorio(Produto produto, int quantidade) throws ClassNotFoundException, SQLException {
        Relatorio relatorio = new Relatorio();
        String descricaoProduto;

        if (quantidade > 0) {
            descricaoProduto = ("Entrada de " + quantidade + " unidades do produto #ID " + produto.getId());
        } else {
            descricaoProduto = ("Saída de " + (quantidade * (-1)) + " unidades do produto #ID " + produto.getId());
        }

        relatorio.setId(1);
        relatorio.setDescricao(descricaoProduto);

        RelatorioDao rDao = new RelatorioDao(gDao);
        rDao.inserir(relatorio);
    }
}