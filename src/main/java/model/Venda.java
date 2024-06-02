package model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Venda {

	private int id;
	private Funcionario funcionario;
	private Cliente cliente;
	private List<Produto> produtos;
	
}
