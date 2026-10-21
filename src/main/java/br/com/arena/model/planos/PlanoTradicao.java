package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;
import java.time.LocalDateTime;

public class PlanoTradicao implements ModalidadeSocio {

    @Override
    public String retornarPlano() {
        return "Plano Tradicao";
    }
    @Override
    public boolean acessoDoPlano(RegiaoEstadio regiaoEstadio) {
        if(regiaoEstadio == RegiaoEstadio.ANEL_PANORAMICO || regiaoEstadio == RegiaoEstadio.CONVES_INFERIOR) {
            return true;
        }
        return false;
    }
    @Override
    public LocalDateTime inicioCheckin(LocalDateTime dataHora) {
        return dataHora.minusHours(72);
    }
    @Override
    public LocalDateTime fimCheckin(LocalDateTime dataHora) {
        return dataHora.minusHours(24);
    }
}
