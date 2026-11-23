package br.ufal.ic.p2.bikeshare.services;

import br.ufal.ic.p2.bikeshare.exceptions.*;
import br.ufal.ic.p2.bikeshare.models.EstadoSistema;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.bicicleta.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.models.bicicleta.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Estacao;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

/**
 * Cadastro de estacoes, gestao de bicicletas e consulta de disponibilidade (US5 a US7).
 */
public class EstacaoService {

    public Estacao buscarEstacao(EstadoSistema estado, String id) throws BikeshareException {
        if (Conversor.estaVazio(id)) {
            throw new IdentificacaoEstacaoNulaException();
        }
        return estado.buscarEstacao(id);
    }

    public Bicicleta buscarBicicleta(EstadoSistema estado, String id) throws BikeshareException {
        if (Conversor.estaVazio(id)) {
            throw new IdentificacaoBicicletaNulaException();
        }
        return estado.buscarBicicleta(id);
    }

    // ---------------------------------------------------------------- estacoes

    public String criarEstacao(EstadoSistema estado, String nome, String endereco, String capacidade)
            throws BikeshareException {
        Estacao.validarNomeEEndereco(nome, endereco);
        int vagas = Conversor.paraInteiro(capacidade, new CapacidadeNulaException(),
                new CapacidadeNumericaException());
        Estacao estacao = new Estacao(estado.proximoIdEstacao(), nome, endereco, vagas);
        estado.adicionarEstacao(estacao);
        return estacao.getId();
    }

    public String getBicicletasDisponiveis(EstadoSistema estado, String idEstacao) throws BikeshareException {
        return String.valueOf(buscarEstacao(estado, idEstacao).getBicicletasDisponiveis());
    }

    public String getVagasDisponiveis(EstadoSistema estado, String idEstacao) throws BikeshareException {
        return String.valueOf(buscarEstacao(estado, idEstacao).getVagasDisponiveis());
    }

    // ---------------------------------------------------------------- bicicletas

    /**
     * Adiciona uma bicicleta na estacao. nivelBateria e nulo quando nao foi informado.
     * Os dados da bicicleta sao validados antes da vaga.
     */
    public void adicionarBicicleta(EstadoSistema estado, String idEstacao, String idBicicleta, String tipo,
                                   String nivelBateria) throws BikeshareException {
        Estacao estacao = buscarEstacao(estado, idEstacao);
        if (Conversor.estaVazio(idBicicleta)) {
            throw new IdentificacaoBicicletaNulaException();
        }
        if (estado.existeBicicleta(idBicicleta)) {
            throw new BicicletaJaExisteException();
        }
        Bicicleta bicicleta = criarBicicleta(idBicicleta, tipo, nivelBateria);
        estacao.adicionarBicicleta(bicicleta);
        estado.adicionarBicicleta(bicicleta);
    }

    private Bicicleta criarBicicleta(String id, String tipo, String nivelBateria) throws BikeshareException {
        if ("convencional".equals(tipo)) {
            if (nivelBateria != null) {
                throw new BicicletaSemBateriaException();
            }
            return new BicicletaConvencional(id);
        }
        if ("eletrica".equals(tipo)) {
            int nivel = Conversor.paraInteiro(nivelBateria, new NivelBateriaNuloException(),
                    new NivelBateriaNumericoException());
            return new BicicletaEletrica(id, nivel);
        }
        throw new TipoInvalidoException();
    }

    public void removerBicicleta(EstadoSistema estado, String idEstacao, String idBicicleta)
            throws BikeshareException {
        Estacao estacao = buscarEstacao(estado, idEstacao);
        Bicicleta bicicleta = buscarBicicleta(estado, idBicicleta);
        estacao.removerBicicleta(bicicleta);
        estado.removerBicicleta(idBicicleta);
    }

    public String getAtributoBicicleta(EstadoSistema estado, String idBicicleta, String atributo)
            throws BikeshareException {
        return buscarBicicleta(estado, idBicicleta).getAtributo(atributo);
    }

    public String getEstadoBicicleta(EstadoSistema estado, String idBicicleta) throws BikeshareException {
        return buscarBicicleta(estado, idBicicleta).getEstado().getDescricao();
    }
}
