package br.ufal.ic.p2.bikeshare.exceptions;

public class MensalidadeNumericaException extends BikeshareException {
    public MensalidadeNumericaException() {
        super("Mensalidade deve ser numerica.");
    }
}
