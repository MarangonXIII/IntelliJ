import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorUDP_Temperatura {
    private static Map<String, String[]> bancoDados = new HashMap<>();

    public static void main(String[] args) {
        inicializarBancoDados();

        try (DatagramSocket socket = new DatagramSocket(12345)) {
            System.out.println("Servidor UDP de Temperatura iniciado na porta 12345...");
            System.out.println("Cidades cadastradas: " + bancoDados.size());

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteRecebido);

                new Thread(() -> processarCliente(socket, pacoteRecebido)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static void inicializarBancoDados() {
        bancoDados.put("Belo Horizonte", new String[]{"27°C", "Ensolarado"});
        bancoDados.put("São Paulo", new String[]{"22°C", "Nublado"});
        bancoDados.put("Rio de Janeiro", new String[]{"30°C", "Ensolarado"});
        bancoDados.put("Ubá", new String[]{"25°C", "Chuvoso"});
        bancoDados.put("Curitiba", new String[]{"18°C", "Frio"});
        bancoDados.put("Salvador", new String[]{"32°C", "Quente"});
    }

    private static void processarCliente(DatagramSocket socket, DatagramPacket pacoteRecebido) {
        try {
            InetAddress enderecoCliente = pacoteRecebido.getAddress();
            int portaCliente = pacoteRecebido.getPort();

            String comando = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
            System.out.println("Comando recebido de " + enderecoCliente.getHostAddress() + ": " + comando);

            String resposta;
            if (comando.equalsIgnoreCase("SAIR")) {
                resposta = "Até logo!";
            } else if (comando.equalsIgnoreCase("INICIAR")) {
                resposta = "Bem-vindo ao Servidor de Temperaturas (UDP)!\n" +
                        "Comandos disponíveis: CONSULTAR <cidade>, CADASTRAR <cidade> <temp> <condicao>, LISTAR, SAIR\n" +
                        "Exemplos:\n" +
                        "• CONSULTAR São Paulo\n" +
                        "• CADASTRAR Brasília 28°C Ensolarado\n" +
                        "• LISTAR";
            } else {
                resposta = processarComando(comando);
            }

            byte[] dadosResposta = resposta.getBytes();
            DatagramPacket pacoteResposta = new DatagramPacket(
                    dadosResposta, dadosResposta.length, enderecoCliente, portaCliente);
            socket.send(pacoteResposta);

        } catch (IOException e) {
            System.out.println("Erro no processamento do cliente: " + e.getMessage());
        }
    }

    private static String processarComando(String comando) {
        String[] partes = comando.split(" ", 2);
        String acao = partes[0].toUpperCase();

        switch (acao) {
            case "CONSULTAR":
                if (partes.length < 2) return "Formato: CONSULTAR <cidade>";
                return consultarCidade(partes[1]);

            case "CADASTRAR":
                if (partes.length < 2) return "Formato: CADASTRAR <cidade> <temperatura> <condicao>";
                return cadastrarCidade(partes[1]);

            case "LISTAR":
                return listarCidades();

            default:
                return "Comando não reconhecido. Use: CONSULTAR, CADASTRAR, LISTAR ou SAIR";
        }
    }

    private static String consultarCidade(String cidade) {
        String[] dados = bancoDados.get(cidade);
        if (dados == null) {
            return "Cidade '" + cidade + "' não encontrada.";
        }
        return cidade + ": " + dados[0] + " - " + dados[1];
    }

    private static String cadastrarCidade(String parametros) {
        String[] partes = parametros.split(" ");
        if (partes.length < 3) {
            return "Formato: CADASTRAR <cidade> <temperatura> <condicao>";
        }

        String cidade = partes[0];
        String temperatura = partes[1];
        String condicao = partes[2];

        for (int i = 3; i < partes.length; i++) {
            condicao += " " + partes[i];
        }

        bancoDados.put(cidade, new String[]{temperatura, condicao});
        return "Cidade '" + cidade + "' cadastrada com sucesso!";
    }

    private static String listarCidades() {
        if (bancoDados.isEmpty()) {
            return "Nenhuma cidade cadastrada.";
        }

        StringBuilder lista = new StringBuilder("Cidades disponíveis (" + bancoDados.size() + "):\n");
        for (String cidade : bancoDados.keySet()) {
            String[] dados = bancoDados.get(cidade);
            lista.append("• ").append(cidade).append(": ").append(dados[0]).append(" - ").append(dados[1]).append("\n");
        }
        return lista.toString();
    }
}