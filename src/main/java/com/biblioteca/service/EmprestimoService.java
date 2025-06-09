package com.biblioteca.service;

import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.model.Emprestimo;

public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;

    // Construtor que inicializa o DAO de empréstimo, lançando SQLException se houver erro na conexão

    public EmprestimoService() throws SQLException {
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
            emprestimoDAO.salvar(emprestimo);
            return "Empréstimo salvo com sucesso!";
        } catch (SQLException e) {
            // Retorna mensagem de erro em caso de exceção SQL
            return "Erro ao salvar empréstimo: " + e.getMessage();
        }
    }

     // Método para listar todos os empréstimos cadastrados
    public List<Emprestimo> listarTodos() {
        try {
            // Retorna a lista de empréstimos obtida pelo DAO
            return emprestimoDAO.listarTodos();
        } catch (SQLException e) {
            // Imprime erro no console e retorna null em caso de falha
            System.err.println("Erro ao listar empréstimos: " + e.getMessage());
            return null;
        }
    }

     // Método para atualizar dados de um empréstimo existente
    public String atualizarEmprestimo(Emprestimo emprestimo) {
        try {
            emprestimoDAO.atualizar(emprestimo);
            return "Empréstimo atualizado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao atualizar empréstimo: " + e.getMessage();
        }
    }

     // Método para deletar um empréstimo pelo seu ID
    public String deletarEmprestimo(long id) {
        try {
            emprestimoDAO.deletar(id);
            return "Empréstimo deletado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao deletar empréstimo: " + e.getMessage();
        }
    }
}
