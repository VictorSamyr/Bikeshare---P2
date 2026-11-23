package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.MetodoPagamentoNaoCadastradoException;

import java.math.BigDecimal;

/**
 * Situacao inicial do usuario, que ainda nao cadastrou uma forma de pagamento.
 */
public class SemMetodoPagamento extends MetodoPagamento {

    @Override
    public String getCodigo() {
        return "nenhum";
    }

    @Override
    public void autorizarRetirada() throws MetodoPagamentoNaoCadastradoException {
        throw new MetodoPagamentoNaoCadastradoException();
    }

    @Override
    public void cobrar(BigDecimal valor) throws MetodoPagamentoNaoCadastradoException {
        throw new MetodoPagamentoNaoCadastradoException();
    }
}
