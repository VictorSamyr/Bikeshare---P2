package br.ufal.ic.p2.bikeshare.models;

import br.ufal.ic.p2.bikeshare.exceptions.AluguelNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BicicletaNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.EstacaoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.PlanoInvalidoException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.models.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Estacao;
import br.ufal.ic.p2.bikeshare.models.usuario.Plano;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Guarda todos os dados do sistema que precisam ser salvos entre execucoes.
 * As colecoes internas nao sao expostas: o acesso e feito pelos metodos abaixo.
 */
public class EstadoSistema implements Serializable {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Estacao> estacoes = new LinkedHashMap<>();
    private final Map<String, Bicicleta> bicicletas = new LinkedHashMap<>();
    private final Map<String, Aluguel> alugueis = new LinkedHashMap<>();
    private final Map<String, Plano> planos = new LinkedHashMap<>();
    // Os contadores so avancam quando um cadastro e concluido; ids nunca sao reaproveitados.
    private int proximoUsuario = 1;
    private int proximaEstacao = 1;
    private int proximoAluguel = 1;

    public EstadoSistema() {
        Plano avulso = Plano.criarAvulso();
        Plano assinante = Plano.criarAssinante();
        planos.put(avulso.getNome(), avulso);
        planos.put(assinante.getNome(), assinante);
    }

    // ---------------------------------------------------------------- usuarios

    public String proximoIdUsuario() {
        return "U" + proximoUsuario;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        proximoUsuario++;
    }

    public Usuario buscarUsuario(String id) throws UsuarioNaoExisteException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNaoExisteException();
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorEmail(String email) throws UsuarioNaoExisteException {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.temEmail(email)) {
                return usuario;
            }
        }
        throw new UsuarioNaoExisteException();
    }

    /** Indica se outro usuario (diferente de quemAltera) ja usa este email. */
    public boolean emailEmUso(String email, Usuario quemAltera) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario != quemAltera && usuario.temEmail(email)) {
                return true;
            }
        }
        return false;
    }

    public void removerUsuario(String id) {
        usuarios.remove(id);
    }

    // ---------------------------------------------------------------- estacoes e bicicletas

    public String proximoIdEstacao() {
        return "E" + proximaEstacao;
    }

    public void adicionarEstacao(Estacao estacao) {
        estacoes.put(estacao.getId(), estacao);
        proximaEstacao++;
    }

    public Estacao buscarEstacao(String id) throws EstacaoNaoExisteException {
        Estacao estacao = estacoes.get(id);
        if (estacao == null) {
            throw new EstacaoNaoExisteException();
        }
        return estacao;
    }

    public boolean existeBicicleta(String id) {
        return bicicletas.containsKey(id);
    }

    public void adicionarBicicleta(Bicicleta bicicleta) {
        bicicletas.put(bicicleta.getId(), bicicleta);
    }

    public Bicicleta buscarBicicleta(String id) throws BicicletaNaoExisteException {
        Bicicleta bicicleta = bicicletas.get(id);
        if (bicicleta == null) {
            throw new BicicletaNaoExisteException();
        }
        return bicicleta;
    }

    public void removerBicicleta(String id) {
        bicicletas.remove(id);
    }

    // ---------------------------------------------------------------- alugueis e planos

    public String proximoIdAluguel() {
        return "A" + proximoAluguel;
    }

    public void adicionarAluguel(Aluguel aluguel) {
        alugueis.put(aluguel.getId(), aluguel);
        proximoAluguel++;
    }

    public Aluguel buscarAluguel(String id) throws AluguelNaoExisteException {
        Aluguel aluguel = alugueis.get(id);
        if (aluguel == null) {
            throw new AluguelNaoExisteException();
        }
        return aluguel;
    }

    public void adicionarPlano(Plano plano) {
        planos.put(plano.getNome(), plano);
    }

    public Plano buscarPlano(String nome) throws PlanoInvalidoException {
        Plano plano = planos.get(nome);
        if (plano == null) {
            throw new PlanoInvalidoException();
        }
        return plano;
    }
}
