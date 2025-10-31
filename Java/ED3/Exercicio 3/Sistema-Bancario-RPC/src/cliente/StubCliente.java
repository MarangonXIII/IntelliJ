package cliente;

import java.io.*;
import java.net.*;
import common.Operacao;

public class StubCliente {
    private String host;
    private int porta;

    public StubCliente(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public StubCliente() {
        this(Operacao.HOST_SERVIDOR, Operacao.PORTA_SERVIDOR);
    }

    public double somar(double a, double b) throws IOException {
        return chamarOperacaoRemota(Operacao.OPERACAO_SOMA, a, b);
    }

    public double subtrair(double a, double b) throws IOException {
        return chamarOperacaoRemota(Operacao.OPERACAO_SUBTRACAO, a, b);
    }

    public double multiplicar(double a, double b) throws IOException {
        return chamarOperacaoRemota(Operacao.OPERACAO_MULTIPLICACAO, a, b);
    }

    public double dividir(double a, double b) throws IOException {
        return chamarOperacaoRemota(Operacao.OPERACAO_DIVISAO, a, b);
    }

    private double chamarOperacaoRemota(int operacao, double num1, double num2) throws IOException {
        System.out.println("[CLIENTE] Empacotando parâmetros para operação " + operacao + ": " + num1 + ", " + num2);
        String requisicao = operacao + ";" + num1 + ";" + num2;
        System.out.println("[CLIENTE] Requisição empacotada: " + requisicao);
        try (
                Socket socket = new Socket(host, porta);
                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            saida.println(requisicao);
            System.out.println("[CLIENTE] Requisição enviada ao servidor");
            String resposta = entrada.readLine();
            System.out.println("[CLIENTE] Resposta recebida: " + resposta);
            return processarResposta(resposta);
        } catch (ConnectException e) {
            throw new IOException("Não foi possível conectar ao servidor em " + host + ":" + porta, e);
        }
    }

    private double processarResposta(String resposta) throws IOException {
        if (resposta == null) {
            throw new IOException("Resposta vazia do servidor");
        }

        String[] partes = resposta.split(";");
        if (partes.length != 3) {
            throw new IOException("Formato de resposta inválido: " + resposta);
        }

        int codigo = Integer.parseInt(partes[0]);
        double resultado = Double.parseDouble(partes[1]);
        String mensagem = partes[2];

        if (codigo != Operacao.SUCESSO) {
            throw new IOException("Erro no servidor: " + mensagem);
        }

        System.out.println("[CLIENTE] Resultado desempacotado: " + resultado);
        return resultado;
    }
}