import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ProductCatalogServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(5000);
            System.out.println("\nRegistry RMI criado na porta 5000");
            ProductCatalogService catalogService = new ProductCatalogServiceImpl();
            registry.rebind("ProductCatalogService", catalogService);

            System.out.println("\n" + "=" .repeat(50));
            System.out.println("\nSERVIDOR DE CATÁLOGO INICIADO\n");
            System.out.println("=" .repeat(50));
            System.out.println("\nEndereço: localhost:5000");
            System.out.println("Serviço: ProductCatalogService");
            System.out.println("Stub: Criado automaticamente pelo UnicastRemoteObject");
            System.out.println("Status: Aguardando conexões...\n");
            System.out.println("=" .repeat(50));
        } catch (Exception e) {
            System.err.println("\nErro no servidor: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}