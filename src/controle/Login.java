//clase para criar uma tela para login do usuario.

package controle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import banco.BancoDeDados;

public class Login extends BancoDeDados {

	ImageIcon figura = new ImageIcon("pontoMA.png");
	JLabel txtMatriculaLogin;
	public JTextField campoMatriculaLogin;
	public String retornoMatricula = null;

	private JFrame janela;
	private JPanel cabecalho;
	private JPanel painelDeLogin;
	private JButton botaoEntrar = new JButton("   Entrar   ");
	private JButton botaoCancelar = new JButton("Cancelar");

	// Cria a janela de login
	public void CriarJanela() {
		// Cria o cabeçalho com o titulo do programa
		cabecalho = new JPanel();
		cabecalho.setBackground(Color.LIGHT_GRAY);
		cabecalho.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 4, true));
		cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.X_AXIS));
		cabecalho.add(Box.createHorizontalStrut(200));
		cabecalho.add(new JLabel("Controle de Horario"));
		cabecalho.add(Box.createVerticalStrut(50));
		cabecalho.add(new JLabel(figura), BorderLayout.CENTER);

		// Cria todo a interface de login onde vai o login a senha e os butons.

		txtMatriculaLogin = new JLabel("Login: ", JLabel.LEFT);
		campoMatriculaLogin = new JTextField();

		painelDeLogin = new JPanel();
		painelDeLogin.setLayout(new GridLayout(10, 3));
		painelDeLogin.add(Box.createVerticalBox());
		painelDeLogin.add(txtMatriculaLogin);
		painelDeLogin.add(campoMatriculaLogin);
		// painelDeLogin.add(new JLabel("Login: "));
		// painelDeLogin.add(new JTextField());
		// painelDeLogin.add(new JLabel("Senha: "));
		// painelDeLogin.add(new JPasswordField());
		painelDeLogin.add(Box.createVerticalBox());
		JPanel botao = new JPanel();
		botao.setLayout(new BoxLayout(botao, BoxLayout.X_AXIS));
		botao.add(Box.createHorizontalStrut(70));
		botao.add(botaoEntrar);

		// Executa o botao confirmar.
		botaoEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//metodo que seleciona a matricula e nome do funcionario do banco e compara com o digitado
				validarMatriculaLogin();
				
				//retornoMatricula recebe o que foi digitado
				retornoMatricula = campoMatriculaLogin.getText();
				Ponto ponto = new Ponto();
				ponto.exibirJanelaPonto();
				janela.dispose();


				retornoMatricula = (ponto.setMatricula(campoMatriculaLogin.getText()));
			}
		});

		botaoCancelar.setForeground(Color.red);
		botao.add(Box.createHorizontalStrut(70));
		botao.add(botaoCancelar);

		// Executa o botao cancelar.
		botaoCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				janela.dispose();
			}
		});
		painelDeLogin.add(botao);

		// Adiciona os painels no frame com um border
		janela = new JFrame("PONTO");
		janela.setSize(500, 600);
		janela.setLocation(450, 240);
		janela.setLayout(new BorderLayout());
		janela.add(cabecalho, BorderLayout.NORTH);
		janela.add(painelDeLogin, BorderLayout.CENTER);
		janela.add(Box.createHorizontalStrut(50), BorderLayout.EAST);
		janela.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
		janela.add(new JTextField("PROG 4"), BorderLayout.SOUTH);

	}

	public void exibirJanela() {
		CriarJanela();
		janela.setLocation(800, 100);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setResizable(false);
		janela.setVisible(true);
	}

	public void validarMatriculaLogin() {

		try {
			conectar();
			String query = "SELECT matricula,nmFuncionario FROM funcionario_ponto";
			this.resultado = this.comando.executeQuery(query);

			while (this.resultado.next()) {

				boolean registro = retornoMatricula.equals(resultado.getString("matricula"));
				if (registro == true) {
					if(retornoMatricula != null){
					//JOptionPane.showMessageDialog(null, this.resultado.getString("nmFuncionario")
					//		+ "         Matricula: " + this.resultado.getString("matricula"));
					
					System.out.println("Usuario logado Matricula: " +
					this.resultado.getString("matricula")+ " Nome: " +
					this.resultado.getString("nmFuncionario"));
					}
					/*else {
							JOptionPane.showMessageDialog(null,"Matricula não cadastrada tente novamente");
							janela.dispose();
							exibirJanela();
						 }*/
				}

			}
		} catch (Exception e) {
		//	JOptionPane.showMessageDialog(null,"Matricula não cadastrada tente novamente");
		//	desconectar();
		//	janela.dispose();
		//	conectar();
		//	exibirJanela();

			System.out.println("'ValidarMatriculaLogin' Erro 0007: " + e.getMessage());
		}
	}

}