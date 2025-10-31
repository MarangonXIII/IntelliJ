package common;

/**
 * Define as constantes para operações RPC
 */
public class Operacao {
    public static final int OPERACAO_SOMA = 1;
    public static final int OPERACAO_SUBTRACAO = 2;
    public static final int OPERACAO_MULTIPLICACAO = 3;
    public static final int OPERACAO_DIVISAO = 4;

    public static final int PORTA_SERVIDOR = 12345;
    public static final String HOST_SERVIDOR = "localhost";

    // Códigos de resposta
    public static final int SUCESSO = 200;
    public static final int ERRO = 500;
    public static final int OPERACAO_INVALIDA = 400;
}