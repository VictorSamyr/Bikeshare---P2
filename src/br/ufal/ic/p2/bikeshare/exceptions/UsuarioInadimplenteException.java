package br.ufal.ic.p2.bikeshare.exceptions;

public class UsuarioInadimplenteException extends BikeshareException {
    public UsuarioInadimplenteException() {
        super("Usuario inadimplente.");
    }
}
