package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.exceptions.AluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.exceptions.AluguelNaoEmAndamentoException;
import br.ufal.ic.p2.bikeshare.exceptions.AtributoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.DataDevolucaoAnteriorException;
import br.ufal.ic.p2.bikeshare.exceptions.EstacaoCheiaException;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Estacao;
import br.ufal.ic.p2.bikeshare.models.usuario.PenalidadeAtraso;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.utils.Configuracao;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Uma corrida: vai da retirada da bicicleta ate a devolucao.
 */
public class Aluguel implements Serializable {
    private final String id;
    private final Usuario usuario;
    private final Bicicleta bicicleta;
    private final Estacao estacaoOrigem;
    private final LocalDateTime inicio;
    private Estacao estacaoDestino;
    private LocalDateTime fim;
    private BigDecimal valorCobrado;
    private EstadoAluguel estado = EstadoAluguel.EM_ANDAMENTO;

    private Aluguel(String id, Usuario usuario, Bicicleta bicicleta, Estacao estacaoOrigem, LocalDateTime inicio) {
        this.id = id;
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacaoOrigem = estacaoOrigem;
        this.inicio = inicio;
    }

    /**
     * Cria o aluguel e retira a bicicleta da estacao.
     * As regras de retirada devem ter sido conferidas antes.
     */
    public static Aluguel iniciar(String id, Usuario usuario, Bicicleta bicicleta, Estacao estacaoOrigem,
                                  LocalDateTime inicio) {
        Aluguel aluguel = new Aluguel(id, usuario, bicicleta, estacaoOrigem, inicio);
        estacaoOrigem.entregarParaAluguel(bicicleta);
        usuario.iniciarAluguel(aluguel);
        return aluguel;
    }

    /**
     * Devolve a bicicleta na estacao de destino. Se alguma regra falhar, nada e alterado.
     */
    public void devolver(Estacao destino, LocalDateTime dataFim) throws BikeshareException {
        if (!estaEmAndamento()) {
            throw new AluguelNaoEmAndamentoException();
        }
        if (dataFim.isBefore(inicio)) {
            throw new DataDevolucaoAnteriorException();
        }
        if (!destino.temVaga()) {
            throw new EstacaoCheiaException();
        }
        long minutos = Duration.between(inicio, dataFim).toMinutes();
        encerrar(destino, dataFim, minutos);
        if (estado == EstadoAluguel.EXPIRADO) {
            bicicleta.enviarParaManutencao();
        } else {
            bicicleta.liberar();
        }
        destino.adicionarBicicleta(bicicleta);
        usuario.encerrarAluguel();
    }

    /** Define o resultado da corrida: cancelada (carencia), expirada (tempo maximo) ou finalizada. */
    private void encerrar(Estacao destino, LocalDateTime dataFim, long minutos) throws BikeshareException {
        BigDecimal valor = BigDecimal.ZERO;
        if (destino == estacaoOrigem && minutos <= Configuracao.getCarenciaMinutos()) {
            estado = EstadoAluguel.CANCELADO;
        } else if (minutos > Configuracao.getTempoMaximoMinutos()) {
            estado = EstadoAluguel.EXPIRADO;
            usuario.adicionarPenalidade(new PenalidadeAtraso());
        } else {
            valor = calcularValor(minutos);
            usuario.pagar(valor);
            estado = EstadoAluguel.FINALIZADO;
        }
        this.estacaoDestino = destino;
        this.fim = dataFim;
        this.valorCobrado = valor;
    }

    /** Tarifa do plano vigente do usuario mais o adicional do tipo de bicicleta. */
    private BigDecimal calcularValor(long minutos) {
        return usuario.getPlano().calcularValor(minutos).add(bicicleta.calcularAdicional(minutos));
    }

    public boolean estaEmAndamento() {
        return estado == EstadoAluguel.EM_ANDAMENTO;
    }

    public String getAtributo(String atributo) throws BikeshareException {
        switch (atributo) {
            case "estado":
                return estado.getDescricao();
            case "usuario":
                return usuario.getId();
            case "bicicleta":
                return bicicleta.getId();
            case "estacaoOrigem":
                return estacaoOrigem.getId();
            case "inicio":
                return Conversor.formatarDataHora(inicio);
            case "fim":
            case "estacaoDestino":
            case "valorCobrado":
                return getAtributoDeDevolucao(atributo);
            default:
                throw new AtributoNaoExisteException();
        }
    }

    /** Atributos que so existem depois que a bicicleta foi devolvida. */
    private String getAtributoDeDevolucao(String atributo) throws AluguelEmAndamentoException {
        if (estaEmAndamento()) {
            throw new AluguelEmAndamentoException();
        }
        switch (atributo) {
            case "fim":
                return Conversor.formatarDataHora(fim);
            case "estacaoDestino":
                return estacaoDestino.getId();
            default:
                return Conversor.formatarDinheiro(valorCobrado);
        }
    }

    public String getId() {
        return id;
    }
}
