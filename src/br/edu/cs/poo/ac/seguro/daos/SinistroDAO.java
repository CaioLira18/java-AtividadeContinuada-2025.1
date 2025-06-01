package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

import java.util.ArrayList;
import java.util.List;

public class SinistroDAO extends DAOGenerico<Sinistro> {

    @Override
    public Class<Sinistro> getClasseEntidade() {
        return Sinistro.class;
    }

    public Sinistro[] buscarTodosSinistros() {
        Registro[] registros = super.buscarTodos();
        Sinistro[] sinistros = new Sinistro[registros.length];
        for (int i = 0; i < registros.length; i++) {
            sinistros[i] = (Sinistro) registros[i];
        }
        return sinistros;
    }

    public Sinistro[] buscarTodos() {
        return buscarTodosSinistros();
    }

    public boolean incluirSinistro(Sinistro sinistro) {
        if (super.buscar(sinistro.getNumero()) != null) {
            return false;
        } else {
            super.incluir(sinistro);
            return true;
        }
    }

    public boolean alterarSinistro(Sinistro sinistro) {
        if (super.buscar(sinistro.getNumero()) == null) {
            return false;
        } else {
            super.alterar(sinistro);
            return true;
        }
    }

    public boolean excluirSinistro(String numero) {
        if (super.buscar(numero) == null) {
            return false;
        } else {
            super.excluir(numero);
            return true;
        }
    }

    public List<Sinistro> buscarPorNumeroApolice(String numeroApolice) {
        Sinistro[] todos = buscarTodosSinistros();
        List<Sinistro> filtrados = new ArrayList<>();
        for (Sinistro s : todos) {
            if (s.getNumeroApolice().equalsIgnoreCase(numeroApolice)) {
                filtrados.add(s);
            }
        }
        return filtrados;
    }
}