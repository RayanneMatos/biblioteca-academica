package com.biblioteca.service;

import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    // Construtor que inicializa o DAO de usuario
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Cadastrar usuário com regra de validação simples
    public String cadastrarUsuario(Usuario usuario) {
        try {
            if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
                return "Nome do usuário não pode ser vazio.";
            }
            if (usuario.getMatricula() == null || usuario.getMatricula().isEmpty()) {
                return "Matrícula do usuário não pode ser vazia.";
            }
            return usuarioDAO.cadastrarUsuario(usuario);
        } catch (SQLException e) {
            return "Erro ao cadastrar usuário no serviço: " + e.getMessage();
        }
    }

    // Buscar por matrícula
    public Usuario buscarPorMatricula(String matricula) {
        try {
            return usuarioDAO.buscarPorMatricula(matricula);
        } catch (SQLException e) {
            System.err.println("Erro no serviço ao buscar por matrícula: " + e.getMessage());
            return null;
        }
    }

    // Buscar por nome
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioDAO.buscarPorNome(nome);
    }

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os usuários: " + e.getMessage());
            return null;
        }
    }

    // Listar apenas alunos
    public List<Usuario> listarAlunos() {
        try {
            return usuarioDAO.listarAlunos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos: " + e.getMessage());
            return null;
        }
    }

    // Listar apenas professores
    public List<Usuario> listarProfessores() {
        try {
            return usuarioDAO.listarProfessores();
        } catch (SQLException e) {
            System.err.println("Erro ao listar professores: " + e.getMessage());
            return null;
        }
    }

    // Deletar usuário por ID
    public String deletarUsuario(long id) {
        try {
            usuarioDAO.deletar(id);
            return "Usuário deletado com sucesso!";
        } catch (Exception e) {
            return "Erro ao deletar usuário: " + e.getMessage();
        }
    }
}
