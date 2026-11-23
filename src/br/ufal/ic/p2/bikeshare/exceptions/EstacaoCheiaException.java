package br.ufal.ic.p2.bikeshare.exceptions;

public class EstacaoCheiaException extends BikeshareException {
    public EstacaoCheiaException() {
        super("Estacao cheia.");
    }
}
