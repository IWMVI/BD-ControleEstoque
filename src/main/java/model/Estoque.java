package model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estoque {
	
	private int id;
	private String nome;
	private List<Produto> produtos;
	private int qtdTotal;
	private int quantidade;
	
}
