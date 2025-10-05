import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorTCP {
    private static final Map<PrintWriter, String> clientes = new HashMap<>();

    public static void main(String[] args) {
        int porta = 12345;
        if (args.length > 0) {
            try {
                porta = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor TCP iniciado na porta " + porta);
            System.out.println("Aguardando conexões de clientes...");

            while (true) {
                Socket socket = serverSocket.accept();
                String enderecoCliente = socket.getInetAddress().getHostAddress();
                System.out.println("Nova conexão recebida de: " + enderecoCliente);

                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static class ClienteHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String apelido;

        ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                apelido = in.readLine();
                if (apelido == null || apelido.trim().isEmpty()) {
                    apelido = "Cliente-" + socket.getPort();
                }

                synchronized (clientes) {
                    clientes.put(out, apelido);
                }

                System.out.println(apelido + " conectou-se");
                broadcast("*** " + apelido + " entrou no chat ***");

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (mensagem.equalsIgnoreCase("/sair")) {
                        break;
                    }
                    System.out.println("[" + apelido + "]: " + mensagem);
                    broadcast(apelido + ": " + mensagem);
                }

            } catch (IOException e) {
                System.out.println("Erro de comunicação com " + apelido);
            } finally {
                if (apelido != null) {
                    System.out.println(apelido + " desconectou-se");
                    broadcast("*** " + apelido + " saiu do chat ***");
                }
                try { socket.close(); } catch (IOException ignored) {}
                synchronized (clientes) { clientes.remove(out); }
            }
        }

        private void broadcast(String mensagem) {
            synchronized (clientes) {
                for (PrintWriter cliente : clientes.keySet()) {
                    cliente.println(mensagem);
                }
            }
        }
    }
}