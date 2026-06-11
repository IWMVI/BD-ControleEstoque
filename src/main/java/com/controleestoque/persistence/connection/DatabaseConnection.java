package com.controleestoque.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String H2_URL = "jdbc:h2:mem:controleEstoque;DB_CLOSE_DELAY=-1";
    private static volatile boolean usingFallback = false;
    private static volatile boolean h2Initialized = false;
    private static final Object INIT_LOCK = new Object();

    private final String hostName;
    private final String dbName;
    private final String user;
    private final String senha;

    public DatabaseConnection(String hostName, String dbName, String user, String senha) {
        this.hostName = hostName;
        this.dbName = dbName;
        this.user = user;
        this.senha = senha;
    }

    public Connection getConnection() throws SQLException {
        if (!usingFallback) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                return DriverManager.getConnection(String.format(
                        "jdbc:jtds:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s;charset=UTF-8;sendStringParametersAsUnicode=true;",
                        hostName, dbName, user, senha));
            } catch (Exception e) {
                switchToH2Fallback();
            }
        }
        return getH2Connection();
    }

    private void switchToH2Fallback() throws SQLException {
        if (!h2Initialized) {
            synchronized (INIT_LOCK) {
                if (!h2Initialized) {
                    try {
                        Class.forName("org.h2.Driver");
                    } catch (ClassNotFoundException e) {
                        throw new SQLException("H2 driver not found on classpath", e);
                    }
                    try (Connection conn = DriverManager.getConnection(H2_URL, "sa", "")) {
                        initSchema(conn);
                    }
                    usingFallback = true;
                    h2Initialized = true;
                }
            }
        }
    }

    private Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection(H2_URL, "sa", "");
    }

    private void initSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS produto ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "nome VARCHAR(100) NOT NULL UNIQUE, "
                    + "valor DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK(valor >= 0), "
                    + "quantidade INT NOT NULL DEFAULT 0, "
                    + "PRIMARY KEY (id))");

            st.execute("CREATE TABLE IF NOT EXISTS estoque ("
                    + "id INT NOT NULL PRIMARY KEY, "
                    + "quantidade INT NOT NULL DEFAULT 0, "
                    + "FOREIGN KEY (id) REFERENCES produto(id))");

            st.execute("CREATE TABLE IF NOT EXISTS cliente ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "qtdCompras INT NOT NULL DEFAULT 0 CHECK(qtdCompras >= 0), "
                    + "PRIMARY KEY (id))");

            st.execute("CREATE TABLE IF NOT EXISTS funcionario ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "senha VARCHAR(255) NOT NULL CHECK(CHAR_LENGTH(senha) >= 8), "
                    + "PRIMARY KEY (id))");

            st.execute("INSERT INTO funcionario (nome, username, senha) "
                    + "SELECT 'admin', 'admin', '12345678' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM funcionario WHERE username = 'admin')");

            st.execute("CREATE TABLE IF NOT EXISTS venda ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "clienteID INT NOT NULL, "
                    + "funcionarioID INT NOT NULL, "
                    + "dataVenda DATE NOT NULL, "
                    + "total DECIMAL(10,2) NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (clienteID) REFERENCES cliente(id), "
                    + "FOREIGN KEY (funcionarioID) REFERENCES funcionario(id))");

            st.execute("CREATE TABLE IF NOT EXISTS venda_produto ("
                    + "vendaID INT NOT NULL, "
                    + "produtoID INT NOT NULL, "
                    + "quantidade INT NOT NULL, "
                    + "PRIMARY KEY (vendaID, produtoID), "
                    + "FOREIGN KEY (vendaID) REFERENCES venda(id), "
                    + "FOREIGN KEY (produtoID) REFERENCES produto(id))");

            st.execute("CREATE TABLE IF NOT EXISTS relatorio ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "descricao VARCHAR(255), "
                    + "vendaID INT, "
                    + "FOREIGN KEY (vendaID) REFERENCES venda(id))");
        }
    }

    public static boolean isUsingFallback() {
        return usingFallback;
    }
}
