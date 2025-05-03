package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApoliceDAO extends DAOGenerico{

    private final List<Apolice> apolices = new ArrayList<>();

    public ApoliceDAO() {
        cadastro = new CadastroObjetos(Apolice.class);
    }

    public void insert(Apolice apolice) {
        apolices.add(apolice);
    }

    public Optional<Apolice> findByPlaca(String placa) {
        return apolices.stream()
                .filter(a -> a.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }

    public Optional<Apolice> findByNumero(String numero) {
        return apolices.stream()
                .filter(a -> a.getVeiculo().getPlaca().equalsIgnoreCase(numero))
                .findFirst();
    }

    public void update(String placa, Apolice novaApolice) {
            findByPlaca(placa).ifPresent(original -> {
            original.setValorFranquia(novaApolice.getValorFranquia());
            original.setValorPremio(novaApolice.getValorPremio());
            original.setValorMaximoSegurado(novaApolice.getValorMaximoSegurado());
        });
    }

    public void remove(String placa) {
        apolices.removeIf(a -> a.getVeiculo().getPlaca().equalsIgnoreCase(placa));
    }

}