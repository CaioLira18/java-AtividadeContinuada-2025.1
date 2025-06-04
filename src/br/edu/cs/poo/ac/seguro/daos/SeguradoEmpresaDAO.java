package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends SeguradoDAO {

    @Override
    public Class getClasseEntidade() {
        return SeguradoEmpresa.class;
    }

    public SeguradoEmpresa buscar(String numero) {
        return (SeguradoEmpresa) super.buscar(numero);
    }

    public boolean incluir(SeguradoEmpresa segurado) {
        return super.incluir(segurado);
    }

    public boolean alterar(SeguradoEmpresa segurado) {
        return super.alterar(segurado);
    }

    public boolean excluir(String numero) {
        return super.excluir(numero);
    }
}
