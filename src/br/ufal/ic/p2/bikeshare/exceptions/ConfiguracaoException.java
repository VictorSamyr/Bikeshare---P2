package br.ufal.ic.p2.bikeshare.exceptions;

/**
 * Erro ao ler o arquivo de configuracao (bikeshare.properties).
 * E nao-verificada porque a configuracao e carregada em um bloco estatico,
 * que nao pode lancar excecoes verificadas.
 */
public class ConfiguracaoException extends RuntimeException {
    public ConfiguracaoException(String mensagem) {
        super(mensagem);
    }
}
