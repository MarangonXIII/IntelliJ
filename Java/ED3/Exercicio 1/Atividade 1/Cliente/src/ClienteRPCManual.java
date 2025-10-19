import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ClienteRPCManual {
    public void executarTeste(String host, int porta) {
        try (Socket socket = new Socket(host, porta);
             OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream()) {

            System.out.println("Conectado ao servidor " + host + ":" + porta);

            int num1 = 25;
            int num2 = 17;

            System.out.println("\nChamando somar(" + num1 + ", " + num2 + ")");

            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(num1);
            buffer.putInt(num2);
            byte[] requisicao = buffer.array();
            System.out.println("Parâmetros empacotados manualmente: " + bytesParaHex(requisicao));

            os.write(requisicao);
            os.flush();
            System.out.println("Aguardando resposta (bloqueado)...");

            byte[] respostaBytes = new byte[4];
            int bytesLidos = 0;
            while (bytesLidos < 4) {
                int lido = is.read(respostaBytes, bytesLidos, 4 - bytesLidos);
                if (lido == -1) throw new IOException("Conexão fechada");
                bytesLidos += lido;
            }

            int resultado = ByteBuffer.wrap(respostaBytes).getInt();
            System.out.println("Resposta recebida: " + bytesParaHex(respostaBytes));
            System.out.println("Resultado desempacotado: " + resultado);
            System.out.println("RESULTADO: " + num1 + " + " + num2 + " = " + resultado);

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private String bytesParaHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        new ClienteRPCManual().executarTeste("localhost", 8081);
    }
}