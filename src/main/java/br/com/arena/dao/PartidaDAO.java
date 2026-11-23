package br.com.arena.dao;

import br.com.arena.config.FabricaConexao;
import br.com.arena.model.Partida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {

    public void salvar(Partida partida) {
        String sql = "INSERT INTO partidas (adversario, data_hora) VALUES (?, ?) RETURNING id";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, partida.getAdversario());
            stmt.setTimestamp(2, Timestamp.valueOf(partida.getDataHora()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    partida.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar partida no banco: " + e.getMessage(), e);
        }
    }

    public List<Partida> listarTodas() {
        String sql = "SELECT id, adversario, data_hora FROM partidas ORDER BY data_hora";
        List<Partida> partidas = new ArrayList<>();

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                partidas.add(new Partida(
                        rs.getLong("id"),
                        rs.getString("adversario"),
                        rs.getTimestamp("data_hora").toLocalDateTime()
                ));
            }
            return partidas;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar partidas: " + e.getMessage(), e);
        }
    }

    public Partida buscarPorId(Long id) {
        String sql = "SELECT id, adversario, data_hora FROM partidas WHERE id = ?";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Partida(
                            rs.getLong("id"),
                            rs.getString("adversario"),
                            rs.getTimestamp("data_hora").toLocalDateTime()
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar partida: " + e.getMessage(), e);
        }
    }

    public void garantirPartidasIniciais() {
        if (listarTodas().size() < 3) {
            salvar(new Partida(0L, "Palmeiras", LocalDateTime.now().plusHours(30)));
            salvar(new Partida(0L, "São Paulo", LocalDateTime.now().plusHours(50)));
            salvar(new Partida(0L, "Corinthians", LocalDateTime.now().plusHours(80)));
        }
    }
}