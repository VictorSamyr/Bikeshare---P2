package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.BateriaInsuficienteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.NivelBateriaForaDoIntervaloException;
import br.ufal.ic.p2.bikeshare.utils.Configuracao;

import java.math.BigDecimal;

/**
 * Bicicleta com bateria: cobra adicional por minuto e exige bateria minima para ser retirada.
 */
public class BicicletaEletrica extends Bicicleta {
    private final int nivelBateria;

    public BicicletaEletrica(String id, int nivelBateria) throws NivelBateriaForaDoIntervaloException {
        super(id);
        if (nivelBateria < 0 || nivelBateria > 100) {
            throw new NivelBateriaForaDoIntervaloException();
        }
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return "eletrica";
    }

    @Override
    public BigDecimal calcularAdicional(long minutos) {
        return Configuracao.getAdicionalMinutoEletrica().multiply(BigDecimal.valueOf(minutos));
    }

    private boolean temBateriaSuficiente() {
        return nivelBateria >= Configuracao.getBateriaMinima();
    }

    @Override
    public boolean podeSerRetirada() {
        return super.podeSerRetirada() && temBateriaSuficiente();
    }

    @Override
    public void validarRetirada() throws BikeshareException {
        super.validarRetirada();
        if (!temBateriaSuficiente()) {
            throw new BateriaInsuficienteException();
        }
    }

    @Override
    public String getAtributo(String atributo) throws BikeshareException {
        if (atributo.equals("nivelBateria")) {
            return String.valueOf(nivelBateria);
        }
        return super.getAtributo(atributo);
    }
}
