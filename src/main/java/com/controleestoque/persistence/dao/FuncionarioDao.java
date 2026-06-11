package com.controleestoque.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Funcionario;

public class FuncionarioDao extends AbstractDao<Funcionario> {

    private final RowMapper<Funcionario> FUNCIONARIO_MAPPER = rs -> {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setUserName(rs.getString("username"));
        return f;
    };

    public void inserir(Funcionario funcionario) throws SQLException, ClassNotFoundException {
        executeInsert(
            "INSERT INTO funcionario (nome, username, senha) VALUES (?, ?, ?)",
            ps -> {
                ps.setString(1, funcionario.getNome());
                ps.setString(2, funcionario.getUserName());
                ps.setString(3, funcionario.getSenha());
            });
    }

    public void atualizar(Funcionario funcionario) throws SQLException, ClassNotFoundException {
        executeTransaction(c -> {
            if (funcionario.getNome() != null && !funcionario.getNome().isEmpty()) {
                try (var ps = c.prepareStatement(
                        "UPDATE funcionario SET nome = ? WHERE id = ?")) {
                    ps.setString(1, funcionario.getNome());
                    ps.setInt(2, funcionario.getId());
                    ps.execute();
                }
            }
            if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
                try (var ps = c.prepareStatement(
                        "UPDATE funcionario SET senha = ? WHERE id = ?")) {
                    ps.setString(1, funcionario.getSenha());
                    ps.setInt(2, funcionario.getId());
                    ps.execute();
                }
            }
            return 0;
        });
    }

    public void excluir(Funcionario funcionario) throws SQLException, ClassNotFoundException {
        executeUpdate(
            "DELETE FROM funcionario WHERE id = ? OR nome = ?",
            ps -> {
                ps.setInt(1, funcionario.getId());
                ps.setString(2, funcionario.getNome());
            });
    }

    public Funcionario consultar(Funcionario funcionario) throws SQLException, ClassNotFoundException {
        return executeSingleResult(
            "SELECT id, nome, username FROM funcionario WHERE id = ? OR nome = ?",
            ps -> {
                ps.setInt(1, funcionario.getId());
                ps.setString(2, funcionario.getNome());
            },
            FUNCIONARIO_MAPPER).orElse(funcionario);
    }

    public List<Funcionario> listar() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT id, nome, username FROM funcionario",
            null, FUNCIONARIO_MAPPER);
    }

    public Funcionario autenticar(Funcionario funcionario) throws ClassNotFoundException, SQLException {
        return executeSingleResult(
            "SELECT id, nome, username FROM funcionario WHERE username = ? AND senha = ?",
            ps -> {
                ps.setString(1, funcionario.getUserName());
                ps.setString(2, funcionario.getSenha());
            },
            FUNCIONARIO_MAPPER).orElse(null);
    }

    public int qtdVendas(Funcionario funcionario) throws SQLException, ClassNotFoundException {
        return executeAggregate(
            "SELECT COUNT(*) AS total FROM venda WHERE funcionarioID = ?",
            ps -> ps.setInt(1, funcionario.getId()),
            "total");
    }

    public List<Funcionario> listarFuncionariosComVendas() throws SQLException, ClassNotFoundException {
        return executeQuery(
            "SELECT f.id, f.nome, COUNT(v.id) AS total_vendas "
                + "FROM funcionario f "
                + "LEFT JOIN venda v ON f.id = v.funcionarioID "
                + "GROUP BY f.id, f.nome",
            null, rs -> {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTotalVendas(rs.getInt("total_vendas"));
                return f;
            });
    }

    public float valorVenda(Funcionario funcionario) throws ClassNotFoundException, SQLException {
        return executeAggregateFloat(
            "SELECT SUM(total) as total FROM venda WHERE funcionarioID = ?",
            ps -> ps.setInt(1, funcionario.getId()),
            "total");
    }

    public boolean existe(int id) throws ClassNotFoundException, SQLException {
        return executeSingleResult(
            "SELECT id FROM funcionario WHERE id = ?",
            ps -> ps.setInt(1, id),
            rs -> rs.getInt("id")).isPresent();
    }
}
