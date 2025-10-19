import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class ProductCatalogClient {

    public static void main(String[] args) {
        try {
            System.out.println("\nCLIENTE - CATÁLOGO DE PRODUTOS\n");
            System.out.println("=" .repeat(40));
            System.out.println("\nConectando ao registry...");
            Registry registry = LocateRegistry.getRegistry("localhost", 5000);
            ProductCatalogService catalogService = (ProductCatalogService) registry.lookup("ProductCatalogService");
            System.out.println("Conectado ao serviço remoto!\n");
            testFindProductByName(catalogService);
            testGetAllProducts(catalogService);
            testCategoryStatistics(catalogService);
            testHealthCheck(catalogService);
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testFindProductByName(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 1 - BUSCAR PRODUTO POR NOME");
        System.out.println("-".repeat(30));
        Product laptop = catalogService.findProductByName("Laptop Gamer");
        if (laptop != null) {
            System.out.println("Produto encontrado:");
            System.out.println("   Nome: " + laptop.getName());
            System.out.println("   Preço: R$ " + laptop.getPrice());
            System.out.println("   Estoque: " + laptop.getStock() + " unidades");
            System.out.println("   Categoria: " + laptop.getCategory());
        } else {
            System.out.println("Produto não encontrado");
        }
        System.out.println();
    }

    private static void testGetAllProducts(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 2 - LISTAR TODOS OS PRODUTOS");
        System.out.println("-".repeat(30));
        List<Product> allProducts = catalogService.getAllProducts();
        System.out.println("Total de produtos: " + allProducts.size());
        System.out.println("\nLista de produtos:");
        for (Product product : allProducts) {
            System.out.println("   - " + product.getName() + " (R$ " + product.getPrice() + ", Estoque: " + product.getStock() + ")");
        }
        System.out.println();
    }

    private static void testCategoryStatistics(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 3 - ESTATÍSTICAS POR CATEGORIA");
        System.out.println("-".repeat(30));
        Map<String, Integer> stats = catalogService.getCategoryStatistics();
        System.out.println("Estatísticas por categoria:");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println("   - " + entry.getKey() + ": " + entry.getValue() + " produtos");
        }
        System.out.println();
    }

    private static void testHealthCheck(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 4 - VERIFICAÇÃO DE SAÚDE");
        System.out.println("-".repeat(30));
        boolean healthy = catalogService.healthCheck();
        System.out.println("Saúde do serviço: " + (healthy ? "SAUDÁVEL" : "PROBLEMAS"));
        System.out.println();
    }
}