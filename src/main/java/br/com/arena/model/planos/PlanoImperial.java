package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;
import java.time.LocalDateTime;

public class PlanoImperial implements ModalidadeSocio {
    @Override
    public String retornarPlano() {
        return "Plano Imperial";
    }

    @Override
    public boolean acessoDoPlano(RegiaoEstadio regiaoEstadio) {
        if(regiaoEstadio == RegiaoEstadio.TERRACO_VIP || regiaoEstadio == RegiaoEstadio.CONVES_INFERIOR || regiaoEstadio == RegiaoEstadio.ANEL_PANORAMICO) {
            return true;
        }
        return false;
    }

    @Override
    public LocalDateTime inicioCheckin(LocalDateTime dataHora) {
        return dataHora.minusHours(96);
    }

    @Override
    public LocalDateTime fimCheckin(LocalDateTime dataHora) {
        return dataHora.minusHours(6);
    }
}
