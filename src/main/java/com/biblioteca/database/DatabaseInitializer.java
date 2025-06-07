//Classe para testes

package com.biblioteca.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void criarTabelas() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id BIGINT PRIMARY KEY,
                nome VARCHAR(100),
                matricula VARCHAR(20),
                cpf VARCHAR(15),
                ativo BOOLEAN,
                email VARCHAR(100),
                turno VARCHAR(20),
                tipousuario VARCHAR(50)
            );
        """;

        try (Connection conn = ConexaoJDBC.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'usuarios' criada com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao criar tabela:");
            e.printStackTrace();
        }
    }

}
