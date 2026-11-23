package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.NumeroCartaoNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.ValidadeCartaoInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.ValidadeCartaoNulaException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Cartao extends MetodoPagamento {
    private static final DateTimeFormatter FORMATO_VALIDADE = DateTimeFormatter.ofPattern("MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) throws BikeshareException {
        if (Conversor.estaVazio(numero)) {
            throw new NumeroCartaoNuloException();
        }
        validarValidade(validade);
        this.numero = numero;
        this.validade = validade;
    }

    /** A validade segue o formato MM/AAAA, com mes entre 01 e 12. */
    private static void validarValidade(String validade)
            throws ValidadeCartaoNulaException, ValidadeCartaoInvalidaException {
        if (Conversor.estaVazio(validade)) {
            throw new ValidadeCartaoNulaException();
        }
        try {
            YearMonth.parse(validade, FORMATO_VALIDADE);
        } catch (DateTimeParseException e) {
            throw new ValidadeCartaoInvalidaException();
        }
    }

    @Override
    public String getCodigo() {
        return "cartao";
    }

    @Override
    public String getAtributo(String atributo) throws BikeshareException {
        switch (atributo) {
            case "numeroCartao":
                return numero;
            case "validadeCartao":
                return validade;
            default:
                return super.getAtributo(atributo);
        }
    }
}
