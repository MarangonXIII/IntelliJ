package Cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import Servidor.InterfaceServidor;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PORTA = 1099;
    private static final String NOME_SERVICO = "ServicoCalculadora";
    private InterfaceServidor servidor;
    private Scanner scanner;

    public Cliente() {
        this.scanner = new Scanner(System.in);
    }

    public boolean conectar() {
        try {
            System.out.println("[CLIENTE] Conectando ao servidor RMI...");
            System.out.println("[CLIENTE] Host: " + HOST + ", Porta: " + PORTA);
            Registry registry = LocateRegistry.getRegistry(HOST, PORTA);
            System.out.println("[CLIENTE] Registry localizado");
            servidor = (InterfaceServidor) registry.lookup(NOME_SERVICO);
            System.out.println("[CLIENTE] Serviço encontrado: " + NOME_SERVICO);
            String status = servidor.getStatusServidor();
            System.out.println("[CLIENTE] Conexão estabelecida: " + status);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao conectar no servidor: " + e.getMessage());
            System.err.println("Verifique se o servidor está executando");
            return false;
        }
    }

    public void executar() {
        System.out.println("\n=== CLIENTE RMI - CALCULADORA REMOTA ===");
        if (!conectar()) {
            return;
        }
        try {
            while (true) {
                exibirMenu();
                int opcao = lerOpcao();
                if (opcao == 0) {
                    System.out.println("Encerrando cliente...");
                    break;
                }
                processarOpcao(opcao);
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Erro durante execução: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU DE OPERAÇÕES REMOTAS ---");
        System.out.println("1 - Somar dois números");
        System.out.println("2 - Somar vetor de números");
        System.out.println("3 - Converter texto para maiúsculas");
        System.out.println("4 - Ver status do servidor");
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

    private void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1:
                    somarDoisNumeros();
                    break;
                case 2:
                    somarVetor();
                    break;
                case 3:
                    converterMaiusculas();
                    break;
                case 4:
                    verStatusServidor();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.err.println("Erro na operação remota: " + e.getMessage());
        }
    }

    private void somarDoisNumeros() throws Exception {
        System.out.println("\n--- SOMA DE DOIS NÚMEROS ---");
        System.out.print("Digite o primeiro número: ");
        int a = Integer.parseInt(scanner.nextLine());

        System.out.print("Digite o segundo número: ");
        int b = Integer.parseInt(scanner.nextLine());
        System.out.println("[CLIENTE] Invocando método remoto: somar(" + a + ", " + b + ")");

        long inicio = System.currentTimeMillis();
        int resultado = servidor.somar(a, b);
        long fim = System.currentTimeMillis();

        System.out.println("RESULTADO REMOTO: " + a + " + " + b + " = " + resultado);
        System.out.println("Tempo da chamada remota: " + (fim - inicio) + "ms");
    }

    private void somarVetor() throws Exception {
        System.out.println("\n--- SOMA DE VETOR ---");
        System.out.print("Quantos números deseja somar? ");
        int tamanho = Integer.parseInt(scanner.nextLine());
        if (tamanho <= 0) {
            System.out.println("Tamanho inválido!");
            return;
        }
        int[] vetor = new int[tamanho];
        System.out.println("Digite os " + tamanho + " números:");
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            vetor[i] = Integer.parseInt(scanner.nextLine());
        }
        StringBuilder vetorStr = new StringBuilder("[");
        for (int i = 0; i < vetor.length; i++) {
            vetorStr.append(vetor[i]);
            if (i < vetor.length - 1) vetorStr.append(", ");
        }
        vetorStr.append("]");
        System.out.println("[CLIENTE] Invocando método remoto: somar(" + vetorStr + ")");

        long inicio = System.currentTimeMillis();
        int resultado = servidor.somar(vetor);
        long fim = System.currentTimeMillis();

        System.out.println("RESULTADO REMOTO: Soma do vetor = " + resultado);
        System.out.println("Tempo da chamada remota: " + (fim - inicio) + "ms");
    }

    private void converterMaiusculas() throws Exception {
        System.out.println("\n--- CONVERSÃO PARA MAIÚSCULAS ---");
        System.out.print("Digite o texto a ser convertido: ");
        String texto = scanner.nextLine();

        System.out.println("[CLIENTE] Invocando método remoto: upperCase(\"" + texto + "\")");

        long inicio = System.currentTimeMillis();
        String resultado = servidor.upperCase(texto);
        long fim = System.currentTimeMillis();

        System.out.println("RESULTADO REMOTO: \"" + texto + "\" → \"" + resultado + "\"");
        System.out.println("Tempo da chamada remota: " + (fim - inicio) + "ms");
    }

    private void verStatusServidor() throws Exception {
        System.out.println("\n--- STATUS DO SERVIDOR ---");
        System.out.println("[CLIENTE] Solicitando status do servidor...");

        long inicio = System.currentTimeMillis();
        String status = servidor.getStatusServidor();
        long fim = System.currentTimeMillis();

        System.out.println(status);
        System.out.println("Tempo da chamada remota: " + (fim - inicio) + "ms");
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.executar();
    }
}