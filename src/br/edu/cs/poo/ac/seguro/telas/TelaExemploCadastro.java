package br.edu.cs.poo.ac.seguro.telas;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TelaExemploCadastro {

	protected Shell shlTelaDeCdastro;
	private EntidadeMediator mediator = new EntidadeMediator();	
	private Text txtCodigo;
	private Text txtNome;
	private Text txtRenda;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TelaExemploCadastro window = new TelaExemploCadastro();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlTelaDeCdastro.open();
		shlTelaDeCdastro.layout();
		while (!shlTelaDeCdastro.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTelaDeCdastro = new Shell();
		shlTelaDeCdastro.setSize(584, 323);
		shlTelaDeCdastro.setText("Tela de cdastro");
		
		Label lblCdigo = new Label(shlTelaDeCdastro, SWT.NONE);
		lblCdigo.setBounds(42, 41, 70, 20);
		lblCdigo.setText("Codigo");
		
		txtCodigo = new Text(shlTelaDeCdastro, SWT.BORDER);
		txtCodigo.setToolTipText("Digite o codigo");
		txtCodigo.setBounds(110, 41, 100, 26);
		Button btnNovo = new Button(shlTelaDeCdastro, SWT.NONE);
		
		btnNovo.setBounds(237, 41, 90, 30);
		btnNovo.setText("Novo");
		
		Button btnBuscar = new Button(shlTelaDeCdastro, SWT.NONE);

		btnBuscar.setBounds(345, 41, 90, 30);
		btnBuscar.setText("Buscar");
		
		Label lblNome = new Label(shlTelaDeCdastro, SWT.NONE);
		lblNome.setBounds(42, 101, 70, 20);
		lblNome.setText("Nome");
		
		txtNome = new Text(shlTelaDeCdastro, SWT.BORDER);
		txtNome.setEnabled(false);
		txtNome.setBounds(110, 101, 225, 26);
		
		Label lblRenda = new Label(shlTelaDeCdastro, SWT.NONE);
		lblRenda.setBounds(42, 162, 70, 20);
		lblRenda.setText("Renda");
		
		txtRenda = new Text(shlTelaDeCdastro, SWT.BORDER);
		txtRenda.setEnabled(false);
		txtRenda.setBounds(110, 162, 118, 26);
		
		Button btnIncluirAlterar = new Button(shlTelaDeCdastro, SWT.NONE);

		btnIncluirAlterar.setEnabled(false);
		btnIncluirAlterar.setBounds(165, 223, 90, 30);
		btnIncluirAlterar.setText("Incluir");
		
		Button btnCancelar = new Button(shlTelaDeCdastro, SWT.NONE);

		btnCancelar.setEnabled(false);
		btnCancelar.setBounds(357, 223, 90, 30);
		btnCancelar.setText("Cancelar");
		
		Button btnLimpar = new Button(shlTelaDeCdastro, SWT.NONE);

		btnLimpar.setBounds(453, 223, 90, 30);
		btnLimpar.setText("Limpar");
		
		Button btnExcluir = new Button(shlTelaDeCdastro, SWT.NONE);
		btnExcluir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String msg = mediator.excluir(txtCodigo.getText());
				if (msg != null) {
					JOptionPane.showMessageDialog(null, 
					msg);					
				} else {
					btnIncluirAlterar.setEnabled(false);
					btnCancelar.setEnabled(false);
					btnExcluir.setEnabled(false);
					txtNome.setEnabled(false);
					txtRenda.setEnabled(false);
					btnNovo.setEnabled(true);
					btnBuscar.setEnabled(true);
					txtCodigo.setEnabled(true);
					txtCodigo.setText("");
					txtRenda.setText("");
					txtNome.setText("");
					btnIncluirAlterar.setText("Incluir");
					JOptionPane.showMessageDialog(null, 
					"Exclus�o realizada com sucesso!");					
				}				
			}
		});
		btnExcluir.setText("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.setBounds(261, 223, 90, 30);
		btnNovo.addMouseListener(new MouseAdapter() { 
			@Override
			public void mouseDown(MouseEvent e) {
				Entidade ent = mediator.buscar(txtCodigo.getText());
				if (ent != null) {
					JOptionPane.showMessageDialog(null, 
					"Entidade j� existente!");
				} else {
					btnIncluirAlterar.setEnabled(true);
					btnCancelar.setEnabled(true);
					txtNome.setEnabled(true);
					txtRenda.setEnabled(true);
					btnNovo.setEnabled(false);
					btnBuscar.setEnabled(false);
					txtCodigo.setEnabled(false);
				}
			}
		});
		btnBuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Entidade ent = mediator.buscar(txtCodigo.getText());
				if (ent == null) {
					JOptionPane.showMessageDialog(null, 
					"Entidade n�o existente!");
				} else {
					txtNome.setText(ent.getNome());
					txtRenda.setText(ent.getRenda() + "");
					btnIncluirAlterar.setText("Alterar");
					btnIncluirAlterar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnCancelar.setEnabled(true);
					txtNome.setEnabled(true);
					txtRenda.setEnabled(true);
					btnNovo.setEnabled(false);
					btnBuscar.setEnabled(false);
					txtCodigo.setEnabled(false);
				}			
			}
		});
		btnIncluirAlterar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				double renda = 0.0;
				try {
					renda = Double.parseDouble(txtRenda.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, 
					"Renda deve ser um n�mero");	
					return;
				}
				Entidade ent = new Entidade(txtCodigo.getText(), txtNome.getText(),
						renda);
				String msg = null;
				String msgOk = null;
				if (btnIncluirAlterar.getText().equals("Incluir")) {
					msg = mediator.incluir(ent);
					msgOk = "Inclus�o realizada com sucesso";
				} else {
					msg = mediator.alterar(ent);
					msgOk = "Altera��o realizada com sucesso";
				}
				if (msg != null) {
					JOptionPane.showMessageDialog(null, 
					msg);					
				} else {
					btnIncluirAlterar.setEnabled(false);
					btnCancelar.setEnabled(false);
					btnExcluir.setEnabled(false);
					txtNome.setEnabled(false);
					txtRenda.setEnabled(false);
					btnNovo.setEnabled(true);
					btnBuscar.setEnabled(true);
					txtCodigo.setEnabled(true);
					txtCodigo.setText("");
					txtRenda.setText("");
					txtNome.setText("");
					btnIncluirAlterar.setText("Incluir");
					JOptionPane.showMessageDialog(null, 
					msgOk);					
				}
			}
		});
		btnCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				btnIncluirAlterar.setEnabled(false);
				btnCancelar.setEnabled(false);
				btnExcluir.setEnabled(false);
				txtNome.setEnabled(false);
				txtRenda.setEnabled(false);
				btnNovo.setEnabled(true);
				btnBuscar.setEnabled(true);
				txtCodigo.setEnabled(true);
				txtCodigo.setText("");
				txtRenda.setText("");
				txtNome.setText("");
				btnIncluirAlterar.setText("Incluir");
			}
		});
		btnLimpar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (txtCodigo.isEnabled()) {
					txtCodigo.setText("");
				}
				txtRenda.setText("");
				txtNome.setText("");
			}
		});
	}
}
