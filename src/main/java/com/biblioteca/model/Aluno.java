package com.biblioteca.model;

import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;

public class Aluno extends Usuario {
    //subclasse de Usuario

    //construtor da subclasse Aluno, que está herdando da classe Usuario
    //Essa subclase irá construir um Usuario, passando todos os dados e definindo o tipo como ALUNO sem precisar setar manualmente depois


    public Aluno(long id, String nome, String matricula, String cpf, boolean ativo, String email, Turno turno) {
        super(id, nome, matricula, cpf, ativo, email, turno, TipoUsuario.ALUNO);
    }

}