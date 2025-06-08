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
import com.biblioteca.model.Professor;

public class ProfessorDAO {

    // Cadastra um novo professor
    public String cadastrarProfessor(Professor professor) {
        String sql = "INSERT INTO professores (nome, matricula, cpf, email, turno, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, professor.getNome());
            stm.setString(2, professor.getMatricula());
            stm.setString(3, professor.getCpf());
            stm.setString(4, professor.getEmail());
            stm.setString(5, professor.getTurno().name());
            stm.setString(6, professor.getTipoUsuario().name());

            int rows = stm.executeUpdate();

            if (rows > 0) {
                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    professor.setId(rs.getInt(1));
                    return "Professor cadastrado com sucesso! ID: " + professor.getId();
                }
            }

            return "Falha ao cadastrar professor.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao cadastrar professor: " + e.getMessage();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }
    }

    // Lista todos os professores
    public List<Professor> listarTodos() {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT * FROM professores WHERE tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, TipoUsuario.PROFESSOR.name());
            rs = stm.executeQuery();

            while (rs.next()) {
                professores.add(mapearProfessor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }

        return professores;
    }

    // Busca professor por ID
    public Professor buscarPorId(int id) {
        String sql = "SELECT * FROM professores WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, TipoUsuario.PROFESSOR.name());
            rs = stm.executeQuery();

            if (rs.next()) {
                return mapearProfessor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }

        return null;
    }

    // Atualiza dados do professor
    public boolean atualizarProfessor(Professor professor) {
        String sql = "UPDATE professores SET nome = ?, matricula = ?, cpf = ?, email = ?, turno = ? WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);

            stm.setString(1, professor.getNome());
            stm.setString(2, professor.getMatricula());
            stm.setString(3, professor.getCpf());
            stm.setString(4, professor.getEmail());
            stm.setString(5, professor.getTurno().name());
            stm.setLong(6, professor.getId());
            stm.setString(7, TipoUsuario.PROFESSOR.name());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }

        return false;
    }

    // Deleta professor pelo ID
    public boolean deletarProfessor(int id) {
        String sql = "DELETE FROM professores WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, TipoUsuario.PROFESSOR.name());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }

        return false;
    }

    // Mapeia o ResultSet para objeto Professor
    private Professor mapearProfessor(ResultSet rs) throws SQLException {
        Professor professor = new Professor(
            rs.getString("nome"),
            rs.getString("matricula"),
            rs.getString("cpf"),
            rs.getString("email"),
            Turno.valueOf(rs.getString("turno"))
        );
        professor.setId(rs.getInt("id"));
        return professor;
    }
}
