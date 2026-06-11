package com.controleestoque.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controleestoque.model.dto.ClienteResumoDTO;
import com.controleestoque.model.entity.Cliente;
import com.controleestoque.persistence.dao.ClienteDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class ClienteService {

    private final ClienteDao clienteDao;

    public ClienteService() {
        this.clienteDao = DaoFactory.createClienteDao();
    }

    public void cadastrar(Cliente cliente) throws ClassNotFoundException, SQLException {
        clienteDao.inserir(cliente);
    }

    public void atualizar(Cliente cliente) throws ClassNotFoundException, SQLException {
        clienteDao.atualizar(cliente);
    }

    public List<ClienteResumoDTO> listarComTotalCompras() throws ClassNotFoundException, SQLException {
        List<Cliente> clientes = clienteDao.listarClientesComTotalCompras();
        List<ClienteResumoDTO> dtoList = new ArrayList<>();
        for (Cliente c : clientes) {
            dtoList.add(new ClienteResumoDTO(c.getId(), c.getNome(), c.getTotalCompras()));
        }
        return dtoList;
    }

    public int contar() throws ClassNotFoundException, SQLException {
        return clienteDao.contarClientes();
    }
}
