package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

public class Apolice implements Registro {
    private static final long serialVersionUID = 1L;

    private String numero;
    private BigDecimal valorMaximoSegurado;
    private BigDecimal valorPremio;
    private BigDecimal valorFranquia;
    private Veiculo veiculo;
    private LocalDate dataInicioVigencia;

    public Apolice(String numero, Veiculo veiculo, BigDecimal valorPremio, BigDecimal valorFranquia, BigDecimal valorMaximoSegurado, LocalDate dataInicioVigencia) {
        this.numero = numero;
        this.valorMaximoSegurado = valorMaximoSegurado;
        this.valorPremio = valorPremio;
        this.valorFranquia = valorFranquia;
        this.veiculo = veiculo;
        this.dataInicioVigencia = dataInicioVigencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getValorMaximoSegurado() {
        return valorMaximoSegurado;
    }

    public void setValorMaximoSegurado(BigDecimal valorMaximoSegurado) {
        this.valorMaximoSegurado = valorMaximoSegurado;
    }

    public BigDecimal getValorPremio() {
        return valorPremio;
    }

    public void setValorPremio(BigDecimal valorPremio) {
        this.valorPremio = valorPremio;
    }

    public BigDecimal getValorFranquia() {
        return valorFranquia;
    }

    public void setValorFranquia(BigDecimal valorFranquia) {
        this.valorFranquia = valorFranquia;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDate getDataInicioVigencia() {
        return dataInicioVigencia;
    }

    public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }

    @Override
    public String getIdUnico() {
        return getNumero();
    }
}