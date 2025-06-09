package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.enums.Status;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;



public class EmprestimoDAO {

    private final Connection connection;

    public EmprestimoDAO() throws SQLException {
        this.connection = ConexaoJDBC.getConnection();
    }

    public void salvar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_devolucao_prevista, data_devolucao, devolvido) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, emprestimo.getUsuario().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            stmt.setDate(3, java.sql.Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, java.sql.Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stmt.setDate(5, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            stmt.setBoolean(6, emprestimo.isDevolvido());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimos";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long usuarioId = rs.getLong("usuario_id");
                long livroId = rs.getLong("livro_id");

                Usuario usuario = buscarUsuarioPorId(usuarioId);
                Livro livro = buscarLivroPorId(livroId);

                Emprestimo e = new Emprestimo(usuario, livro);
                e.setId(rs.getLong("id"));
                e.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                e.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());

                Date dataDev = rs.getDate("data_devolucao");
                if (dataDev != null) {
                    e.setDataDevolucao(dataDev.toLocalDate());
                }
                e.setDevolvido(rs.getBoolean("devolvido"));

                emprestimos.add(e);
            }
        }

        return emprestimos;
    }

    // Método auxiliar para buscar usuário por ID
    private Usuario buscarUsuarioPorId(long id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipoUsuario = rs.getString("tipo_usuario"); // Exemplo: "ALUNO", "PROFESSOR"
                    Turno turno = Turno.valueOf(rs.getString("turno")); // Enum

                    return switch (tipoUsuario) {
                        case "ALUNO" -> new Aluno(
                                rs.getLong("id"),
                                rs.getString("nome"),
                                rs.getString("matricula"),
                                rs.getString("cpf"),
                                rs.getString("email"),
                                turno
                            );
                        case "PROFESSOR" -> new Professor(
                                rs.getLong("id"),
                                rs.getString("nome"),
                                rs.getString("matricula"),
                                rs.getString("cpf"),
                                rs.getString("email"),
                                turno
                            );
                        default -> throw new IllegalArgumentException("Tipo de usuário desconhecido: " + tipoUsuario);
                    };
                }
            }
        }
        return null;
    }

    // Método auxiliar para buscar livro por ID
    private Livro buscarLivroPorId(long id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("editora"),
                        rs.getString("isbn"),
                        Status.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimos SET data_devolucao = ?, devolvido = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            stmt.setBoolean(2, emprestimo.isDevolvido());
            stmt.setLong(3, emprestimo.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(long id) throws SQLException {
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
