# BD-ControleEstoque

Sistema web para controle de estoque da **Joalheria Brilho Mais**, desenvolvido em **Java 17 + Jakarta EE 6.0 (Servlets + JSP)** com **Apache Tomcat 10/11** e **SQL Server** como banco de dados principal, com fallback automático para **H2 em memória** em ambiente de desenvolvimento.

## Funcionalidades

- **Login / Registro** — Autenticação e cadastro de funcionários com sessão protegida por filtro
- **Dashboard** — Visão central com hero section e cards de acesso rápido a cada módulo
- **Produtos** — Cadastro, listagem, alteração, busca por nome, entrada/saída de estoque com geração automática de relatório
- **Clientes** — Cadastro, listagem com total de compras via JOIN, atualização, contagem
- **Funcionários** — Cadastro, listagem com DTO (senha omitida), busca, alteração, exclusão, total e valor de vendas por funcionário
- **Vendas** — Registro de venda com múltiplos itens, cálculo automático do total, baixa no estoque e geração de relatório
- **Relatórios** — Listagem de logs do sistema, soma total de vendas, contagem de vendas realizadas

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Jakarta Servlet | 6.0 |
| Jakarta JSTL | 3.0 |
| Apache Tomcat | 10.1 / 11.0 |
| SQL Server | — |
| jTDS | 1.3.1 |
| H2 (fallback dev) | 2.2.224 |
| Lombok | 1.18.46 (declarado mas **não utilizado** no código fonte) |

## Estrutura do Projeto

