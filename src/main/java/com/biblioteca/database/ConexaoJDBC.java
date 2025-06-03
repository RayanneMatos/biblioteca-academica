package com.biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoJDBC {

    private static final String URL = "jdbc:h2:~/test";  // Banco H2 local (arquivo "test" na sua home)
    private static final String USUARIO = "sa";          // Usuário padrão do H2
    private static final String SENHA = "";              // Senha padrão do H2 é vazia

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro a conectar ao banco.");
            e.printStackTrace();
            return null;
        }
    }
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão");
                e.printStackTrace();
            }
        }
    }
}
