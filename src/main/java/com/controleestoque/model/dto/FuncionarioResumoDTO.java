package com.controleestoque.model.dto;

public class FuncionarioResumoDTO {
    private int id;
    private String nome;
    private String userName;
    private int totalVendas;

    public FuncionarioResumoDTO(int id, String nome, String userName, int totalVendas) {
        this.id = id;
        this.nome = nome;
        this.userName = userName;
        this.totalVendas = totalVendas;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getUserName() { return userName; }
    public int getTotalVendas() { return totalVendas; }
}
