package br.com.arena.model.enums;

public enum RegiaoEstadio {

    MURALHA_SUL ("Muralha-Sul", 5000, 90.00),
    CONVES_INFERIOR("Conves-Inferior", 18000, 150.00),
    ANEL_PANORAMICO("Anel-Panoramico", 15000, 140.00),
    TERRACO_VIP("Terraco-Vip", 3000, 350.00),
    SUITES_EXECUTIVAS("Suites-Executivas", 1500, 600.00),;

    private String nome;
    private int capacidade;
    private double preco;

    RegiaoEstadio(String nome, int capacidade, double preco) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
