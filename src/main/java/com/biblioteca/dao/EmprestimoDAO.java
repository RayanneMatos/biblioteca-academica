package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.enums.Status;
import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;

public class EmprestimoDAO {

    // Método construtor
    public EmprestimoDAO() {
    }

    // Método para realizar um empréstimo
    public void realizarEmprestimo(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_devolucao_prevista, data_devolucao, devolvido) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoJDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, emprestimo.getUsuario().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            stmt.setDate(3, java.sql.Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, java.sql.Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stmt.setDate(5, emprestimo.getDataDevolucao() != null ? java.sql.Date.valueOf(emprestimo.getDataDevolucao())
                    : null);
            stmt.setBoolean(6, emprestimo.isDevolvido());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprestimo.setId(rs.getLong(1));
                    }
                }
            }
        }
    }

    // Método para realizar devolução de um livro
    public void devolverPorIdDoLivro(long livroId) throws SQLException {
        String sqlUpdateEmprestimo = "UPDATE emprestimo SET devolvido = ?, data_devolucao_real = ? WHERE id_livro = ? AND devolvido = false";
        String sqlUpdateLivro = "UPDATE livro SET status = ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = ConexaoJDBC.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // Atualiza o registro do empréstimo
            try (PreparedStatement stmEmprestimo = conn.prepareStatement(sqlUpdateEmprestimo)) {
                stmEmprestimo.setBoolean(1, true);
                stmEmprestimo.setDate(2, Date.valueOf(LocalDate.now()));
                stmEmprestimo.setLong(3, livroId);
                stmEmprestimo.executeUpdate();
            }

            // Atualiza o status do livro para DISPONIVEL
            try (PreparedStatement stmLivro = conn.prepareStatement(sqlUpdateLivro)) {
                stmLivro.setString(1, Status.DISPONIVEL.name());
                stmLivro.setLong(2, livroId);
                stmLivro.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para listar o histórico de empréstimo pela matrícula do usuário
    public List<Emprestimo> listarPorMatriculaDeUsuario(String matricula) throws SQLException {
        String filtroSql = " WHERE u.matricula = ?";
        return listarComFiltroEParametro(filtroSql, matricula);
    }

    // Método para listar os empréstimos que já foram realizados do livro
    public List<Emprestimo> listarPorIsbnDoLivro(String isbn) throws SQLException {
        String filtroSql = " WHERE l.isbn = ?";
        return listarComFiltroEParametro(filtroSql, isbn);
    }

    // Método para listar os livros atrasados, cujo a data de devolução já
    // ultrapassou
    public List<Emprestimo> listarAtrasados() throws SQLException {
        String filtroSql = " WHERE e.devolvido = false AND e.data_devolucao_prevista < CURRENT_DATE()";
        return listarComFiltroEParametro(filtroSql, null);
    }

    // Método para listar os empréstimos que estão ativos
    public List<Emprestimo> listarAtivos() throws SQLException {
        String filtroSql = " WHERE e.devolvido = false";
        return listarComFiltroEParametro(filtroSql, null);
    }

    // Método para listar todo o histórico de empréstimos
    public List<Emprestimo> listarHistoricoCompleto() throws SQLException {
        return listarComFiltroEParametro("", null); // Chama o helper sem nenhum filtro.
    }

    // Método que realiza a busca e insere o filtro para os demais métodos
    private List<Emprestimo> listarComFiltroEParametro(String filtroSql, String parametro) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, " +
                "u.id as usuario_id, u.nome as usuario_nome, u.matricula, u.cpf, u.email, u.turno, u.tipo, u.ativo, " +
                "l.id as livro_id, l.titulo, l.autor, l.ano_publicacao, l.editora, l.isbn, l.status " +
                "FROM emprestimo e " +
                "JOIN usuario u ON e.id_usuario = u.id " +
                "JOIN livro l ON e.id_livro = l.id " +
                filtroSql + " ORDER BY e.data_emprestimo DESC";

        try (Connection conn = ConexaoJDBC.getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            if (parametro != null) {
                stm.setString(1, parametro);
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapearDoResultSet(rs));
                }
            }
        }
        return emprestimos;
    }

    // Método que mapeia o resultset
    private Emprestimo mapearDoResultSet(ResultSet rs) throws SQLException {
        // Monta o objeto Livro usando o NOVO construtor de 6 argumentos
        Livro livro = new Livro(
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("ano_publicacao"),
                rs.getString("editora"),
                rs.getString("isbn"),
                Status.valueOf(rs.getString("status")) // <-- Argumento status adicionado aqui
        );
        // O ID ainda é definido separadamente, pois não está no construtor
        livro.setId(rs.getInt("livro_id"));

        // Monta o objeto Usuario (Aluno ou Professor) - Sem alterações aqui
        Usuario usuario;
        if (TipoUsuario.valueOf(rs.getString("tipo")) == TipoUsuario.ALUNO) {
            usuario = new Aluno(rs.getString("usuario_nome"), rs.getString("matricula"), rs.getString("cpf"),
                    rs.getString("email"), Turno.valueOf(rs.getString("turno")));
        } else {
            usuario = new Professor(rs.getString("usuario_nome"), rs.getString("matricula"), rs.getString("cpf"),
                    rs.getString("email"), Turno.valueOf(rs.getString("turno")));
        }
        usuario.setId(rs.getLong("usuario_id"));
        usuario.setAtivo(rs.getBoolean("ativo"));

        // Monta o objeto Emprestimo principal - Sem alterações aqui
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        emprestimo.setId(rs.getInt("id"));
        emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
        emprestimo.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());
        emprestimo.setDevolvido(rs.getBoolean("devolvido"));

        Date dataDevolucaoRealSql = rs.getDate("data_devolucao_real");
        if (dataDevolucaoRealSql != null) {
            emprestimo.setDataDevolucao(dataDevolucaoRealSql.toLocalDate());
        }

        return emprestimo;
    }

}
