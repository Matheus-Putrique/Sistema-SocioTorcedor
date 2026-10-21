package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;
import java.time.LocalDateTime;

public class PlanoMuralha implements ModalidadeSocio {

    @Override
    public String retornarPlano() {
        return "Plano Muralha";
    }

    @Override
    public boolean acessoDoPlano(RegiaoEstadio regiaoEstadio) {
        if(regiaoEstadio == RegiaoEstadio.MURALHA_SUL){
            return true;
        }

        return false;
    }

    @Override
    public LocalDateTime inicioCheckin(LocalDateTime dataHoraPartida) {
        return dataHoraPartida.minusHours(48);
    }

    @Override
    public LocalDateTime fimCheckin(LocalDateTime dataHoraPartida) {
        return dataHoraPartida.minusHours(24);
    }
}