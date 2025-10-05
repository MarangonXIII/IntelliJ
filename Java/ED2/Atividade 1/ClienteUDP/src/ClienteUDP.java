import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClienteUDP {
    private static final String GRUPO_MULTICAST = "230.0.0.1";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket(PORTA);
            InetAddress grupo = InetAddress.getByName(GRUPO_MULTICAST);

            socket.joinGroup(grupo);
            System.out.println("Conectado ao grupo multicast " + GRUPO_MULTICAST + ":" + PORTA);

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Digite seu apelido: ");
            String apelido = teclado.readLine();
            System.out.println("Digite /sair para encerrar.");

            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (true) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                        if (!msg.startsWith(apelido + ": ")) {
                            System.out.println(msg);
                        }
                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            System.out.println("Erro ao receber mensagem: " + e.getMessage());
                        }
                        break;
                    }
                }
            }).start();

            String entradaMsg = "*** " + apelido + " entrou no chat ***";
            byte[] entradaData = entradaMsg.getBytes(StandardCharsets.UTF_8);
            DatagramPacket entradaPacket = new DatagramPacket(
                    entradaData, entradaData.length, grupo, PORTA);
            socket.send(entradaPacket);

            String linha;
            while ((linha = teclado.readLine()) != null) {
                if (linha.equalsIgnoreCase("/sair")) {
                    // Enviar mensagem de saída
                    String saidaMsg = "*** " + apelido + " saiu do chat ***";
                    byte[] saidaData = saidaMsg.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket saidaPacket = new DatagramPacket(
                            saidaData, saidaData.length, grupo, PORTA);
                    socket.send(saidaPacket);

                    System.out.println("Saindo...");
                    break;
                }

                String msg = apelido + ": " + linha;
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, grupo, PORTA);
                socket.send(packet);

                System.out.println(msg);
            }

            socket.leaveGroup(grupo);
            socket.close();

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}