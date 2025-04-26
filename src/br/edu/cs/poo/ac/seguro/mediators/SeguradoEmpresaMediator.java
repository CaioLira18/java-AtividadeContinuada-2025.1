package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
	private static SeguradoEmpresaMediator instancia;
	private SeguradoEmpresaDAO dao;


	private SeguradoEmpresaMediator() {
		this.dao = SeguradoEmpresaDAO.getInstancia();
	}

	public static SeguradoEmpresaMediator getInstancia() {
		if (instancia == null) {
			instancia = new SeguradoEmpresaMediator();
		}
		return instancia;
	}

	public String validarCnpj(String cnpj) {
		return ValidadorCpfCnpj.ehCnpjValido(cnpj);
	}

	public String validarFaturamento(double faturamento) {
		if (faturamento <= 0) {
			return "O faturamento deve ser maior que zero";
		}

		if (faturamento > 1000000000) {
			return "Faturamento excede o limite permitido";
		}

		return null; // Faturamento válido
	}

	public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
		if (seg == null) {
			return "Segurado não pode ser nulo";
		}

		if (seg.getNome() == null || seg.getNome().trim().isEmpty()) {
			return "Nome da empresa não pode ser vazio";
		}

		if (seg.getEndereco() == null) {
			return "Endereço não pode ser nulo";
		}

		if (seg.getDataAbertura() == null) {
			return "Data de abertura não pode ser nula";
		}

		String validacaoCnpj = validarCnpj(seg.getCnpj());
		if (validacaoCnpj != null) {
			return validacaoCnpj;
		}

		String validacaoFaturamento = validarFaturamento(seg.getFaturamento());
		if (validacaoFaturamento != null) {
			return validacaoFaturamento;
		}

		return null; // Segurado válido
	}

	public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
		String validacao = validarSeguradoEmpresa(seg);
		if (validacao != null) {
			return validacao;
		}

		SeguradoEmpresa existente = dao.buscar(seg.getCnpj());
		if (existente != null) {
			return "Já existe um segurado com este CNPJ";
		}

		boolean resultado = dao.incluir(seg);
		if (!resultado) {
			return "Erro ao incluir segurado";
		}

		return null; // Incluído com sucesso
	}

	public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
		String validacao = validarSeguradoEmpresa(seg);
		if (validacao != null) {
			return validacao;
		}

		SeguradoEmpresa existente = dao.buscar(seg.getCnpj());
		if (existente == null) {
			return "Segurado não encontrado";
		}

		boolean resultado = dao.alterar(seg);
		if (!resultado) {
			return "Erro ao alterar segurado";
		}

		return null; // Alterado com sucesso
	}

	public String excluirSeguradoEmpresa(String cnpj) {
		if (cnpj == null || cnpj.trim().isEmpty()) {
			return "CNPJ não pode ser vazio";
		}

		SeguradoEmpresa existente = dao.buscar(cnpj);
		if (existente == null) {
			return "Segurado não encontrado";
		}

		boolean resultado = dao.excluir(cnpj);
		if (!resultado) {
			return "Erro ao excluir segurado";
		}

		return null; // Excluído com sucesso
	}

	public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
		if (cnpj == null || cnpj.trim().isEmpty()) {
			return null;
		}

		return dao.buscar(cnpj);
	}
}