```
BD-ControleEstoque/
├── build.ps1                          # Script de build via linha de comando
├── .classpath / .project              # Configuração Eclipse WTP
├── .settings/                         # Configurações do projeto Eclipse
│
└── src/main/
    ├── java/
    │   └── com/controleestoque/
    │       ├── config/
    │       │   ├── DatabaseConfig.java    # Singleton de conexão (localhost, sa, P4ssw0rd)
    │       │   ├── WebConfig.java         # Filter UTF-8 + atributo h2Fallback
    │       │   └── AuthFilter.java        # Protege rotas, exceto /login e /css/
    │       │
    │       ├── controller/
    │       │   ├── BaseServlet.java       # Classe abstrata (não utilizada pelos servlets concretos)
    │       │   ├── LoginServlet.java      # /login: login, logout, register
    │       │   ├── ProdutoServlet.java    # /produto: CRUD + estoque + listar vendidos
    │       │   ├── ClienteServlet.java    # /cliente: cadastrar, listar, atualizar, contar
    │       │   ├── FuncionarioServlet.java # /funcionario: CRUD + valor/quantidade vendas
    │       │   ├── VendaServlet.java      # /venda: GET lista, POST registra
    │       │   └── RelatorioServlet.java  # /relatorio: listar, total, total vendas
    │       │
    │       ├── model/
    │       │   ├── entity/
    │       │   │   ├── Cliente.java       # id, nome, totalCompras, qtdCompras
    │       │   │   ├── Estoque.java       # id, quantidade
    │       │   │   ├── Funcionario.java   # id, nome, userName, senha, totalVendas
    │       │   │   ├── ItemVenda.java     # id, vendaID, produtoID, quantidade
    │       │   │   ├── Produto.java       # id, nome, valor, valorFormatado, quantidade
    │       │   │   ├── Relatorio.java     # id, descricao, vendaID, totalVendas
    │       │   │   └── Venda.java         # id, clienteID, funcionarioID, clienteNome,
    │       │   │                          # funcionarioNome, dataVenda, total, itens
    │       │   └── dto/
    │       │       ├── ProdutoListagemDTO.java   # id, nome, valor, valorFormatado, quantidade
    │       │       ├── ClienteResumoDTO.java     # id, nome, totalCompras
    │       │       ├── FuncionarioResumoDTO.java # id, nome, userName, totalVendas
    │       │       ├── VendaResumoDTO.java       # id, clienteNome, funcionarioNome,
    │       │       │                             # dataVenda, total, itens (List<ItemVendaDTO>)
    │       │       └── ItemVendaDTO.java         # produtoID, quantidade
    │       │
    │       ├── persistence/
    │       │   ├── connection/
    │       │   │   └── DatabaseConnection.java   # Conexão JDBC: tenta SQL Server, fallback H2
    │       │   ├── dao/
    │       │   │   ├── AbstractDao.java     # Genérico: executeQuery, executeInsert,
    │       │   │   │                        # executeUpdate, executeTransaction
    │       │   │   ├── ClienteDao.java      # CRUD + listarClientesComTotalCompras (LEFT JOIN)
    │       │   │   ├── EstoqueDao.java      # upsert quantidade, listar sem estoque,
    │       │   │   │                        # calcularValorTotalEstoque
    │       │   │   ├── FuncionarioDao.java  # CRUD + autenticar, qtdVendas, valorVenda,
    │       │   │   │                        # listarFuncionariosComVendas (LEFT JOIN)
    │       │   │   ├── ProdutoDao.java      # CRUD + entrada/saída, listar vendidos,
    │       │   │   │                        # adicionarRelatorio automático
    │       │   │   ├── RelatorioDao.java    # inserir, listar, somarVendas, contarVendas
    │       │   │   └── VendaDao.java        # registrar (transação: insert + itens + total
    │       │   │                            # + baixa estoque), listar com JOIN
    │       │   └── factory/
    │       │       └── DaoFactory.java      # Static factory para todos os DAOs
    │       │
    │       ├── service/
    │       │   ├── ProdutoService.java      # Converte Produto → ProdutoListagemDTO com
    │       │   │                             # formatação BRL (NumberFormat pt_BR)
    │       │   ├── ClienteService.java      # Converte Cliente → ClienteResumoDTO
    │       │   ├── FuncionarioService.java  # Converte Funcionario → FuncionarioResumoDTO
    │       │   ├── VendaService.java        # Valida funcionarioID, converte para DTO
    │       │   ├── EstoqueService.java      # Delega para EstoqueDao
    │       │   └── RelatorioService.java    # Delega para RelatorioDao
    │       │
    │       └── util/
    │           └── EntityContext.java       # Wrapper para extração de parâmetros HTTP
    │
    └── webapp/
        ├── META-INF/
        │   └── MANIFEST.MF
        ├── WEB-INF/
        │   ├── web.xml                      # Jakarta EE 6.0, filters, welcome-file
        │   └── lib/                         # Dependências (9 JARs)
        │       ├── jakarta.annotation-api-2.1.1.jar
        │       ├── jakarta.el-api-4.0.0.jar
        │       ├── jakarta.servlet.jsp.jstl-3.0.0.jar
        │       ├── jakarta.servlet.jsp.jstl-api-3.0.0.jar
        │       ├── jakarta.xml.bind-api-4.0.0.jar
        │       ├── jtds-1.3.1.jar
        │       ├── servlet-api.jar
        │       ├── lombok-1.18.46.jar
        │       └── h2-2.2.224.jar
        ├── Database/
        │   └── ControleEstoque.sql          # Script SQL Server (7 tabelas)
        ├── css/
        │   └── styles.css                   # 704 linhas, tema dourado, responsivo
        ├── index.jsp                        # Forward para views/login.jsp
        └── views/
            ├── login.jsp                    # Login/registro com toggle JS
            ├── dashboard.jsp                # Hero + grid de 5 cards
            ├── menu.jsp                     # Navbar com active page, user info, logout
            ├── produto/listar.jsp           # CRUD + entrada/saída + listar vendidos
            ├── cliente/listar.jsp           # Cadastrar, listar, atualizar, contar
            ├── funcionario/listar.jsp       # CRUD + valor/quantidade vendas
            ├── venda/listar.jsp             # Registro com itens dinâmicos + listagem
            └── relatorio/listar.jsp         # Listar relatórios, total, contar vendas
```

## Arquitetura

### Camadas

```
[Browser] → [AuthFilter] → [Servlet (Controller)]
                                   ↓
                             [Service]
                                   ↓
                             [DAO (AbstractDao)]
                                   ↓
                    [DatabaseConnection / DatabaseConfig]
                                   ↓
                    [SQL Server] ← falha → [H2 (fallback)]
```

| Camada | Responsabilidade |
|---|---|
| **Config** | `WebConfig` (UTF-8), `AuthFilter` (proteção de rotas), `DatabaseConfig` (conexão) |
| **Controller** | Servlets que recebem requisições, interpretam parâmetros, chamam serviços e encaminham para JSPs |
| **Service** | Lógica de negócio, orquestração de DAOs, conversão entidade → DTO |
| **DAO** | Acesso a dados SQL via JDBC, mapeamento ResultSet → entidade |
| **Connection** | Gerenciamento de conexão com fallback automático SQL Server → H2 |
| **DTO** | Objetos imutáveis para transporte de dados à view (excluem senhas, IDs internos) |
| **View** | JSPs com JSTL/EL, organizadas por entidade em `views/` |

