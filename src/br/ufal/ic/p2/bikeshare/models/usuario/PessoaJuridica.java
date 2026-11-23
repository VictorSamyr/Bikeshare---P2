package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.AtributoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.CnpjNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.RazaoSocialNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.UsuarioNaoEhPessoaFisicaException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.math.BigDecimal;

public class PessoaJuridica extends Usuario {
    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj, String razaoSocial,
                          Plano plano, BigDecimal mensalidade) throws BikeshareException {
        super(id, nome, email, senha, plano, mensalidade);
        if (Conversor.estaVazio(cnpj)) {
            throw new CnpjNuloException();
        }
        if (Conversor.estaVazio(razaoSocial)) {
            throw new RazaoSocialNulaException();
        }
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String getTipo() {
        return "pessoaJuridica";
    }

    @Override
    protected String getAtributoEspecifico(String atributo) throws BikeshareException {
        switch (atributo) {
            case "cnpj":
                return cnpj;
            case "razaoSocial":
                return razaoSocial;
            case "cpf":
                throw new UsuarioNaoEhPessoaFisicaException();
            default:
                throw new AtributoNaoExisteException();
        }
    }
}
