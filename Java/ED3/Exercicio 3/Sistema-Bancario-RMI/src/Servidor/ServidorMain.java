package Servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class ServidorMain {

    private static final int PORTA_RMI = 1099;
    private static final String NOME_SERVICO = "ServicoCalculadora";

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SERVIDOR RMI ===");

        try {
            Registry registry = LocateRegistry.createRegistry(PORTA_RMI);
            System.out.println("[SERVIDOR] RMI Registry criado na porta " + PORTA_RMI);
            Servidor servidor = new Servidor();
            System.out.println("[SERVIDOR] Instância do servidor criada");
            registry.rebind(NOME_SERVICO, servidor);
            System.out.println("[SERVIDOR] Serviço registrado como: " + NOME_SERVICO);
            System.out.println("\nSERVIDOR RMI INICIADO COM SUCESSO!");
            System.out.println("Endereço: localhost:" + PORTA_RMI + "/" + NOME_SERVICO);
            System.out.println("Aguardando chamadas remotas...");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (RemoteException e) {
            System.err.println("Erro RMI no servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro geral no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}