package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class TelaSinistro extends JFrame {

    private JPanel contentPane;
    private JTextField txtPlaca;
    private JFormattedTextField txtDataHoraSinistro;
    private JTextField txtUsuarioRegistro;
    private JTextField txtValorSinistro;
    private JComboBox<TipoSinistro> cmbTipoSinistro;

    private JButton btnIncluir;
    private JButton btnLimpar;

    private SinistroMediator mediator = SinistroMediator.getInstancia();

    public TelaSinistro() {
        setTitle("Inclusão de Sinistro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Campo Placa
        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(20, 20, 80, 20);
        contentPane.add(lblPlaca);

        txtPlaca = new JTextField();
        txtPlaca.setBounds(120, 20, 150, 25);
        contentPane.add(txtPlaca);
        txtPlaca.setColumns(10);

        // Campo Data/Hora do Sinistro
        JLabel lblDataHora = new JLabel("Data/Hora:");
        lblDataHora.setBounds(20, 60, 80, 20);
        contentPane.add(lblDataHora);

        try {
            MaskFormatter maskDataHora = new MaskFormatter("##/##/#### ##:##");
            maskDataHora.setPlaceholderCharacter('_');
            txtDataHoraSinistro = new JFormattedTextField(maskDataHora);
        } catch (ParseException e) {
            txtDataHoraSinistro = new JFormattedTextField();
        }
        txtDataHoraSinistro.setBounds(120, 60, 150, 25);
        contentPane.add(txtDataHoraSinistro);

        // Campo Usuário de Registro
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(20, 100, 80, 20);
        contentPane.add(lblUsuario);

        txtUsuarioRegistro = new JTextField();
        txtUsuarioRegistro.setBounds(120, 100, 200, 25);
        contentPane.add(txtUsuarioRegistro);
        txtUsuarioRegistro.setColumns(10);

        // Campo Valor do Sinistro
        JLabel lblValor = new JLabel("Valor:");
        lblValor.setBounds(20, 140, 80, 20);
        contentPane.add(lblValor);

        txtValorSinistro = new JTextField();
        txtValorSinistro.setBounds(120, 140, 150, 25);
        contentPane.add(txtValorSinistro);
        txtValorSinistro.setColumns(10);

        // ComboBox Tipo de Sinistro
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 180, 80, 20);
        contentPane.add(lblTipo);

        cmbTipoSinistro = new JComboBox<>();
        cmbTipoSinistro.setBounds(120, 180, 200, 25);
        preencherComboTipoSinistro();
        contentPane.add(cmbTipoSinistro);

        // Botões
        btnIncluir = new JButton("Incluir");
        btnIncluir.setBounds(120, 220, 80, 30);
        contentPane.add(btnIncluir);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(220, 220, 80, 30);
        contentPane.add(btnLimpar);

        // Configurar ações
        configurarAcoes();

        // Configurar ordem de navegação por tab
        configurarOrdemTab();
    }

    private void preencherComboTipoSinistro() {
        // Adiciona os tipos de sinistro em ordem alfabética
        TipoSinistro[] tipos = TipoSinistro.values();

        // Ordena alfabeticamente pelo nome do enum
        java.util.Arrays.sort(tipos, (t1, t2) -> t1.name().compareTo(t2.name()));

        for (TipoSinistro tipo : tipos) {
            cmbTipoSinistro.addItem(tipo);
        }

        // Seleciona o primeiro item
        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0);
        }
    }

    private void configurarAcoes() {
        btnIncluir.addActionListener(e -> incluirSinistro());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void configurarOrdemTab() {
        // Define a ordem de navegação por Tab
        java.util.Vector<Component> order = new java.util.Vector<>();
        order.add(txtPlaca);
        order.add(txtDataHoraSinistro);
        order.add(txtUsuarioRegistro);
        order.add(txtValorSinistro);
        order.add(cmbTipoSinistro);
        order.add(btnIncluir);
        order.add(btnLimpar);

        setFocusTraversalPolicy(new java.awt.DefaultFocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container container, Component component) {
                int index = order.indexOf(component);
                if (index >= 0 && index < order.size() - 1) {
                    return order.get(index + 1);
                }
                return order.get(0);
            }

            @Override
            public Component getComponentBefore(Container container, Component component) {
                int index = order.indexOf(component);
                if (index > 0) {
                    return order.get(index - 1);
                }
                return order.get(order.size() - 1);
            }
        });
    }

    private void incluirSinistro() {
        try {
            DadosSinistro dados = montarDadosSinistro();
            LocalDateTime dataHoraAtual = LocalDateTime.now();

            String numeroSinistro = mediator.incluirSinistro(dados, dataHoraAtual);

            JOptionPane.showMessageDialog(this,
                    "Sinistro incluído com sucesso! Anote o número do sinistro: " + numeroSinistro,
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

        } catch (ExcecaoValidacaoDados e) {
            StringBuilder mensagem = new StringBuilder();
            for (String msg : e.getMensagens()) {
                mensagem.append(msg).append("\n");
            }
            JOptionPane.showMessageDialog(this,
                    mensagem.toString(),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private DadosSinistro montarDadosSinistro() throws Exception {
        String placa = txtPlaca.getText().trim();
        String dataHoraStr = txtDataHoraSinistro.getText().trim();
        String usuario = txtUsuarioRegistro.getText().trim();
        String valorStr = txtValorSinistro.getText().trim().replace(",", ".");
        TipoSinistro tipoSelecionado = (TipoSinistro) cmbTipoSinistro.getSelectedItem();

        // Conversão da data/hora
        LocalDateTime dataHoraSinistro = null;
        if (!dataHoraStr.isEmpty() && !dataHoraStr.contains("_")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                dataHoraSinistro = LocalDateTime.parse(dataHoraStr, formatter);
            } catch (DateTimeParseException e) {
                throw new Exception("Formato de data/hora inválido. Use: dd/MM/yyyy HH:mm");
            }
        }

        // Conversão do valor
        double valor = 0.0;
        if (!valorStr.isEmpty()) {
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                throw new Exception("Valor do sinistro inválido");
            }
        }

        // Código do tipo de sinistro
        int codigoTipo = tipoSelecionado != null ? tipoSelecionado.getCodigo() : 0;

        return new DadosSinistro(placa, dataHoraSinistro, usuario, valor, codigoTipo);
    }

    private void limparCampos() {
        txtPlaca.setText("");
        txtDataHoraSinistro.setText("");
        txtUsuarioRegistro.setText("");
        txtValorSinistro.setText("");

        // Limpar combo significa mostrar o primeiro elemento
        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0);
        }

        // Foco no primeiro campo
        txtPlaca.requestFocus();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                TelaSinistro frame = new TelaSinistro();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}