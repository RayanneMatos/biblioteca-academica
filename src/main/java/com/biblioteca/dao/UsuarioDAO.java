package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;

public class UsuarioDAO {

    // Cadastrar um novo usuário
    public String cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, matricula, cpf, ativo, email, turno, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getMatricula());
            stm.setString(3, usuario.getCpf());
            stm.setBoolean(4, usuario.isAtivo());
            stm.setString(5, usuario.getEmail());
            stm.setString(6, usuario.getTurno().name());
            stm.setString(7, usuario.getTipoUsuario().name());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getLong(1));
                        return "Usuário cadastrado com sucesso! ID: " + usuario.getId();
                    }
                }
                return "Usuário cadastrado, mas falha ao recuperar ID.";
            } else {
                return "Falha ao cadastrar usuário: Nenhuma linha afetada.";
            }

        } catch (SQLException e) {
            return "Erro ao cadastrar usuário: " + e.getMessage();
        }
    }

    // Buscar usuário por matrícula
    public Usuario buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM usuarios WHERE matricula = ?";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, matricula);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                } else {
                    System.out.println("Matrícula não encontrada: " + matricula);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar por matrícula '" + matricula + "': " + e.getMessage());
        }

        return null;
    }

    // Listar todos os usuários
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    //Método para buscar um usuário por nome
    public List<Usuario> buscarPorNome(String nome) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE LOWER(nome) LIKE ?"; //Utilização do operador para ignorar letra maiuscula e minuscula


        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + nome.toLowerCase() + "%"); //Realiza a transformação das palavras para minuscula
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
                if (usuarios.isEmpty()) {
                    System.out.println("Nenhum usuário encontrado com o nome: " + nome);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por nome " + nome + ":" + e.getMessage());
        }
        return usuarios;
    }

    // Mapeamento de ResultSet para objeto Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo").toUpperCase());

        Usuario usuario = (tipo == TipoUsuario.ALUNO)
                ? new Aluno(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("matricula"),
                rs.getString("cpf"), rs.getString("email"),
                Turno.valueOf(rs.getString("turno")))
                : new Professor(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("matricula"),
                rs.getString("cpf"),
                rs.getString("email"), 
                Turno.valueOf(rs.getString("turno")));

        usuario.setId(rs.getLong("id"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        return usuario;
    }
}
