package br.ufal.ic.p2.bikeshare.exceptions;

public class BicicletaSemBateriaException extends BikeshareException {
    public BicicletaSemBateriaException() {
        super("Bicicleta nao possui bateria.");
    }
}
