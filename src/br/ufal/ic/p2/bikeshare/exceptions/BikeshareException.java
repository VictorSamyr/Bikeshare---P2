package br.ufal.ic.p2.bikeshare.exceptions;

/**
 * Superclasse de todos os erros de negocio do sistema.
 * Permite que a Facade e os servicos declarem um unico tipo de excecao
 * em vez de usar a classe generica Exception.
 */
public class BikeshareException extends Exception {
    public BikeshareException(String mensagem) {
        super(mensagem);
    }

    public BikeshareException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
