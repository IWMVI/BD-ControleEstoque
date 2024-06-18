package model;

public class Cliente {

	private int id;
	private String nome;
	private float totalCompras;
	private int qtdCompras;

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

	public float getTotalCompras() {
		return totalCompras;
	}

	public void setTotalCompras(float totalCompras) {
		this.totalCompras = totalCompras;
	}

	public void setQtdCompras(int qtdCompras) {
		this.qtdCompras = qtdCompras;
	}

	public int getQtdCompras() {
		return qtdCompras;
	}
}
