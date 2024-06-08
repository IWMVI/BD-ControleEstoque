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

    public VendaDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public void inserir(Venda venda) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "INSERT INTO venda (clienteID, funcionarioID) VALUES (?, ?)";

        PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, venda.getClienteID());
        ps.setInt(2, venda.getFuncionarioID());

        ps.execute();
        
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            venda.setId(rs.getInt(1));
        }
        
        ps.close();
        c.close();

        inserirItensVenda(venda);
    }

    private void inserirItensVenda(Venda venda) throws SQLException, ClassNotFoundException {
        for (ItemVenda item : venda.getItens()) {
            Connection c = gDao.getConnection();
            String sql = "INSERT INTO item_venda (vendaID, produtoID, quantidade) VALUES (?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, venda.getId());
            ps.setInt(2, item.getProdutoID());
            ps.setInt(3, item.getQuantidade());

            ps.execute();
            ps.close();
            c.close();
        }
    }

    public List<Venda> listar() throws SQLException, ClassNotFoundException {
        List<Venda> vendas = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT id, clienteID, funcionarioID FROM venda";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Venda venda = new Venda();
            venda.setId(rs.getInt("id"));
            venda.setClienteID(rs.getInt("clienteID"));
            venda.setFuncionarioID(rs.getInt("funcionarioID"));
            
            List<ItemVenda> itens = recuperarItensVenda(venda.getId());
            venda.setItens(itens);
            
            vendas.add(venda);
        }

        rs.close();
        ps.close();
        c.close();

        return vendas;
    }

    private List<ItemVenda> recuperarItensVenda(int vendaID) throws SQLException, ClassNotFoundException {
        List<ItemVenda> itens = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT produtoID, quantidade FROM item_venda WHERE vendaID = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, vendaID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ItemVenda item = new ItemVenda();
            item.setProdutoID(rs.getInt("produtoID"));
            item.setQuantidade(rs.getInt("quantidade"));
            itens.add(item);
        }

        rs.close();
        ps.close();
        c.close();

        return itens;
    }

    public Venda consultar(int idVenda) throws SQLException, ClassNotFoundException {
        Venda venda = null;
        Connection c = gDao.getConnection();
        String sql = "SELECT clienteID, funcionarioID FROM venda WHERE id = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idVenda);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            venda = new Venda();
            venda.setId(idVenda);
            venda.setClienteID(rs.getInt("clienteID"));
            venda.setFuncionarioID(rs.getInt("funcionarioID"));

            List<ItemVenda> itens = recuperarItensVenda(idVenda);
            venda.setItens(itens);
        }

        rs.close();
        ps.close();
        c.close();

        return venda;
    }
    
    public List<Venda> listarVendasDetalhadas() throws SQLException, ClassNotFoundException {
        List<Venda> vendas = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT v.id, v.clienteID, v.funcionarioID, c.nome AS clienteNome, f.nome AS funcionarioNome " +
                     "FROM venda v " +
                     "JOIN cliente c ON v.clienteID = c.id " +
                     "JOIN funcionario f ON v.funcionarioID = f.id";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Venda venda = new Venda();
            venda.setId(rs.getInt("id"));
            venda.setClienteID(rs.getInt("clienteID"));
            venda.setFuncionarioID(rs.getInt("funcionarioID"));
            venda.setClienteNome(rs.getString("clienteNome"));
            venda.setFuncionarioNome(rs.getString("funcionarioNome"));
            vendas.add(venda);
        }

        rs.close();
        ps.close();
        c.close();

        return vendas;
    }

}
