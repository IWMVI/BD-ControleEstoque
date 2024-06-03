package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Estoque;
import model.Produto;

public class EstoqueDao {

    private GenericDao gDao;

    public EstoqueDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public void inserir(Produto p, int quantidade) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE id = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, p.getId());
        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public void atualizar(Estoque estoque) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE id = ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, estoque.getQuantidade());
        ps.setInt(2, estoque.getId());
        ps.execute();
        ps.close();
        c.close();
    }

    public void excluir(Estoque estoque) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "DELETE FROM estoque WHERE id = ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, estoque.getId());
        ps.execute();
        ps.close();
        c.close();
    }

    public List<Estoque> listar() throws SQLException, ClassNotFoundException {
        List<Estoque> estoques = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT id, quantidade FROM estoque";
        
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Estoque estoque = new Estoque();
            estoque.setId(rs.getInt("id"));
            estoque.setQuantidade(rs.getInt("quantidade"));
            estoques.add(estoque);
        }

        rs.close();
        ps.close();
        c.close();

        return estoques;
    }

    public Estoque consultarPorId(int id) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT nome, quantidade FROM estoque WHERE id = ?"; 

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Estoque estoque = null;
        if (rs.next()) {
            estoque = new Estoque();
            estoque.setId(id);
            estoque.setNome(rs.getString("nome"));
            estoque.setQuantidade(rs.getInt("quantidade"));
        }

        rs.close();
        ps.close();
        c.close();

        return estoque;
    }

    
    public void adicionarQuantidade(int quantidade) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql;
        
        if (quantidade >= 0) {
            sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE id = ?";
        } else {
            sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE id = ?";
            quantidade = Math.abs(quantidade);
        }
        
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, 1);
        ps.executeUpdate();
        ps.close();
        c.close();
    }

}