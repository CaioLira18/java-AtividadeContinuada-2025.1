package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class SeguradoPessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cpf;
	private String nome;
	private BigDecimal bonus;
	private String endereco;
	private LocalDate dataNascimento;
	private BigDecimal renda;

	public SeguradoPessoa(String nome, String endereco, LocalDate dataNascimento, BigDecimal renda, String cpf, double bonusInicial) {
		this.cpf = cpf;
		this.nome = nome;
		this.endereco = endereco;
		this.dataNascimento = dataNascimento;
		this.renda = renda;
		this.bonus = BigDecimal.valueOf(bonusInicial);
	}

	public SeguradoPessoa(String cpf, String nome) {
		this.cpf = cpf;
		this.nome = nome;
		this.bonus = BigDecimal.ZERO;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public BigDecimal getRenda() {
		return renda;
	}

	public void setRenda(BigDecimal renda) {
		this.renda = renda;
	}

	public int getIdade() {
		if (dataNascimento == null) {
			return 0;
		}
		return Period.between(dataNascimento, LocalDate.now()).getYears();
	}

	public void debitarBonus(BigDecimal valor) {
		if (this.bonus.compareTo(valor) >= 0) {
			this.bonus = this.bonus.subtract(valor);
		} else {
			this.bonus = BigDecimal.ZERO;
		}
	}
}