import java.io.*;
import java.net.*;

public class ClienteUDP2_Temperatura {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 12346;

        if (args.length > 0) {
            host = args[0];
        }

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            InetAddress enderecoServidor = InetAddress.getByName(host);

            System.out.println("Cliente UDP conectado ao servidor " + host + ":" + porta);
            System.out.println("Comandos: CONSULTAR <cidade>, LISTAR");
            System.out.println("Digite SAIR para encerrar.\n");

            String comando;
            while ((comando = teclado.readLine()) != null) {
                if (comando.equalsIgnoreCase("SAIR")) {
                    break;
                }

                byte[] dadosEnvio = comando.getBytes();
                DatagramPacket pacoteEnvio = new DatagramPacket(
                        dadosEnvio, dadosEnvio.length, enderecoServidor, porta
                );
                socket.send(pacoteEnvio);

                byte[] buffer = new byte[1024];
                DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
                socket.setSoTimeout(5000);

                try {
                    socket.receive(pacoteResposta);
                    String resposta = new String(pacoteResposta.getData(), 0, pacoteResposta.getLength());
                    System.out.println("Resposta: " + resposta);
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout: Servidor não respondeu.");
                }

                System.out.print("\nPróximo comando: ");
            }

            System.out.println("Cliente encerrado.");

        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + host);
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }
}