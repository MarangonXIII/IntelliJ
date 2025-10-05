import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorRPCManual {
    private ServerSocket serverSocket;

    public void iniciarServidor(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
        System.out.println("Servidor RPC Manual iniciado na porta " + porta);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("\nCliente conectado: " + socket.getInetAddress());

            new Thread(() -> processarRequisicao(socket)).start();
        }
    }

    private void processarRequisicao(Socket socket) {
        try (InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream()) {

            byte[] buffer = new byte[8];
            int bytesLidos = 0;
            while (bytesLidos < 8) {
                int lido = is.read(buffer, bytesLidos, 8 - bytesLidos);
                if (lido == -1) throw new IOException("Conexão fechada prematuramente");
                bytesLidos += lido;
            }

            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            int num1 = byteBuffer.getInt();
            int num2 = byteBuffer.getInt();

            System.out.println("Parâmetros desempacotados manualmente: " + num1 + ", " + num2);

            int resultado = num1 + num2;
            System.out.println("Soma: " + num1 + " + " + num2 + " = " + resultado);
            byte[] resposta = ByteBuffer.allocate(4).putInt(resultado).array();
            os.write(resposta);
            os.flush();

            System.out.println("Resposta enviada (empacotamento manual)");

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                System.err.println("Erro ao fechar socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ServidorRPCManual().iniciarServidor(8081);
        } catch (IOException e) {
            System.err.println("Falha ao iniciar servidor: " + e.getMessage());
        }
    }
}