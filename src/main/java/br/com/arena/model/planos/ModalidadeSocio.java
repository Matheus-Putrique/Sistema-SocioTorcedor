package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;

import java.time.LocalDateTime;

public interface ModalidadeSocio {
    String retornarPlano();
    boolean acessoDoPlano(RegiaoEstadio regiaoEstadio);
    LocalDateTime inicioCheckin(LocalDateTime dataHora);
    LocalDateTime fimCheckin(LocalDateTime dataHora);


}