### Fluxo de Autenticação

1. `AuthFilter` intercepta todas as requisições (`/*`)
2. Rotas públicas (`/login`, `/views/login.jsp`, `/css/*`, `/`, `/index.jsp`) passam livremente
3. Demais rotas redirecionam para `/views/login.jsp` se não houver sessão com atributo `"funcionario"`
4. `LoginServlet` gerencia login (POST `action=login`), registro (POST `action=register`) e logout (POST `action=logout`)

### Estratégia de Banco de Dados

- **Tenta SQL Server primeiro** via driver jTDS (`net.sourceforge.jtds.jdbc.Driver`)
- **Fallback automático** para H2 em memória se SQL Server estiver indisponível
- O H2 é inicializado com todas as tabelas e um usuário admin padrão via `DatabaseConnection.initSchema()`
- A view JSP exibe um banner de aviso quando está usando H2 (atributo `h2Fallback`)
- Thread-safe com double-checked locking na inicialização do H2

## Endpoints da API

### LoginServlet — `/login`

| Método | Parâmetros | Descrição |
|---|---|---|
| `GET` | — | Exibe página de login |
| `POST` | `action=login`, `username`, `password` | Autentica funcionário, redireciona para dashboard |
| `POST` | `action=logout` | Invalida sessão, redireciona para login |
| `POST` | `action=register`, `nome`, `username`, `password`, `confirmPassword` | Cadastra novo funcionário (valida senha ≥ 8 caracteres) |

### ProdutoServlet — `/produto`

| Parâmetro `enviar` | Campos esperados | Descrição |
|---|---|---|
| `Cadastrar` | `nome`, `valor`, `quantidade` | Cria produto + relatório automático |
| `Alterar` | `id`, `nome` (opcional), `valor` (opcional), `quantidade` (opcional) | Atualização dinâmica (só altera campos preenchidos) |
| `Buscar` | `nome` | Busca por nome exato |
| `Listar` | — | Lista todos os produtos com valor formatado (R$) |
| `Entrada/Saida` | `id`, `quantidade` | Positivo = entrada, negativo = saída + relatório |
| `Listar Vendidos` | — | Lista produtos com total vendido (INNER JOIN venda_produto) |

### ClienteServlet — `/cliente`

| Parâmetro `botao` | Campos esperados | Descrição |
|---|---|---|
| `Cadastrar` | `nome` | Cria cliente |
| `Listar` | — | Lista clientes com total de compras via LEFT JOIN |
| `Atualizar` | `id`, `nome` | Atualiza nome |
| `Contar` | — | Retorna contagem total de clientes |

### FuncionarioServlet — `/funcionario`

| Parâmetro `botao` | Campos esperados | Descrição |
|---|---|---|
| `Cadastrar` | `nome`, `username`, `password` | Cria funcionário (valida senha ≥ 8) |
| `Alterar` | `id`, `nome`, `password` | Transação: atualiza nome e/ou senha |
| `Listar` | — | Lista funcionários com DTO (senha omitida) |
| `Buscar` | `id` | Busca por ID |
| `Excluir` | `id` | Exclui por ID (WHERE id = ? OR nome = ?) |
| `Valor em vendas` | `id` | Soma do total de vendas do funcionário |
| `Quantidade em vendas` | `id` | Contagem de vendas do funcionário |

### VendaServlet — `/venda`

| Método | Parâmetros | Descrição |
|---|---|---|
| `GET` | — | Lista todas as vendas com itens |
| `POST` | `enviar=Registrar Venda`, `clienteId`, `funcionarioId`, `produtoId[]`, `quantidade[]` | Registra venda em transação: insere venda, itens, calcula total, baixa estoque |

### RelatorioServlet — `/relatorio`

| Parâmetro `botao` | Descrição |
|---|---|
| `Listar` | Lista todos os logs do sistema |
| `Total` | Soma total de vendas (`SUM(venda.total)`) |
| `Total vendas` | Contagem de vendas (`COUNT(venda)`) |

## Modelo do Banco

