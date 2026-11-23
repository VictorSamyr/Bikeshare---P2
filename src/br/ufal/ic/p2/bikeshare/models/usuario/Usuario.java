package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.*;
import br.ufal.ic.p2.bikeshare.models.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.models.pagamento.Cartao;
import br.ufal.ic.p2.bikeshare.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.models.pagamento.Pix;
import br.ufal.ic.p2.bikeshare.models.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.models.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclasse dos usuarios (pessoa fisica e pessoa juridica).
 */
public abstract class Usuario implements Serializable {
    private final String id;
    private String nome;
    private String email;
    private String senha;
    private Plano plano;
    private BigDecimal mensalidade;
    private MetodoPagamento metodoPagamento = new SemMetodoPagamento();
    // Guardado a parte para o saldo nao se perder quando o usuario troca de metodo de pagamento.
    private SaldoPreCarregado saldoPreCarregado;
    private Aluguel aluguelAtivo;
    private final List<Penalidade> penalidades = new ArrayList<>();

    protected Usuario(String id, String nome, String email, String senha, Plano plano, BigDecimal mensalidade)
            throws BikeshareException {
        this.id = id;
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.senha = validarSenha(senha);
        contratarPlano(plano, mensalidade);
    }

    public abstract String getTipo();

    /** Atributos que so existem em um dos subtipos (cpf, cnpj, razaoSocial). */
    protected abstract String getAtributoEspecifico(String atributo) throws BikeshareException;

    public String getAtributo(String atributo) throws BikeshareException {
        switch (atributo) {
            case "nome":
                return nome;
            case "email":
                return email;
            case "senha":
                return senha;
            case "tipo":
                return getTipo();
            case "estado":
                return getEstado().getDescricao();
            case "plano":
                return plano.getNome();
            default:
                return getAtributoDeUso(atributo);
        }
    }

    /** Atributos ligados ao uso do servico: mensalidade, aluguel, pendencias e pagamento. */
    private String getAtributoDeUso(String atributo) throws BikeshareException {
        switch (atributo) {
            case "mensalidade":
                return Conversor.formatarDinheiro(getMensalidade());
            case "aluguelAtivo":
                return aluguelAtivo == null ? "nenhum" : aluguelAtivo.getId();
            case "totalPendencias":
                return Conversor.formatarDinheiro(getTotalPendencias());
            case "metodoPagamento":
                return metodoPagamento.getCodigo();
            case "numeroCartao":
            case "validadeCartao":
            case "chavePix":
            case "saldo":
                return metodoPagamento.getAtributo(atributo);
            default:
                return getAtributoEspecifico(atributo);
        }
    }

    // ---------------------------------------------------------------- dados cadastrais

    private static String validarNome(String nome) throws NomeNuloException {
        if (Conversor.estaVazio(nome)) {
            throw new NomeNuloException();
        }
        return nome;
    }

    private static String validarEmail(String email) throws EmailNuloException, EmailInvalidoException {
        if (Conversor.estaVazio(email)) {
            throw new EmailNuloException();
        }
        int posicaoArroba = email.indexOf('@');
        if (posicaoArroba <= 0 || posicaoArroba == email.length() - 1) {
            throw new EmailInvalidoException();
        }
        return email;
    }

    private static String validarSenha(String senha) throws SenhaNulaException {
        if (Conversor.estaVazio(senha)) {
            throw new SenhaNulaException();
        }
        return senha;
    }

    public void setNome(String nome) throws NomeNuloException {
        this.nome = validarNome(nome);
    }

    public void setEmail(String email) throws EmailNuloException, EmailInvalidoException {
        this.email = validarEmail(email);
    }

    public void setSenha(String senha) throws SenhaNulaException {
        this.senha = validarSenha(senha);
    }

    /** Email e unico no sistema, sem diferenciar maiusculas de minusculas. */
    public boolean temEmail(String outroEmail) {
        return email.equalsIgnoreCase(outroEmail);
    }

