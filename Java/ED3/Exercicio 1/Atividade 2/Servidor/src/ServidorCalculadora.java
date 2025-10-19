import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class ServidorCalculadora implements Calculadora {

    private final Date dataInicio;

    public ServidorCalculadora() throws RemoteException {
        super();
        this.dataInicio = new Date();
    }

    @Override
    public int somar(int a, int b) throws RemoteException {
        System.out.println("[" + new Date() + "] Chamada remota: somar(" + a + ", " + b + ")");
        int resultado = a + b;
        System.out.println("[" + new Date() + "] Resultado: " + resultado);
        return resultado;
    }

    @Override
    public int subtrair(int a, int b) throws RemoteException {
        System.out.println("[" + new Date() + "] Chamada remota: subtrair(" + a + ", " + b + ")");
        int resultado = a - b;
        System.out.println("[" + new Date() + "] Resultado: " + resultado);
        return resultado;
    }

    @Override
    public int multiplicar(int a, int b) throws RemoteException {
        System.out.println("[" + new Date() + "] Chamada remota: multiplicar(" + a + ", " + b + ")");
        int resultado = a * b;
        System.out.println("[" + new Date() + "] Resultado: " + resultado);
        return resultado;
    }

    @Override
    public double dividir(int a, int b) throws RemoteException {
        System.out.println("[" + new Date() + "] Chamada remota: dividir(" + a + ", " + b + ")");
        if (b == 0) {
            System.err.println("[" + new Date() + "] Erro: Divisão por zero!");
            throw new RemoteException("Divisão por zero não é permitida");
        }
        double resultado = (double) a / b;
        System.out.println("[" + new Date() + "] Resultado: " + resultado);
        return resultado;
    }

    @Override
    public String getStatusServidor() throws RemoteException {
        String status = "Servidor RMI Ativo - Iniciado em: " + dataInicio;
        System.out.println("[" + new Date() + "] Status solicitado pelo cliente");
        return status;
    }

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int porta = 1099;
            String nomeServico = "CalculadoraService";

            System.out.println("=".repeat(60));
            System.out.println("SERVIDOR RMI - CALCULADORA REMOTA");
            System.out.println("=".repeat(60));
            System.out.println("Host: " + host);
            System.out.println("Porta: " + porta);
            System.out.println("Serviço: " + nomeServico);
            System.out.println("Iniciado em: " + new Date());
            System.out.println("-".repeat(60));

            ServidorCalculadora servidor = new ServidorCalculadora();
            System.out.println("Objeto servidor instanciado");

            Calculadora stub = (Calculadora) UnicastRemoteObject.exportObject(servidor, 0);
            System.out.println("Stub exportado na porta 0 (automática)");

            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(porta);
                System.out.println("RMI Registry criado na porta " + porta);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(porta);
                System.out.println("Conectado ao RMI Registry existente na porta " + porta);
            }

            registry.rebind(nomeServico, stub);
            System.out.println("Serviço registrado como: '" + nomeServico + "'");

            System.out.println("-".repeat(60));
            System.out.println("SERVIDOR RMI PRONTO PARA CONEXÕES!");
            System.out.println("Aguardando chamadas remotas de clientes...");
            System.out.println("=".repeat(60));

            while (true) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.err.println("Erro no servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}