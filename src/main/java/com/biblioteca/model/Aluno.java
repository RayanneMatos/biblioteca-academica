// Aluno.java
package com.biblioteca.model;

import com.biblioteca.enums.TipoUsuario;
import com.biblioteca.enums.Turno;

public class Aluno extends Usuario {
    //subclasse de Usuario

    //construtor da subclasse Aluno, que está herdando da classe Usuario
    //Essa subclase irá construir um Usuario, passando todos os dados e definindo o tipo como ALUNO sem precisar setar manualmente depois

    public Aluno(String nome, String matricula, String cpf, String email, Turno turno) {
        super(nome, matricula, cpf, email, turno, TipoUsuario.ALUNO);
    }
}
