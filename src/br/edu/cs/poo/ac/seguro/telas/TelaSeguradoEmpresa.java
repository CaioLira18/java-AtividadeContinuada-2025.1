package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class TelaSeguradoEmpresa extends JFrame {
    private JTextField txtCnpj, txtNome, txtLogradouro, txtNumero, txtComplemento, txtCep, txtCidade, txtEstado, txtPais, txtBonus;
    private JCheckBox chkLocadora;
    private JTextField txtFaturamento;
    private JButton btnBuscar, btnSalvar, btnExcluir, btnLimpar;
    private SeguradoEmpresaDAO dao = new SeguradoEmpresaDAO();

    public TelaSeguradoEmpresa() {
        setTitle("CRUD Segurado Empresa");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setBounds(10, 10, 100, 20);
        getContentPane().add(lblCnpj);

        txtCnpj = new JTextField();
        txtCnpj.setBounds(120, 10, 200, 25);
        getContentPane().add(txtCnpj);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 40, 100, 20);
        getContentPane().add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(120, 40, 200, 25);
        getContentPane().add(txtNome);

        JLabel lblLogradouro = new JLabel("Logradouro:");
        lblLogradouro.setBounds(10, 70, 100, 20);
        getContentPane().add(lblLogradouro);

        txtLogradouro = new JTextField();
        txtLogradouro.setBounds(120, 70, 200, 25);
        getContentPane().add(txtLogradouro);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(10, 100, 100, 20);
        getContentPane().add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(120, 100, 200, 25);
        getContentPane().add(txtNumero);

        JLabel lblComplemento = new JLabel("Complemento:");
        lblComplemento.setBounds(10, 130, 100, 20);
        getContentPane().add(lblComplemento);

        txtComplemento = new JTextField();
        txtComplemento.setBounds(120, 130, 200, 25);
        getContentPane().add(txtComplemento);

        JLabel lblCep = new JLabel("CEP:");
        lblCep.setBounds(10, 160, 100, 20);
        getContentPane().add(lblCep);

        txtCep = new JTextField();
        txtCep.setBounds(120, 160, 200, 25);
        getContentPane().add(txtCep);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(10, 190, 100, 20);
        getContentPane().add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setBounds(120, 190, 200, 25);
        getContentPane().add(txtCidade);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(10, 220, 100, 20);
        getContentPane().add(lblEstado);

        txtEstado = new JTextField();
        txtEstado.setBounds(120, 220, 200, 25);
        getContentPane().add(txtEstado);

        JLabel lblPais = new JLabel("País:");
        lblPais.setBounds(10, 250, 100, 20);
        getContentPane().add(lblPais);

        txtPais = new JTextField();
        txtPais.setBounds(120, 250, 200, 25);
        getContentPane().add(txtPais);

        JLabel lblBonus = new JLabel("Bônus:");
        lblBonus.setBounds(10, 280, 100, 20);
        getContentPane().add(lblBonus);

        txtBonus = new JTextField();
        txtBonus.setBounds(120, 280, 200, 25);
        getContentPane().add(txtBonus);

        JLabel lblFaturamento = new JLabel("Faturamento:");
        lblFaturamento.setBounds(10, 310, 100, 20);
        getContentPane().add(lblFaturamento);

        txtFaturamento = new JTextField();
        txtFaturamento.setBounds(120, 310, 200, 25);
        getContentPane().add(txtFaturamento);

        chkLocadora = new JCheckBox("É locadora de veículos");
        chkLocadora.setBounds(120, 340, 200, 25);
        getContentPane().add(chkLocadora);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(10, 350, 100, 30);
        getContentPane().add(btnBuscar);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 350, 100, 30);
        getContentPane().add(btnSalvar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(230, 350, 100, 30);
        getContentPane().add(btnExcluir);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(340, 350, 80, 30);
        getContentPane().add(btnLimpar);

        btnBuscar.addActionListener(e -> buscar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());

        setVisible(true);
    }

    private void buscar() {
        String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
        SeguradoEmpresa seg = dao.buscar(cnpj);

        if (seg != null) {
            txtNome.setText(seg.getNome());
            txtBonus.setText(seg.getBonus().toString());
            chkLocadora.setSelected(seg.getEhLocadoraDeVeiculos());
            Endereco end = seg.getEndereco();
            txtLogradouro.setText(end.getLogradouro());
            txtNumero.setText(end.getNumero());
            txtComplemento.setText(end.getComplemento());
            txtCep.setText(end.getCep());
            txtCidade.setText(end.getCidade());
            txtEstado.setText(end.getEstado());
            txtPais.setText(end.getPais());
        } else {
            JOptionPane.showMessageDialog(this, "Segurado não encontrado. Pode ser incluído.");
            txtNome.requestFocus();
        }
    }

    private void salvar() {
        try {
            String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
            String nome = txtNome.getText();
            BigDecimal bonus = new BigDecimal(txtBonus.getText());
            boolean ehLocadora = chkLocadora.isSelected();
            double faturamento = Double.parseDouble(txtFaturamento.getText());

            Endereco endereco = new Endereco(
                    txtLogradouro.getText(),
                    txtCep.getText(),
                    txtNumero.getText(),
                    txtComplemento.getText(),
                    txtPais.getText(),
                    txtEstado.getText(),
                    txtCidade.getText()
            );

            SeguradoEmpresa seg = new SeguradoEmpresa(
                    nome,
                    endereco,
                    LocalDate.now(),
                    bonus,
                    cnpj,
                    faturamento,
                    ehLocadora
            );

            dao.incluir(seg);
            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }


    private void excluir() {
        String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
        dao.excluir(cnpj);
        JOptionPane.showMessageDialog(this, "Registro removido.");
        limparCampos();
    }

    private void limparCampos() {
        txtCnpj.setText("");
        txtNome.setText("");
        txtLogradouro.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtCep.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtPais.setText("");
        txtBonus.setText("");
        chkLocadora.setSelected(false);
    }

    public static void main(String[] args) {
        new TelaSeguradoEmpresa();
    }
}
