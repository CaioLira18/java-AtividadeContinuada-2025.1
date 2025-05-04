package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

public class Apolice implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numero;
    private BigDecimal valorMaximoSegurado;
    private BigDecimal valorPremio;
    private BigDecimal valorFranquia;
    private Veiculo veiculo;
    private LocalDate dataInicioVigencia;
//    private LocalDate dataFimVigencia; // Novo campo

//    public Apolice(String numero, Veiculo veiculo, BigDecimal valorPremio,
//                   BigDecimal valorFranquia, BigDecimal valorMaximoSegurado,
//                   LocalDate dataInicioVigencia) {
//        this(numero, valorMaximoSegurado, valorPremio, valorFranquia,
//                veiculo, dataInicioVigencia);
//    }

    public Apolice(String numero, Veiculo veiculo, BigDecimal valorPremio, BigDecimal valorFranquia, BigDecimal valorMaximoSegurado, LocalDate dataInicioVigencia) {
        this.numero = numero;
        this.valorMaximoSegurado = valorMaximoSegurado;
        this.valorPremio = valorPremio;
        this.valorFranquia = valorFranquia;
        this.veiculo = veiculo;
        this.dataInicioVigencia = dataInicioVigencia;
//        this.dataFimVigencia = dataFimVigencia;
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

//    public LocalDate getDataFimVigencia() {
//        return dataFimVigencia;
//    }
//
//    public void setDataFimVigencia(LocalDate dataFimVigencia) {
//        this.dataFimVigencia = dataFimVigencia;
//    }
}