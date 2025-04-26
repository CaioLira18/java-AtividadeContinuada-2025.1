package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import java.util.HashMap;
import java.util.Map;

public class SeguradoEmpresaDAO {
    private static Map<String, SeguradoEmpresa> banco = new HashMap<>();

    private static SeguradoEmpresaDAO instancia;

    public SeguradoEmpresaDAO() {}

    public static SeguradoEmpresaDAO getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoEmpresaDAO();
        }
        return instancia;
    }

    public boolean alterar(SeguradoEmpresa seg) {
        if (seg == null || seg.getCnpj() == null || !banco.containsKey(seg.getCnpj())) {
            return false;
        }
        banco.put(seg.getCnpj(), seg);
        return true;
    }

    public SeguradoEmpresa buscar(String cnpj) {
        if (cnpj == null || !banco.containsKey(cnpj)) {
            return null;
        }
        return banco.get(cnpj);
    }

    public boolean incluir(SeguradoEmpresa seg) {
        if (seg == null || seg.getCnpj() == null || banco.containsKey(seg.getCnpj())) {
            return false;
        }
        banco.put(seg.getCnpj(), seg);
        return true;
    }

    public boolean excluir(String cnpj) {
        if (cnpj == null || !banco.containsKey(cnpj)) {
            return false;
        }
        banco.remove(cnpj);
        return true;
    }
}