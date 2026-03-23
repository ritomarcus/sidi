package comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		try {
			Socket sClient = new Socket("localhost", 8080);

			// preparar para receber dados do servidor
			InputStream inStream = sClient.getInputStream();
			Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);

			// preparar para enviar dados para o servidor
			OutputStream outStream = sClient.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true, StandardCharsets.UTF_8);

			// loop de comunicacao
			Scanner sc = new Scanner(System.in);
			boolean fim = false;

			while (!fim) {
				System.out.print("Digite: ");
				String mensagem = sc.nextLine();
				// enviar para o servidor
				out.println(mensagem);

				if (in.hasNextLine()) {
					String linha = in.nextLine();
					System.out.println("Servidor: " + linha);
					fim = linha.equalsIgnoreCase("Foi bom falar contigo.");
				}
			}

			// fechar conexão
			sClient.close();

			in.close();
			sc.close();
		} catch (IOException ex) {
			System.err.println("Não foi possível conectar no servidor");
		}
	}
}