```
┌─────────────────┐       ┌───────────────────┐
│    produto       │       │     estoque       │
├─────────────────┤       ├───────────────────┤
│ id (PK, IDENT)  │◄──────│ id (PK, FK)       │
│ nome (UNIQUE)   │       │ quantidade        │
│ valor (≥ 0)     │       └───────────────────┘
│ quantidade      │
└────────┬────────┘
         │
         │ ┌─────────────────────┐
         │ │   venda_produto     │
         │ ├─────────────────────┤
         └─┤ produtoID (PK, FK)  │
           │ vendaID (PK, FK)    │
           │ quantidade          │
           └──────────┬──────────┘
                      │
┌─────────────────┐  │  ┌───────────────────┐
│     venda       │──┘  │    relatorio      │
├─────────────────┤     ├───────────────────┤
│ id (PK, IDENT)  │     │ id (PK, IDENT)    │
│ clienteID (FK)  │     │ descricao         │
│ funcionarioID   │     │ vendaID (FK)      │
│ dataVenda       │     └───────────────────┘
│ total           │
└────┬──────┬─────┘
     │      │
     │      └──────────────────────────┐
     │                                 │
┌────▼──────────────┐    ┌─────────────▼──────────┐
│     cliente        │    │     funcionario        │
├───────────────────┤    ├────────────────────────┤
│ id (PK, IDENT)    │    │ id (PK, IDENT)         │
│ nome              │    │ nome                   │
│ qtdCompras (≥ 0)  │    │ username (UNIQUE)      │
└───────────────────┘    │ senha (LEN ≥ 8)        │
                         └────────────────────────┘
```

### Usuário padrão

| Username | Senha |
|---|---|
| admin | 12345678 |

## Configuração do Banco de Dados

1. Execute o script `src/main/webapp/Database/ControleEstoque.sql` no SQL Server para criar o banco e as tabelas.

2. Ajuste as credenciais em `src/main/java/com/controleestoque/config/DatabaseConfig.java`:

```java
private static final DatabaseConnection INSTANCE = new DatabaseConnection(
    "localhost",   // host
    "controleEstoque", // database
    "sa",          // usuário
    "P4ssw0rd"     // senha
);
```

### Fallback H2

Se o SQL Server não estiver disponível, o sistema automaticamente utiliza H2 em memória com o mesmo esquema de tabelas. Um banner amarelo é exibido no topo das páginas para indicar o fallback.

## Como Executar

### Pelo Eclipse (recomendado)

1. **Importe o projeto**: `File > Import > General > Existing Projects into Workspace`
2. **Configure o Tomcat 10.1 ou 11.0** em `Window > Preferences > Server > Runtime Environments`
3. **Adicione o projeto ao servidor**: clique com botão direito no Tomcat > `Add and Remove...`
4. **Acesse**: `http://localhost:8080/BD-ControleEstoque/`

### Linha de comando

```powershell
.\build.ps1
```

O script compila as classes (javac --release 21), copia para o diretório de deploy do Tomcat e disponibiliza a aplicação.

### Credenciais de acesso

| Usuário | Senha |
|---|---|
| admin | 12345678 |

## Observações Técnicas

### Segurança
- As senhas são armazenadas **em texto puro** (sem hash)
- Não há proteção contra CSRF
- Não há sanitização de entrada

### Limitações Conhecidas
- `ProdutoDao.atualizar()` não permite zerar `valor` ou `quantidade` (condição `> 0`)
- `EstoqueDao.removerQuantidade()` em produto sem registro na tabela `estoque` insere quantidade negativa
- `FuncionarioDao.excluir()` usa `WHERE id = ? OR nome = ?` (pode deletar registro inesperado)
- `BaseServlet` está definido mas **não é utilizado** por nenhum servlet concreto
- `cliente.qtdCompras` nunca é atualizada pela lógica de negócio (permanece 0)
- Sem testes automatizados
- Sem connection pooling (nova conexão JDBC por operação)

### Dependências
O projeto usa **9 JARs** em `WEB-INF/lib`:
- Jakarta EE API (annotation, el, jstl, xml.bind)
- jTDS 1.3.1 (driver SQL Server)
- H2 2.2.224 (banco fallback)
- Lombok 1.18.46 (declarado mas **não utilizado** no código)
- servlet-api.jar (para compilação fora do runtime Tomcat)
