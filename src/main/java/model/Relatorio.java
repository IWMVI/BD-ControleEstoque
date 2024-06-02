package model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relatorio {

	private int id;
	private String descricao;
	private List<Produto> produtos;
	private int vendaID;
}
