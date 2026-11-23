package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.utils.Configuracao;

/**
 * Penalidade automatica, de valor fixo, gerada quando um aluguel expira.
 */
public class PenalidadeAtraso extends Penalidade {
    public PenalidadeAtraso() {
        super(Configuracao.getValorPenalidadeAtraso());
    }
}
