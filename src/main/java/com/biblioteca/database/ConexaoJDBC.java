package com.biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoJDBC {

    private static final String URL = "jdbc:h2:~/test";  // Banco H2 local (arquivo "test" na sua home)
    private static final String USUARIO = "sa";          // Usuário padrão do H2
    private static final String SENHA = "";              // Senha padrão do H2 é vazia

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
