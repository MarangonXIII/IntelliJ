package cliente;

import java.io.IOException;
import java.util.Scanner;

public class ClienteRPC {
    private StubCliente stub;
    private Scanner scanner;

    public ClienteRPC() {
        this.stub = new StubCliente();
        this.scanner = new Scanner(System.in);
    }

    public void executar() {
        System.out.println("=== CLIENTE RPC ===");
        System.out.println("Conectando ao servidor...");
        try {
            while (true) {
                exibirMenu();
                int opcao = lerOpcao();
                if (opcao == 0) {
                    System.out.println("Encerrando cliente...");
                    break;
                }
                if (opcao >= 1 && opcao <= 4) {
                    processarOperacao(opcao);
                } else {
                    System.out.println("Opção inválida!");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU DE OPERAÇÕES ---");
        System.out.println("1 - Somar dois números");
        System.out.println("2 - Subtrair dois números");
        System.out.println("3 - Multiplicar dois números");
        System.out.println("4 - Dividir dois números");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void processarOperacao(int operacao) {
        try {
            System.out.print("Digite o primeiro número: ");
            double num1 = Double.parseDouble(scanner.nextLine());
            System.out.print("Digite o segundo número: ");
            double num2 = Double.parseDouble(scanner.nextLine());
            System.out.println("\n--- EXECUTANDO OPERAÇÃO REMOTA ---");
            double resultado;
            long inicio = System.currentTimeMillis();
            switch (operacao) {
                case 1:
                    resultado = stub.somar(num1, num2);
                    System.out.println("SOMA REMOTA: " + num1 + " + " + num2 + " = " + resultado);
                    break;
                case 2:
                    resultado = stub.subtrair(num1, num2);
                    System.out.println("SUBTRAÇÃO REMOTA: " + num1 + " - " + num2 + " = " + resultado);
                    break;
                case 3:
                    resultado = stub.multiplicar(num1, num2);
                    System.out.println("MULTIPLICAÇÃO REMOTA: " + num1 + " * " + num2 + " = " + resultado);
                    break;
                case 4:
                    resultado = stub.dividir(num1, num2);
                    System.out.println("DIVISÃO REMOTA: " + num1 + " / " + num2 + " = " + resultado);
                    break;
                default:
                    return;
            }
            long fim = System.currentTimeMillis();
            System.out.println("Tempo da operação remota: " + (fim - inicio) + "ms");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite números válidos!");
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClienteRPC cliente = new ClienteRPC();
        cliente.executar();
    }
}