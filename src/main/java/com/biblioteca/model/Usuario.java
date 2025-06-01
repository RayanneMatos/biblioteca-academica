package main.java.com.biblioteca.model;

import java.io.ObjectInputFilter.Status;

public abstract class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private boolean status;
    private String email;
    private Turno turno;

    public Usuario(int id, String nome, String cpf, boolean status, String email, Status turno) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.status = status;
        this.email = email;
        this.turno = turno;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Status getTurno() {
        return turno;
    }

    public void setTurno(Status turno) {
        this.status = turno;
    }

}