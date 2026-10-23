package br.com.arena.model;

import br.com.arena.model.enums.StatusSocio;
import br.com.arena.model.planos.ModalidadeSocio;

public class Socio {

    private Long id;
    private String nome;
    private String cpf;
    private String senhaHash;
    private StatusSocio status;
    private ModalidadeSocio plano;

    Socio(){}

    public Socio(Long id, String nome, String cpf, String senha, StatusSocio status, ModalidadeSocio plano) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.senhaHash = senha;
        this.status = status;
        this.plano = plano;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public StatusSocio getStatus() {
        return status;
    }

    public void setStatus(StatusSocio status) {
        this.status = status;
    }

    public ModalidadeSocio getPlano() {
        return plano;
    }

    public void setPlano(ModalidadeSocio plano) {
        this.plano = plano;
    }
}
