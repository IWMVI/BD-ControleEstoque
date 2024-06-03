package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Produto {

	private int id;
	private String nome;
	private float valor;
	private String valorFormatado;
	private int estoqueID;
	private int quantidade;
	
}
