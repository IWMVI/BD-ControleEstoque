package com.controleestoque.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controleestoque.model.dto.FuncionarioResumoDTO;
import com.controleestoque.model.entity.Funcionario;
import com.controleestoque.persistence.dao.FuncionarioDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class FuncionarioService {

    private final FuncionarioDao funcionarioDao;

    public FuncionarioService() {
        this.funcionarioDao = DaoFactory.createFuncionarioDao();
    }

    public void cadastrar(Funcionario f) throws ClassNotFoundException, SQLException {
        funcionarioDao.inserir(f);
    }

    public void alterar(Funcionario f) throws ClassNotFoundException, SQLException {
        funcionarioDao.atualizar(f);
    }

    public void excluir(Funcionario f) throws ClassNotFoundException, SQLException {
        funcionarioDao.excluir(f);
    }

    public List<FuncionarioResumoDTO> listar() throws ClassNotFoundException, SQLException {
        List<Funcionario> funcionarios = funcionarioDao.listar();
        List<FuncionarioResumoDTO> dtoList = new ArrayList<>();
        for (Funcionario f : funcionarios) {
            dtoList.add(new FuncionarioResumoDTO(f.getId(), f.getNome(), f.getUserName(), f.getTotalVendas()));
        }
        return dtoList;
    }

    public Funcionario buscarPorId(int id) throws ClassNotFoundException, SQLException {
        Funcionario f = new Funcionario();
        f.setId(id);
        return funcionarioDao.consultar(f);
    }

    public Funcionario autenticar(Funcionario f) throws ClassNotFoundException, SQLException {
        return funcionarioDao.autenticar(f);
    }

    public boolean existe(int id) throws ClassNotFoundException, SQLException {
        return funcionarioDao.existe(id);
    }

    public float valorVendas(Funcionario f) throws ClassNotFoundException, SQLException {
        return funcionarioDao.valorVenda(f);
    }

    public int quantidadeVendas(Funcionario f) throws ClassNotFoundException, SQLException {
        return funcionarioDao.qtdVendas(f);
    }
}
