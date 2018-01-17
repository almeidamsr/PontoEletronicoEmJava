package controle;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.JPasswordField;
import javax.swing.JTextField;

import banco.BancoDeDados;

public class Ponto extends Login {

	JFrame janela;
	JPanel cabecalho;
	JPanel painelDoPonto;
	JPanel rodape;
	ImageIcon figura = new ImageIcon("pontoMA.png");

	JButton botaoRegistro = new JButton("                REGISTRAR PONTO             ");
	JButton botaoConsultar = new JButton("           CONSULTAR RELATÒRIO        ");
	JButton botaoLogar = new JButton("               TROCAR DE USUARIO           ");
	JButton botaoDemonstrativo = new JButton("              DEMONSTRATIVO MES           ");
	//Button b;

	BancoDeDados bancoDeDados = new BancoDeDados();
	int contTipoPonto = 1;

	public void CriarJanelaPonto() {

		cabecalho = new JPanel();
		cabecalho.setBackground(Color.LIGHT_GRAY);
		cabecalho.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 4, true));
		cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.X_AXIS));
		cabecalho.add(Box.createHorizontalStrut(200));
		cabecalho.add(new JLabel("Controle de Horario"));
		cabecalho.add(Box.createVerticalStrut(50));
		cabecalho.add(new JLabel(figura), BorderLayout.CENTER);

		painelDoPonto = new JPanel();
		painelDoPonto.setLayout(new BoxLayout(painelDoPonto, BoxLayout.Y_AXIS));
		painelDoPonto.add(Box.createVerticalStrut(70));

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");

		// JLabel imprimir = new JLabel("Ponto registrado as " +
		// formatHora.format(cal.getTime()));
		// painelDoPonto.add(imprimir);

		// JLabel imprimir = new JLabel();
		// painelDoPonto.add(imprimir);
		
		// JLabel hora = new JLabel(" " + formatHora.format(cal.getTime()));
		// painelDoPonto.add(hora);

		painelDoPonto.add(Box.createVerticalStrut(50));
		painelDoPonto.add(botaoRegistro);

		// Executa o botao registrar ponto.
		botaoRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				bancoDeDados.conectar();	
				
				/*Metodo que guarda a contagem do codponto deacordo com a matricula*/
				bancoDeDados.guardarCodPonto(retornoMatricula);
				
				Object src = e.getSource();
				if (src == botaoRegistro && bancoDeDados.contCodPonto % 2 == 0) {
					
					//contTipoPonto++;
					if (retornoMatricula != null) {
						JOptionPane.showMessageDialog(null,
								"Entrada Registrada as " + formatHora.format(cal.getTime()));
					}
					/*metodo que insere na tabela ponto data horario, matrcula digitada e tipo = Entrada*/
					bancoDeDados.inserirPonto(retornoMatricula, "Entrada");

				} else {
					//contTipoPonto++;
					if (retornoMatricula != null) {
						JOptionPane.showMessageDialog(null, "Saida Registrada as " + formatHora.format(cal.getTime()));
					}
					/*metodo que insere na tabela ponto data horario, matrcula digitada e tipo = Saida*/
					bancoDeDados.inserirPonto(retornoMatricula, "Saida");

				}
				
				/*Metodo que recebe o horario inicial e final e nele são executados os metodo
				  inserirDiferenca(matricula) que usa o horario inicial e final para calcular e inserir as horas trabalhadas e horas extras do dia
				  validarHorarioMes(matricula) que faz a soma das horas trabalhadas em cada dia e armazena em uma variavel
			      updateDifMes() que usa a variavel de validarHorarioMes para calcular as horas trabalhadas no mes e horas extras mes e fa o update no ultimo ponto*/
				bancoDeDados.validarHorario(retornoMatricula);
				
				System.out.println(
						"------------------------------------------------------------------------------------");
				bancoDeDados.listarEntradaSaida();
				System.out.println(
						"------------------------------------------------------------------------------------");
				/*
				 * System.out.println(
				 * "---------------------------------Tabela Ponto---------------------------------------"
				 * ); bancoDeDados.listarTabelaPonto(); System.out.println(
				 * "------------------------------------------------------------------------------------"
				 * );
				 * 
				 */
			}

		});

		painelDoPonto.add(Box.createVerticalStrut(50));
		painelDoPonto.add(botaoConsultar);
		painelDoPonto.add(Box.createVerticalStrut(50));

		// Executa o botao registrar RELATORIO.
		botaoConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			/*	System.out.println("RELATÓRIO DIA");
				bancoDeDados.listarRelatorio(retornoMatricula);
				System.out.println("  ");
				System.out.println("RELATÓRIO MENSAL");
				bancoDeDados.listarRelatorioMes(retornoMatricula);
				System.out.println("  ");
				System.out.println("HORARIOS DE PONTO");
				bancoDeDados.listarTabelaPonto(retornoMatricula);
			*/			
				bancoDeDados.listarRelatorio(retornoMatricula);
				//bancoDeDados.demonstrativoMes(retornoMatricula);
			}

		});
		
		//----botao demostrativo mes
			painelDoPonto.add(Box.createVerticalStrut(50));
			painelDoPonto.add(botaoDemonstrativo);
			painelDoPonto.add(Box.createHorizontalStrut(50));
		
			// Executa o botaoDemonstrativo.
			botaoDemonstrativo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						bancoDeDados.demonstrativoMes(retornoMatricula);
					
				}
	
			});
		//----
	   
		botaoLogar.setForeground(Color.RED);
		painelDoPonto.add(Box.createVerticalStrut(50));
		painelDoPonto.add(botaoLogar);
		painelDoPonto.add(Box.createVerticalStrut(50));
	
		// Executa o botao  Logar.
		botaoLogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*Retorna para a janela login*/
				janela.dispose();
				exibirJanela();
				
			}

		});
		
		janela = new JFrame("PONTO");
		janela.setSize(500, 600);
		janela.setLocation(450, 240);
		janela.setLayout(new BorderLayout());
		janela.add(cabecalho, BorderLayout.NORTH);
		janela.add(painelDoPonto, BorderLayout.CENTER);
		janela.add(Box.createHorizontalStrut(120), BorderLayout.EAST);
		janela.add(Box.createHorizontalStrut(120), BorderLayout.WEST);
		janela.add(new JTextField("PROG 4"), BorderLayout.SOUTH);
	}

	public void exibirJanelaPonto() {
		CriarJanelaPonto();
		janela.setLocation(800, 100);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setResizable(false);
		janela.setVisible(true);
	}
	public void fecharJanelaPonto(){
		janela.dispose();
	}
	/*Metodo que recebe o valor de retornoMatricula que foi digitado no login*/
	public String setMatricula(String matricula) {
		// TODO Auto-generated method stub
		this.retornoMatricula = matricula;
		return matricula;
	}

}
