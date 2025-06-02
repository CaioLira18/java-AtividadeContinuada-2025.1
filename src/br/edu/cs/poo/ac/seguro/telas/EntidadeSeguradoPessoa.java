package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.ParseException;

public class EntidadeSeguradoPessoa extends JFrame {

    private JPanel contentPane;
    private JTextField txtCpf;
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtBonus;

    private JButton btnIncluir;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnBuscar;
    private JButton btnSair;
    private JButton btnCancelar;
    private JTextField txtLogradouro;
    private JTextField txtCep;
    private JTextField txtNumero;
    private JTextField txtComplemento;
    private JTextField txtPais;
    private JTextField txtEstado;
    private JTextField txtCidade;


    private SeguradoPessoaDAO dao = new SeguradoPessoaDAO();

    public EntidadeSeguradoPessoa() {
        setTitle("CRUD Segurado Pessoa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 480, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(20, 20, 80, 20);
        contentPane.add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(100, 20, 150, 25);
        contentPane.add(txtCpf);
        txtCpf.setColumns(10);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 60, 80, 20);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(100, 60, 300, 25);
        contentPane.add(txtNome);
        txtNome.setColumns(10);

        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setBounds(20, 100, 80, 20);
        contentPane.add(lblEndereco);

        txtEndereco = new JTextField();
        txtEndereco.setBounds(100, 100, 300, 25);
        contentPane.add(txtEndereco);
        txtEndereco.setColumns(10);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(20, 140, 80, 20);
        contentPane.add(lblTelefone);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(100, 140, 150, 25);
        contentPane.add(txtTelefone);
        txtTelefone.setColumns(10);

        JLabel lblBonus = new JLabel("Bônus:");
        lblBonus.setBounds(20, 180, 80, 20);
        contentPane.add(lblBonus);

        txtBonus = new JTextField();
        txtBonus.setBounds(100, 180, 150, 25);
        contentPane.add(txtBonus);
        txtBonus.setColumns(10);

        // Botões
        btnIncluir = new JButton("Incluir");
        btnIncluir.setBounds(20, 230, 80, 30);
        contentPane.add(btnIncluir);

        btnAlterar = new JButton("Alterar");
        btnAlterar.setBounds(110, 230, 80, 30);
        contentPane.add(btnAlterar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(200, 230, 80, 30);
        contentPane.add(btnExcluir);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(290, 230, 80, 30);
        contentPane.add(btnBuscar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(380, 230, 80, 30);
        contentPane.add(btnCancelar);

        btnSair = new JButton("Sair");
        btnSair.setBounds(180, 270, 100, 30);
        contentPane.add(btnSair);

        // Ações dos botões
        configurarAcoes();

        // Estado inicial dos campos
        habilitarCampos(false);
    }

    private void configurarAcoes() {

        btnBuscar.addActionListener(e -> buscar());

        btnIncluir.addActionListener(e -> {
            incluir();
            limparCampos();
            habilitarCampos(false);
        });

        btnAlterar.addActionListener(e -> {
            alterar();
            limparCampos();
            habilitarCampos(false);
        });

        btnExcluir.addActionListener(e -> {
            excluir();
            limparCampos();
            habilitarCampos(false);
        });

        btnCancelar.addActionListener(e -> {
            limparCampos();
            habilitarCampos(false);
        });

        btnSair.addActionListener(e -> dispose());

        txtCpf.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!txtCpf.getText().isBlank()) {
                    buscar();
                }
            }
        });
    }

    private void habilitarCampos(boolean hab) {
        txtNome.setEnabled(hab);
        txtEndereco.setEnabled(hab);
        txtTelefone.setEnabled(hab);
        txtBonus.setEnabled(hab);

        btnIncluir.setEnabled(hab);
        btnAlterar.setEnabled(hab);
        btnExcluir.setEnabled(hab);
    }

    private void limparCampos() {
        txtCpf.setText("");
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtBonus.setText("");
    }

    private void buscar() {
        String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
        SeguradoPessoa seg = dao.buscar(cpf);

        if (seg != null) {
            txtNome.setText(seg.getNome());

            Endereco end = seg.getEndereco();
            if (end != null) {
                txtLogradouro.setText(end.getLogradouro());
                txtNumero.setText(end.getNumero());
                txtComplemento.setText(end.getComplemento());
                txtCep.setText(end.getCep());
                txtCidade.setText(end.getCidade());
                txtEstado.setText(end.getEstado());
                txtPais.setText(end.getPais());
            }


            txtBonus.setText(seg.getBonus().toString());

            habilitarCampos(true);
        } else {
            JOptionPane.showMessageDialog(this, "Segurado não encontrado. Pode ser incluído.");
            habilitarCampos(true);
            txtNome.requestFocus();
        }
    }


    private void incluir() {
        try {
            SeguradoPessoa seg = montarSegurado();
            dao.incluir(seg);
            JOptionPane.showMessageDialog(this, "Incluído com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao incluir: " + e.getMessage());
        }
    }

    private void alterar() {
        try {
            SeguradoPessoa seg = montarSegurado();
            dao.alterar(seg);
            JOptionPane.showMessageDialog(this, "Alterado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
        dao.excluir(cpf);
        JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
    }

    private SeguradoPessoa montarSegurado() {
        String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
        String nome = txtNome.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();
        BigDecimal bonus = new BigDecimal(txtBonus.getText().replace(",", "."));

        return new SeguradoPessoa(cpf, nome, endereco, telefone, bonus);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EntidadeSeguradoPessoa frame = new EntidadeSeguradoPessoa();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
