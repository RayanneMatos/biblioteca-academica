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

    public static void criarTabelas() {
        String sqlBibliotecario = "CREATE TABLE IF NOT EXISTS bibliotecario (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(255) NOT NULL," +
                "cpf VARCHAR(11) NOT NULL UNIQUE," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "senha VARCHAR(255) NOT NULL)";

        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(255) NOT NULL," +
                "matricula VARCHAR(50) NOT NULL UNIQUE," +
                "cpf VARCHAR(11) NOT NULL UNIQUE," +
                "ativo BOOLEAN NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "turno VARCHAR(50)," +
                "tipo VARCHAR(50) NOT NULL)";

        String sqlLivro = "CREATE TABLE IF NOT EXISTS livro (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "titulo VARCHAR(255) NOT NULL," +
                "autor VARCHAR(255) NOT NULL," +
                "ano_publicacao INT NOT NULL," +
                "editora VARCHAR(255) NOT NULL," +
                "isbn VARCHAR(13) NOT NULL UNIQUE," +
                "status VARCHAR(50) NOT NULL)";

        String sqlEmprestimo = "CREATE TABLE IF NOT EXISTS emprestimo (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "id_usuario BIGINT NOT NULL," +
                "id_livro INT NOT NULL," +
                "data_emprestimo DATE NOT NULL," +
                "data_devolucao DATE," +
                "devolvido BOOLEAN NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES usuario(id)," +
                "FOREIGN KEY (id_livro) REFERENCES livro(id))";

        try (Connection conn = getConnection()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlBibliotecario);
                stmt.execute(sqlUsuario);
                stmt.execute(sqlLivro);
                stmt.execute(sqlEmprestimo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
