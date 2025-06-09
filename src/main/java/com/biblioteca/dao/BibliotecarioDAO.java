package com.biblioteca.dao;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.model.Bibliotecario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BibliotecarioDAO {

    public String cadastrar(Bibliotecario bibliotecario) {
        String sql = "INSERT INTO bibliotecario (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, bibliotecario.getNome());
            stm.setString(2, bibliotecario.getCpf());
            stm.setString(3, bibliotecario.getEmail());
            stm.setString(4, bibliotecario.getSenha());
            int rowsAffected = stm.executeUpdate();
            if(rowsAffected > 0) {
                return "Bibliotecário cadastrado com sucesso.";
            }
            return "Falha ao cadastrar bibliotecário.";
        } catch (SQLException e) {
            return "Erro ao cadastrar bibliotecário: " + e.getMessage();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }
    }

    public Bibliotecario login(String email, String senha) {
        String sql = "SELECT * FROM bibliotecario WHERE email = ? AND senha = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            stm.setString(2, senha);
            rs = stm.executeQuery();
            if (rs.next()) {
                Bibliotecario bibliotecario = new Bibliotecario(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        "*****"
                );
                bibliotecario.setId(rs.getLong("id"));
                return bibliotecario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }
        return null;
    }
}
