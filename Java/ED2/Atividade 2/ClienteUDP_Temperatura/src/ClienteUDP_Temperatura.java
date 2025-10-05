import java.io.*;
import java.net.*;

public class ClienteUDP_Temperatura {
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

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            InetAddress enderecoServidor = InetAddress.getByName(host);

            System.out.println("Cliente UDP de Temperatura iniciado");
            System.out.println("Conectando ao servidor " + host + ":" + porta);

            enviarComando(socket, enderecoServidor, porta, "INICIAR");
            String resposta = receberResposta(socket);
            System.out.println(resposta);

            System.out.print("\nDigite seus comandos abaixo (ou 'SAIR' para encerrar):\n> ");

            String comando;
            while ((comando = teclado.readLine()) != null) {
                if (comando.equalsIgnoreCase("SAIR")) {
                    enviarComando(socket, enderecoServidor, porta, "SAIR");
                    String respostaFinal = receberResposta(socket);
                    System.out.println(respostaFinal);
                    break;
                }

                enviarComando(socket, enderecoServidor, porta, comando);
                resposta = receberResposta(socket);
                System.out.println(resposta);
                System.out.print("\n> ");
            }

            System.out.println("Conexão encerrada.");

        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + host);
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }

    private static void enviarComando(DatagramSocket socket, InetAddress servidor, int porta, String comando)
            throws IOException {
        byte[] dadosEnvio = comando.getBytes();
        DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, servidor, porta);
        socket.send(pacoteEnvio);
    }

    private static String receberResposta(DatagramSocket socket) throws IOException {
        byte[] bufferResposta = new byte[2048];
        DatagramPacket pacoteResposta = new DatagramPacket(bufferResposta, bufferResposta.length);
        socket.setSoTimeout(10000);
        socket.receive(pacoteResposta);
        return new String(pacoteResposta.getData(), 0, pacoteResposta.getLength());
    }
}