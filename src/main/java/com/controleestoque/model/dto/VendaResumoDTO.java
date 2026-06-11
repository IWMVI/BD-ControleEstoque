package com.controleestoque.model.dto;

import java.sql.Date;
import java.util.List;

public class VendaResumoDTO {
    private int id;
    private String clienteNome;
    private String funcionarioNome;
    private Date dataVenda;
    private float total;
    private List<ItemVendaDTO> itens;

    public VendaResumoDTO(int id, String clienteNome, String funcionarioNome,
                          Date dataVenda, float total, List<ItemVendaDTO> itens) {
        this.id = id;
        this.clienteNome = clienteNome;
        this.funcionarioNome = funcionarioNome;
        this.dataVenda = dataVenda;
        this.total = total;
        this.itens = itens;
    }

    public int getId() { return id; }
    public String getClienteNome() { return clienteNome; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Date getDataVenda() { return dataVenda; }
    public float getTotal() { return total; }
    public List<ItemVendaDTO> getItens() { return itens; }
}
