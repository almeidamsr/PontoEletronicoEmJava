package banco;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controle.Login;
import controle.Ponto;

public class BancoDeDados {

	protected Connection conect = null;
	protected Statement comando = null;
	protected ResultSet resultado = null;
	public String sql = null;
	public Time horaInicial = null;
	public Time horaFinal = null;
	public Time horaPadrao = new Time(8, 00, 00);
	public Time horaMes = new Time(176, 00, 00);
	// public String calcMesStr = "0.00000000";
	public String calcMesStr = null;
	public Time calcMesTime = null;
	public int ultimoCodRelatorio;
	public int contCodPonto;
	public String nomeFuncionario;
	public String resultArray = "RELATÓRIO DIA \n";
	public String resultArrayMes = "\n RELATÓRIO MES \n";
	public String resultArrayPonto = "\n HORARIOS DE PONTO \n";
	public String resultArrayDemonstrativo = "\n DEMOSTRATIVO MES \n";
	// public Time horaInicialParaMes = null;
	// public Time horaFinalParaMes = null;
	// public JTextField meuJText;

	public void conectar() {

		String servidor = "jdbc:mysql://localhost:3306/banco_6per_java";
		String usuario = "root";
		String senha = "matheus";
		String driver = "com.mysql.jdbc.Driver";

		try {
			Class.forName(driver);
			this.conect = DriverManager.getConnection(servidor, usuario, senha);
			this.comando = this.conect.createStatement();
		} catch (Exception e) {
			System.out.println("'Conectar' Erro 0001: " + e.getMessage());
		}

	}

	public boolean estaConectado() {
		if (this.conect != null) {
			return true;
		} else {
			return false;
		}
	}

	public void desconectar() {
		try {
			this.conect.close();
		} catch (Exception e) {
			System.out.println("'Desconectar' Erro 0002: " + e.getMessage());
		}
	}

	public void listarTabelaPonto(String matricula) {
		try {
			conectar();
			int x = 0;
			String[] arrayPonto = new String[300];

			String query = "SELECT matricula,dataPonto,horario,tipoPonto FROM ponto where matricula = '" + matricula
					+ "' ;";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {

				String resultado = "| Matricula: " + this.resultado.getString("matricula") + "|     | Data: "
						+ this.resultado.getString("dataPonto") + "|      |Horario: "
						+ this.resultado.getString("horario") + "|     | Tipo do Ponto: "
						+ this.resultado.getString("tipoPonto") + "    |";

				arrayPonto[x] = resultado;
				x++;
			}
			for (String s : arrayPonto) {
				if (s != null) {
					resultArrayPonto = resultArrayPonto + "\n" + s;
				}
				// System.out.println(resultArrayPonto);

			}
			// JOptionPane.showMessageDialog(null,resultArrayPonto);
		} catch (Exception e) {
			System.out.println("listarTabelaPonto Erro 0003: " + e.getMessage());
		}
	}

	public void listarEntradaSaida() {
		try {
			String query = "SELECT matricula,dataPonto,horario,tipoPonto FROM ponto WHERE horario = CURTIME();";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {
				System.out.println("Matricula: " + this.resultado.getString("matricula") + " Data: "
						+ this.resultado.getString("dataPonto") + " Horario: " + this.resultado.getString("horario")
						+ " Tipo do Ponto: " + this.resultado.getString("tipoPonto"));
			}
		} catch (Exception e) {
			System.out.println("'ListarEntradaSaida' Erro 0004: " + e.getMessage());
		}
	}

