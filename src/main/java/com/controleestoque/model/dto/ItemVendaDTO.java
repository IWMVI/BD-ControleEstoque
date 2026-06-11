package com.controleestoque.model.dto;

public class ItemVendaDTO {
    private int produtoID;
    private int quantidade;

    public ItemVendaDTO(int produtoID, int quantidade) {
        this.produtoID = produtoID;
        this.quantidade = quantidade;
    }

    public int getProdutoID() { return produtoID; }
    public int getQuantidade() { return quantidade; }
}
