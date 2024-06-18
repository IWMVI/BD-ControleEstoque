package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Relatorio;
import persistence.GenericDao;
import persistence.RelatorioDao;

@WebServlet("/relatorio")
public class RelatorioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RelatorioServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Relatorio> relatorios = new ArrayList<>();
		String cmd = request.getParameter("botao");
		String saida = "";
		String erro = "";

		try {
			GenericDao gDao = new GenericDao();
			RelatorioDao rDao = new RelatorioDao(gDao);

			if ("Listar".equals(cmd)) {
				relatorios = listarRelatorios();
			}
			if ("Total".equals(cmd)) {
				float qtd = rDao.somarVendas();
				saida = "Total em vendas: " + qtd;
			}
			if ("Vendas cliente".equals(cmd)) {
				int qtd = rDao.contarVendas();
				saida = "Total de vendas: " + qtd;
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("relatorios", relatorios);

			RequestDispatcher rd = request.getRequestDispatcher("relatorio.jsp");
			rd.forward(request, response);
		}
	}

	private List<Relatorio> listarRelatorios() throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		RelatorioDao rDao = new RelatorioDao(gDao);
		return rDao.listar();
	}
}
