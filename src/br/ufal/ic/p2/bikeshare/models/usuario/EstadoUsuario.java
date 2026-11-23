package br.ufal.ic.p2.bikeshare.models.usuario;

public enum EstadoUsuario {
    ATIVO("ativo"),
    INADIMPLENTE("inadimplente");

    private final String descricao;

    EstadoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
