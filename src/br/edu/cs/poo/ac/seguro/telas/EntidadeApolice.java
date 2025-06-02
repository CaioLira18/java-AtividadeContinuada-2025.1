package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;

public class EntidadeApolice extends JFrame {

    private JPanel contentPane;
    private JTextField txtCpfCnpj;
    private JTextField txtPlaca;
    private JTextField txtAno;
    private JTextField txtValorMaximoSegurado;
    private JComboBox<String> cmbCategoria;

    private JButton btnIncluir;
    private JButton btnLimpar;
    private JButton btnSair;

    private ApoliceMediator mediator;

    public EntidadeApolice() {
        this.mediator = ApoliceMediator.getInstancia();

        setTitle("Inclusão de Apólice");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // CPF ou CNPJ
        JLabel lblCpfCnpj = new JLabel("CPF ou CNPJ:");
        lblCpfCnpj.setBounds(20, 20, 100, 20);
        contentPane.add(lblCpfCnpj);

        txtCpfCnpj = new JTextField();
        txtCpfCnpj.setBounds(130, 20, 200, 25);
        contentPane.add(txtCpfCnpj);
        txtCpfCnpj.setColumns(10);

        // Placa
        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(20, 60, 100, 20);
        contentPane.add(lblPlaca);

        txtPlaca = new JTextField();
        txtPlaca.setBounds(130, 60, 150, 25);
        contentPane.add(txtPlaca);
        txtPlaca.setColumns(10);

        // Ano
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setBounds(20, 100, 100, 20);
        contentPane.add(lblAno);

        txtAno = new JTextField();
        txtAno.setBounds(130, 100, 100, 25);
        contentPane.add(txtAno);
        txtAno.setColumns(10);

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(20, 140, 100, 20);
        contentPane.add(lblCategoria);

        // Preenchendo o combo com as categorias em ordem alfabética
        String[] categorias = Arrays.stream(CategoriaVeiculo.values())
                .map(CategoriaVeiculo::name)
                .sorted()
                .toArray(String[]::new);

        cmbCategoria = new JComboBox<>(categorias);
        cmbCategoria.setBounds(130, 140, 200, 25);
        contentPane.add(cmbCategoria);

        // Valor Máximo Segurado
        JLabel lblValorMaximo = new JLabel("Valor Máx. Segurado:");
        lblValorMaximo.setBounds(20, 180, 120, 20);
        contentPane.add(lblValorMaximo);

        txtValorMaximoSegurado = new JTextField();
        txtValorMaximoSegurado.setBounds(150, 180, 150, 25);
        contentPane.add(txtValorMaximoSegurado);
        txtValorMaximoSegurado.setColumns(10);

        // Botões
        btnIncluir = new JButton("Incluir");
        btnIncluir.setBounds(100, 220, 80, 30);
        contentPane.add(btnIncluir);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(200, 220, 80, 30);
        contentPane.add(btnLimpar);

        btnSair = new JButton("Sair");
        btnSair.setBounds(300, 220, 80, 30);
        contentPane.add(btnSair);

        // Configurar ações dos botões
        configurarAcoes();

        // Configurar ordem de navegação por tab
        configurarOrdemTab();

        // Aplicar máscaras se disponível
        aplicarMascaras();
    }

    private void configurarAcoes() {
        btnIncluir.addActionListener(e -> incluir());
        btnLimpar.addActionListener(e -> limpar());
        btnSair.addActionListener(e -> dispose());
    }

    private void configurarOrdemTab() {
        // Define a ordem de navegação com Tab
        Component[] ordem = {
                txtCpfCnpj, txtPlaca, txtAno, cmbCategoria,
                txtValorMaximoSegurado, btnIncluir, btnLimpar, btnSair
        };

        for (int i = 0; i < ordem.length; i++) {
            final int next = (i + 1) % ordem.length;
            ordem[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        ordem[next].requestFocus();
                        e.consume();
                    }
                }
            });
        }
    }

    private void aplicarMascaras() {
        try {
            // Máscara para ano (4 dígitos)
            MaskFormatter maskAno = new MaskFormatter("####");
            maskAno.setPlaceholderCharacter('_');
            JFormattedTextField txtAnoFormatted = new JFormattedTextField(maskAno);
            txtAnoFormatted.setBounds(txtAno.getBounds());
            contentPane.remove(txtAno);
            contentPane.add(txtAnoFormatted);
            txtAno = txtAnoFormatted;

            // Máscara para placa (formato brasileiro ABC-1234)
            MaskFormatter maskPlaca = new MaskFormatter("UUU-####");
            maskPlaca.setPlaceholderCharacter('_');
            JFormattedTextField txtPlacaFormatted = new JFormattedTextField(maskPlaca);
            txtPlacaFormatted.setBounds(txtPlaca.getBounds());
            contentPane.remove(txtPlaca);
            contentPane.add(txtPlacaFormatted);
            txtPlaca = txtPlacaFormatted;

        } catch (ParseException e) {
            // Se não conseguir aplicar máscara, mantém os campos sem máscara
            System.err.println("Erro ao aplicar máscaras: " + e.getMessage());
        }
    }

    private void incluir() {
        try {
            DadosVeiculo dados = montarDadosVeiculo();
            RetornoInclusaoApolice retorno = mediator.incluirApolice(dados);

            if (retorno.getMensagemErro() == null) {
                // Inclusão bem sucedida
                JOptionPane.showMessageDialog(this,
                        "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice(),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                // Inclusão mal sucedida
                JOptionPane.showMessageDialog(this,
                        retorno.getMensagemErro(),
                        "Erro na Inclusão",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao incluir apólice: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        txtCpfCnpj.setText("");
        txtPlaca.setText("");
        txtAno.setText("");
        txtValorMaximoSegurado.setText("");
        cmbCategoria.setSelectedIndex(0); // Primeiro elemento

        // Foca no primeiro campo
        txtCpfCnpj.requestFocus();
    }

    private DadosVeiculo montarDadosVeiculo() {
        String cpfOuCnpj = txtCpfCnpj.getText().trim();
        String placa = txtPlaca.getText().trim();
        int ano = 0;

        try {
            String anoStr = txtAno.getText().replaceAll("[^0-9]", "");
            if (!anoStr.isEmpty()) {
                ano = Integer.parseInt(anoStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano deve ser um número válido");
        }

        // Conversão cuidadosa de String para BigDecimal
        BigDecimal valorMaximoSegurado = null;
        try {
            String valorStr = txtValorMaximoSegurado.getText().replace(",", ".");
            if (!valorStr.trim().isEmpty()) {
                valorMaximoSegurado = new BigDecimal(valorStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor máximo segurado deve ser um número válido");
        }

        int codigoCategoria = cmbCategoria.getSelectedIndex() + 1;

        return new DadosVeiculo(cpfOuCnpj, placa, ano, valorMaximoSegurado, codigoCategoria);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                EntidadeApolice frame = new EntidadeApolice();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}