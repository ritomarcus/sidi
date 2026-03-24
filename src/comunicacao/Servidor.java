package comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Servidor {
	public static void main(String[] args) {
		try {
			ServerSocket ssServer = new ServerSocket(8080);

			System.out.println("Aguardando conexao...");
			// aguardar a conexão de um cliente
			Socket sIncoming = ssServer.accept();
			System.out.println("Chegou alguem...");

			// preparar para receber dados do cliente
			InputStream inStream = sIncoming.getInputStream();
			Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);

			// preparar para enviar dados para o cliente
			OutputStream outStream = sIncoming.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true, StandardCharsets.UTF_8);

			// loop de comunicação
			boolean fim = false;

			// enquanto não for fim e tiver uma linha
			// vou enviar ou receber

			while (!fim && in.hasNextLine()) {
				// consumir os dados do cliente
				String linha = in.nextLine();
				System.out.println("Cliente: " + linha);

				// fim da comunicação
				if (linha.equalsIgnoreCase("TCHAU")) {
					fim = true;
					out.println("Foi bom falar contigo.");
				}
				// enviar uma mensagem para o cliente
				out.println(requisicao(linha));
			}

			// fechar conexão
			sIncoming.close();

			ssServer.close();
			in.close();

		} catch (IOException ex) {
			System.err.println("Não foi possível iniciar o servidor.");
		}
	}

	// Verifica qual serviço deve ser executado pelo servidor 1
	// para calculadora e 2 para busca de palavra dentro da frase
	static String requisicao(String entrada) {
		String[] partes = processarEntrada(entrada);

		if (partes[0].equals("1")) {
			return String.valueOf(calculadora(partes[1]));
		} else if (partes[0].equals("2")) {
			String[] partesSemTrim = entrada.split(";");
			return String.valueOf(buscarPalavra(partesSemTrim[1], partesSemTrim[2]));
		}
		return "ok";
	}

	// Processa a entrada removendo todos os espaços vazios e separando os
	// identificadores de serviços 1 ou 2
	static String[] processarEntrada(String entrada) {
		String semEspaco = entrada.replace(" ", "");
		return semEspaco.split(";");
	}

	// Faz as operações solicitadas pelo cliente
	static double calculadora(String entrada) {
		String expressao[] = entrada.split("[+\\-*/%]");

		int indice = -1;

		if (entrada.indexOf('+') != -1) {
			indice = entrada.indexOf('+');
		} else if (entrada.indexOf('-') != -1) {
			indice = entrada.indexOf('-');
		} else if (entrada.indexOf('*') != -1) {
			indice = entrada.indexOf('*');
		} else if (entrada.indexOf('/') != -1) {
			indice = entrada.indexOf('/');
		} else if (entrada.indexOf('%') != -1) {
			indice = entrada.indexOf('%');
		}

		char operador = entrada.charAt(indice);

		double n1 = Double.parseDouble(expressao[0]);
		double n2 = Double.parseDouble(expressao[1]);

		switch (operador) {
			case '+':
				return n1 + n2;
			case '-':
				return n1 - n2;
			case '*':
				return n1 * n2;
			case '/':
				return n1 / n2;
			case '%':
				return n1 % n2;
			default:
				return 0;
		}
	}

	static String buscarPalavra(String frase, String palavra) {
		String[] palavras = frase.trim().split(" ");
		for (int i = 0; i < palavras.length; i++) {
			if (palavras[i].equals(palavra.trim())) {
				return String.valueOf(i + 1);
			}
		}
		return "A frase não contém está palavra";
	}
}
