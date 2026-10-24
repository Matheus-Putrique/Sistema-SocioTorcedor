package br.com.arena.model;

import br.com.arena.model.enums.RegiaoEstadio;

import java.time.LocalDateTime;

public class Checkin {

    private Long id;
    private Socio socio;
    private Partida partida;
    private RegiaoEstadio regiao;
    private LocalDateTime dataHoraConfirmacao;

    Checkin(){}

    public Checkin(long id, Socio socio,  Partida partida, RegiaoEstadio regiao, LocalDateTime dataHoraConfirmacao) {
        this.id = id;
        this.socio = socio;
        this.partida = partida;
        this.regiao = regiao;
        this.dataHoraConfirmacao = dataHoraConfirmacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public RegiaoEstadio getRegiao() {
        return regiao;
    }

    public void setRegiao(RegiaoEstadio regiao) {
        this.regiao = regiao;
    }

    public LocalDateTime getDataHoraConfirmacao() {
        return dataHoraConfirmacao;
    }

    public void setDataHoraConfirmacao(LocalDateTime dataHoraConfirmacao) {
        this.dataHoraConfirmacao = dataHoraConfirmacao;
    }
}
