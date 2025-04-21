package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
	private static SeguradoPessoaMediator instancia;
	public static SeguradoPessoaMediator getInstancia() {
    	if(instancia == null) {
			instancia = new SeguradoPessoaMediator();
		}
		return instancia;
	}
	private SeguradoPessoaDAO dao;

	public String validarCpf(String cpf) {
		return null;
	}

	public String validarRenda(double renda) {
		if(renda < 0) {
			return "O renda deve ser maior que zero";
		}

		if(renda > 1000000000) {
			return "Faturamento excede o limite permitido";
		}
		return null;
	}

	public String incluirSeguradoPessoa(SeguradoPessoa seg) {
		String validacao = validarSeguradoPessoa(seg);
		if (validacao != null) {
			return validacao;
		}

		SeguradoPessoa existente = dao.buscar(seg.getCpf());
		if (existente != null) {
			return "Já existe um segurado com este CPF";
		}

		boolean resultado = dao.incluir(seg);
		if (!resultado) {
			return "Erro ao incluir segurado";
		}

		return null;
	}

	public String alterarSeguradoPessoa(SeguradoPessoa seg) {
		String validacao = validarSeguradoPessoa(seg);
		if (validacao != null) {
			return validacao;
		}

		SeguradoPessoa existente = dao.buscar(seg.getCpf());
		if (existente == null) {
			return "Segurado não encontrado";
		}

		boolean resultado = dao.alterar(seg);
		if (!resultado) {
			return "Erro ao alterar segurado";
		}

		return null; // Alterado com sucesso
	}

	public String excluirSeguradoPessoa(String cpf) {
		if (cpf == null || cpf.trim().isEmpty()) {
			return "CNPJ não pode ser vazio";
		}

		SeguradoPessoa existente = dao.buscar(cpf);
		if (existente == null) {
			return "Segurado não encontrado";
		}

		boolean resultado = dao.excluir(cpf);
		if (!resultado) {
			return "Erro ao excluir segurado";
		}

		return null; // Excluído com sucesso
	}

	public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
		if (cpf == null || cpf.trim().isEmpty()) {
			return null;
		}

		return dao.buscar(cpf);
	}

	public String validarSeguradoPessoa(SeguradoPessoa seg) {
		if (seg == null) {
			return "Segurado não pode ser nulo";
		}

		if (seg.getNome() == null || seg.getNome().trim().isEmpty()) {
			return "Nome da Pessoa não pode ser vazio";
		}

		if (seg.getEndereco() == null) {
			return "Endereço não pode ser nulo";
		}

		String validacaoCPF = validarCpf(seg.getCpf());
		if (validacaoCPF != null) {
			return validacaoCPF;
		}

		String validacaoRenda = validarRenda(seg.getRenda());
		if (validacaoRenda != null) {
			return validacaoRenda;
		}
		return null;
	}
}