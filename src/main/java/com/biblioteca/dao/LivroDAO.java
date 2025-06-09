package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.enums.Status;
import com.biblioteca.model.Livro;

public class LivroDAO {

    //Método para registrar um novo livro
    public String cadastrarLivro(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, editora, isbn, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        // try-with-resources para garantir fechamento automático da conexão e statement
        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Setar parâmetros da query com dados do livro
            stm.setString(1, livro.getTitulo());
            stm.setString(2, livro.getAutor());
            stm.setInt(3, livro.getAnoPublicacao());
            stm.setString(4, livro.getEditora());
            stm.setString(5, livro.getIsbn());
            stm.setString(6, livro.getStatus().name());

            // Executa o insert e verifica se houve alguma linha afetada
            int rows = stm.executeUpdate();

            if (rows > 0) {
                // Obter a chave gerada automaticamente (ID do livro)
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        livro.setId(rs.getLong(1));  // Atualiza o objeto com o ID gerado
                        return "Livro cadastrado com sucesso! ID: " + livro.getId();
                    }
                }
            }
            return "Falha ao cadastrar livro.";
        }
    }


     //Método que Lista todos os livros cadastrados no banco de dados.
    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";

        // try-with-resources garante fechamento dos recursos usados na consulta
        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            // Para cada registro retornado, mapeia para objeto Livro e adiciona à lista
            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
        }
        return livros;
    }


    //Método para buscar um livro pelo ID
    public Livro buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setLong(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapearLivro(rs);  // Mapeia e retorna o livro encontrado
                }
            }
        }
        return null;  // Caso não encontre nenhum livro com o ID informado
    }

    //Método para buscar livro por nome
    public String buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM livros WHERE LOWER(titulo) LIKE ?";  // Usando LIKE para busca parcial

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + nome.toLowerCase() + "%");
            try (ResultSet rs = stm.executeQuery()) {
                List<Livro> livros = new ArrayList<>();
                while (rs.next()) {
                    livros.add(mapearLivro(rs));
                }

                if (livros.isEmpty()) {
                    return "Nenhum livro encontrado com o título: " + nome;
                } else {
                    return "Livros encontrados: " + livros.size();
                }
            }
        }
    }

    //Método para atualizar os dados de um livro existente
    public boolean atualizarLivro(Livro livro) throws SQLException {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, ano_publicacao = ?, editora = ?, isbn = ?, status = ? WHERE id = ?";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            // Preenche os parâmetros da query com os dados do livro
            stm.setString(1, livro.getTitulo());
            stm.setString(2, livro.getAutor());
            stm.setInt(3, livro.getAnoPublicacao());
            stm.setString(4, livro.getEditora());
            stm.setString(5, livro.getIsbn());
            stm.setString(6, livro.getStatus().name());
            stm.setLong(7, livro.getId());

            // Executa a atualização e retorna se teve sucesso
            return stm.executeUpdate() > 0;
        }
    }


    //Método que atualiza somente o status de um livro.
    public boolean alterarStatus(long id, Status status) throws SQLException {
        String sql = "UPDATE livros SET status = ? WHERE id = ?";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, status.name());
            stm.setLong(2, id);

            return stm.executeUpdate() > 0;
        }
    }

    //Método que remove um livro do banco pelo seu ID.

    public boolean deletarLivro(long id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConexaoJDBC.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setLong(1, id);
            return stm.executeUpdate() > 0;
        }
    }


    //Método auxiliar que mapeia um registro do ResultSet para um objeto Livro.

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        // Cria o objeto Livro com dados do banco, incluindo status convertido de String para enum
        return new Livro(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("ano_publicacao"),
                rs.getString("editora"),
                rs.getString("isbn"),
                Status.valueOf(rs.getString("status"))
        ) {{
            setId(rs.getLong("id"));  // Seta o ID do livro no objeto
        }};
    }
}
