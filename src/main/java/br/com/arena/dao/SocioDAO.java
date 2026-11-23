package br.com.arena.dao;

import br.com.arena.config.FabricaConexao;
import br.com.arena.model.Socio;
import br.com.arena.model.enums.StatusSocio;
import br.com.arena.model.planos.EscolhaPlano;
import br.com.arena.model.planos.ModalidadeSocio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    public void salvar(Socio socio) {
        String sql = """
                INSERT INTO socios (nome, cpf, senha_hash, status, plano)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (cpf) DO UPDATE
                SET nome = EXCLUDED.nome,
                    status = EXCLUDED.status,
                    plano = EXCLUDED.plano
                RETURNING id
                """;

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, socio.getNome());
            stmt.setString(2, socio.getCpf());
            stmt.setString(3, socio.getSenhaHash());
            stmt.setString(4, socio.getStatus().name());
            stmt.setString(5, socio.getPlano().retornarPlano());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    socio.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar sócio no banco: " + e.getMessage(), e);
        }
    }

    public Socio buscarPorCpf(String cpf) {
        String sql = "SELECT id, nome, cpf, senha_hash, status, plano FROM socios WHERE cpf = ?";

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearSocio(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar sócio por CPF: " + e.getMessage(), e);
        }
    }

    public List<Socio> listarTodos() {
        String sql = "SELECT id, nome, cpf, senha_hash, status, plano FROM socios ORDER BY id";
        List<Socio> socios = new ArrayList<>();

        try (Connection conn = FabricaConexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                socios.add(mapearSocio(rs));
            }
            return socios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar sócios: " + e.getMessage(), e);
        }
    }

    private Socio mapearSocio(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String senhaHash = rs.getString("senha_hash");
        StatusSocio status = StatusSocio.valueOf(rs.getString("status"));
        ModalidadeSocio plano = EscolhaPlano.criarPlano(rs.getString("plano"));
        return new Socio(id, nome, cpf, senhaHash, status, plano);
    }
}