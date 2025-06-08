package com.biblioteca.dao;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;

import java.sql.*;
import java.util.List;

public class UsuarioDAO {

    //Método para cadastrar um novo usuário
    public String cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, matricula, cpf, ativo, email, turno, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stm = null;
        //Tratamento do método para garantir funcionalidade e retornar uma mensagem clara ao usuário
        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getMatricula());
            stm.setString(3, usuario.getCpf());
            stm.setBoolean(4, usuario.isAtivo());
            stm.setString(5, usuario.getEmail());
            stm.setString(6, usuario.getTurno().name());
            stm.setString(7, usuario.getTipo().name());

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                //try/catch para recuperar e setar o id que esta sendo gerado
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getLong(1));
                        return "Usuário cadastrado com sucesso!" + usuario.getId();
                    } else {
                        return "Falha ao recuperar ID do usuario inserido";
                    }
                }
            } else {
                return "Nenhuma linha afetada. Falha ao cadastrar usuario.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao cadastrar usuário: " + e.getMessage();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, null);
        }
    }

    //TODO: Implementar método
    public Usuario buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM usuario WHERE matricula = ?";
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = ConexaoJDBC.getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, matricula); // Busca pelo parâmetro String
            rs = stm.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            } else {
                System.out.println("Matricula não encontrada: " + matricula);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar a matrícula: " + matricula);
            e.printStackTrace();
        } finally {
            ConexaoJDBC.closeConnection(conn, stm, rs);
        }
        return null;
    }

    //TODO: Implementar método
    public List<Usuario> ListarUsuarios() {
        String sql = "SELECT cpf, nome";
        return null;
    }

    //Método privado da classe Usuario que vai servir para "montar" um objeto para ser utilizado como retorno para os outros métodos
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        //Define qual construtor chamar
        TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
        Usuario usuario = (tipo == TipoUsuario.ALUNO)
                ? new Aluno(rs.getString("nome"), rs.getString("matricula"), rs.getString("cpf"), rs.getString("email"), Turno.valueOf(rs.getString("turno")))
                : new Professor(rs.getString("nome"), rs.getString("matricula"), rs.getString("cpf"), rs.getString("email"), Turno.valueOf(rs.getString("turno")));
        usuario.setId(rs.getLong("id"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        return usuario;
    }
}

