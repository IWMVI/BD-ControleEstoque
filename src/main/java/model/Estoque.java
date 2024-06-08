package model;

import java.util.List;

public class Estoque {

	private int id;
	private String nome;
	private List<Produto> produtos;
	private int qtdTotal;
	private int quantidade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public int getQtdTotal() {
		return qtdTotal;
	}

	public void setQtdTotal(int qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}
