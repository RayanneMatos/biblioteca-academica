package com.biblioteca.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;

public class Main {
    public static void main(String[] args) {
        try (Connection con = ConexaoJDBC.getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");

            UsuarioDAO usuarioDAO = new UsuarioDAO(con); // Passando conexão ao DAO

            // Cadastrar professor
            Professor prof = new Professor(0, "Carlos", "carlos123", "123", "carlos@email.com", "123456789");
            usuarioDAO.cadastrar(prof);

            // Buscar por login e senha
            Usuario u = usuarioDAO.buscarPorLoginSenha("carlos123", "123");
            System.out.println("Usuário encontrado: " + u.getNome());

            // Listar todos os professores
            List<Usuario> professores = usuarioDAO.listarPorTipo(TipoUsuario.PROFESSOR);
            System.out.println("Professores cadastrados:");
            for (Usuario professor : professores) {
                System.out.println(professor.getNome());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
