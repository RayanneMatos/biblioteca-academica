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

            // Insere um usuário fictício se a tabela estiver vazia
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                    INSERT INTO usuarios (nome, matricula, cpf, ativo, email, turno, tipo)
                    VALUES ('João da Silva', '12345', '000.000.000-00', TRUE, 'joao@email.com', 'Matutino', 'Aluno');
                """);
            }

            System.out.println("✔️ Banco inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inicializar banco de dados: " + e.getMessage());
        }
    }

    public static void mostrarUsuarios() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {

            System.out.println("\n📋 Lista de usuários:");

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Matrícula: %s | Ativo: %b\n",
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("matricula"),
                        rs.getBoolean("ativo"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao consultar usuários: " + e.getMessage());
        }
    }
}