    // ---------------------------------------------------------------- plano

    /**
     * Contrata um plano. Se a mensalidade nao for informada, usa a mensalidade padrao do plano.
     * E final porque tambem e chamado pelo construtor.
     */
    public final void contratarPlano(Plano novoPlano, BigDecimal novaMensalidade)
            throws PlanoSemMensalidadeException, MensalidadeNegativaException {
        if (!novoPlano.possuiMensalidade()) {
            if (novaMensalidade != null) {
                throw new PlanoSemMensalidadeException();
            }
        } else if (novaMensalidade == null) {
            novaMensalidade = novoPlano.getMensalidadePadrao();
        } else if (novaMensalidade.signum() < 0) {
            throw new MensalidadeNegativaException();
        }
        this.plano = novoPlano;
        this.mensalidade = novaMensalidade;
    }

    /** Escolher o plano atual de novo mantem a mensalidade ja contratada. */
    public void alterarPlano(Plano novoPlano) throws BikeshareException {
        if (!novoPlano.getNome().equals(plano.getNome())) {
            contratarPlano(novoPlano, null);
        }
    }

    public Plano getPlano() {
        return plano;
    }

    public BigDecimal getMensalidade() throws PlanoSemMensalidadeException {
        if (!plano.possuiMensalidade()) {
            throw new PlanoSemMensalidadeException();
        }
        return mensalidade;
    }

    // ---------------------------------------------------------------- metodo de pagamento

    public void usarCartao(String numero, String validade) throws BikeshareException {
        metodoPagamento = new Cartao(numero, validade);
    }

    public void usarPix(String chave) throws ChavePixNulaException {
        metodoPagamento = new Pix(chave);
    }

    public void usarSaldoPreCarregado() {
        if (saldoPreCarregado == null) {
            saldoPreCarregado = new SaldoPreCarregado();
        }
        metodoPagamento = saldoPreCarregado;
    }

    public boolean usaSaldoPreCarregado() {
        return saldoPreCarregado != null && metodoPagamento == saldoPreCarregado;
    }

    public void recarregarSaldo(BigDecimal valor) throws UsuarioNaoUsaSaldoException, ValorPositivoException {
        if (!usaSaldoPreCarregado()) {
            throw new UsuarioNaoUsaSaldoException();
        }
        saldoPreCarregado.recarregar(valor);
    }

    public void autorizarPagamento() throws BikeshareException {
        metodoPagamento.autorizarRetirada();
    }

    public void pagar(BigDecimal valor) throws BikeshareException {
        metodoPagamento.cobrar(valor);
    }

    // ---------------------------------------------------------------- aluguel e penalidades

    /** Regras do usuario para iniciar um aluguel (a inadimplencia tem precedencia). */
    public void validarNovaRetirada() throws UsuarioInadimplenteException, UsuarioJaPossuiAluguelException {
        if (getEstado() == EstadoUsuario.INADIMPLENTE) {
            throw new UsuarioInadimplenteException();
        }
        if (possuiAluguelAtivo()) {
            throw new UsuarioJaPossuiAluguelException();
        }
    }

    public boolean possuiAluguelAtivo() {
        return aluguelAtivo != null;
    }

    public void iniciarAluguel(Aluguel aluguel) {
        this.aluguelAtivo = aluguel;
    }

    public void encerrarAluguel() {
        this.aluguelAtivo = null;
    }

    public void adicionarPenalidade(Penalidade penalidade) {
        penalidades.add(penalidade);
    }

    public BigDecimal getTotalPendencias() {
        BigDecimal total = BigDecimal.ZERO;
        for (Penalidade penalidade : penalidades) {
            total = total.add(penalidade.getValor());
        }
        return total;
    }

    public EstadoUsuario getEstado() {
        return getTotalPendencias().signum() > 0 ? EstadoUsuario.INADIMPLENTE : EstadoUsuario.ATIVO;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
