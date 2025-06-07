package br.edu.cs.poo.ac.seguro.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
// @AllArgsConstructor // Com construtor explícito, AllArgsConstructor não é gerado ou é sobrescrito
public class Sinistro implements Registro {
    private static final long serialVersionUID = 1L;

    private String numero;
    private Veiculo veiculo;
    private LocalDateTime dataHoraSinistro;
    private LocalDateTime dataHoraRegistro;
    private String usuarioRegistro;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;

    private int sequencial;
    private String numeroApolice;

    // Construtor com tratamento de nulls para dataHoraSinistro, dataHoraRegistro e valorSinistro
    public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraSinistro, LocalDateTime dataHoraRegistro, String usuarioRegistro, BigDecimal valorSinistro, TipoSinistro tipo) {
        this.numero = numero;
        this.veiculo = veiculo;

        // Trata dataHoraSinistro: se for null, atribui null; caso contrário, zera nanosegundos
        this.dataHoraSinistro = (dataHoraSinistro != null) ? dataHoraSinistro.withNano(0) : null;

        // Trata dataHoraRegistro (esta era a linha que dava erro 47 no seu caso):
        // Se for null, atribui null; caso contrário, zera nanosegundos
        this.dataHoraRegistro = (dataHoraRegistro != null) ? dataHoraRegistro.withNano(0) : null;

        this.usuarioRegistro = usuarioRegistro;

        // Trata valorSinistro: se for null, atribui BigDecimal.ZERO; caso contrário, aplica setScale(2)
        this.valorSinistro = (valorSinistro != null) ? valorSinistro.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        // Use ROUND_HALF_UP ou outro RoundingMode apropriado. Se setScale(2) sem modo de arredondamento
        // for permitido, pode causar ArithmeticException em alguns cenários.

        this.tipo = tipo;
    }


    @Override
    public String getIdUnico() {
        return getNumero();
    }

    // Métodos get/set para os novos atributos (mantidos do seu código original)
    public int getSequencial() {
        return sequencial;
    }

    public void setSequencial(int sequencial) {
        this.sequencial = sequencial;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    // Se você tiver outros getters/setters aqui devido ao Lombok, eles não precisam ser alterados.
    // O Lombok pode gerar automaticamente os getters/setters para dataHoraSinistro, dataHoraRegistro,
    // valorSinistro, etc., mas a atribuição no construtor prevalece.
    // Certifique-se de que não haja um construtor @AllArgsConstructor sendo gerado e usado
    // que ignore o seu construtor manual. Se houver, remova a anotação @AllArgsConstructor.
}