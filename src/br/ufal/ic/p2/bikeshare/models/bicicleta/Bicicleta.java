package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.AtributoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.BicicletaIndisponivelException;
import br.ufal.ic.p2.bikeshare.exceptions.BicicletaSemBateriaException;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Superclasse das bicicletas (convencional e eletrica).
 */
public abstract class Bicicleta implements Serializable {
    private final String id;
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIVEL;
    private Estacao estacao;

    protected Bicicleta(String id) {
        this.id = id;
    }

    public abstract String getTipo();

    /** Valor cobrado alem da tarifa do plano, de acordo com o tipo da bicicleta. */
    public abstract BigDecimal calcularAdicional(long minutos);

    /** Indica se a bicicleta pode ser retirada agora (conta como disponivel na estacao). */
    public boolean podeSerRetirada() {
        return estado == EstadoBicicleta.DISPONIVEL;
    }

    public void validarRetirada() throws BikeshareException {
        if (estado != EstadoBicicleta.DISPONIVEL) {
            throw new BicicletaIndisponivelException();
        }
    }

    public String getAtributo(String atributo) throws BikeshareException {
        switch (atributo) {
            case "tipo":
                return getTipo();
            case "estado":
                return estado.getDescricao();
            case "estacao":
                return estacao == null ? "nenhuma" : estacao.getId();
            case "nivelBateria":
                throw new BicicletaSemBateriaException();
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public void iniciarUso() {
        estado = EstadoBicicleta.EM_USO;
    }

    public void liberar() {
        estado = EstadoBicicleta.DISPONIVEL;
    }

    public void enviarParaManutencao() {
        estado = EstadoBicicleta.EM_MANUTENCAO;
    }

    public boolean estaDisponivel() {
        return estado == EstadoBicicleta.DISPONIVEL;
    }

    /** So a Estacao altera onde a bicicleta esta, para manter os dois lados coerentes. */
    void setEstacao(Estacao estacao) {
        this.estacao = estacao;
    }

    public String getId() {
        return id;
    }

    public EstadoBicicleta getEstado() {
        return estado;
    }
}
