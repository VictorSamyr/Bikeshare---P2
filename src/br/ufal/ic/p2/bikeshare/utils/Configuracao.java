package br.ufal.ic.p2.bikeshare.utils;

import br.ufal.ic.p2.bikeshare.exceptions.ConfiguracaoException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Parametros de tarifacao e limites de negocio (requisito nao funcional BS04).
 * Os valores ficam no arquivo bikeshare.properties, na raiz do projeto,
 * e nao embutidos no codigo.
 */
public final class Configuracao {
    private static final Path ARQUIVO = Paths.get("bikeshare.properties");
    private static final Properties PARAMETROS = carregar();

    private Configuracao() {
    }

    private static Properties carregar() {
        Properties parametros = new Properties();
        try (InputStream entrada = Files.newInputStream(ARQUIVO)) {
            parametros.load(entrada);
        } catch (IOException e) {
            throw new ConfiguracaoException("Nao foi possivel ler o arquivo " + ARQUIVO + ".");
        }
        return parametros;
    }

    private static String ler(String chave) {
        String valor = PARAMETROS.getProperty(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new ConfiguracaoException("Parametro ausente em " + ARQUIVO + ": " + chave);
        }
        return valor.trim();
    }

    private static BigDecimal lerDecimal(String chave) {
        return new BigDecimal(ler(chave));
    }

    private static int lerInteiro(String chave) {
        return Integer.parseInt(ler(chave));
    }

    public static BigDecimal getValorMinutoAvulso() {
        return lerDecimal("plano.avulso.valorMinuto");
    }

    public static int getFranquiaAssinante() {
        return lerInteiro("plano.assinante.franquiaMinutos");
    }

    public static BigDecimal getValorMinutoExcedenteAssinante() {
        return lerDecimal("plano.assinante.valorMinutoExcedente");
    }

    public static BigDecimal getMensalidadePadraoAssinante() {
        return lerDecimal("plano.assinante.mensalidadePadrao");
    }

    public static BigDecimal getAdicionalMinutoEletrica() {
        return lerDecimal("bicicleta.eletrica.adicionalMinuto");
    }

    public static int getBateriaMinima() {
        return lerInteiro("bicicleta.eletrica.bateriaMinima");
    }

    public static int getCarenciaMinutos() {
        return lerInteiro("aluguel.carenciaMinutos");
    }

    public static int getTempoMaximoMinutos() {
        return lerInteiro("aluguel.tempoMaximoMinutos");
    }

    public static BigDecimal getValorPenalidadeAtraso() {
        return lerDecimal("penalidade.atraso.valor");
    }
}
