package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.utils.Configuracao;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modalidade de cobranca: franquia de minutos gratuitos por corrida e valor do minuto excedente.
 * Um plano sem mensalidade (como o avulso) guarda mensalidadePadrao nula.
 */
public class Plano implements Serializable {
    private final String nome;
    private final int franquiaMinutos;
    private final BigDecimal valorMinutoExcedente;
    private final BigDecimal mensalidadePadrao;

    public Plano(String nome, int franquiaMinutos, BigDecimal valorMinutoExcedente, BigDecimal mensalidadePadrao) {
        this.nome = nome;
        this.franquiaMinutos = franquiaMinutos;
        this.valorMinutoExcedente = valorMinutoExcedente;
        this.mensalidadePadrao = mensalidadePadrao;
    }

    /** Plano avulso: sem franquia e sem mensalidade, paga todos os minutos. */
    public static Plano criarAvulso() {
        return new Plano("avulso", 0, Configuracao.getValorMinutoAvulso(), null);
    }

    public static Plano criarAssinante() {
        return new Plano("assinante", Configuracao.getFranquiaAssinante(),
                Configuracao.getValorMinutoExcedenteAssinante(), Configuracao.getMensalidadePadraoAssinante());
    }

    public BigDecimal calcularValor(long minutos) {
        long minutosExcedentes = Math.max(0, minutos - franquiaMinutos);
        return valorMinutoExcedente.multiply(BigDecimal.valueOf(minutosExcedentes));
    }

    public boolean possuiMensalidade() {
        return mensalidadePadrao != null;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getMensalidadePadrao() {
        return mensalidadePadrao;
    }
}
