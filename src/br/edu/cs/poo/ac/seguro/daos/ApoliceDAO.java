package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApoliceDAO {
    private final List<Apolice> apolices = new ArrayList<>();
    private final CadastroObjetos cadastro;

    public ApoliceDAO() {
        this.cadastro = new CadastroObjetos(Apolice.class);
    }

    public Optional<Apolice> findByNumero(String numero) {
        // Primeiro verifica na lista interna
        Optional<Apolice> apoliceLocal = apolices.stream()
                .filter(a -> a.getNumero().equalsIgnoreCase(numero))
                .findFirst();

        if (apoliceLocal.isPresent()) {
            return apoliceLocal;
        }

        // Se não encontrar, verifica no CadastroObjetos
        Apolice apoliceCadastro = (Apolice) cadastro.buscar(numero);
        if (apoliceCadastro != null) {
            apolices.add(apoliceCadastro); // Adiciona na lista interna para cache
            return Optional.of(apoliceCadastro);
        }

        return Optional.empty();
    }

    public void insert(Apolice apolice) {
        apolices.add(apolice);
        cadastro.incluir(apolice, apolice.getNumero());
    }

    // Remove a lista interna e mantém apenas o cadastro
    public Optional<Apolice> findByPlaca(String placa) {
        // Implementação alternativa se necessário
        return Optional.empty();
    }

    public void update(String placa, Apolice novaApolice) {
        // Implementação atualizada se necessário
    }

    public void remove(String placa) {
        // Implementação atualizada se necessário
    }
}