	public void listarRelatorio(String matricula) {
		try {
			conectar();
			listarRelatorioMes(matricula);
			listarTabelaPonto(matricula);
			guardarNomeFuncionario(matricula);

			int x = 0;
			String[] array = new String[300];

			String query = "select matricula,dataPonto,difHorario,totalFaltante from relatorio where matricula = '"
					+ matricula + "';";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {

				String resultado = " | Matricula: " + this.resultado.getString("matricula") + " |      |Data do Ponto: "
						+ this.resultado.getString("dataPonto") + " |      | Quantidade_Horas_Trabalhas_Dia: "
						+ this.resultado.getString("difHorario")
						+ " |      |Hora_Extra_Ou_Faltante_Dia: " + this.resultado.getString("totalFaltante") + " |";

				array[x] = resultado;
				x++;
			}
			for (String s : array) {
				if (s != null) {
					resultArray = resultArray + "\n" + s;
				}
				// System.out.println(resultArray);

			}
			// JOptionPane.showMessageDialog(null,resultArray);
			JOptionPane.showMessageDialog(null,nomeFuncionario + "\n" + resultArray + "\n" + resultArrayMes + "\n" + resultArrayPonto);
			resultArray = "RELATÓRIO DIA \n";
			resultArrayMes = "\n RELATÓRIO MES \n";
			resultArrayPonto = "\n HORARIOS DE PONTO \n";
		} catch (Exception e) {
			System.out.println("'Listar Relatorio' Erro 0005: " + e.getMessage());
		}
	}

	public void listarRelatorioMes(String matricula) {
		try {
			conectar();
			int x = 0;
			String[] arrayMes = new String[300];
			
			/*String query = "select matricula,dataPonto,sec_to_time((difHorarioMes)*60*60) as difHorarioMes,faltanteMes from relatorio where matricula = '"
					+ matricula + "';";*/
			
			//com arredondamento 
			String query = "select matricula,dataPonto,sec_to_time((difHorarioMes)*60*60) as difHorarioMes,floor(faltanteMes) as faltanteMes from relatorio where matricula = '"
					+ matricula + "';";
			
			this.resultado = this.comando.executeQuery(query);

			while (this.resultado.next()) {
				
				String resultado = " | Matricula: " + this.resultado.getString("matricula") + " |      |Data do Ponto: "
						+ this.resultado.getString("dataPonto") + " |      | Quantidade_Horas_Trabalhas_Mes: "
						+ this.resultado.getString("difHorarioMes") + " |      |Hora_Extra_Ou_Faltante_Mes: "
						+ this.resultado.getString("faltanteMes") + " |";

				arrayMes[x] = resultado;
				x++;
			}
			for (String s : arrayMes) {
				if (s != null) {
					resultArrayMes = resultArrayMes + "\n" + s;
				}
				// System.out.println(resultArray);

			}
			// JOptionPane.showMessageDialog(null,resultArrayMes);
		} catch (Exception e) {
			System.out.println("'listarRelatorioMes' Erro 0006: " + e.getMessage());
		}
	}
	
	/*Metodo que recebe o horario inicial e final e nele são executados os metodos ,inserirDiferenca(matricula); validarHorarioMes(matricula);
	 * e updateDifMes();*/
	public void validarHorario(String matricula) {
		try {
			conectar();

			String query1 = "SELECT matricula,dataPonto,horario,tipoPonto FROM ponto where matricula = '" + matricula
					+ "' AND dataPonto= curdate(); ";

			this.resultado = this.comando.executeQuery(query1);

			while (this.resultado.next()) {

				if (this.resultado.isFirst()) {
					horaInicial = this.resultado.getTime("horario");
				}
				if (this.resultado.isLast()) {
					horaFinal = this.resultado.getTime("horario");
				}

			}
			
			/*metodo que usa o horaInicial e horaFinal para calcular e inserir as horas trabalhadas e horas extras do dia*/
			inserirDiferenca(matricula);
			/*metodo que faz a soma das horas trabalhadas em cada dia e armazena em uma variavel*/
			validarHorarioMes(matricula);
			/*que usa a variavel de validarHorarioMes para calcular as horas trabalhadas no mes e horas extras mes e fa o update no ultimo ponto*/
			updateDifMes();
			
			// System.out.println("hora inicial: " + horaInicial + " hora final:
			// " + horaFinal);
		} catch (Exception e) {
			System.out.println("'validarHorario' Erro 0007: " + e.getMessage());
		}
	}

