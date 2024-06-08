package model;

public class ItemVenda {

	private int id;
	private int vendaID;
	private int produtoID;
	private int quantidade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVendaID() {
		return vendaID;
	}

	public void setVendaID(int vendaID) {
		this.vendaID = vendaID;
	}

	public int getProdutoID() {
		return produtoID;
	}

	public void setProdutoID(int produtoID) {
		this.produtoID = produtoID;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}
