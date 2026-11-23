package br.ufal.ic.p2.bikeshare.exceptions;

public class ValorNumericoException extends BikeshareException {
    public ValorNumericoException() {
        super("Valor deve ser numerico.");
    }
}
