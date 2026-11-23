package br.ufal.ic.p2.bikeshare.exceptions;

public class AluguelNaoEmAndamentoException extends BikeshareException {
    public AluguelNaoEmAndamentoException() {
        super("Aluguel nao esta em andamento.");
    }
}
