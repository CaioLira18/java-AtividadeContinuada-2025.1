package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoDAOImpl extends SeguradoDAO {
    @Override
    public Class<Segurado> getClasseEntidade() {
        return Segurado.class;
    }

    public SeguradoPessoa buscarPorCpf(String cpf) {
        Registro[] registros = buscarTodos();
        for (Registro r : registros) {
            SeguradoPessoa s = (SeguradoPessoa) r;
            if (s.getCpf().equals(cpf)) {
                return s;
            }
        }
        return null;
    }


}
