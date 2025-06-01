package br.edu.cs.poo.ac.seguro.telas;

public class EntidadeMediator {
	public String incluir(Entidade ent) {
		String msg = validar(ent);
		if (msg == null) {
			// Chamar incluir no DAO
		}
		return msg;
	}
	public String alterar(Entidade ent) {
		String msg = validar(ent);
		if (msg == null) {
			// Chamar alterar no DAO
		}
		return msg;		
	}
	public String excluir(String id) {
		// Chamar excluir no DAO
		return null;		
	}
	private String validar(Entidade ent) {
		if (ent.getNome() == null || ent.getNome().trim().equals("")) {
			return "Nome deve ser preenchido";
		} else {
			return null;
		}
	}
	public Entidade buscar(String codigo) {
		// Mock, implementar busca valendo no DAO
		if (codigo == null || !codigo.equals("001")) {
			return null;
		} else {
			return new Entidade("001", "Carlos", 1000.00);
		}
	}
}
