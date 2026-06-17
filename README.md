# BD-ControleEstoque

Sistema web para controle de estoque de uma joalheria, desenvolvido em **Java 17 + Jakarta EE (Tomcat 10)** com **SQL Server** como banco de dados.

## Funcionalidades

- **Login** — Autenticação de funcionários
- **Produtos** — Cadastro, listagem, alteração, busca, entrada/saída de estoque
- **Clientes** — Cadastro, listagem com total de compras, atualização
- **Funcionários** — Cadastro, listagem, busca, exclusão, total e valor de vendas por funcionário
- **Vendas** — Registro de venda com múltiplos itens, cálculo automático do total e baixa no estoque
- **Relatórios** — Listagem de logs do sistema, total em vendas, total de vendas realizadas

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Jakarta Servlet | 6.0 |
| Jakarta JSTL | 3.0 |
| Apache Tomcat | 10.1 |
| SQL Server | — |
| jTDS | 1.3.1 |

## Estrutura do Projeto

```
src/main/
├── java/
│   ├── controller/       # Servlets (Login, Produto, Cliente, Funcionario, Venda, Relatorio)
│   ├── model/            # Classes de domínio (Produto, Cliente, Funcionario, Venda, ItemVenda, Estoque, Relatorio)
│   └── persistence/      # DAOs e GenericDao (conexão com banco)
└── webapp/
    ├── Database/
    │   └── ControleEstoque.sql   # Script de criação do banco
    ├── WEB-INF/
    │   ├── web.xml               # Descritor de deploy
    │   └── lib/                  # Dependências (.jar)
    ├── css/
    │   └── styles.css            # Estilos do sistema
    ├── index.jsp                 # Página de login
    ├── menu.jsp                  # Menu de navegação
    ├── produto.jsp               # CRUD de produtos
    ├── cliente.jsp               # CRUD de clientes
    ├── funcionario.jsp           # CRUD de funcionários
    ├── vendas.jsp                # Registro e listagem de vendas
    └── relatorio.jsp             # Relatórios do sistema
```

## Pré-requisitos

- **JDK 17** ou superior
- **Apache Tomcat 10.1** ou superior
- **SQL Server** (local ou remoto)
- **Eclipse IDE for Enterprise Java** (recomendado) ou qualquer IDE compatível com Jakarta EE

## Configuração do Banco de Dados

1. Execute o script `src/main/webapp/Database/ControleEstoque.sql` no SQL Server para criar o banco e as tabelas.

2. Ajuste as credenciais de conexão em `src/main/java/persistence/GenericDao.java`:

```java
String hostName = "localhost";     // servidor do SQL Server
String dbName = "controleEstoque"; // nome do banco
String user = "sa";                // usuário
String senha = "P4ssw0rd";         // senha
```

### Usuário padrão

O script já insere um funcionário inicial:

| Username | Senha |
|---|---|
| admin | 12345678 |

## Como Executar

### Pelo Eclipse

1. **Importe o projeto**: `File > Import > General > Existing Projects into Workspace`
2. **Configure o Tomcat 10.1** em `Window > Preferences > Server > Runtime Environments`
3. **Adicione o projeto ao servidor**: clique com botão direito no Tomcat > `Add and Remove...`
4. **Acesse**: `http://localhost:8080/BD-ControleEstoque/`

### Manualmente (sem IDE)

1. Compile os fontes para `build/classes`
2. Copie os jars de `src/main/webapp/WEB-INF/lib/` para `WEB-INF/lib/` no diretório de deploy
3. Faça o deploy da aplicação no Tomcat (cópia da pasta `webapp` ou `.war`)
4. Acesse `http://localhost:8080/BD-ControleEstoque/`

## API de Endpoints

| Servlet | Rota | Método | Descrição |
|---|---|---|---|
| LoginServlet | `/login` | POST | Autentica funcionário |
| ProdutoServlet | `/produto` | POST | CRUD de produtos |
| ClienteServlet | `/cliente` | POST | CRUD de clientes |
| FuncionarioServlet | `/funcionario` | POST | CRUD de funcionários |
| VendaServlet | `/venda` | GET/POST | Registro e listagem de vendas |
| RelatorioServlet | `/relatorio` | POST | Relatórios do sistema |

## Modelo do Banco

```
produto         (id, nome, valor, quantidade)
estoque         (id → produto.id, quantidade)
cliente         (id, nome, qtdCompras)
funcionario     (id, nome, username, senha)
venda           (id, clienteID, funcionarioID, dataVenda, total)
venda_produto   (vendaID, produtoID, quantidade)
relatorio       (id, descricao, vendaID)
```
