package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro;

import java.io.Serializable;

public abstract class DAOGenerico<T extends Registro> {
	private CadastroObjetos cadastro;

	public DAOGenerico() {
		this.cadastro = new CadastroObjetos(getClasseEntidade());
	}

	public abstract Class<T> getClasseEntidade();

	public boolean incluir(T entidade) {
		if (entidade != null && entidade.getIdUnico() != null) {
			if (cadastro.buscar(entidade.getIdUnico()) != null) {
				return false;
			}
			// CORREÇÃO: passar objeto como primeiro parâmetro, chave como segundo
			cadastro.incluir(entidade, entidade.getIdUnico());
			return true;
		}
		return false;
	}

	public boolean alterar(T entidade) {
		if (entidade != null && entidade.getIdUnico() != null) {
			if (cadastro.buscar(entidade.getIdUnico()) == null) {
				return false;
			}
			// CORREÇÃO: passar objeto como primeiro parâmetro, chave como segundo
			cadastro.alterar(entidade, entidade.getIdUnico());
			return true;
		}
		return false;
	}

	public T buscar(String id) {
		return (T) cadastro.buscar(id);
	}

	public Registro[] buscarTodos() {
		Serializable[] valores = cadastro.buscarTodos();
		Registro[] registros = new Registro[valores.length];
		for (int i = 0; i < valores.length; i++) {
			registros[i] = (Registro) valores[i];
		}
		return registros;
	}

	public boolean excluir(String id) {
		if (cadastro.buscar(id) != null) {
			cadastro.excluir(id);
			return true;
		}
		return false;
	}
}