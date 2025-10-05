import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteCalculadora {

    private static final String HOST = "localhost";
    private static final int PORTA = 1099;
    private static final String SERVICO = "CalculadoraService";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=".repeat(60));
        System.out.println("CLIENTE RMI - CALCULADORA REMOTA");
        System.out.println("=".repeat(60));
        System.out.println("Conectando em: " + HOST + ":" + PORTA);
        System.out.println("Serviço: " + SERVICO);
        System.out.println("-".repeat(60));

        try {
            // Localizar o RMI Registry
            System.out.print("Procurando servidor RMI...");
            Registry registry = LocateRegistry.getRegistry(HOST, PORTA);

            // Obter o stub remoto
            System.out.print("Obtendo stub remoto...");
            Calculadora calculadora = (Calculadora) registry.lookup(SERVICO);

            // Testar conexão
            System.out.print("Testando conexão...");
            String status = calculadora.getStatusServidor();
            System.out.println(status);

            System.out.println("-".repeat(60));
            System.out.println("CONEXÃO RMI ESTABELECIDA COM SUCESSO!");
            System.out.println("Agora você pode usar a calculadora remota");
            System.out.println("=".repeat(60));

            // Menu interativo
            executarMenu(calculadora, scanner);

        } catch (Exception e) {
            System.err.println("Erro ao conectar com servidor RMI: " + e.getMessage());
            System.err.println("Solução: Execute o servidor em outro terminal primeiro!");
            System.err.println("   Comando: java ServidorCalculadora");
        } finally {
            scanner.close();
            System.out.println("Cliente RMI encerrado.");
        }
    }

    private static void executarMenu(Calculadora calculadora, Scanner scanner) {
        boolean executando = true;

        while (executando) {
            System.out.println("\n" + "━".repeat(50));
            System.out.println("            CALCULADORA REMOTA RMI");
            System.out.println("━".repeat(50));
            System.out.println("1. Somar dois números");
            System.out.println("2. Subtrair dois números");
            System.out.println("3. Multiplicar dois números");
            System.out.println("4. Dividir dois números");
            System.out.println("5. Ver status do servidor");
            System.out.println("6. Sair");
            System.out.print("\n Escolha uma opção: ");

            try {
                int opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        executarOperacao(calculadora, scanner, "SOMA");
                        break;
                    case 2:
                        executarOperacao(calculadora, scanner, "SUBTRACAO");
                        break;
                    case 3:
                        executarOperacao(calculadora, scanner, "MULTIPLICACAO");
                        break;
                    case 4:
                        executarOperacao(calculadora, scanner, "DIVISAO");
                        break;
                    case 5:
                        verStatusServidor(calculadora);
                        break;
                    case 6:
                        executando = false;
                        System.out.println("Encerrando cliente...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Entrada inválida! Digite um número.");
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static void executarOperacao(Calculadora calculadora, Scanner scanner, String operacao) {
        try {
            System.out.println("\nDIGITE OS NÚMEROS PARA " + operacao + ":");
            System.out.print("Número 1: ");
            int a = scanner.nextInt();
            System.out.print("Número 2: ");
            int b = scanner.nextInt();

            System.out.println("\nEXECUTANDO CHAMADA RMI...");
            System.out.println("Esta operação está sendo processada REMOTAMENTE");
            System.out.println("RMI cuidando automaticamente da comunicação");

            long inicio = System.currentTimeMillis();

            switch (operacao) {
                case "SOMA":
                    int soma = calculadora.somar(a, b);
                    exibirResultado(a + " + " + b + " = " + soma, inicio);
                    break;
                case "SUBTRACAO":
                    int subtracao = calculadora.subtrair(a, b);
                    exibirResultado(a + " - " + b + " = " + subtracao, inicio);
                    break;
                case "MULTIPLICACAO":
                    int multiplicacao = calculadora.multiplicar(a, b);
                    exibirResultado(a + " × " + b + " = " + multiplicacao, inicio);
                    break;
                case "DIVISAO":
                    try {
                        double divisao = calculadora.dividir(a, b);
                        exibirResultado(a + " ÷ " + b + " = " + divisao, inicio);
                    } catch (Exception e) {
                        System.err.println("Erro na divisão: " + e.getMessage());
                    }
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erro na operação: " + e.getMessage());
            scanner.nextLine(); // Limpar buffer
        }
    }

    private static void verStatusServidor(Calculadora calculadora) {
        try {
            System.out.println("\nSOLICITANDO STATUS DO SERVIDOR...");
            String status = calculadora.getStatusServidor();
            System.out.println("STATUS DO SERVIDOR REMOTO:");
            System.out.println("   " + status);
        } catch (Exception e) {
            System.err.println("Erro ao obter status: " + e.getMessage());
        }
    }

    private static void exibirResultado(String resultado, long inicio) {
        long tempo = System.currentTimeMillis() - inicio;

        System.out.println("           RESULTADO DA OPERAÇÃO REMOTA");
        System.out.println("   " + resultado);
        System.out.println("   Tempo total da chamada RMI: " + tempo + "ms");
        System.out.println("   Executado remotamente no servidor RMI");

        System.out.println("\n TRANSFERÊNCIA RMI COMPLETA:");
        System.out.println("   • Cliente → Stub → Serialização → Rede");
        System.out.println("   • Servidor → Processamento → Resposta");
        System.out.println("   • Rede → Stub → Desserialização → Cliente");
    }
}