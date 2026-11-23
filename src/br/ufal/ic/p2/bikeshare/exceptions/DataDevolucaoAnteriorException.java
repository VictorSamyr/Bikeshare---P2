package br.ufal.ic.p2.bikeshare.exceptions;

public class DataDevolucaoAnteriorException extends BikeshareException {
    public DataDevolucaoAnteriorException() {
        super("Data de devolucao anterior a data de retirada.");
    }
}
