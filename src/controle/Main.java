package controle;

import banco.BancoDeDados;
import controle.Login;

public class Main {

	public static void main(String[] args) {
		Login telaLogin = new Login();
		telaLogin.exibirJanela();

        BancoDeDados bancoDeDados = new BancoDeDados();

		bancoDeDados.conectar();
			
			if (bancoDeDados.estaConectado()){
				System.out.println("Est� conectado ao banco");
			}
			else
			{
				System.out.println("N�o foi possivel conectar");
			}						

	  }

}
