package br.ufal.ic.p2.bikeshare.models.bicicleta;

public enum EstadoBicicleta {
    DISPONIVEL("disponivel"),
    EM_USO("emUso"),
    EM_MANUTENCAO("emManutencao");

    private final String descricao;

    EstadoBicicleta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
