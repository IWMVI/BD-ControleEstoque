package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import persistence.GenericDao;

public class ProdutoCommandValorMedio implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		Connection c = gDao.getConnection();
		String sql = "SELECT AVG(valor) AS valor_medio FROM produto";

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		float valorMedio = 0;
		if (rs.next()) {
			valorMedio = rs.getFloat("valor_medio");
		}

		rs.close();
		ps.close();
		c.close();

		request.setAttribute("valorMedio", valorMedio);
	}
}
