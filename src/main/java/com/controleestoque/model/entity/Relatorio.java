package com.controleestoque.model.entity;

public class Relatorio {
    private int id;
    private String descricao;
    private int vendaID;
    private int totalVendas;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public int getVendaID() { return vendaID; }
    public void setVendaID(int vendaID) { this.vendaID = vendaID; }
    public int getTotalVendas() { return totalVendas; }
    public void setTotalVendas(int totalVendas) { this.totalVendas = totalVendas; }
}
