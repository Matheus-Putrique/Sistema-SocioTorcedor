package br.com.arena.model.planos;

public class EscolhaPlano {

    public static ModalidadeSocio criarPlano(String nomePlano) {
        if (nomePlano == null) {
            throw new IllegalArgumentException("Nome do plano não pode ser nulo");
        }
        return switch (nomePlano.trim().toUpperCase()) {
            case "PLANO MURALHA", "MURALHA" -> new PlanoMuralha();
            case "PLANO TRADICAO", "TRADICAO", "PLANO TRADIÇÃO" -> new PlanoTradicao();
            case "PLANO IMPERIAL", "IMPERIAL" -> new PlanoImperial();
            case "PLANO PRESIDENCIAL", "PRESIDENCIAL" -> new PlanoPresidencial();
            default -> throw new IllegalArgumentException("Plano desconhecido: " + nomePlano);
        };
    }
}