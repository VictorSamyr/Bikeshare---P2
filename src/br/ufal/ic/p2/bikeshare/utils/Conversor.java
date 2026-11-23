package br.ufal.ic.p2.bikeshare.utils;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.DataInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.DataNulaException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Converte os textos recebidos pela Facade em valores (e o contrario).
 */
public final class Conversor {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("d/M/uuuu H:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private Conversor() {
    }

    /** Texto nulo ou preenchido so com espacos conta como vazio. */
    public static boolean estaVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static int paraInteiro(String texto, BikeshareException seVazio, BikeshareException seInvalido)
            throws BikeshareException {
        if (estaVazio(texto)) {
            throw seVazio;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw seInvalido;
        }
    }

    /** Aceita virgula ou ponto como separador decimal (ex.: "120,50"). */
    public static BigDecimal paraDecimal(String texto, BikeshareException seVazio, BikeshareException seInvalido)
            throws BikeshareException {
        if (estaVazio(texto)) {
            throw seVazio;
        }
        try {
            return new BigDecimal(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw seInvalido;
        }
    }

    public static LocalDateTime paraDataHora(String texto) throws DataNulaException, DataInvalidaException {
        if (estaVazio(texto)) {
            throw new DataNulaException();
        }
        try {
            return LocalDateTime.parse(texto.trim(), FORMATO_DATA_HORA);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }
    }

    public static String formatarDinheiro(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }

    public static String formatarDataHora(LocalDateTime data) {
        return FORMATO_DATA_HORA.format(data);
    }
}
