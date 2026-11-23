package br.ufal.ic.p2.bikeshare.exceptions;

public class BicicletaIndisponivelException extends BikeshareException {
    public BicicletaIndisponivelException() {
        super("Bicicleta nao esta disponivel.");
    }
}
