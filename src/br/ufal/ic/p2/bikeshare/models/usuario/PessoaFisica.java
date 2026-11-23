package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.AtributoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.CpfNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoEhPessoaJuridicaException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.math.BigDecimal;

public class PessoaFisica extends Usuario {
    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, String cpf,
                        Plano plano, BigDecimal mensalidade) throws BikeshareException {
        super(id, nome, email, senha, plano, mensalidade);
        if (Conversor.estaVazio(cpf)) {
            throw new CpfNuloException();
        }
        this.cpf = cpf;
    }

    @Override
    public String getTipo() {
        return "pessoaFisica";
    }

    @Override
    protected String getAtributoEspecifico(String atributo) throws BikeshareException {
        switch (atributo) {
            case "cpf":
                return cpf;
            case "cnpj":
            case "razaoSocial":
                throw new UsuarioNaoEhPessoaJuridicaException();
            default:
                throw new AtributoNaoExisteException();
        }
    }
}
