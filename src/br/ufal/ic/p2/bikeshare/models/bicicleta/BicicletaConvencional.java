package br.ufal.ic.p2.bikeshare.models.bicicleta;

import java.math.BigDecimal;

public class BicicletaConvencional extends Bicicleta {

    public BicicletaConvencional(String id) {
        super(id);
    }

    @Override
    public String getTipo() {
        return "convencional";
    }

    @Override
    public BigDecimal calcularAdicional(long minutos) {
        return BigDecimal.ZERO;
    }
}
