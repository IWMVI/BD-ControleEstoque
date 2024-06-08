package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public interface IProdutoCommand {
    void execute(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException;
}
