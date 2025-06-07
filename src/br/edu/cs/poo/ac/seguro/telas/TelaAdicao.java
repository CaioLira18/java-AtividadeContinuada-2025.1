package br.edu.cs.poo.ac.seguro.telas;

import br.edu.cs.poo.ac.seguro.entidades.SWTResourceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class TelaAdicao {

	protected Shell shlTelaAdio;
	private Text txtPrimeiroNumero;
	private Text txtSegundoNUmero;
	private Text txtResultado;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TelaAdicao window = new TelaAdicao();
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
		shlTelaAdio.open();
		shlTelaAdio.layout();
		while (!shlTelaAdio.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shlTelaAdio = new Shell();
		shlTelaAdio.setSize(556, 330);
		shlTelaAdio.setText("Tela adição");
		
		Label lblPrimeiroNmero = new Label(shlTelaAdio, SWT.NONE);
		lblPrimeiroNmero.setBounds(41, 40, 121, 20);
		lblPrimeiroNmero.setText("Primeiro número");
		
		Label lblNewLabel = new Label(shlTelaAdio, SWT.NONE);
		lblNewLabel.setBounds(41, 102, 121, 20);
		lblNewLabel.setText("Segundo número");
		
		txtPrimeiroNumero = new Text(shlTelaAdio, SWT.BORDER);
		txtPrimeiroNumero.setBounds(183, 40, 78, 26);
		
		txtSegundoNUmero = new Text(shlTelaAdio, SWT.BORDER);
		txtSegundoNUmero.setBounds(183, 102, 78, 26);
		
		Label lblResultado = new Label(shlTelaAdio, SWT.NONE);
		lblResultado.setBounds(41, 163, 70, 20);
		lblResultado.setText("Resultado");
		
		txtResultado = new Text(shlTelaAdio, SWT.BORDER);
		txtResultado.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		txtResultado.setEnabled(false);
		txtResultado.setEditable(false);
		txtResultado.setBounds(183, 163, 78, 37);
		
		Button btnSomar = new Button(shlTelaAdio, SWT.NONE);
		btnSomar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				double n1 = Double.parseDouble(txtPrimeiroNumero.getText());
				double n2 = Double.parseDouble(txtSegundoNUmero.getText()); 
				double soma = n1 + n2;
				txtResultado.setText("" + soma);	
			}
		});
		btnSomar.setBounds(130, 220, 90, 30);
		btnSomar.setText("Somar");
		
		Button btnLimpar = new Button(shlTelaAdio, SWT.NONE);
		btnLimpar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				txtPrimeiroNumero.setText("");
				txtSegundoNUmero.setText("");
				txtResultado.setText("");
			}
		});
		btnLimpar.setBounds(254, 220, 90, 30);
		btnLimpar.setText("Limpar");

	}
}
