package br.ufal.ic.p2.bikeshare.services;

import br.ufal.ic.p2.bikeshare.exceptions.*;
import br.ufal.ic.p2.bikeshare.models.EstadoSistema;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaFisica;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.models.usuario.Plano;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.math.BigDecimal;

/**
 * Cadastro, consulta, alteracao e remocao de usuarios (US1 a US4).
 */
public class UsuarioService {
    private static final String PLANO_PADRAO = "avulso";

    public Usuario buscarUsuario(EstadoSistema estado, String id) throws BikeshareException {
        if (Conversor.estaVazio(id)) {
            throw new IdentificacaoUsuarioNulaException();
        }
        return estado.buscarUsuario(id);
    }

    // ---------------------------------------------------------------- cadastro

    public String criarPessoaFisica(EstadoSistema estado, String nome, String email, String senha, String tipo,
                                    String cpf) throws BikeshareException {
        return criarPessoaFisica(estado, nome, email, senha, tipo, cpf, PLANO_PADRAO, "");
    }

    public String criarPessoaFisica(EstadoSistema estado, String nome, String email, String senha, String tipo,
                                    String cpf, String nomePlano, String mensalidade) throws BikeshareException {
        validarTipo(tipo, "pessoaFisica");
        Plano plano = estado.buscarPlano(nomePlano);
        BigDecimal valorMensalidade = converterMensalidade(plano, mensalidade);
        Usuario usuario = new PessoaFisica(estado.proximoIdUsuario(), nome, email, senha, cpf,
                plano, valorMensalidade);
        return cadastrar(estado, usuario);
    }

    public String criarPessoaJuridica(EstadoSistema estado, String nome, String email, String senha, String tipo,
                                      String cnpj, String razaoSocial) throws BikeshareException {
        return criarPessoaJuridica(estado, nome, email, senha, tipo, cnpj, razaoSocial, PLANO_PADRAO, "");
    }

    public String criarPessoaJuridica(EstadoSistema estado, String nome, String email, String senha, String tipo,
                                      String cnpj, String razaoSocial, String nomePlano, String mensalidade)
            throws BikeshareException {
        validarTipo(tipo, "pessoaJuridica");
        Plano plano = estado.buscarPlano(nomePlano);
        BigDecimal valorMensalidade = converterMensalidade(plano, mensalidade);
        Usuario usuario = new PessoaJuridica(estado.proximoIdUsuario(), nome, email, senha, cnpj, razaoSocial,
                plano, valorMensalidade);
        return cadastrar(estado, usuario);
    }

    /** Cada sobrecarga da Facade atende um tipo de usuario; outro tipo e invalido. */
    private void validarTipo(String tipo, String tipoEsperado) throws TipoInvalidoException {
        if (!tipoEsperado.equals(tipo)) {
            throw new TipoInvalidoException();
        }
    }

    /** Mensalidade vazia em plano sem mensalidade e aceita; nos demais casos o texto e convertido. */
    private BigDecimal converterMensalidade(Plano plano, String mensalidade) throws BikeshareException {
        if (!plano.possuiMensalidade() && Conversor.estaVazio(mensalidade)) {
            return null;
        }
        return Conversor.paraDecimal(mensalidade, new MensalidadeNulaException(), new MensalidadeNumericaException());
    }

    private String cadastrar(EstadoSistema estado, Usuario usuario) throws EmailJaCadastradoException {
        if (estado.emailEmUso(usuario.getEmail(), usuario)) {
            throw new EmailJaCadastradoException();
        }
        estado.adicionarUsuario(usuario);
        return usuario.getId();
    }

    // ---------------------------------------------------------------- consulta

    public String getAtributoUsuario(EstadoSistema estado, String id, String atributo) throws BikeshareException {
        return buscarUsuario(estado, id).getAtributo(atributo);
    }

