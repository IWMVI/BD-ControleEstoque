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
import model.ItemVenda;
import model.Venda;
import persistence.GenericDao;
import persistence.VendaDao;
import persistence.FuncionarioDao;

@WebServlet("/venda")
public class VendaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public VendaServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Venda> vendas = listarVendas();
            request.setAttribute("vendas", vendas);
            RequestDispatcher rd = request.getRequestDispatcher("vendas.jsp");
            rd.forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmd = request.getParameter("enviar");
        String erro = "";
        String saida = "";

        if ("Registrar Venda".equals(cmd)) {
            try {
                String clienteId = request.getParameter("clienteId");
                String funcionarioId = request.getParameter("funcionarioId");
                String[] produtoIds = request.getParameterValues("produtoId");
                String[] quantidades = request.getParameterValues("quantidade");

                if (!funcionarioExiste(funcionarioId)) {
                    throw new IllegalArgumentException("Funcionário não encontrado. Verifique o ID do funcionário.");
                }

                if (produtoIds != null && quantidades != null && produtoIds.length == quantidades.length) {
                    List<ItemVenda> itens = new ArrayList<>();

                    for (int i = 0; i < produtoIds.length; i++) {
                        ItemVenda item = new ItemVenda();
                        item.setProdutoID(Integer.parseInt(produtoIds[i]));
                        item.setQuantidade(Integer.parseInt(quantidades[i]));
                        itens.add(item);
                    }

                    registrarVenda(clienteId, funcionarioId, itens);
                    saida = "Venda registrada com sucesso!";
                } else {
                    erro = "Dados incompletos para registrar a venda.";
                }
            } catch (ClassNotFoundException | SQLException | IllegalArgumentException e) {
                erro = e.getMessage();
            }
        }

        request.setAttribute("saida", saida);
        request.setAttribute("erro", erro);

        doGet(request, response); 
    }

    private void registrarVenda(String clienteId, String funcionarioId, List<ItemVenda> itens)
            throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        VendaDao vDao = new VendaDao(gDao);

        Venda venda = new Venda();
        venda.setClienteID(Integer.parseInt(clienteId));
        venda.setFuncionarioID(Integer.parseInt(funcionarioId));
        venda.setItens(itens);
        vDao.registrar(venda);
    }

    private List<Venda> listarVendas() throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        VendaDao vDao = new VendaDao(gDao);

        return vDao.listar();
    }

    private boolean funcionarioExiste(String funcionarioId) throws ClassNotFoundException, SQLException {
        GenericDao gDao = new GenericDao();
        FuncionarioDao fDao = new FuncionarioDao(gDao);

        return fDao.existe(Integer.parseInt(funcionarioId));
    }
}
