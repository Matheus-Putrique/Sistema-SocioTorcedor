package br.com.arena.service;

import br.com.arena.dao.CheckinDAO;
import br.com.arena.model.Checkin;
import br.com.arena.model.Partida;
import br.com.arena.model.Socio;
import br.com.arena.model.enums.RegiaoEstadio;
import br.com.arena.model.enums.StatusSocio;

import java.time.LocalDateTime;

public class ServicoCheckin {

    private final CheckinDAO checkinDAO = new CheckinDAO();

    public void realizarCheckin(Socio socio, Partida partida, RegiaoEstadio regiaoEstadio) {
        if (socio.getStatus() != StatusSocio.ATIVO) {
            throw new IllegalStateException("Checkin só pode ser realizado por sócios ativos");
        }
        if (!socio.getPlano().acessoDoPlano(regiaoEstadio)) {
            throw new IllegalArgumentException("Plano não é válido para esta região");
        }

        LocalDateTime horaAtual = LocalDateTime.now();

        if (horaAtual.isBefore(socio.getPlano().inicioCheckin(partida.getDataHora()))) {
            throw new IllegalStateException("Checkin ainda não foi aberto");
        }
        if (horaAtual.isAfter(socio.getPlano().fimCheckin(partida.getDataHora()))) {
            throw new IllegalStateException("Checkin já fechado");
        }

        if (checkinDAO.jaRealizouCheckin(socio.getId(), partida.getId())) {
            throw new IllegalStateException("Sócio já realizou check-in nesta partida");
        }

        int ocupados = checkinDAO.contarPorPartidaERegiao(partida.getId(), regiaoEstadio);
        if (ocupados >= regiaoEstadio.getCapacidade()) {
            throw new IllegalStateException("Setor lotado");
        }

        Checkin novoCheckin = new Checkin(0L, socio, partida, regiaoEstadio, horaAtual);
        checkinDAO.salvar(novoCheckin);
    }
}