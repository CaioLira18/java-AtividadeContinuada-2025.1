package br.edu.cs.poo.ac.seguro.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Sinistro implements Registro {
    private static final long serialVersionUID = 1L;

    private String numero;
    private Veiculo veiculo;
    private LocalDateTime dataHoraSinistro;
    private LocalDateTime dataHoraRegistro;
    private String usuarioRegistro;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;

    // Novos atributos da 4ª melhoria - não incluir no construtor existente
    private int sequencial;
    private String numeroApolice;

    @Override
    public String getIdUnico() {
        return getNumero();
    }

    // Métodos get/set para os novos atributos
    public int getSequencial() {
        return sequencial;
    }

    public void setSequencial(int sequencial) {
        this.sequencial = sequencial;
    }

    public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraSinistro, LocalDateTime dataHoraRegistro, String usuarioRegistro, BigDecimal valorSinistro, TipoSinistro tipo) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.dataHoraSinistro = dataHoraSinistro;
        this.dataHoraRegistro = dataHoraRegistro;
        this.usuarioRegistro = usuarioRegistro;
        this.valorSinistro = valorSinistro;
        this.tipo = tipo;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }
}