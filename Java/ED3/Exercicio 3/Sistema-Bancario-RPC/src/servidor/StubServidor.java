package servidor;

import java.io.*;
import java.net.*;
import common.Operacao;

public class StubServidor {

    public String processarRequisicao(String requisicao) {
        System.out.println("[SERVIDOR] Recebendo requisição: " + requisicao);

        try {
            String[] partes = requisicao.split(";");

            if (partes.length != 3) {
                return criarResposta(Operacao.ERRO, 0, "Formato de mensagem inválido");
            }

            int operacao = Integer.parseInt(partes[0]);
            double num1 = Double.parseDouble(partes[1]);
            double num2 = Double.parseDouble(partes[2]);
            double resultado = executarOperacao(operacao, num1, num2);
            System.out.println("[SERVIDOR] Operação executada: " + num1 + " op " + operacao + " " + num2 + " = " + resultado);
            return criarResposta(Operacao.SUCESSO, resultado, "Operação realizada com sucesso");

        } catch (NumberFormatException e) {
            return criarResposta(Operacao.ERRO, 0, "Erro ao converter números: " + e.getMessage());
        } catch (ArithmeticException e) {
            return criarResposta(Operacao.ERRO, 0, "Erro aritmético: " + e.getMessage());
        } catch (Exception e) {
            return criarResposta(Operacao.ERRO, 0, "Erro inesperado: " + e.getMessage());
        }
    }

    private double executarOperacao(int operacao, double num1, double num2) {
        switch (operacao) {
            case Operacao.OPERACAO_SOMA:
                return num1 + num2;

            case Operacao.OPERACAO_SUBTRACAO:
                return num1 - num2;

            case Operacao.OPERACAO_MULTIPLICACAO:
                return num1 * num2;

            case Operacao.OPERACAO_DIVISAO:
                if (num2 == 0) {
                    throw new ArithmeticException("Divisão por zero");
                }
                return num1 / num2;

            default:
                throw new IllegalArgumentException("Operação inválida: " + operacao);
        }
    }

    private String criarResposta(int codigo, double resultado, String mensagem) {
        return codigo + ";" + resultado + ";" + mensagem;
    }
}