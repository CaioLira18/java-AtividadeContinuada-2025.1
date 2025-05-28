package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDateTime;

public class DadosSinistro {
    private String placa;
    private LocalDateTime dataHoraSinistro;
    private String usuarioRegistro;
    private double valorSinistro;
    private int codigoTipoSinistro;

    public DadosSinistro(String placa, LocalDateTime dataHoraSinistro, String usuarioRegistro,
                         double valorSinistro, int codigoTipoSinistro) {
        this.placa = placa;
        this.dataHoraSinistro = dataHoraSinistro;
        this.usuarioRegistro = usuarioRegistro;
        this.valorSinistro = valorSinistro;
        this.codigoTipoSinistro = codigoTipoSinistro;
    }

    public String getPlaca() {
        return placa;
    }

    public LocalDateTime getDataHoraSinistro() {
        return dataHoraSinistro;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public double getValorSinistro() {
        return valorSinistro;
    }

    public int getCodigoTipoSinistro() {
        return codigoTipoSinistro;
    }
}
