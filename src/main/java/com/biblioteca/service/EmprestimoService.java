package com.biblioteca.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.model.Emprestimo;

public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;

    // Construtor que inicializa o DAO de empréstimo

    public EmprestimoService() {
        this.emprestimoDAO = new EmprestimoDAO();
    }

    // Método para salvar um novo empréstimo
    public String salvarEmprestimo(Emprestimo emprestimo) {
        try {
            // Valida se o empréstimo possui usuário e livro associados
            if (emprestimo.getUsuario() == null || emprestimo.getLivro() == null) {
                return "Usuário e livro são obrigatórios para o empréstimo.";
            }
            // Salva o empréstimo no banco usando o DAO
            emprestimoDAO.realizarEmprestimo(emprestimo);
            return "Empréstimo salvo com sucesso!";
        } catch (SQLException e) {
            // Retorna mensagem de erro em caso de exceção SQL
            return "Erro ao salvar empréstimo: " + e.getMessage();
        }
    }

    // Método para devolver livro
    public String devolverLivro(long livroId) {
        try {
            emprestimoDAO.devolverPorIdDoLivro(livroId);
            return "Devolução processada com sucesso para o livro ID: " + livroId;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao processar devolução: " + e.getMessage();
        }
    }

    // Método para listar todos os empréstimos que estão atualmente ativos.
    public List<Emprestimo> listarEmprestimosAtivos() {
        try {
            return emprestimoDAO.listarAtivos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos ativos: " + e.getMessage());
            return Collections.emptyList(); // Retorna uma lista vazia
        }
    }

    // Método para listar todos os empréstimos que estão atrasados.
    public List<Emprestimo> listarEmprestimosAtrasados() {
        try {
            return emprestimoDAO.listarAtrasados();
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos atrasados: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Método para listar o histórico completo de empréstimos do sistema.
    public List<Emprestimo> listarHistoricoCompleto() {
        try {
            // Retorna a lista de empréstimos obtida pelo DAO
            return emprestimoDAO.listarHistoricoCompleto();
        } catch (SQLException e) {
            // Imprime erro no console e retorna lista vazia em caso de falha
            System.err.println("Erro ao listar histórico de empréstimos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Método para buscar o histórico de empréstimos de um usuário pela matrícula.
    public List<Emprestimo> buscarHistoricoPorUsuario(String matricula) {
        try {
            if (matricula == null || matricula.trim().isEmpty()) {
                System.err.println("Matrícula não pode ser vazia.");
                return Collections.emptyList();
            }
            return emprestimoDAO.listarPorMatriculaDeUsuario(matricula);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico do usuário: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Método para buscar o histórico de empréstimos de um livro pelo ISBN.
    public List<Emprestimo> buscarHistoricoPorLivro(String isbn) {
        try {
            if (isbn == null || isbn.trim().isEmpty()) {
                System.err.println("ISBN não pode ser vazio.");
                return Collections.emptyList();
            }
            return emprestimoDAO.listarPorIsbnDoLivro(isbn);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico do livro: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
