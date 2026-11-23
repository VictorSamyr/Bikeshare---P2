package br.ufal.ic.p2.bikeshare.exceptions;

public class ValorPositivoException extends BikeshareException {
    public ValorPositivoException() {
        super("Valor deve ser positivo.");
    }
}
