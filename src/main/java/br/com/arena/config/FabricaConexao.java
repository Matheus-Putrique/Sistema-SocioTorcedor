package br.com.arena.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FabricaConexao {

    private static final String NOME_BANCO = "arena_db";
    private static final String URL_SERVIDOR = "jdbc:postgresql://localhost:5432/postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/" + NOME_BANCO;
    private static final Properties PROPS = carregarPropriedades();
    private static final String USUARIO = obterConfig("DB_USER", "db.user", "postgres");
    private static final String SENHA = obterConfig("DB_PASSWORD", "db.password", "");

    private static Properties carregarPropriedades() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
        } catch (IOException ignored) {
        }
        return props;
    }

    private static String obterConfig(String envKey, String propKey, String padrao) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return PROPS.getProperty(propKey, padrao);
    }

    public static Connection obterConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    public static Connection obterConexaoServidor() {
        try {
            return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao conectar ao servidor PostgreSQL: " + e.getMessage(), e);
        }
    }

    public static String getNomeBanco() {
        return NOME_BANCO;
    }
}
