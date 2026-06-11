package com.controleestoque.service;

import java.sql.SQLException;
import java.util.List;

import com.controleestoque.model.entity.Relatorio;
import com.controleestoque.persistence.dao.RelatorioDao;
import com.controleestoque.persistence.factory.DaoFactory;

public class RelatorioService {

    private final RelatorioDao relatorioDao;

    public RelatorioService() {
        this.relatorioDao = DaoFactory.createRelatorioDao();
    }

    public List<Relatorio> listar() throws ClassNotFoundException, SQLException {
        return relatorioDao.listar();
    }

    public float somarVendas() throws ClassNotFoundException, SQLException {
        return relatorioDao.somarVendas();
    }

    public int contarVendas() throws ClassNotFoundException, SQLException {
        return relatorioDao.contarVendas();
    }
}
