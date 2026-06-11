package com.controleestoque.controller;

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

import com.controleestoque.model.dto.ProdutoListagemDTO;
import com.controleestoque.model.entity.Produto;
import com.controleestoque.service.ProdutoService;
import com.controleestoque.util.EntityContext;

@WebServlet("/produto")
public class ProdutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ProdutoService produtoService = new ProdutoService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityContext ctx = new EntityContext(request);
		String cmd = ctx.getParam("enviar");
		String saida = "";
		String erro = "";

		Produto p = new Produto();
		List<ProdutoListagemDTO> produtos = new ArrayList<>();

		try {
			if (cmd.equals("Cadastrar")) {
				String nome = ctx.getParam("nome");
				if (nome != null && !nome.isEmpty()) {
					p.setNome(nome);
					p.setValor(ctx.getFloatParam("valor", 0));
					p.setQuantidade(ctx.getIntParam("quantidade", 0));
					produtoService.cadastrar(p);
					saida = "Produto cadastrado com sucesso!";
				} else {
					saida = "Nome do produto não especificado.";
				}
			}
			if (cmd.equals("Listar")) {
				produtos = produtoService.listar();
			}
			if (cmd.equals("Alterar")) {
				int id = ctx.getIntParam("id", 0);
				if (id > 0) {
					p.setId(id);
					p.setValor(ctx.getFloatParam("valor", 0));
					String nome = ctx.getParam("nome");
					if (nome != null && !nome.isEmpty()) {
						p.setNome(nome);
					}
					produtoService.alterar(p);
					saida = "Produto alterado com sucesso.";
				} else {
					saida = "ID do produto não especificado.";
				}
			}
			if (cmd.equals("Buscar")) {
				String nome = ctx.getParam("nome");
				if (nome != null && !nome.isEmpty()) {
					produtos = produtoService.consultarPorNome(nome);
				} else {
					saida = "Informe um nome para buscar o produto!";
				}
			}
			if (cmd.equals("Entrada/Saída")) {
				int id = ctx.getIntParam("id", 0);
				int qtd = ctx.getIntParam("quantidade", 0);
				if (id > 0) {
					p.setId(id);
					p.setQuantidade(qtd);
					if (qtd > 0) {
						produtoService.adicionarQuantidade(p, qtd);
						saida = "Quantidade adicionada com sucesso!";
					} else if (qtd < 0) {
						produtoService.removerQuantidade(p, qtd * -1);
						saida = "Quantidade removida com sucesso!";
					} else {
						saida = "Quantidade deve ser diferente de zero.";
					}
					produtos = produtoService.listar();
				} else {
					erro = "ID e/ou Quantidade não especificados.";
					saida = erro;
				}
			}
			if (cmd.equals("Listar Vendidos")) {
				produtos = produtoService.listarVendidos();
			}
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("produto", p);
			request.setAttribute("produtos", produtos);

			RequestDispatcher rd = request.getRequestDispatcher("views/produto/listar.jsp");
			rd.forward(request, response);
		}
	}
}
