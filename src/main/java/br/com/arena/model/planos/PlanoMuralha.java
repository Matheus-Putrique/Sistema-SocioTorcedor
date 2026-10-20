package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;
import java.time.LocalDateTime;

public class PlanoMuralha implements ModalidadeSocio {

    @Override
    public String retornarPlano() {
        return null;
    }

    @Override
    public boolean acessoDoPlano(RegiaoEstadio regiaoEstadio) {
        return false;
    }

    @Override
    public LocalDateTime inicioCheckin(LocalDateTime dataHoraPartida) {
        return null;
    }

    @Override
    public LocalDateTime fimCheckin(LocalDateTime dataHoraPartida) {
        return null;
    }
}