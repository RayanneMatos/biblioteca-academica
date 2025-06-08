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

public class AlunoDAO {

    // Cadastra um novo aluno no banco
    public String cadastrarAluno(Aluno aluno) {
        String sql = "INSERT INTO alunos (nome, matricula, cpf, email, turno, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, aluno.getNome());
            stm.setString(2, aluno.getMatricula());
            stm.setString(3, aluno.getCpf());
            stm.setString(4, aluno.getEmail());
            stm.setString(5, aluno.getTurno().name());
            stm.setString(6, aluno.getTipoUsuario().name());

            int rows = stm.executeUpdate();

            if (rows > 0) {
                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    aluno.setId(rs.getInt(1));
                    return "Aluno cadastrado com sucesso! ID: " + aluno.getId();
                }
            }

            return "Falha ao cadastrar aluno.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao cadastrar aluno: " + e.getMessage();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }
    }

    // Lista todos os alunos
    public List<Aluno> listarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos WHERE tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, TipoUsuario.ALUNO.name());
            rs = stm.executeQuery();

            while (rs.next()) {
                alunos.add(mapearAluno(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }

        return alunos;
    }

    // Busca aluno por ID
    public Aluno buscarPorId(int id) {
        String sql = "SELECT * FROM alunos WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, TipoUsuario.ALUNO.name());
            rs = stm.executeQuery();

            if (rs.next()) {
                return mapearAluno(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }

        return null;
    }

    // Atualiza dados do aluno
    public boolean atualizarAluno(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, matricula = ?, cpf = ?, email = ?, turno = ? WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);

            stm.setString(1, aluno.getNome());
            stm.setString(2, aluno.getMatricula());
            stm.setString(3, aluno.getCpf());
            stm.setString(4, aluno.getEmail());
            stm.setString(5, aluno.getTurno().name());
            stm.setLong(6, aluno.getId());
            stm.setString(7, TipoUsuario.ALUNO.name());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }

        return false;
    }

    // Deleta aluno pelo ID
    public boolean deletarAluno(int id) {
        String sql = "DELETE FROM alunos WHERE id = ? AND tipo_usuario = ?";
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, TipoUsuario.ALUNO.name());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }

        return false;
    }

    // Mapeia o ResultSet para objeto Aluno
    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno(
            rs.getString("nome"),
            rs.getString("matricula"),
            rs.getString("cpf"),
            rs.getString("email"),
            Turno.valueOf(rs.getString("turno"))
        );
        aluno.setId(rs.getInt("id"));
        return aluno;
    }
}
