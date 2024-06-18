CREATE DATABASE controleEstoque;
USE controleEstoque;

CREATE TABLE produto (
    id INT NOT NULL IDENTITY(1,1),
    nome VARCHAR(100) NOT NULL UNIQUE,
    valor DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK(valor >= 0),
    quantidade INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE cliente (
    id INT NOT NULL IDENTITY (1,1),
    nome VARCHAR(100) NOT NULL,
    qtdCompras INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE cliente
ADD CONSTRAINT CK__cliente__qtdComp__3E52440B CHECK(qtdCompras >= 0);

CREATE TABLE funcionario (
    id INT NOT NULL IDENTITY (1,1),
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL CHECK(LEN(senha) >= 8),
    PRIMARY KEY (id)
);

INSERT INTO funcionario (nome, username, senha)
VALUES ('admin', 'admin', '12345678');

CREATE TABLE venda (
    id INT NOT NULL IDENTITY (1,1),
    clienteID INT NOT NULL,
    funcionarioID INT NOT NULL,
    dataVenda DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (clienteID) REFERENCES cliente(id),
    FOREIGN KEY (funcionarioID) REFERENCES funcionario(id)
);

CREATE TABLE venda_produto (
    vendaID INT NOT NULL,
    produtoID INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (vendaID, produtoID),
    FOREIGN KEY (vendaID) REFERENCES venda(id),
    FOREIGN KEY (produtoID) REFERENCES produto(id)
);

CREATE TABLE relatorio (
    id INT NOT NULL IDENTITY (1,1),
    descricao VARCHAR(255),
    vendaID INT,
    FOREIGN KEY (vendaID) REFERENCES venda(id)
);

SELECT * FROM funcionario;