    public String getUsuarioPorEmail(EstadoSistema estado, String email) throws BikeshareException {
        if (Conversor.estaVazio(email)) {
            throw new EmailNuloException();
        }
        return estado.buscarUsuarioPorEmail(email).getId();
    }

    // ---------------------------------------------------------------- alteracao

    public void alterarUsuario(EstadoSistema estado, String id, String atributo, String valor)
            throws BikeshareException {
        Usuario usuario = buscarUsuario(estado, id);
        switch (atributo) {
            case "nome":
                usuario.setNome(valor);
                break;
            case "email":
                alterarEmail(estado, usuario, valor);
                break;
            case "senha":
                usuario.setSenha(valor);
                break;
            case "plano":
                usuario.alterarPlano(estado.buscarPlano(valor));
                break;
            default:
                alterarDadosDePagamento(usuario, atributo, valor);
        }
    }

    /** Alteracoes simples (com um unico valor) ligadas ao pagamento. */
    private void alterarDadosDePagamento(Usuario usuario, String atributo, String valor) throws BikeshareException {
        if ("metodoPagamento".equals(atributo)) {
            alterarParaSaldoPreCarregado(usuario, valor);
        } else if ("saldo".equals(atributo)) {
            recarregarSaldo(usuario, valor);
        } else {
            throw new AtributoNaoPodeSerAlteradoException();
        }
    }

    /** Pagamento por pix: alterarUsuario(usuario, "metodoPagamento", "pix", chave). */
    public void alterarUsuario(EstadoSistema estado, String id, String atributo, String metodo, String chave)
            throws BikeshareException {
        Usuario usuario = buscarUsuarioParaTrocaDeMetodo(estado, id, atributo);
        validarMetodo(metodo, "pix");
        usuario.usarPix(chave);
    }

    /** Pagamento por cartao: alterarUsuario(usuario, "metodoPagamento", "cartao", numero, validade). */
    public void alterarUsuario(EstadoSistema estado, String id, String atributo, String metodo, String numero,
                               String validade) throws BikeshareException {
        Usuario usuario = buscarUsuarioParaTrocaDeMetodo(estado, id, atributo);
        validarMetodo(metodo, "cartao");
        usuario.usarCartao(numero, validade);
    }

    private Usuario buscarUsuarioParaTrocaDeMetodo(EstadoSistema estado, String id, String atributo)
            throws BikeshareException {
        Usuario usuario = buscarUsuario(estado, id);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoNaoPodeSerAlteradoException();
        }
        return usuario;
    }

    private void validarMetodo(String metodo, String metodoEsperado) throws MetodoPagamentoInvalidoException {
        if (!metodoEsperado.equals(metodo)) {
            throw new MetodoPagamentoInvalidoException();
        }
    }

    private void alterarEmail(EstadoSistema estado, Usuario usuario, String email) throws BikeshareException {
        if (estado.emailEmUso(email, usuario)) {
            throw new EmailJaCadastradoException();
        }
        usuario.setEmail(email);
    }

    /** Sem dados adicionais, o unico metodo de pagamento possivel e o saldo pre-carregado. */
    private void alterarParaSaldoPreCarregado(Usuario usuario, String metodo) throws MetodoPagamentoInvalidoException {
        validarMetodo(metodo, "saldoPreCarregado");
        usuario.usarSaldoPreCarregado();
    }

    private void recarregarSaldo(Usuario usuario, String valor) throws BikeshareException {
        if (!usuario.usaSaldoPreCarregado()) {
            throw new UsuarioNaoUsaSaldoException();
        }
        usuario.recarregarSaldo(Conversor.paraDecimal(valor, new ValorNuloException(), new ValorNumericoException()));
    }

    // ---------------------------------------------------------------- remocao

    public void removerUsuario(EstadoSistema estado, String id) throws BikeshareException {
        Usuario usuario = buscarUsuario(estado, id);
        if (usuario.possuiAluguelAtivo()) {
            throw new UsuarioComAluguelException();
        }
        estado.removerUsuario(id);
    }
}
