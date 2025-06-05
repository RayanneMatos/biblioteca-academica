package com.biblioteca.dao;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

    //Método para cadastrar um novo usuário
    public String cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, matricula, cpf, ativo, email, turno, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, usuario.getId());
            stm.setString(2, usuario.getNome());
            stm.setString(3, usuario.getMatricula());
            stm.setString(4, usuario.getCpf());
            stm.setBoolean(5, usuario.isAtivo());
            stm.setString(6, usuario.getEmail());
            stm.setString(7, usuario.getTurno().name());
            stm.setString(8, usuario.getTipo().name());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                return "Usuário cadastrado com sucesso!";
            } else {
                return "Falha ao cadastrar usuário.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao cadastrar usuário: " + e.getMessage();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }
    }

    //TODO: Implementar método
    public void ListarUsuarios() {
        String sql = "SELECT cpf, nome";
    }
}