	/*Metodo que calcula a soma do difHorario(Quantidade de horas trabalhadas em cada dia) transforma em segundo 
	 logo em seguidas em horas para tirar do formato Time dentro do mes e ano do ponto e guarda na variavel calcMesStr*/
	public void validarHorarioMes(String matricula) {
		try {
			conectar();

			// String query1 = "select ABS(timediff(difHorario,'"+horaMes+"'))
			// as faltanteMes from relatorio where month(dataPonto) =
			// month(now()) and year(now()) and dataPonto=curdate();";
			
			// String query1 = "select SUM(TIME_TO_SEC(difHorario)/60/60) as
			// difHorario from relatorio where month(dataPonto) = month(now())
			// and year(now()) and dataPonto=curdate();"; //lembrar de por o curdate() !!

			String query1 = "select SUM(TIME_TO_SEC(difHorario))/60/60 as difHorario from relatorio where month(dataPonto) = month(now()) and year(now()) and matricula = '"
					+ matricula + "'";

			this.resultado = this.comando.executeQuery(query1);

			while (this.resultado.next()) {
				// if (!this.resultado.wasNull()) {
				// if (this.resultado.isFirst()) {
				if (this.resultado != null) {
					calcMesStr = this.resultado.getString("difHorario");
					System.out.println("calMesStr: " + this.resultado.getString("difHorario"));
				}
				// if (this.resultado.isLast()) {
				// horaFinalParaMes =
				// }
				//
			}

		} catch (Exception e) {
			System.out.println("validarHorarioMes Erro 0008: " + e.getMessage());
		}
	}

	/*Metodo que insere no momento do ponto na tabela ponto a matricula,dataPonto,horario,tipoPonto 
	 onde a matricula for digitada no login nas datas e horas atuais*/
	public void inserirPonto(String matricula, String tipo) {
		Login login = new Login();
		Ponto ponto = new Ponto();
		try {
			conectar();
			
			// String query = "insert into ponto
			// (matricula,dataPonto,horario,tipoPonto) values
			// ('"+matricula+"',CURDATE(),CURTIME(),'" +tipo+ "');";
			
			String query = "insert into ponto (matricula,dataPonto,horario,tipoPonto) values ((select matricula from funcionario_ponto where matricula = '"
					+ matricula + "'),CURDATE(),CURTIME(),'" + tipo + "');";
			this.comando.executeUpdate(query);
		} catch (Exception e) {
			login.exibirJanela();
			//ponto.fecharJanelaPonto();
			System.out.println("'InserirPonto' Erro 0009: " + e.getMessage());
		}
	}

	/*Metodo que insere a matricula,dataPonto,horarioSaida,horarioEntrada,difHorario,totalFaltante 
	 com o calculo de diferença utilizando as variaveis horaInicial e horafinal que vieram do metodo validar horario*/
	public void inserirDiferenca(String matricula) {
		try {

			/*
			 * String query =
			 * "insert into relatorio (matricula,dataPonto,horarioSaida,horarioEntrada,difHorario,totalFaltante,difHorarioMes) values ((select matricula from funcionario_ponto where matricula = '"
			 * + matricula + "'),CURDATE(),'" + horaFinal + "','" + horaInicial
			 * +
			 * "',timediff(horarioSaida,horarioEntrada),  timediff(timediff(horarioSaida,horarioEntrada),'"
			 * + horaPadrao+ "'),'"+calcMesStr+"' );";
			 */
			String query = "insert into relatorio (matricula,dataPonto,horarioSaida,horarioEntrada,difHorario,totalFaltante) values ((select matricula from funcionario_ponto where matricula = '"
					+ matricula + "'),CURDATE(),'" + horaFinal + "','" + horaInicial
					+ "',timediff(horarioSaida,horarioEntrada),  timediff(timediff(horarioSaida,horarioEntrada),'"
					+ horaPadrao + "') );";

			/*
			 * String query =
			 * "insert into relatorio (matricula,dataPonto,horarioSaida,horarioEntrada,difHorario,totalFaltante) values ((select matricula from funcionario_ponto where matricula = '"
			 * + matricula + "'),CURDATE(),'" + horaFinal + "','" + horaInicial
			 * + "',ABS(timediff(horarioSaida,horarioEntrada)),  ABS(timediff('"
			 * + horaPadrao+ "',timediff(horarioSaida,horarioEntrada))) );";
			 */
			this.comando.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("'inserirDiferenca' Erro 0010: " + e.getMessage());
		}
	}

