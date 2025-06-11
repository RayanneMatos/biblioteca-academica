package com.biblioteca.model;

import com.biblioteca.enums.Status;

public class Livro {

    // Classe responsável por gerenciar os atributos dos livros!

    // Atributos da classe
    private long id;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private String editora;
    private String isbn;
    private Status status;

    // Método construtor
    public Livro(String titulo, String autor, int anoPublicacao, String editora, String isbn, Status status) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.isbn = isbn;
        this.status = status;
    }

    // Métodos getters e setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "\nTítulo: " + titulo +
                "Autor: " + autor +
                "Ano de Publicação: " + anoPublicacao +
                "Editora: " + editora +
                "ISBN: " + isbn +
                "Status: " + status;
    }
}