package com.biblioteca.database;

import java.sql.*;

public class ConexaoJDBC {

    //Informações para o caminho, usuário e senha para rastramento de conexão
    private static final String URL = "jdbc:h2:~/test";  // Banco H2 local (arquivo "test" na sua home)
    private static final String USUARIO = "sa";          // Usuário padrão do H2
    private static final String SENHA = "";              // Senha padrão do H2 é vazia


    //Método para inicilizar a conexão com o banco
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro a conectar ao banco.");
            e.printStackTrace();
            return null;
        }
    }

    //Método para fechar a conexão com o banco
    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed())
                rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar ResultSet");
            e.printStackTrace();
        }
        try {
            if (stmt != null && !stmt.isClosed()) stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar PreparedStatement");
            e.printStackTrace();
        }

        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar Connection");
            e.printStackTrace();
        }
    }
}
