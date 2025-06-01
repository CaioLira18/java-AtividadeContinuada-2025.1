package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApoliceDAO extends DAOGenerico {
    private final List<Apolice> apolices = new ArrayList<>();
    private final CadastroObjetos cadastro;

    public ApoliceDAO() {
        this.cadastro = new CadastroObjetos(Apolice.class);
    }

    public Optional<Apolice> findByNumero(String numero) {
        Optional<Apolice> apoliceLocal = apolices.stream()
                .filter(a -> a.getNumero().equalsIgnoreCase(numero))
                .findFirst();

        if (apoliceLocal.isPresent()) {
            return apoliceLocal;
        }

        Apolice apoliceCadastro = (Apolice) cadastro.buscar(numero);
        if (apoliceCadastro != null) {
            apolices.add(apoliceCadastro); // Adiciona na lista interna para cache
            return Optional.of(apoliceCadastro);
        }

        return Optional.empty();
    }

    public List<Apolice> buscarTodas() {
        Registro[] registros = super.buscarTodos();
        List<Apolice> lista = new ArrayList<>();
        for (Registro r : registros) {
            lista.add((Apolice) r);
        }
        return lista;
    }



    public void insert(Apolice apolice) {
        apolices.add(apolice);
        cadastro.incluir(apolice, apolice.getNumero());
    }

    public Optional<Apolice> findByPlaca(String placa) {
        return Optional.empty();
    }

    public void update(String placa, Apolice novaApolice) {
    }

    public void remove(String placa) {
    }

    @Override
    public Class<Apolice> getClasseEntidade() {
        return Apolice.class;
    }




}