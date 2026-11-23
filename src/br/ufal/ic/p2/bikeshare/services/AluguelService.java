package br.ufal.ic.p2.bikeshare.services;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.IdentificacaoAluguelNulaException;
import br.ufal.ic.p2.bikeshare.models.EstadoSistema;
import br.ufal.ic.p2.bikeshare.models.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Estacao;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.time.LocalDateTime;

/**
 * Retirada e devolucao de bicicletas (US8 e US9).
 */
public class AluguelService {
    private final UsuarioService usuarioService;
    private final EstacaoService estacaoService;

    public AluguelService(UsuarioService usuarioService, EstacaoService estacaoService) {
        this.usuarioService = usuarioService;
        this.estacaoService = estacaoService;
    }

    public Aluguel buscarAluguel(EstadoSistema estado, String id) throws BikeshareException {
        if (Conversor.estaVazio(id)) {
            throw new IdentificacaoAluguelNulaException();
        }
        return estado.buscarAluguel(id);
    }

    /**
     * Ordem das regras: dados informados, usuario (inadimplencia e aluguel em andamento),
     * bicicleta (estacao, estado e bateria) e, por fim, o metodo de pagamento.
     */
    public String retirarBicicleta(EstadoSistema estado, String idUsuario, String idEstacao, String idBicicleta,
                                   String data) throws BikeshareException {
        Usuario usuario = usuarioService.buscarUsuario(estado, idUsuario);
        Estacao estacao = estacaoService.buscarEstacao(estado, idEstacao);
        Bicicleta bicicleta = estacaoService.buscarBicicleta(estado, idBicicleta);
        LocalDateTime inicio = Conversor.paraDataHora(data);

        usuario.validarNovaRetirada();
        estacao.validarRetirada(bicicleta);
        usuario.autorizarPagamento();

        Aluguel aluguel = Aluguel.iniciar(estado.proximoIdAluguel(), usuario, bicicleta, estacao, inicio);
        estado.adicionarAluguel(aluguel);
        return aluguel.getId();
    }

    public void devolverBicicleta(EstadoSistema estado, String idAluguel, String idEstacao, String data)
            throws BikeshareException {
        Aluguel aluguel = buscarAluguel(estado, idAluguel);
        Estacao destino = estacaoService.buscarEstacao(estado, idEstacao);
        LocalDateTime fim = Conversor.paraDataHora(data);
        aluguel.devolver(destino, fim);
    }

    public String getAtributoAluguel(EstadoSistema estado, String idAluguel, String atributo)
            throws BikeshareException {
        return buscarAluguel(estado, idAluguel).getAtributo(atributo);
    }
}
