package com.controleestoque.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controleestoque.model.dto.ItemVendaDTO;
import com.controleestoque.model.dto.VendaResumoDTO;
import com.controleestoque.model.entity.Venda;
import com.controleestoque.persistence.dao.VendaDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class VendaService {

    private final VendaDao vendaDao;

    public VendaService() {
        this.vendaDao = DaoFactory.createVendaDao();
    }

    public void registrar(Venda venda) throws Exception {
        if (!new FuncionarioService().existe(venda.getFuncionarioID())) {
            throw new IllegalArgumentException(
                    "Funcionário não encontrado. Verifique o ID do funcionário.");
        }
        vendaDao.registrar(venda);
    }

    public List<VendaResumoDTO> listar() throws ClassNotFoundException, SQLException {
        List<Venda> vendas = vendaDao.listar();
        List<VendaResumoDTO> dtoList = new ArrayList<>();
        for (Venda v : vendas) {
            List<ItemVendaDTO> itensDTO = new ArrayList<>();
            if (v.getItens() != null) {
                for (var item : v.getItens()) {
                    itensDTO.add(new ItemVendaDTO(item.getProdutoID(), item.getQuantidade()));
                }
            }
            dtoList.add(new VendaResumoDTO(v.getId(), v.getClienteNome(), v.getFuncionarioNome(),
                    v.getDataVenda(), v.getTotal(), itensDTO));
        }
        return dtoList;
    }
}
