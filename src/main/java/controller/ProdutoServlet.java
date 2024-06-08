package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/produto")
public class ProdutoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Map<String, IProdutoCommand> commands;

    public ProdutoServlet() {
        super();
        commands = new HashMap<>();
        commands.put("Cadastrar", new CadastrarProdutoCommand());
        commands.put("Listar", new ListarProdutoCommand());
        commands.put("Alterar", new AlterarProdutoCommand());
        commands.put("Buscar", new BuscarProdutoCommand());
        commands.put("Entrada/Saída", new EntradaSaidaProdutoCommand());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("produto.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmd = request.getParameter("enviar");
        IProdutoCommand command = commands.get(cmd);

        if (command != null) {
            try {
                command.execute(request, response);
            } catch (ClassNotFoundException | SQLException e) {
                request.setAttribute("erro", e.getMessage());
            }
        } else {
            request.setAttribute("saida", "Comando não reconhecido.");
        }

        RequestDispatcher rd = request.getRequestDispatcher("produto.jsp");
        rd.forward(request, response);
    }
}
