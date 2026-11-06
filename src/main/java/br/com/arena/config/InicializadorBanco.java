package br.com.arena.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBanco {

    public static void inicializar() {
        criarBancoSeNaoExistir();
        criarTabelas();
    }

    private static void criarBancoSeNaoExistir() {
        String nomeBanco = FabricaConexao.getNomeBanco();
        String sqlVerifica = "SELECT 1 FROM pg_database WHERE datname = ?";

        try (Connection conn = FabricaConexao.obterConexaoServidor();
             PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica)) {

            stmtVerifica.setString(1, nomeBanco);

            try (ResultSet rs = stmtVerifica.executeQuery()) {
                if (!rs.next()) {
                    try (Statement stmtCria = conn.createStatement()) {
                        stmtCria.executeUpdate("CREATE DATABASE " + nomeBanco);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar o banco de dados: " + e.getMessage(), e);
        }
    }

    private static void criarTabelas() {
        String sqlSocios = """
                CREATE TABLE IF NOT EXISTS socios (
                    id BIGSERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cpf VARCHAR(14) NOT NULL UNIQUE,
                    senha_hash VARCHAR(255) NOT NULL,
                    status VARCHAR(30) NOT NULL,
                    plano VARCHAR(50) NOT NULL
                );
                """;

        String sqlPartidas = """
                CREATE TABLE IF NOT EXISTS partidas (
                    id BIGSERIAL PRIMARY KEY,
                    adversario VARCHAR(100) NOT NULL,
                    data_hora TIMESTAMP NOT NULL
                );
                """;

        String sqlCheckins = """
                CREATE TABLE IF NOT EXISTS checkins (
                    id BIGSERIAL PRIMARY KEY,
                    socio_id BIGINT NOT NULL REFERENCES socios(id),
                    partida_id BIGINT NOT NULL REFERENCES partidas(id),
                    regiao VARCHAR(50) NOT NULL,
                    data_hora_confirmacao TIMESTAMP NOT NULL,
                    UNIQUE (socio_id, partida_id)
                );
                """;

        try (Connection conn = FabricaConexao.obterConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlSocios);
            stmt.execute(sqlPartidas);
            stmt.execute(sqlCheckins);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar as tabelas no banco de dados: " + e.getMessage(), e);
        }
    }
}
