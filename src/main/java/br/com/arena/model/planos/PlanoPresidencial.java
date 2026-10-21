package br.com.arena.model.planos;

import br.com.arena.model.enums.RegiaoEstadio;
import java.time.LocalDateTime;

public class PlanoPresidencial implements ModalidadeSocio{

    @Override
    public String retornarPlano(){
        return "Plano Presidencial";
    }

    @Override
    public boolean acessoDoPlano(RegiaoEstadio regiaoEstadio){
        if(regiaoEstadio != RegiaoEstadio.CURVA_NORTE && regiaoEstadio != null){
            return true;
        }
        return false;
    }

    @Override
    public LocalDateTime inicioCheckin(LocalDateTime dataHora){
        return dataHora.minusHours(120);
    }
    @Override
    public LocalDateTime fimCheckin(LocalDateTime dataHora){
        return dataHora.minusHours(1);
    }
}
