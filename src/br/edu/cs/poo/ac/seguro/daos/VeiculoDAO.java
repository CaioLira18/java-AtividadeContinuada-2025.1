package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class VeiculoDAO extends DAOGenerico<Veiculo> {

    @Override
    public Class<Veiculo> getClasseEntidade() {
        return Veiculo.class;
    }

    public Veiculo buscar(String placa) {
        return super.buscar(placa);
    }

    public boolean incluir(Veiculo veiculo) {
        return super.incluir(veiculo);
    }

    public boolean alterar(Veiculo veiculo) {
        return super.alterar(veiculo);
    }

    public boolean excluir(String placa) {
        return super.remover(placa);
    }
}