	/*metodo que usa a variavel calcMesStr de validarHorarioMes para calcular as horas trabalhadas no mes e horas extras mes 
	 e faz o update no ultimo ponto*/
	public void updateDifMes() {
		try {
			// validarHorarioMes();
			guardarCodRelatorio();
			// String query = "update relatorio set faltanteMes =
			// timediff('"+calcMesStr+"','"+horaMes+"') where codRelatorio =
			// '"+ultimoCodRelatorio+"' ;";

			// String query = "update relatorio set faltanteMes =
			// TIME_TO_SEC(timediff('"+calcMesStr+"','208:00:00'))/60/60 where
			// codRelatorio = '"+ultimoCodRelatorio+"' ;";

			// String query = "update relatorio set faltanteMes =
			// ('"+calcMesStr+"')-(TIME_TO_SEC('176:00:00')/60/60) where
			// codRelatorio = '"+ultimoCodRelatorio+"' ;";

			String query = "update relatorio set faltanteMes =  ('" + calcMesStr
					+ "')-(TIME_TO_SEC('176:00:00')/60/60) , difHorarioMes = '" + calcMesStr
					+ "' where codRelatorio = '" + ultimoCodRelatorio + "' ;";

			this.comando.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Update Dif Mes Erro 0011: " + e.getMessage());
		}
	}

	/*guarda o ultimo valor de codRelatorio e guarda na variavel ultimoCodRelatorio, resultado deste metodo usado no metodo updateDifMes*/
	public void guardarCodRelatorio() {
		try {
			String query = "select max(codRelatorio) as codRelatorio from relatorio;";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {
				ultimoCodRelatorio = this.resultado.getInt("codRelatorio");
				System.out.println("CodRelatorio: " + this.resultado.getInt("codRelatorio"));
			}
		} catch (Exception e) {
			System.out.println("'guardarCodRelatorio' Erro 0012: " + e.getMessage());
		}
	}

	/*metodo que guarda a contagem de codPonto na variavel contCodPonto para ser usado na classe Ponto para definir se o ponto é Entrada ou Saida*/
	public void guardarCodPonto(String matricula) {
		try {
			String query = "select count(codPonto) as contPonto from ponto where matricula = '" + matricula + "';";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {
				// if (this.resultado.getInt("contPonto") != 0){
				contCodPonto = this.resultado.getInt("contPonto");
				System.out.println("contCodPonto: " + this.resultado.getInt("contPonto"));
				// }
			}
		} catch (Exception e) {
			System.out.println("guardarCodPonto Erro 0013: " + e.getMessage());
		}
	}
	
	/*Guarda o nome do funcionario para ser usado no relatorio*/
	public void guardarNomeFuncionario(String matricula) {

		try {
			conectar();
			String query = "SELECT nmFuncionario FROM funcionario_ponto where matricula = '" + matricula + "'";
			this.resultado = this.comando.executeQuery(query);

			while (this.resultado.next()) {
				nomeFuncionario = this.resultado.getString("nmFuncionario");
			}
		} catch (Exception e) {
			System.out.println("'guardarNomeFuncionario' Erro 0014: " + e.getMessage());
		}
	}
	
	public void demonstrativoMes(String matricula) {
		try {
			conectar();
			int x = 0;
			String[] arrayDemonstrativo = new String[300];

			String query = "select matricula,dataPonto,difHorarioMes,faltanteMes from relatorioMes where matricula = '"
					+ matricula + "';";
			this.resultado = this.comando.executeQuery(query);
			while (this.resultado.next()) {

				String resultado = " | Matricula: " + this.resultado.getString("matricula") + " |      |Data do Ponto: "
						+ this.resultado.getString("dataPonto") + " |      | Quantidade_Horas_Trabalhas_Mes: "
						+ this.resultado.getString("difHorarioMes") + " |      |Hora_Extra_Ou_Faltante_Mes: "
						+ this.resultado.getString("faltanteMes") + " |";

				arrayDemonstrativo[x] = resultado;
				x++;
			}
			for (String s : arrayDemonstrativo) {
				if (s != null) {
					resultArrayDemonstrativo = resultArrayDemonstrativo + "\n" + s;
				}
				// System.out.println(resultArrayPonto);

			}
			 JOptionPane.showMessageDialog(null,resultArrayDemonstrativo);
			 resultArrayDemonstrativo = "\n DEMOSTRATIVO MES \n";
		} catch (Exception e) {
			System.out.println("demonstrativoMes Erro: " + e.getMessage());
		}
	}
}
