package com.controleestoque.model.dto;

public class ClienteResumoDTO {
    private int id;
    private String nome;
    private float totalCompras;

    public ClienteResumoDTO(int id, String nome, float totalCompras) {
        this.id = id;
        this.nome = nome;
        this.totalCompras = totalCompras;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public float getTotalCompras() { return totalCompras; }
}
