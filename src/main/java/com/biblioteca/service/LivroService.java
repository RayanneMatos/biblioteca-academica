package com.biblioteca.service;

import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.LivroDAO;
import com.biblioteca.enums.Status;
import com.biblioteca.model.Livro;

public class LivroService {

    private final LivroDAO livroDAO;

    // Construtor que inicializa o DAO de livro, lançando SQLException se houver erro na conexão
    public LivroService() throws SQLException {
        this.livroDAO = new LivroDAO();
    }

    //Cadastra um novo livro no banco de dados
    public String cadastrarLivro(Livro livro) {
        // Validação básica dos campos obrigatórios
        if (livro == null || livro.getTitulo() == null || livro.getAutor() == null ||
            livro.getEditora() == null || livro.getIsbn() == null || livro.getStatus() == null) {
            return "Todos os campos do livro são obrigatórios.";
        }

        try {
            return livroDAO.cadastrarLivro(livro);
        } catch (SQLException e) {
            return "Erro ao cadastrar livro: " + e.getMessage();
        }
    }

     //Lista todos os livros cadastrados no sistema
    public List<Livro> listarTodosLivros() {
        try {
            return livroDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
            return null;
        }
    }

        //Busca um livro pelo ID
    public Livro buscarLivroPorId(long id) {
        try {
            return livroDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
            return null;
        }
    }

    //Busca livros pelo nome (parcial ou completo)
    public String buscarLivroPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "O nome do livro não pode estar vazio.";
        }

        try {
            return livroDAO.buscarPorNome(nome);
        } catch (SQLException e) {
            return "Erro ao buscar livro por nome: " + e.getMessage();
        }
    }

    public String atualizarLivro(Livro livro) {
         // Validação do parâmetro
        if (livro == null || livro.getId() == 0) {
            return "Livro inválido para atualização.";
        }

        try {
            boolean atualizado = livroDAO.atualizarLivro(livro);
            return atualizado ? "Livro atualizado com sucesso." : "Livro não encontrado ou não atualizado.";
        } catch (SQLException e) {
            return "Erro ao atualizar livro: " + e.getMessage();
        }
    }

    //Altera o status de um livro (DISPONIVEL, EMPRESTADO, etc)
    public String alterarStatusLivro(long id, Status status) {
        try {
            boolean alterado = livroDAO.alterarStatus(id, status);
            return alterado ? "Status do livro alterado com sucesso." : "Livro não encontrado.";
        } catch (SQLException e) {
            return "Erro ao alterar status do livro: " + e.getMessage();
        }
    }

    //Remove um livro do banco de dados
    public String deletarLivro(long id) {
        try {
            boolean deletado = livroDAO.deletarLivro(id);
            return deletado ? "Livro deletado com sucesso." : "Livro não encontrado.";
        } catch (SQLException e) {
            return "Erro ao deletar livro: " + e.getMessage();
        }
    }
}
