import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ProductCatalogServer {
    public static void main(String[] args) {
        try {
            System.out.println("\n\nIniciando servidor RMI...");
            Registry registry = LocateRegistry.createRegistry(5000);
            System.out.println("Registry RMI criado na porta 5000");
            ProductCatalogService catalogService = new ProductCatalogServiceImpl();

            System.out.println("Serviço de catálogo instanciado");
            registry.rebind("ProductCatalogService", catalogService);
            System.out.println("Serviço registrado como 'ProductCatalogService'");
            System.out.println("\n" + "=" .repeat(60));
            System.out.println("SERVIDOR RMI INICIADO COM SUCESSO!");
            System.out.println("=" .repeat(60) + "\n");
            System.out.println("Endereço: localhost:5000");
            System.out.println("Serviço: ProductCatalogService");
            System.out.println("Status: Aguardando conexões de clientes...");
            System.out.println("\n" + "=" .repeat(60));
            System.out.println("\nNÃO FECHE ESTE TERMINAL!");
            System.out.println("O servidor deve permanecer executando para clientes se conectarem");

            Object keepAlive = new Object();
            synchronized (keepAlive) {
                keepAlive.wait();
            }

        } catch (Exception e) {
            System.err.println("Erro crítico no servidor: " + e.getMessage());
            e.printStackTrace();
            System.out.println("\nDica: Verifique se a porta 5000 não está sendo usada por outro processo");
        }
    }
}