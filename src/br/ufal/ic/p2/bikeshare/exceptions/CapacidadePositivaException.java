package br.ufal.ic.p2.bikeshare.exceptions;

public class CapacidadePositivaException extends BikeshareException {
    public CapacidadePositivaException() {
        super("Capacidade deve ser positiva.");
    }
}
