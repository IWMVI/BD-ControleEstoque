package com.controleestoque.model.dto;

public class ProdutoListagemDTO {
    private int id;
    private String nome;
    private float valor;
    private String valorFormatado;
    private int quantidade;

    public ProdutoListagemDTO(int id, String nome, float valor, String valorFormatado, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.valorFormatado = valorFormatado;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public float getValor() { return valor; }
    public String getValorFormatado() { return valorFormatado; }
    public int getQuantidade() { return quantidade; }
}
