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
		if (cnpj == null || cnpj.isEmpty()) {
			return "CNPJ não pode ser vazio";
		}

		cnpj = cnpj.replaceAll("[^0-9]", "");

		if (cnpj.length() != 14) {
			return "CNPJ deve conter 14 dígitos";
		}

		// Validação de dígitos repetidos
		boolean digitosIguais = true;
		for (int i = 1; i < cnpj.length(); i++) {
			if (cnpj.charAt(i) != cnpj.charAt(0)) {
				digitosIguais = false;
				break;
			}
		}
		if (digitosIguais) {
			return "CNPJ inválido: todos os dígitos são iguais";
		}

		// Validação do primeiro dígito verificador
		int soma = 0;
		int peso = 5;
		for (int i = 0; i < 12; i++) {
			int num = cnpj.charAt(i) - '0';
			soma += num * peso;
			peso = (peso == 2) ? 9 : peso - 1;
		}

		int resto = soma % 11;
		int dv1 = (resto < 2) ? 0 : 11 - resto;
		if (dv1 != (cnpj.charAt(12) - '0')) {
			return "CNPJ inválido: primeiro dígito verificador incorreto";
		}

		// Validação do segundo dígito verificador
		soma = 0;
		peso = 6;
		for (int i = 0; i < 13; i++) {
			int num = cnpj.charAt(i) - '0';
			soma += num * peso;
			peso = (peso == 2) ? 9 : peso - 1;
		}

		resto = soma % 11;
		int dv2 = (resto < 2) ? 0 : 11 - resto;
		if (dv2 != (cnpj.charAt(13) - '0')) {
			return "CNPJ inválido: segundo dígito verificador incorreto";
		}

		return null; // CNPJ válido
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