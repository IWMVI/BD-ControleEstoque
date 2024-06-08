package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import persistence.GenericDao;

public class ClienteCommandContar implements ICommand {

    private GenericDao gDao;

    public ClienteCommandContar(GenericDao gDao) {
        this.gDao = gDao;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int quantidade = contarClientes();
            request.setAttribute("quantidade", quantidade);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/cliente.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException | ClassNotFoundException | ServletException | IOException e) {
            request.setAttribute("erro", e.getMessage());
            try {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/cliente.jsp");
                dispatcher.forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int contarClientes() throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT COUNT(*) AS total FROM cliente";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        int total = 0;
        if (rs.next()) {
            total = rs.getInt("total");
        }

        rs.close();
        ps.close();
        c.close();

        return total;
    }
}
