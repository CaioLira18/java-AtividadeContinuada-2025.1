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

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(20, 20, 80, 20);
        contentPane.add(lblPlaca);

        try {
            MaskFormatter maskPlaca = new MaskFormatter("UUU-####");
            maskPlaca.setPlaceholderCharacter('_');
            JFormattedTextField txtPlacaFormatted = new JFormattedTextField(maskPlaca);
            txtPlacaFormatted.setBounds(120, 20, 150, 25);
            contentPane.add(txtPlacaFormatted);
            txtPlaca = txtPlacaFormatted;
        } catch (ParseException e) {
            txtPlaca = new JTextField();
            txtPlaca.setBounds(120, 20, 150, 25);
            contentPane.add(txtPlaca);
        }
        txtPlaca.setColumns(10);

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

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(20, 100, 80, 20);
        contentPane.add(lblUsuario);

        txtUsuarioRegistro = new JTextField();
        txtUsuarioRegistro.setBounds(120, 100, 200, 25);
        contentPane.add(txtUsuarioRegistro);
        txtUsuarioRegistro.setColumns(10);

        JLabel lblValor = new JLabel("Valor:");
        lblValor.setBounds(20, 140, 80, 20);
        contentPane.add(lblValor);

        txtValorSinistro = new JTextField();
        txtValorSinistro.setBounds(120, 140, 150, 25);
        contentPane.add(txtValorSinistro);
        txtValorSinistro.setColumns(10);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 180, 80, 20);
        contentPane.add(lblTipo);

        cmbTipoSinistro = new JComboBox<>();
        cmbTipoSinistro.setBounds(120, 180, 200, 25);
        preencherComboTipoSinistro();
        contentPane.add(cmbTipoSinistro);

        btnIncluir = new JButton("Incluir");
        btnIncluir.setBounds(120, 220, 80, 30);
        contentPane.add(btnIncluir);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(220, 220, 80, 30);
        contentPane.add(btnLimpar);

        configurarAcoes();

        configurarOrdemTab();
    }

    private void preencherComboTipoSinistro() {
        TipoSinistro[] tipos = TipoSinistro.values();

        java.util.Arrays.sort(tipos, (t1, t2) -> t1.name().compareTo(t2.name()));

        for (TipoSinistro tipo : tipos) {
            cmbTipoSinistro.addItem(tipo);
        }

        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0);
        }
    }

    private void configurarAcoes() {
        btnIncluir.addActionListener(e -> incluirSinistro());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void configurarOrdemTab() {
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
        String placaRaw = txtPlaca.getText().trim();
        String placa = placaRaw.replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        if (placa.length() != 7) {
            throw new Exception("Placa deve ter 7 caracteres (formato ABC1234)");
        }

        if (placa.length() == 7) {
            placa = placa.substring(0, 3) + "-" + placa.substring(3);
        }

        String dataHoraStr = txtDataHoraSinistro.getText().trim();
        String usuario = txtUsuarioRegistro.getText().trim();
        String valorStr = txtValorSinistro.getText().trim().replace(",", ".");
        TipoSinistro tipoSelecionado = (TipoSinistro) cmbTipoSinistro.getSelectedItem();

        LocalDateTime dataHoraSinistro = null;
        if (!dataHoraStr.isEmpty() && !dataHoraStr.contains("_")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                dataHoraSinistro = LocalDateTime.parse(dataHoraStr, formatter);
            } catch (DateTimeParseException e) {
                throw new Exception("Formato de data/hora inválido. Use: dd/MM/yyyy HH:mm");
            }
        }

        double valor = 0.0;
        if (!valorStr.isEmpty()) {
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                throw new Exception("Valor do sinistro inválido");
            }
        }

        int codigoTipo = tipoSelecionado != null ? tipoSelecionado.getCodigo() : 0;

        return new DadosSinistro(placa, dataHoraSinistro, usuario, valor, codigoTipo);
    }

    private void limparCampos() {
        txtPlaca.setText("");
        txtDataHoraSinistro.setText("");
        txtUsuarioRegistro.setText("");
        txtValorSinistro.setText("");

        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0);
        }

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