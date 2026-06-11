package com.controleestoque.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.controleestoque.config.DatabaseConfig;

public abstract class AbstractDao<T> {

    @FunctionalInterface
    public interface ParamBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface RowMapper<R> {
        R map(ResultSet rs) throws SQLException;
    }

    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        return DatabaseConfig.getConnection();
    }

    protected int executeInsert(String sql, ParamBinder binder) throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            binder.bind(ps);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    protected <R> List<R> executeQuery(String sql, ParamBinder binder, RowMapper<R> mapper)
            throws SQLException, ClassNotFoundException {
        List<R> results = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        }
        return results;
    }

    protected <R> Optional<R> executeSingleResult(String sql, ParamBinder binder, RowMapper<R> mapper)
            throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        }
    }

    protected int executeUpdate(String sql, ParamBinder binder) throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            return ps.executeUpdate();
        }
    }

    protected int executeAggregate(String sql, ParamBinder binder, String column)
            throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(column) : 0;
            }
        }
    }

    protected float executeAggregateFloat(String sql, ParamBinder binder, String column)
            throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getFloat(column) : 0;
            }
        }
    }

    protected int executeTransaction(TransactionCallback callback)
            throws SQLException, ClassNotFoundException {
        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try {
                int result = callback.execute(c);
                c.commit();
                return result;
            } catch (SQLException | ClassNotFoundException e) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                }
                throw e;
            }
        }
    }

    @FunctionalInterface
    public interface TransactionCallback {
        int execute(Connection c) throws SQLException, ClassNotFoundException;
    }
}
