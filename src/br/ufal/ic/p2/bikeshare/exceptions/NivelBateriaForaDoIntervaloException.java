package br.ufal.ic.p2.bikeshare.exceptions;

public class NivelBateriaForaDoIntervaloException extends BikeshareException {
    public NivelBateriaForaDoIntervaloException() {
        super("Nivel de bateria deve estar entre 0 e 100.");
    }
}
