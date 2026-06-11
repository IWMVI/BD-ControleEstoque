package com.controleestoque.model.entity;

public class Produto {
    private int id;
    private String nome;
    private float valor;
    private String valorFormatado;
    private int quantidade;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }
    public String getValorFormatado() { return valorFormatado; }
    public void setValorFormatado(String valorFormatado) { this.valorFormatado = valorFormatado; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
