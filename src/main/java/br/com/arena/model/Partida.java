package br.com.arena.model;

import java.time.LocalDateTime;

public class Partida {

    private Long id;
    private String adversario;
    private LocalDateTime dataHora;


    Partida(){}

    public Partida(long id, String adversario, LocalDateTime dataHora) {
        this.id = id;
        this.adversario = adversario;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdversario() {
        return adversario;
    }

    public void setAdversario(String adversario) {
        this.adversario = adversario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
