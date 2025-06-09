package com.biblioteca.model;

public class Bibliotecario {

    //Classe responsável pelo gerenciamento do sistema, como emprestimo/devolução de livros, criação de novos usuarios e livros.


    //Atributos da classe
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String senha; //Testar lógica para solicitar senha na hora de relizar o empréstimo


    //Método construtor
    public Bibliotecario(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }


    //Métodos getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}