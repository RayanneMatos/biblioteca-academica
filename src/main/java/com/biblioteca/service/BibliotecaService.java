package com.biblioteca.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.TipoUsuario;

public class BibliotecaService {

    // Método genérico para cadastrar qualquer tipo de usuário
    public void cadastrarUsuario(Usuario usuario) {
        try (Connection con = ConexaoJDBC.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);
            usuarioDAO.cadastrar(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario autenticarUsuario(String login, String senha) {
        try (Connection con = ConexaoJDBC.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);
            return usuarioDAO.buscarPorLoginSenha(login, senha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Usuario> listarUsuariosPorTipo(TipoUsuario tipo) {
        try (Connection con = ConexaoJDBC.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);
            return usuarioDAO.listarPorTipo(tipo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
