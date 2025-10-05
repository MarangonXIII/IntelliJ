import java.io.*;
import java.net.*;

public class ClienteTCP {
    public static void main(String[] args) {
        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            String host;
            if (args.length > 0) {
                host = args[0];
            } else {
                System.out.print("Digite o IP do servidor (Enter para localhost): ");
                host = teclado.readLine();
                if (host.trim().isEmpty()) {
                    host = "localhost";
                }
            }

            int porta = 12345;
            if (args.length > 1) {
                try {
                    porta = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {}
            }

            Socket socket = new Socket(host, porta);
            System.out.println("Conectado ao servidor " + host + ":" + porta);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread threadRecebimento = new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = in.readLine()) != null) {
                        System.out.println(resposta);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com o servidor foi encerrada.");
                }
            });
            threadRecebimento.start();

            System.out.print("Digite seu apelido: ");
            String apelido = teclado.readLine();
            out.println(apelido);

            System.out.println("Digite mensagens para enviar. Use /sair para encerrar.");

            String mensagem;
            while ((mensagem = teclado.readLine()) != null) {
                if (mensagem.equalsIgnoreCase("/sair")) {
                    out.println("/sair");
                    System.out.println("Encerrando conexão...");
                    break;
                }
                out.println(mensagem);
            }

            socket.close();
            System.out.println("Conexão encerrada. Até logo!");

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}