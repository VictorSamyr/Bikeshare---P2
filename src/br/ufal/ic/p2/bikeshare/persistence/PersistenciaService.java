package br.ufal.ic.p2.bikeshare.persistence;

import br.ufal.ic.p2.bikeshare.exceptions.PersistenciaException;
import br.ufal.ic.p2.bikeshare.models.EstadoSistema;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Grava e carrega o estado do sistema usando a serializacao nativa do Java (java.io.Serializable).
 */
public class PersistenciaService {

    /** Sem arquivo salvo, o sistema comeca vazio. Um arquivo que nao pode ser lido e um erro. */
    public EstadoSistema carregar(Path arquivo) throws PersistenciaException {
        if (!Files.exists(arquivo)) {
            return new EstadoSistema();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(arquivo))) {
            return (EstadoSistema) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Nao foi possivel carregar os dados de " + arquivo + ".", e);
        }
    }

    public void salvar(Path arquivo, EstadoSistema estado) throws PersistenciaException {
        try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
            saida.writeObject(estado);
        } catch (IOException e) {
            throw new PersistenciaException("Nao foi possivel salvar os dados em " + arquivo + ".", e);
        }
    }
}
