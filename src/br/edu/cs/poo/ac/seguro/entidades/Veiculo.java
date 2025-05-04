package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

public class Veiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String placa;
	private int ano;
	private SeguradoEmpresa proprietarioEmpresa;
	private SeguradoPessoa proprietarioPessoa;
	private int categoria;

	public Veiculo(String placa, int ano, SeguradoEmpresa proprietarioEmpresa, SeguradoPessoa proprietarioPessoa, CategoriaVeiculo categoria) {
		this.placa = placa;
		this.ano = ano;
		this.proprietarioPessoa = proprietarioPessoa;
		this.proprietarioEmpresa = proprietarioEmpresa;
		this.categoria = categoria.ordinal();
	}

	// Construtor padrão exigido para serialização
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

	public SeguradoPessoa getProprietarioPessoa() {
		return proprietarioPessoa;
	}

	public void setProprietarioPessoa(SeguradoPessoa proprietarioPessoa) {
		this.proprietarioPessoa = proprietarioPessoa;
	}

	public SeguradoEmpresa getProprietarioEmpresa() {
		return proprietarioEmpresa;
	}

	public void setProprietarioEmpresa(SeguradoEmpresa proprietarioEmpresa) {
		this.proprietarioEmpresa = proprietarioEmpresa;
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