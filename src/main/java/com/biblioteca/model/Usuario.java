package com.biblioteca.model;

import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;


/*Requisitos:
Cadastro de usuários: nome, matrícula, tipo (aluno ou professor), CPF e e-mail*/
public abstract class Usuario {
    private long id; //long para garantir a capacidade de persistência no banco de dados, visando a longo prazo
    private String nome;
    private String matricula;
    private String cpf;
    private boolean ativo;
    private String email;
    private Turno turno;
    private final TipoUsuario tipoUsuario; //não poderá ser alterado durante todo o contexto de execução

    //Construtor Usuario
    public Usuario(String nome, String matricula, String cpf, String email, Turno turno, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.matricula = matricula;
        this.cpf = cpf;
        this.email = email;
        this.turno = turno;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
    }

    // get e set ID
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    // get e set nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    // get e set matricula
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    // get e set CPF
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // get e set Turno
    public Turno getTurno() {
        return turno;
    }
    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    // get e set E-mail
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // get e set Status
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // get Tipo de usuário
    // Sem set para não permitir que seja alterado o tipo do usuário após a criação
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}