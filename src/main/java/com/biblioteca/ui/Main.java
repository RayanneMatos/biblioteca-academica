package com.biblioteca.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.biblioteca.database.DatabaseInitializer;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;

public class Main {

    public static void main(String[] args) {
        com.biblioteca.database.DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.mostrarUsuarios();
        
        try (Scanner scanner = new Scanner(System.in)) {
            boolean sair = false;

            while (!sair) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Usuário");
                System.out.println("2. Bibliotecário");
                System.out.println("3. Livro");
                System.out.println("4. Empréstimo");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpa buffer

                switch (opcao) {
                    case 1 -> menuUsuario(scanner);
                    case 2 -> menuBibliotecario(scanner);
                    case 3 -> menuLivro(scanner);
                    case 4 -> menuEmprestimo(scanner);
                    case 0 -> {
                        sair = true;
                        System.out.println("Saindo...");
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
    }

        private static void menuUsuario(Scanner scanner) {
        UsuarioService usuarioService;
        try {
            usuarioService = new UsuarioService();
        } catch (SQLException e) {
            System.err.println("Erro ao iniciar serviço de usuário: " + e.getMessage());
            return;
        }

        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU USUÁRIO ===");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Buscar por matrícula");
            System.out.println("3. Buscar por nome");
            System.out.println("4. Listar todos os usuários");
            System.out.println("5. Listar apenas alunos");
            System.out.println("6. Listar apenas professores");
            System.out.println("7. Deletar usuário por ID");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("E-mail: ");
                    String email = scanner.nextLine();
                    System.out.print("Turno (MATUTINO, VESPERTINO, NOTURNO): ");
                    String turnoStr = scanner.nextLine().toUpperCase();
                    Turno turno;
                    
                    try {
                        turno = Turno.valueOf(turnoStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Turno inválido. Usando MATUTINO como padrão.");
                        turno = Turno.MATUTINO;
                    }

                    System.out.print("tipousuario (Aluno ou Professor): ");
                    String tipousuario = scanner.nextLine().toLowerCase();

                    Usuario novoUsuario = null;
                    if (tipousuario.equals("aluno")) {
                        novoUsuario = new Aluno(nome, matricula, cpf, email, turno);
                    } else if (tipousuario.equals("professor")) {
                        novoUsuario = new Professor(nome, matricula, cpf, email, turno);
                    } else {
                        System.out.println("tipousuario inválido. Cadastro cancelado.");
                        break;
                    }

                    String resultado = usuarioService.cadastrarUsuario(novoUsuario);
                    System.out.println(resultado);
                }


                    case 2 -> {
                    System.out.print("Digite a matrícula: ");
                    String matricula = scanner.nextLine();
                    Usuario usuario = usuarioService.buscarPorMatricula(matricula);
                    if (usuario != null) {
                        System.out.println("Usuário encontrado: " + usuario);
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    }       

                    case 3 -> {
                        System.out.print("Digite o nome: ");
                        String nome = scanner.nextLine();
                        List<Usuario> usuarios = usuarioService.buscarPorNome(nome);
                        if (usuarios.isEmpty()) {
                            System.out.println("Nenhum usuário encontrado.");
                        } else {
                            usuarios.forEach(System.out::println);
                        }
                    }

                    case 4 -> {
                        List<Usuario> todos = usuarioService.listarTodos();
                        if (todos == null || todos.isEmpty()) {
                            System.out.println("Nenhum usuário cadastrado.");
                        } else {
                            todos.forEach(System.out::println);
                        }
                    }

                    case 5 -> {
                        List<Usuario> alunos = usuarioService.listarAlunos();
                        if (alunos == null || alunos.isEmpty()) {
                            System.out.println("Nenhum aluno cadastrado.");
                        } else {
                            alunos.forEach(System.out::println);
                        }
                    }

                    case 6 -> {
                        List<Usuario> professores = usuarioService.listarProfessores();
                        if (professores == null || professores.isEmpty()) {
                            System.out.println("Nenhum professor cadastrado.");
                        } else {
                            professores.forEach(System.out::println);
                        }
                    }

                    case 7 -> {
                        System.out.print("Digite o ID do usuário: ");
                        long id = scanner.nextLong();
                        scanner.nextLine(); // limpar buffer
                        String resposta = usuarioService.deletarUsuario(id);
                        System.out.println(resposta);
                    }

                    case 0 -> voltar = true;

                    default -> System.out.println("Opção inválida.");
                }
        }
    }

    private static void menuBibliotecario(Scanner scanner) {
        System.out.println("Você está no menu de Bibliotecário.");
        // Implementar funcionalidade conforme necessário
    }

    private static void menuLivro(Scanner scanner) {
        System.out.println("Você está no menu de Livro.");
        // Implementar funcionalidade conforme necessário
    }

    private static void menuEmprestimo(Scanner scanner) {
        System.out.println("Você está no menu de Empréstimo.");
        // Implementar funcionalidade conforme necessário
    }
}
