package br.ufal.ic.p2.bikeshare.exceptions;

public class NivelBateriaNumericoException extends BikeshareException {
    public NivelBateriaNumericoException() {
        super("Nivel de bateria deve ser numerico.");
    }
}
