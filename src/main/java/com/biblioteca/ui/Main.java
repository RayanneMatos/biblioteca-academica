package com.biblioteca.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.biblioteca.database.DatabaseInitializer;
import com.biblioteca.enums.Status;
import com.biblioteca.enums.Turno;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Professor;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecarioService;
import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;

public class Main {

    private static final UsuarioService usuarioService = new UsuarioService();
    private static final LivroService livroService = new LivroService();
    private static final BibliotecarioService bibliotecarioService = new BibliotecarioService();
    private static final EmprestimoService emprestimoService = new EmprestimoService();

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

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
        System.out.println("\n=== MENU BIBLIOTECÁRIO ===");
        System.out.println("1. Cadastrar Bibliotecário");
        System.out.println("2. Realizar Login");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        switch (opcao) {
            case 1 -> {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("E-mail: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();
                Bibliotecario bibliotecario = new Bibliotecario(nome, cpf, email, senha);

                // realiza o cadastro do bibliotecario
                String resultado = bibliotecarioService.cadastrarBibliotecario(bibliotecario);
                System.out.println(resultado);

            }
            case 2 -> {
                System.out.print("E-mail: ");
                String login = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                // realiza o login
                Bibliotecario bibliotecario = bibliotecarioService.login(login, senha);
                if (bibliotecario == null) {
                    System.out.println("Não foi possível realizar o login. Login ou Senha incorreta");
                    return;
                }

                System.out.println("Login realizado com sucesso! \n" + bibliotecario.toString());
            }
            case 0 -> System.out.println("Voltando...");
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void menuLivro(Scanner scanner) {
        System.out.println("\n=== MENU LIVRO ===");
        System.out.println("1. Cadastrar Livro");
        System.out.println("2. Listar Todos os Livros");
        System.out.println("3. Buscar Livro por ID");
        System.out.println("4. Buscar Livro por Nome");
        System.out.println("5. Atualizar Livro");
        System.out.println("6. Alterar Status do Livro");
        System.out.println("7. Deletar Livro");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // limpar buffer
        switch (opcao) {
            case 1:
                String nome = scanner.nextLine();
                String autor = scanner.nextLine();
                int anoPublicacao = scanner.nextInt();
                String editora = scanner.nextLine();
                String isbn = scanner.nextLine();
                Livro livro = new Livro(nome, autor, anoPublicacao, editora, isbn, Status.DISPONIVEL);
                String resultado = livroService.cadastrarLivro(livro);
                System.out.println(resultado);
                break;
            case 2:
                List<Livro> livros = livroService.listarTodosLivros();
                if (livros.isEmpty()) {
                    System.out.println("Nenhum livro cadastrado.");
                } else {
                    livros.forEach(System.out::println);
                }
                break;
            case 3:
                System.out.print("Digite o ID do livro: ");
                long id = scanner.nextLong();
                scanner.nextLine();
                Livro livroEncontrado = livroService.buscarLivroPorId(id);
                if (livroEncontrado != null) {
                    System.out.println("Livro encontrado: " + livroEncontrado);
                } else {
                    System.out.println("Livro não encontrado.");
                }
                break;
            case 4:
                System.out.print("Digite o nome do livro: ");
                String nomeLivro = scanner.nextLine();
                String resultadoBusca = livroService.buscarLivroPorNome(nomeLivro);
                System.out.println(resultadoBusca);
                break;
            case 5:
                System.out.print("Digite o ID do livro a ser atualizado: ");
                long idLivro = scanner.nextLong();
                Livro livroAntigo = livroService.buscarLivroPorId(idLivro);
                if (livroAntigo == null) {
                    System.out.println("Livro não encontrado.");
                    return;
                }
                scanner.nextLine();
                System.out.print("Novo título: ");
                livroAntigo.setTitulo(scanner.nextLine());
                System.out.print("Novo autor: ");
                livroAntigo.setAutor(scanner.nextLine());
                System.out.print("Novo ano de publicação: ");
                livroAntigo.setAnoPublicacao(scanner.nextInt());
                scanner.nextLine();
                System.out.print("Nova editora: ");
                livroAntigo.setEditora(scanner.nextLine());
                System.out.print("Novo ISBN: ");
                livroAntigo.setIsbn(scanner.nextLine());

                String resultadoAtualizacao = livroService.atualizarLivro(livroAntigo);
                System.out.println(resultadoAtualizacao);
                break;
            case 6:
                System.out.print("Digite o ID do livro: ");
                long idLivroStatus = scanner.nextLong();
                Livro livroStatus = livroService.buscarLivroPorId(idLivroStatus);
                if (livroStatus == null) {
                    System.out.println("Livro não encontrado.");
                    return;
                }
                Status novoStatus = null;
                while (novoStatus == null) {
                    System.out.print("Novo status (DISPONIVEL, EMPRESTADO, etc): ");
                    String novoStatusStr = scanner.nextLine().toUpperCase();
                    try {
                        novoStatus = Status.valueOf(novoStatusStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Status inválido");
                    }
                }
                String resultadoStatus = livroService.alterarStatusLivro(idLivroStatus, novoStatus);
                System.out.println(resultadoStatus);
                break;
            case 7:
                System.out.print("Digite o ID do livro a ser deletado: ");
                long idLivroDeletar = scanner.nextLong();
                scanner.nextLine();
                String resultadoDeletar = livroService.deletarLivro(idLivroDeletar);
                System.out.println(resultadoDeletar);
                break;
            case 0:
                System.out.println("Voltando...");
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void menuEmprestimo(Scanner scanner) {
        System.out.println("\n=== MENU EMPRÉSTIMO ===");
        System.out.println("1. Cadastrar Empréstimo");
        System.out.println("2. Realizar Devolução Livro");
        System.out.println("3. Listar Empréstimos Ativos");
        System.out.println("4. Listar Empréstimos Atrasados");
        System.out.println("5. Listar Histórico");
        System.out.println("6. Buscar Histórico por Usuário");
        System.out.println("7. Buscar Histórico por Livro");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();
        switch (opcao) {
            case 1:
                // recuperar usuario
                System.out.println("Informe a matrícula do usuário: ");
                String matricula = scanner.nextLine();
                Usuario emprestimo = usuarioService.buscarPorMatricula(matricula);
                if (emprestimo == null) {
                    System.out.println("Usuário não encontrado.");
                    return;
                }
                // recuperar livro
                System.out.println("Informe o ID do livro: ");
                long idLivro = scanner.nextLong();
                scanner.nextLine();
                Livro livro = livroService.buscarLivroPorId(idLivro);
                if (livro == null) {
                    System.out.println("Livro não encontrado.");
                    return;
                }
                // cadastrar emprestimo
                Emprestimo novoEmprestimo = new Emprestimo(emprestimo, livro);
                String resultado = emprestimoService.salvarEmprestimo(novoEmprestimo);
                System.out.println(resultado);
                break;
            case 2:
                System.out.println("Informe a matrícula do usuário: ");
                String matriculaDevolucao = scanner.nextLine();
                List<Emprestimo> emprestimos = emprestimoService.buscarHistoricoPorUsuario(matriculaDevolucao);
                if (emprestimos.isEmpty()) {
                    System.out.println("Nenhum empréstimo encontrado.");
                    return;
                }
                System.out.println("Informe o isbn do livro: ");
                String isbnDevolucao = scanner.nextLine();
                Emprestimo emprestimoDevolucao = emprestimos.stream()
                        .filter(e -> e.getLivro() != null && isbnDevolucao.equals(e.getLivro().getIsbn()))
                        .findFirst()
                        .orElse(null);
                if (emprestimoDevolucao == null) {
                    System.out.println("Empréstimo não encontrado.");
                    return;
                }

                String resultadoDevolucao = emprestimoService.devolverLivro(emprestimoDevolucao.getLivro().getId());
                System.out.println(resultadoDevolucao);
                break;
            case 3:
                List<Emprestimo> emprestimosAtivos = emprestimoService.listarEmprestimosAtivos();
                if (emprestimosAtivos.isEmpty()) {
                    System.out.println("Nenhum empréstimo ativo encontrado.");
                } else {
                    emprestimosAtivos.forEach(System.out::println);
                }
            case 4:
                List<Emprestimo> emprestimosAtrasados = emprestimoService.listarEmprestimosAtrasados();
                if (emprestimosAtrasados.isEmpty()) {
                    System.out.println("Nenhum empréstimo atrasado encontrado.");
                } else {
                    emprestimosAtrasados.forEach(System.out::println);
                }
            case 5:
                List<Emprestimo> historico = emprestimoService.listarHistoricoCompleto();
                if (historico.isEmpty()) {
                    System.out.println("Nenhum histórico encontrado.");
                } else {
                    historico.forEach(System.out::println);
                }
            case 6:
                System.out.println("Informe a matrícula do usuário: ");
                String matriculaHistorico = scanner.nextLine();
                List<Emprestimo> historicoPorUsuario = emprestimoService.buscarHistoricoPorUsuario(matriculaHistorico);
                if (historicoPorUsuario.isEmpty()) {
                    System.out.println("Nenhum histórico encontrado.");
                } else {
                    historicoPorUsuario.forEach(System.out::println);
                }
            case 7:
                System.out.println("Informe o isbn do livro: ");
                String isbnHistorico = scanner.nextLine();
                List<Emprestimo> historicoPorLivro = emprestimoService.buscarHistoricoPorLivro(isbnHistorico);
                if (historicoPorLivro.isEmpty()) {
                    System.out.println("Nenhum histórico encontrado.");
                } else {
                    historicoPorLivro.forEach(System.out::println);
                }

        }

    }
}
