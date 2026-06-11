package com.controleestoque.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Relatorio;

public class RelatorioDao extends AbstractDao<Relatorio> {

    private final RowMapper<Relatorio> RELATORIO_MAPPER = rs -> {
        Relatorio r = new Relatorio();
        r.setId(rs.getInt("id"));
        r.setDescricao(rs.getString("descricao"));
        r.setVendaID(rs.getInt("vendaID"));
        return r;
    };

    public void inserir(Relatorio relatorio) throws SQLException, ClassNotFoundException {
        executeInsert(
            "INSERT INTO relatorio (descricao, vendaID) VALUES (?, ?)",
            ps -> {
                ps.setString(1, relatorio.getDescricao());
                if (relatorio.getVendaID() > 0) {
                    ps.setInt(2, relatorio.getVendaID());
                } else {
                    ps.setNull(2, java.sql.Types.INTEGER);
                }
            });
    }

    public List<Relatorio> listar() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT id, descricao, vendaID FROM relatorio",
            null, RELATORIO_MAPPER);
    }

    public float somarVendas() throws SQLException, ClassNotFoundException {
        return executeAggregateFloat(
            "SELECT SUM(total) AS total_vendas FROM venda",
            null, "total_vendas");
    }

    public int contarVendas() throws SQLException, ClassNotFoundException {
        return executeAggregate(
            "SELECT COUNT(*) AS total_vendas FROM venda",
            null, "total_vendas");
    }
}
