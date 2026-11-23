package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.BicicletaIndisponivelException;
import br.ufal.ic.p2.bikeshare.exceptions.BicicletaNaoEstaNaEstacaoException;
import br.ufal.ic.p2.bikeshare.exceptions.CapacidadePositivaException;
import br.ufal.ic.p2.bikeshare.exceptions.EnderecoNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.EstacaoSemVagasException;
import br.ufal.ic.p2.bikeshare.exceptions.NomeNuloException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Ponto de retirada e devolucao, com um numero fixo de vagas.
 * Toda bicicleta parada na estacao ocupa uma vaga, mesmo que nao possa ser retirada.
 */
public class Estacao implements Serializable {
    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private final List<Bicicleta> bicicletas = new ArrayList<>();

    public Estacao(String id, String nome, String endereco, int capacidade) throws BikeshareException {
        validarNomeEEndereco(nome, endereco);
        if (capacidade <= 0) {
            throw new CapacidadePositivaException();
        }
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    /** Publico para que o servico confira nome e endereco antes de converter a capacidade. */
    public static void validarNomeEEndereco(String nome, String endereco)
            throws NomeNuloException, EnderecoNuloException {
        if (Conversor.estaVazio(nome)) {
            throw new NomeNuloException();
        }
        if (Conversor.estaVazio(endereco)) {
            throw new EnderecoNuloException();
        }
    }

    public int getVagasDisponiveis() {
        return capacidade - bicicletas.size();
    }

    public boolean temVaga() {
        return getVagasDisponiveis() > 0;
    }

    public int getBicicletasDisponiveis() {
        int quantidade = 0;
        for (Bicicleta bicicleta : bicicletas) {
            if (bicicleta.podeSerRetirada()) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public boolean contem(Bicicleta bicicleta) {
        return bicicletas.contains(bicicleta);
    }

    public void adicionarBicicleta(Bicicleta bicicleta) throws EstacaoSemVagasException {
        if (!temVaga()) {
            throw new EstacaoSemVagasException();
        }
        bicicletas.add(bicicleta);
        bicicleta.setEstacao(this);
    }

    /** Remocao feita pelo operador: so vale para bicicleta disponivel nesta estacao. */
    public void removerBicicleta(Bicicleta bicicleta)
            throws BicicletaNaoEstaNaEstacaoException, BicicletaIndisponivelException {
        if (!contem(bicicleta)) {
            throw new BicicletaNaoEstaNaEstacaoException();
        }
        if (!bicicleta.estaDisponivel()) {
            throw new BicicletaIndisponivelException();
        }
        desvincular(bicicleta);
    }

    public void validarRetirada(Bicicleta bicicleta) throws BikeshareException {
        if (!contem(bicicleta)) {
            throw new BicicletaNaoEstaNaEstacaoException();
        }
        bicicleta.validarRetirada();
    }

    /** A bicicleta retirada sai da estacao e libera a vaga. */
    public void entregarParaAluguel(Bicicleta bicicleta) {
        desvincular(bicicleta);
        bicicleta.iniciarUso();
    }

    private void desvincular(Bicicleta bicicleta) {
        bicicletas.remove(bicicleta);
        bicicleta.setEstacao(null);
    }

    public String getId() {
        return id;
    }
}
