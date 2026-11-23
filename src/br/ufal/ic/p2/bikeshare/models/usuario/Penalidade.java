package br.ufal.ic.p2.bikeshare.models.usuario;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cobranca adicional lancada sobre um usuario. Enquanto houver penalidade,
 * o usuario fica inadimplente.
 */
public abstract class Penalidade implements Serializable {
    private final BigDecimal valor;

    protected Penalidade(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
