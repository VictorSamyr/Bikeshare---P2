package br.ufal.ic.p2.bikeshare.exceptions;

public class CapacidadeNumericaException extends BikeshareException {
    public CapacidadeNumericaException() {
        super("Capacidade deve ser numerica.");
    }
}
