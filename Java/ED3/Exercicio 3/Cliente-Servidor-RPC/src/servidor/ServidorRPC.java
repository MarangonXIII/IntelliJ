package servidor;

import java.io.*;
import java.net.*;
import common.Operacao;

public class ServidorRPC {
    private ServerSocket serverSocket;
    private boolean executando;

    public ServidorRPC() {
        this.executando = false;
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(Operacao.PORTA_SERVIDOR);
            executando = true;
            System.out.println("=== SERVIDOR RPC INICIADO ===");
            System.out.println("Aguardando conexões na porta " + Operacao.PORTA_SERVIDOR);
            System.out.println("Operações disponíveis para uso");
            System.out.println("=============================");
            while (executando) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("\n[SERVIDOR] Cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());
                new Thread(new TratadorCliente(clienteSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            parar();
        }
    }

    public void parar() {
        executando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar servidor: " + e.getMessage());
        }
        System.out.println("Servidor finalizado.");
    }

    private static class TratadorCliente implements Runnable {
        private Socket clienteSocket;
        private StubServidor stub;

        public TratadorCliente(Socket socket) {
            this.clienteSocket = socket;
            this.stub = new StubServidor();
        }

        @Override
        public void run() {
            try (
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream())); PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)
            ) {
                String requisicao = entrada.readLine();
                if (requisicao != null) {
                    String resposta = stub.processarRequisicao(requisicao);
                    saida.println(resposta);
                    System.out.println("[SERVIDOR] Resposta enviada: " + resposta);
                }
            } catch (IOException e) {
                System.err.println("Erro ao processar cliente: " + e.getMessage());
            } finally {
                try {
                    clienteSocket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar socket do cliente: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        ServidorRPC servidor = new ServidorRPC();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {System.out.println("\nFinalizando servidor...");servidor.parar();}));
        servidor.iniciar();
    }
}