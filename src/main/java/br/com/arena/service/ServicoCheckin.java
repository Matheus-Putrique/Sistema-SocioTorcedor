package br.com.arena.service;

import br.com.arena.model.Checkin;
import br.com.arena.model.Partida;
import br.com.arena.model.Socio;
import br.com.arena.model.enums.RegiaoEstadio;
import br.com.arena.model.enums.StatusSocio;
import br.com.arena.model.planos.ModalidadeSocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicoCheckin {
    public  List<Checkin> checkins = new ArrayList<>();

    public  void realizarCheckin(Socio socio, Partida partida, RegiaoEstadio regiaoEstadio) {
        if(socio.getStatus() != StatusSocio.ATIVO){
            throw new IllegalStateException("Checkin só pode ser realizado por sócios ativos");
        }
        if(!socio.getPlano().acessoDoPlano(regiaoEstadio)){
            throw new IllegalArgumentException("Plano não é válido para está região");
        }
        LocalDateTime horaAtual  = LocalDateTime.now();

        if(horaAtual.isBefore(socio.getPlano().inicioCheckin(partida.getDataHora())) ){
            throw new IllegalStateException("Checkin ainda não foi aberto");
        }
        if(horaAtual.isAfter(socio.getPlano().fimCheckin(partida.getDataHora())) ){
            throw new IllegalStateException("Checkin já fechado");
        }
        int contador = 0;

        for(Checkin checkin : checkins){

            if(checkin.getPartida().getId().equals(partida.getId()) && checkin.getSocio().getId().equals(socio.getId())){
                throw new IllegalStateException("Sócio já realizou check-in nesta partida");
            }

            if(checkin.getPartida().getId().equals(partida.getId()) && checkin.getRegiao() == regiaoEstadio){
                contador++;
            }

        }

        if(contador >= regiaoEstadio.getCapacidade()){
            throw new IllegalStateException("Setor lotado");
        }

        Checkin novoCheckin = new Checkin(checkins.size() + 1, socio, partida, regiaoEstadio, horaAtual);
        checkins.add(novoCheckin);
    }
}
