package br.com.arena;

import br.com.arena.model.Checkin;
import br.com.arena.model.Partida;
import br.com.arena.model.Socio;
import br.com.arena.model.enums.RegiaoEstadio;
import br.com.arena.model.enums.StatusSocio;
import br.com.arena.model.planos.ModalidadeSocio;
import br.com.arena.model.planos.PlanoMuralha;
import br.com.arena.service.ServicoCheckin;

import java.time.LocalDateTime;

public class Main{
    public static void main(String[] args){

        ServicoCheckin checkin = new ServicoCheckin();

        Partida partida = new Partida(1L, "Flamengo", LocalDateTime.now().plusHours(36));
        Socio socio = new Socio(1L, "José", "12345678901", "1234", StatusSocio.ATIVO, new PlanoMuralha());

        try{
            checkin.realizarCheckin(socio, partida, RegiaoEstadio.MURALHA_SUL);
            System.out.println("Checkin realizado!!!");

        }catch(IllegalArgumentException | IllegalStateException e){
            System.out.println("Erro: " +  e.getMessage());

        }

    }
}
