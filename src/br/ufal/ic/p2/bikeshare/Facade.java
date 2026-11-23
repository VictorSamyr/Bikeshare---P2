package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.models.EstadoSistema;
import br.ufal.ic.p2.bikeshare.persistence.PersistenciaService;
import br.ufal.ic.p2.bikeshare.services.AluguelService;
import br.ufal.ic.p2.bikeshare.services.EstacaoService;
import br.ufal.ic.p2.bikeshare.services.UsuarioService;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unico ponto de entrada do sistema, usado pelos testes de aceitacao.
 * Apenas encaminha as chamadas para os servicos.
 */
public class Facade {
    private static final String AINDA_NAO_IMPLEMENTADO = "Ainda não implementado.";
    private static final Path ARQUIVO_DADOS = Paths.get("bikeshare_data.ser");

    private final PersistenciaService persistenciaService = new PersistenciaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final EstacaoService estacaoService = new EstacaoService();
    private final AluguelService aluguelService = new AluguelService(usuarioService, estacaoService);
    private EstadoSistema estado;

    public Facade() throws BikeshareException {
        estado = persistenciaService.carregar(ARQUIVO_DADOS);
    }

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj)
            throws BikeshareException {
        return usuarioService.criarPessoaFisica(estado, nome, email, senha, tipo, cpfOuCnpj);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj, String razaoSocial)
            throws BikeshareException {
        return usuarioService.criarPessoaJuridica(estado, nome, email, senha, tipo, cnpj, razaoSocial);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                                String plano, String mensalidade) throws BikeshareException {
        return usuarioService.criarPessoaFisica(estado, nome, email, senha, tipo, cpf, plano, mensalidade);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                                String razaoSocial, String plano, String mensalidade) throws BikeshareException {
        return usuarioService.criarPessoaJuridica(estado, nome, email, senha, tipo, cnpj, razaoSocial,
                plano, mensalidade);
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuario, String atributo) throws BikeshareException {
        return usuarioService.getAtributoUsuario(estado, usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) throws BikeshareException {
        return usuarioService.getUsuarioPorEmail(estado, email);
    }

    // =====================================================================
    // US3 - Alteracao de usuario (inclui metodo de pagamento)
    // =====================================================================

    public void alterarUsuario(String usuario, String atributo, String valor) throws BikeshareException {
        usuarioService.alterarUsuario(estado, usuario, atributo, valor);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave)
            throws BikeshareException {
        usuarioService.alterarUsuario(estado, usuario, atributo, valor1, chave);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade)
            throws BikeshareException {
        usuarioService.alterarUsuario(estado, usuario, atributo, valor1, numero, validade);
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) throws BikeshareException {
        usuarioService.removerUsuario(estado, usuario);
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) throws BikeshareException {
        return estacaoService.criarEstacao(estado, nome, endereco, capacidade);
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) throws BikeshareException {
        estacaoService.adicionarBicicleta(estado, estacao, bicicleta, tipo, null);
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria)
            throws BikeshareException {
        estacaoService.adicionarBicicleta(estado, estacao, bicicleta, tipo, nivelBateria);
    }

    public void removerBicicleta(String estacao, String bicicleta) throws BikeshareException {
        estacaoService.removerBicicleta(estado, estacao, bicicleta);
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) throws BikeshareException {
        return estacaoService.getAtributoBicicleta(estado, bicicleta, atributo);
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacao) throws BikeshareException {
        return estacaoService.getBicicletasDisponiveis(estado, estacao);
    }

    public String getVagasDisponiveis(String estacao) throws BikeshareException {
        return estacaoService.getVagasDisponiveis(estado, estacao);
    }

    public String getEstadoBicicleta(String bicicleta) throws BikeshareException {
        return estacaoService.getEstadoBicicleta(estado, bicicleta);
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data)
            throws BikeshareException {
        return aluguelService.retirarBicicleta(estado, usuario, estacao, bicicleta, data);
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) throws BikeshareException {
        aluguelService.devolverBicicleta(estado, aluguel, estacao, data);
    }

    public String getAtributoAluguel(String aluguel, String atributo) throws BikeshareException {
        return aluguelService.getAtributoAluguel(estado, aluguel, atributo);
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis (Milestone 2)
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos (Milestone 2)
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade (Milestone 2)
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao (Milestone 2)
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void concluiManutencao(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US15 - Relatorios (Milestone 2)
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US16 - Undo/redo (Milestone 2)
    // =====================================================================

    public void undo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void redo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        estado = new EstadoSistema();
    }

    public void encerrarSistema() throws BikeshareException {
        persistenciaService.salvar(ARQUIVO_DADOS, estado);
    }
}
