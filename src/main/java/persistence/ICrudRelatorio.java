package persistence;

import java.sql.SQLException;
import java.util.List;

import model.Relatorio;

public interface ICrudRelatorio<T> {


		public void inserir(Relatorio relatorio) throws SQLException, ClassNotFoundException;

		public List<Relatorio> listar() throws SQLException, ClassNotFoundException;

}