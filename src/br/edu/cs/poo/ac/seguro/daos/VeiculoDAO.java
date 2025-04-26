package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

import java.util.HashMap;
import java.util.Map;

public class VeiculoDAO {
    private static Map<String, Veiculo> banco = new HashMap<>();
    public Veiculo buscar(String placa) {
        if (placa == null || !banco.containsKey(placa)) {
            return null;
        }
        return banco.get(placa);
    }

    public boolean excluir(String placa) {
        if (placa == null || !banco.containsKey(placa)) {
            return false;
        }
        banco.remove(placa);
        return true;
    }

    public boolean incluir(Veiculo veiculo) {
        if (veiculo == null || veiculo.getPlaca() == null || banco.containsKey(veiculo.getPlaca())) {
            return false;
        }
        banco.put(veiculo.getPlaca(), veiculo);
        return true;
    }

    public boolean alterar(Veiculo veiculo) {
        if (veiculo == null || veiculo.getPlaca() == null || !banco.containsKey(veiculo.getPlaca())) {
            return false;
        }
        banco.put(veiculo.getPlaca(), veiculo);
        return true;
    }
}
