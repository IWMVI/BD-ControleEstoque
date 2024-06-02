CREATE DATABASE controleEstoque;
USE controleEstoque;

CREATE TABLE estoque (
    id INT NOT NULL DEFAULT 1,
    quantidade INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE produto (
    id INT NOT NULL IDENTITY(1000,2),
    nome VARCHAR(100) NOT NULL UNIQUE,
    valor DECIMAL(10,2) NOT NULL DEFAULT (0) CHECK((valor) >= 0),
    quantidade INT NOT NULL,
    estoqueID INT NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY(estoqueID) REFERENCES estoque(id)
);

CREATE TABLE cliente (
    id INT NOT NULL IDENTITY(100,2),
    nome VARCHAR(100) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE funcionario (
    id INT NOT NULL IDENTITY (100,2),
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL CHECK(LEN(senha) >= 8),

    PRIMARY KEY (id)
);

CREATE TABLE venda (
    id INT NOT NULL IDENTITY(1,1),
    clienteID INT NOT NULL,
    funcionarioID INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY(clienteID) REFERENCES cliente(id),
    FOREIGN KEY(funcionarioID) REFERENCES funcionario(id),
);

CREATE TABLE relatorio (
    id INT NOT NULL IDENTITY (1,1),
    descricao VARCHAR(255),
    vendaID INT NOT NULL

    PRIMARY KEY(id),
    FOREIGN KEY(vendaID) REFERENCES venda(id)
);

CREATE TABLE produto_venda (
    produtoID INT NOT NULL,
    vendaID INT NOT NULL

    PRIMARY KEY(produtoID, vendaID),
    FOREIGN KEY(produtoID) REFERENCES produto(ID),
    FOREIGN KEY(vendaID) REFERENCES venda(ID)
);

INSERT INTO funcionario (nome, username, senha) VALUES ('admin', 'admin', '12345678');

SELECT * FROM funcionario

ALTER TABLE relatorio 
ALTER COLUMN vendaID INT NULL;
