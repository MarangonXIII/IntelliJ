import java.io.*;
import java.net.*;

public class ClienteTCP_Temperatura {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 12345;

        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            try {
                porta = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Porta inválida. Usando padrão 12345.");
            }
        }

        try (Socket socket = new Socket(host, porta)) {
            System.out.println("Conectado ao servidor " + host + ":" + porta);

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String mensagemServidor;
            while ((mensagemServidor = in.readLine()) != null) {
                System.out.println(mensagemServidor);
                if (mensagemServidor.contains("Comandos disponíveis")) {
                    break;
                }
            }

            System.out.println("\nExemplos de uso:");
            System.out.println("• CONSULTAR São Paulo");
            System.out.println("• CADASTRAR Brasília 28°C Ensolarado");
            System.out.println("• LISTAR");
            System.out.println("• SAIR");
            System.out.println("\nDigite seus comandos abaixo:");

            String comando;
            while ((comando = teclado.readLine()) != null) {
                if (comando.equalsIgnoreCase("SAIR")) {
                    out.println("SAIR");
                    break;
                }

                out.println(comando);

                String resposta;
                while ((resposta = in.readLine()) != null) {
                    System.out.println(resposta);
                    if (!in.ready()) break;
                }

                System.out.print("\nPróximo comando: ");
            }

            System.out.println("Conexão encerrada.");

        } catch (ConnectException e) {
            System.err.println("Não foi possível conectar ao servidor " + host + ":" + porta);
            System.err.println("Verifique se o servidor está rodando.");
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + host);
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }
}