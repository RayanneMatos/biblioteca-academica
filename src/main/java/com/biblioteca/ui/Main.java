package com.biblioteca.ui;

import java.sql.Connection;
import java.sql.SQLException;

import com.biblioteca.database.ConexaoJDBC;

public class Main {
    public static void main(String[] args) {
        try (Connection con = ConexaoJDBC.getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
