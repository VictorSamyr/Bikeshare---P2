package br.ufal.ic.p2.bikeshare.exceptions;

public class PersistenciaException extends BikeshareException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
