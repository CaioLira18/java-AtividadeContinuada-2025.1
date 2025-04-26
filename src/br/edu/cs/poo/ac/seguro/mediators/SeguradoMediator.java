package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;

public class SeguradoMediator {
	private static SeguradoMediator instancia;

    public static SeguradoMediator getInstancia() {
		if (instancia == null) {
			instancia = new SeguradoMediator();
		}
		return instancia;
    }

    public String validarNome(String nome) {
		if (nome == null || nome.isEmpty()) {
			return "Nome não pode ser vazio";
		}
		return nome;
	}

	public String validarEndereco(Endereco endereco) {
		if (endereco == null ) {
			return "Endereco não pode ser vazio";
		}
		return endereco.getLogradouro();
	}

	public String validarDataCriacao(LocalDate dataCriacao) {
		if (dataCriacao == null) {
			return "Data de Criação não pode estar vazia";
		}
        return dataCriacao.toString();
    }

	public BigDecimal ajustarDebitoBonus(BigDecimal bonus, BigDecimal valorDebito) {
		if (bonus == null || valorDebito == null) {
			return BigDecimal.ZERO;
		}

		if (bonus.compareTo(valorDebito) < 0) {
			return bonus;

		} else {
			return valorDebito;
		}
	}
}