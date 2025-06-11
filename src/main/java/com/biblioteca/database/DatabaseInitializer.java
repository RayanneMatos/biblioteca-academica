package com.biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:h2:~/biblioteca-academica";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement stmt = connection.createStatement()) {
            criarTabelaUsuario(stmt);
            criarTabelaLivros(stmt);
            criarTabelaEmprestimo(stmt);

            System.out.println("✔️ Banco inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inicializar banco de dados: " + e.getMessage());
        }
    }

    private static void criarTabelaUsuario(Statement stmt) throws SQLException {
        // Exclui a tabela se existir
        String dropTableUsuarios = "DROP TABLE IF EXISTS usuarios";
        stmt.execute(dropTableUsuarios);

        String createTableUsuarios = """
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id IDENTITY PRIMARY KEY,
                        nome VARCHAR(255),
                        matricula VARCHAR(50),
                        cpf VARCHAR(20),
                        ativo BOOLEAN,
                        email VARCHAR(255),
                        turno VARCHAR(20),
                        tipo VARCHAR(20)
                    );
                """;
        stmt.execute(createTableUsuarios);
    }

    private static void criarTabelaLivros(Statement stmt) throws SQLException {
        // Exclui a tabela se existir
        String dropTableLivros = "DROP TABLE IF EXISTS livros";
        stmt.execute(dropTableLivros);

        String createTableLivros = """
                    CREATE TABLE IF NOT EXISTS livros (
                        id IDENTITY PRIMARY KEY,
                        titulo VARCHAR(255),
                        autor VARCHAR(255),
                        ano_publicacao INT,
                        editora VARCHAR(255),
                        status VARCHAR(20)
                    );
                """;
        stmt.execute(createTableLivros);
    }

    public static void criarTabelaEmprestimo(Statement stmt) throws SQLException {
        // Exclui a tabela se existir
        String dropTableEmprestimo = "DROP TABLE IF EXISTS emprestimo";
        stmt.execute(dropTableEmprestimo);

        String createTableEmprestimo = """
                    CREATE TABLE IF NOT EXISTS emprestimo (
                        id IDENTITY PRIMARY KEY,
                        id_usuario INT,
                        id_livro INT,
                        data_emprestimo DATE,
                        data_devolucao_prevista DATE,
                        data_devolucao_real DATE,
                        devolvido BOOLEAN,
                        FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
                        FOREIGN KEY (id_livro) REFERENCES livros(id)
                    );
                """;
        stmt.execute(createTableEmprestimo);
    }

}
