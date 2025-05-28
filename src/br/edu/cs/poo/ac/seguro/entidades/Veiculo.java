package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

public class Veiculo implements Registro {
	private static final long serialVersionUID = 1L;

	private String placa;
	private int ano;
	private Segurado proprietario;
	private int categoria;

	public Veiculo(String placa, int ano, Segurado proprietario, CategoriaVeiculo categoria) {
		this.placa = placa;
		this.ano = ano;
		this.proprietario = proprietario;

		if (categoria == null) {
			categoria = CategoriaVeiculo.BASICO;
		}

		this.categoria = categoria.ordinal();
	}

	public Veiculo() {
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public Segurado getProprietario() {
		return proprietario;
	}

	public void setProprietario(Segurado proprietario) {
		this.proprietario = proprietario;
	}

	@Override
	public String getIdUnico() {
		return getPlaca();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Veiculo other = (Veiculo) obj;
		return placa != null ? placa.equals(other.placa) : other.placa == null;
	}

	@Override
	public int hashCode() {
		return placa != null ? placa.hashCode() : 0;
	}
}