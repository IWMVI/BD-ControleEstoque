package com.controleestoque.persistence.factory;

import com.controleestoque.persistence.dao.ClienteDao;
import com.controleestoque.persistence.dao.EstoqueDao;
import com.controleestoque.persistence.dao.FuncionarioDao;
import com.controleestoque.persistence.dao.ProdutoDao;
import com.controleestoque.persistence.dao.RelatorioDao;
import com.controleestoque.persistence.dao.VendaDao;

public class DaoFactory {

    public static ProdutoDao createProdutoDao() {
        return new ProdutoDao();
    }

    public static FuncionarioDao createFuncionarioDao() {
        return new FuncionarioDao();
    }

    public static ClienteDao createClienteDao() {
        return new ClienteDao();
    }

    public static VendaDao createVendaDao() {
        return new VendaDao();
    }

    public static EstoqueDao createEstoqueDao() {
        return new EstoqueDao();
    }

    public static RelatorioDao createRelatorioDao() {
        return new RelatorioDao();
    }
}
