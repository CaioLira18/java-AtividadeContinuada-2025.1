package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrecoAno implements Serializable {
	private static final long serialVersionUID = 1L;

	private int ano;
	private double valorMaximo;

	public PrecoAno() {
		// Construtor padrão exigido para serialização
	}

	public PrecoAno(int ano, double valorMaximo) {
		this.ano = ano;
		this.valorMaximo = valorMaximo;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public double getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
}