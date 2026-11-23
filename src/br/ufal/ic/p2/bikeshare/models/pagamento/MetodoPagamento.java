package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.AtributoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoUsaCartaoException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoUsaPixException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoUsaSaldoException;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Superclasse das formas de pagamento do usuario.
 */
public abstract class MetodoPagamento implements Serializable {

    public abstract String getCodigo();

    /**
     * Cada subclasse responde pelos proprios atributos e repassa os demais para ca.
     * Aqui so chegam atributos de outros metodos de pagamento.
     */
    public String getAtributo(String atributo) throws BikeshareException {
        switch (atributo) {
            case "numeroCartao":
            case "validadeCartao":
                throw new UsuarioNaoUsaCartaoException();
            case "chavePix":
                throw new UsuarioNaoUsaPixException();
            case "saldo":
                throw new UsuarioNaoUsaSaldoException();
            default:
                throw new AtributoNaoExisteException();
        }
    }

    /** Verifica, antes da retirada, se o pagamento podera ser feito. Por padrao, sempre pode. */
    public void autorizarRetirada() throws BikeshareException {
    }

    /** Cobra o valor de uma corrida. Por padrao, a cobranca e aprovada. */
    public void cobrar(BigDecimal valor) throws BikeshareException {
    }
}
