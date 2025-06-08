package com.biblioteca.dao;

import com.biblioteca.database.ConexaoJDBC;
import com.biblioteca.model.Usuario;

import java.sql.*;

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
    public void ListarUsuarios() {
        String sql = "SELECT cpf, nome";
    }
}
