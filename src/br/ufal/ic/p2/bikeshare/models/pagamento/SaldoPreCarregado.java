package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.SaldoInsuficienteException;
import br.ufal.ic.p2.bikeshare.exceptions.ValorPositivoException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.math.BigDecimal;

/**
 * Credito carregado antecipadamente; cada corrida e descontada do saldo.
 */
public class SaldoPreCarregado extends MetodoPagamento {
    private BigDecimal saldo = BigDecimal.ZERO;

    @Override
    public String getCodigo() {
        return "saldoPreCarregado";
    }

    @Override
    public String getAtributo(String atributo) throws BikeshareException {
        if (atributo.equals("saldo")) {
            return Conversor.formatarDinheiro(saldo);
        }
        return super.getAtributo(atributo);
    }

    public void recarregar(BigDecimal valor) throws ValorPositivoException {
        if (valor.signum() <= 0) {
            throw new ValorPositivoException();
        }
        saldo = saldo.add(valor);
    }

    /** Sem saldo nao ha retirada. */
    @Override
    public void autorizarRetirada() throws SaldoInsuficienteException {
        if (saldo.signum() <= 0) {
            throw new SaldoInsuficienteException();
        }
    }

    @Override
    public void cobrar(BigDecimal valor) throws SaldoInsuficienteException {
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException();
        }
        saldo = saldo.subtract(valor);
    }
}
