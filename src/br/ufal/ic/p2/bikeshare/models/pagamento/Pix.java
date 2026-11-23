package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.ChavePixNulaException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

public class Pix extends MetodoPagamento {
    private final String chave;

    public Pix(String chave) throws ChavePixNulaException {
        if (Conversor.estaVazio(chave)) {
            throw new ChavePixNulaException();
        }
        this.chave = chave;
    }

    @Override
    public String getCodigo() {
        return "pix";
    }

    @Override
    public String getAtributo(String atributo) throws BikeshareException {
        if (atributo.equals("chavePix")) {
            return chave;
        }
        return super.getAtributo(atributo);
    }
}
