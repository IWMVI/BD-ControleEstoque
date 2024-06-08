package model;

import java.util.List;

public class Relatorio {

	private int id;
	private String descricao;
	private List<Produto> produtos;
	private int vendaID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public int getVendaID() {
		return vendaID;
	}

	public void setVendaID(int vendaID) {
		this.vendaID = vendaID;
	}

}
