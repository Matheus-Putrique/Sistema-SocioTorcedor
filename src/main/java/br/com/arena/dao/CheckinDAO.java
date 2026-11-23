package br.com.arena.dao;

import br.com.arena.config.FabricaConexao;
import br.com.arena.model.Checkin;
import br.com.arena.model.enums.RegiaoEstadio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CheckinDAO {

    public void salvar(Checkin checkin) {
        String sql = "INSERT INTO checkins (socio_id, partida_id, regiao, data_hora_confirmacao) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, checkin.getSocio().getId());
            stmt.setLong(2, checkin.getPartida().getId());
            stmt.setString(3, checkin.getRegiao().name());
            stmt.setTimestamp(4, Timestamp.valueOf(checkin.getDataHoraConfirmacao()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    checkin.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar check-in no banco: " + e.getMessage(), e);
        }
    }

    public boolean jaRealizouCheckin(Long socioId, Long partidaId) {
        String sql = "SELECT COUNT(*) FROM checkins WHERE socio_id = ? AND partida_id = ?";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, socioId);
            stmt.setLong(2, partidaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar check-in duplicado: " + e.getMessage(), e);
        }
    }

    public int contarPorPartidaERegiao(Long partidaId, RegiaoEstadio regiao) {
        String sql = "SELECT COUNT(*) FROM checkins WHERE partida_id = ? AND regiao = ?";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, partidaId);
            stmt.setString(2, regiao.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar ocupação do setor: " + e.getMessage(), e);
        }
    }
}