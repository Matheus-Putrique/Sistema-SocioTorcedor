package br.com.arena;

import br.com.arena.config.InicializadorBanco;
import br.com.arena.dao.CheckinDAO;
import br.com.arena.dao.PartidaDAO;
import br.com.arena.dao.SocioDAO;
import br.com.arena.model.Partida;
import br.com.arena.model.Socio;
import br.com.arena.model.enums.RegiaoEstadio;
import br.com.arena.model.enums.StatusSocio;
import br.com.arena.model.planos.ModalidadeSocio;
import br.com.arena.model.planos.PlanoImperial;
import br.com.arena.model.planos.PlanoMuralha;
import br.com.arena.model.planos.PlanoPresidencial;
import br.com.arena.model.planos.PlanoTradicao;
import br.com.arena.service.ServicoCheckin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        InicializadorBanco.inicializar();

        SocioDAO socioDAO = new SocioDAO();
        PartidaDAO partidaDAO = new PartidaDAO();
        CheckinDAO checkinDAO = new CheckinDAO();
        ServicoCheckin servicoCheckin = new ServicoCheckin();

        partidaDAO.garantirPartidasIniciais();

        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("\n==================================================");
            System.out.println("          PORTAL DO SÓCIO-TORCEDOR                ");
            System.out.println("==================================================");
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Cadastrar-se como Sócio");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> {
                    Socio socioLogado = realizarLogin(scanner, socioDAO);
                    if (socioLogado != null) {
                        menuSocioLogado(scanner, socioLogado, partidaDAO, checkinDAO, servicoCheckin);
                    }
                }
                case "2" -> {
                    Socio novoSocio = cadastrarNovoSocio(scanner, socioDAO);
                    if (novoSocio != null) {
                        menuSocioLogado(scanner, novoSocio, partidaDAO, checkinDAO, servicoCheckin);
                    }
                }
                case "0" -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    executando = false;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private static Socio realizarLogin(Scanner scanner, SocioDAO socioDAO) {
        System.out.println("\n--- LOGIN DO SÓCIO ---");
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        Socio socio = socioDAO.buscarPorCpf(cpf);
        if (socio == null || !socio.getSenhaHash().equals(senha)) {
            System.out.println("CPF ou senha incorretos!");
            return null;
        }

        System.out.println("\nLogin realizado com sucesso! Bem-vindo(a), " + socio.getNome() + "!");
        return socio;
    }

    private static Socio cadastrarNovoSocio(Scanner scanner, SocioDAO socioDAO) {
        System.out.println("\n--- CADASTRO DE SÓCIO-TORCEDOR ---");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("Nome não pode ficar vazio!");
            return null;
        }

        System.out.print("CPF (somente números): ");
        String cpf = scanner.nextLine().trim();
        if (cpf.isEmpty()) {
            System.out.println("CPF não pode ficar vazio!");
            return null;
        }

        if (socioDAO.buscarPorCpf(cpf) != null) {
            System.out.println("Já existe um sócio cadastrado com este CPF! Faça login na opção 1.");
            return null;
        }

        System.out.print("Crie uma senha: ");
        String senha = scanner.nextLine().trim();
        if (senha.isEmpty()) {
            System.out.println("A senha não pode ficar vazia!");
            return null;
        }

        System.out.println("\nEscolha o seu Plano:");
        System.out.println("1 - Plano Muralha      (Acesso: Muralha-Sul | Check-in: 48h até 24h antes)");
        System.out.println("2 - Plano Tradição     (Acesso: Convés Inferior e Anel Panorâmico | Check-in: 72h até 24h antes)");
        System.out.println("3 - Plano Imperial     (Acesso: Terraço VIP, Convés e Anel | Check-in: 96h até 6h antes)");
        System.out.println("4 - Plano Presidencial (Acesso: Todos exceto Curva Norte | Check-in: 120h até 1h antes)");
        System.out.print("Opção de plano (1-4): ");

        String opPlano = scanner.nextLine().trim();
        ModalidadeSocio plano = switch (opPlano) {
            case "1" -> new PlanoMuralha();
            case "2" -> new PlanoTradicao();
            case "3" -> new PlanoImperial();
            case "4" -> new PlanoPresidencial();
            default -> {
                System.out.println("Opção inválida! Selecionado Plano Muralha por padrão.");
                yield new PlanoMuralha();
            }
        };

        Socio novo = new Socio(0L, nome, cpf, senha, StatusSocio.ATIVO, plano);
        socioDAO.salvar(novo);
        System.out.println("\nCadastro concluído! Você já está logado no " + plano.retornarPlano() + ".");
        return novo;
    }

    private static void menuSocioLogado(
            Scanner scanner,
            Socio socioLogado,
            PartidaDAO partidaDAO,
            CheckinDAO checkinDAO,
            ServicoCheckin servicoCheckin
    ) {
        boolean logado = true;

        while (logado) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Sócio: " + socioLogado.getNome()
                    + " | Plano: " + socioLogado.getPlano().retornarPlano()
                    + " | Status: " + socioLogado.getStatus());
            System.out.println("--------------------------------------------------");
            System.out.println("1 - Ver Jogos Disponíveis e Fazer Check-in");
            System.out.println("0 - Sair da conta (Logout)");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> realizarCheckinDoSocio(scanner, socioLogado, partidaDAO, checkinDAO, servicoCheckin);
                case "0" -> {
                    System.out.println("Sessão encerrada.");
                    logado = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void realizarCheckinDoSocio(
            Scanner scanner,
            Socio socio,
            PartidaDAO partidaDAO,
            CheckinDAO checkinDAO,
            ServicoCheckin servicoCheckin
    ) {
        List<Partida> partidas = partidaDAO.listarTodas();
        LocalDateTime agora = LocalDateTime.now();

        System.out.println("\n--- JOGOS DISPONÍVEIS ---");
        for (Partida p : partidas) {
            LocalDateTime abertura = socio.getPlano().inicioCheckin(p.getDataHora());
            LocalDateTime fechamento = socio.getPlano().fimCheckin(p.getDataHora());
            boolean janelaAberta = !agora.isBefore(abertura) && !agora.isAfter(fechamento);
            boolean jaFez = checkinDAO.jaRealizouCheckin(socio.getId(), p.getId());

            String statusJanela;
            if (jaFez) {
                statusJanela = "[CHECK-IN JÁ REALIZADO]";
            } else if (janelaAberta) {
                statusJanela = "[CHECK-IN ABERTO PARA SEU PLANO]";
            } else if (agora.isBefore(abertura)) {
                statusJanela = "[ABRE EM " + abertura.format(FORMATADOR_DATA) + "]";
            } else {
                statusJanela = "[CHECK-IN ENCERRADO]";
            }

            System.out.printf("ID [%d] - Arena x %-12s | Data: %s | %s%n",
                    p.getId(), p.getAdversario(), p.getDataHora().format(FORMATADOR_DATA), statusJanela);
        }

        System.out.print("\nDigite o ID do jogo para fazer check-in (ou 0 para voltar): ");
        Long partidaId = lerLong(scanner);
        if (partidaId == null || partidaId == 0) return;

        Partida partidaEscolhida = partidaDAO.buscarPorId(partidaId);
        if (partidaEscolhida == null) {
            System.out.println("Jogo não encontrado!");
            return;
        }

        // Mostra apenas os setores liberados pelo plano do sócio logado
        List<RegiaoEstadio> setoresLiberados = new ArrayList<>();
        for (RegiaoEstadio r : RegiaoEstadio.values()) {
            if (socio.getPlano().acessoDoPlano(r)) {
                setoresLiberados.add(r);
            }
        }

        System.out.println("\n--- SETORES DISPONÍVEIS NO SEU PLANO (" + socio.getPlano().retornarPlano() + ") ---");
        for (int i = 0; i < setoresLiberados.size(); i++) {
            RegiaoEstadio r = setoresLiberados.get(i);
            int ocupados = checkinDAO.contarPorPartidaERegiao(partidaEscolhida.getId(), r);
            System.out.printf("%d - %-18s | Ocupação: %d/%d%n",
                    (i + 1), r.getNome(), ocupados, r.getCapacidade());
        }

        System.out.print("\nEscolha o número do setor (1 a " + setoresLiberados.size() + "): ");
        Long opcaoSetor = lerLong(scanner);
        if (opcaoSetor == null || opcaoSetor < 1 || opcaoSetor > setoresLiberados.size()) {
            System.out.println("Opção de setor inválida!");
            return;
        }

        RegiaoEstadio regiaoEscolhida = setoresLiberados.get(opcaoSetor.intValue() - 1);

        try {
            servicoCheckin.realizarCheckin(socio, partidaEscolhida, regiaoEscolhida);
            System.out.println("\nCHECK-IN REALIZADO COM SUCESSO!");
            System.out.println("Sócio: " + socio.getNome());
            System.out.println("Jogo: Arena x " + partidaEscolhida.getAdversario() + " (" + partidaEscolhida.getDataHora().format(FORMATADOR_DATA) + ")");
            System.out.println("Setor confirmado: " + regiaoEscolhida.getNome());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\nNÃO FOI POSSÍVEL REALIZAR O CHECK-IN: " + e.getMessage());
        }
    }

    private static Long lerLong(Scanner scanner) {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Número inválido!");
            return null;
        }
    }
}