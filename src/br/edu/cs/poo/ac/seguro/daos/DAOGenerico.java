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
			cadastro.incluir(entidade.getIdUnico(), String.valueOf(entidade));
			return true;
		}
		return false;
	}

	public boolean alterar(T entidade) {
		if (entidade != null && entidade.getIdUnico() != null) {
			if (cadastro.buscar(entidade.getIdUnico()) == null) {
				return false;
			}
			cadastro.incluir(entidade.getIdUnico(), String.valueOf(entidade));
			return true;
		}
		return false;
	}

	public T buscar(String id) {
		return (T) cadastro.buscar(id);
	}

	public Registro[] buscarTodos() {
		Serializable[] valores = cadastro.buscarTodos();
		return (Registro[]) valores;
	}

	public boolean remover(String id) {
		if (cadastro.buscar(id) != null) {
			cadastro.excluir(id);
			return true;
		}
		return false;
	}
}
