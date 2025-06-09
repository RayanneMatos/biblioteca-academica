package com.biblioteca.service;

import java.sql.SQLException;

import com.biblioteca.dao.BibliotecarioDAO;
import com.biblioteca.model.Bibliotecario;

public class BibliotecarioService {

    private final BibliotecarioDAO bibliotecarioDAO;

    // Construtor que inicializa o DAO de bibliotecario, lançando SQLException se houver erro na conexão
    public BibliotecarioService() throws SQLException {
        this.bibliotecarioDAO = new BibliotecarioDAO();
    }

     // Método para cadastrar um novo bibliotecário
    public String cadastrarBibliotecario(Bibliotecario bibliotecario) {
        if (bibliotecario.getNome() == null || bibliotecario.getCpf() == null || 
            bibliotecario.getEmail() == null || bibliotecario.getSenha() == null) {
            return "Todos os campos são obrigatórios para cadastro.";
        }

        // Chama o método do DAO para realizar o cadastro no banco de dados
        return bibliotecarioDAO.cadastrar(bibliotecario);
    }

    // Método para autenticar o bibliotecário via login
    public Bibliotecario login(String email, String senha) {
        if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
            return null;
        }

         // Chama o DAO para verificar as credenciais e retornar o objeto bibliotecário, se válido
        return bibliotecarioDAO.login(email, senha);
    }
}
