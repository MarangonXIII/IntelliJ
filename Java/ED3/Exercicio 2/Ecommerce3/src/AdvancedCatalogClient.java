import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.*;

public class AdvancedCatalogClient {
    public static void main(String[] args) {
        try {
            clearConsole();
            System.out.println("CLIENTE AVANÇADO - TESTES DE EXPANSÃO");
            System.out.println("=" .repeat(50));
            Registry registry = LocateRegistry.getRegistry("localhost", 5000);
            ProductCatalogService catalogService =
                    (ProductCatalogService) registry.lookup("ProductCatalogService");
            System.out.println("Conectado ao serviço expandido!\n");
            testNewFeatures(catalogService);
            testSequentialCalls(catalogService);
            testConcurrentCalls(catalogService);
            testBackwardCompatibility(catalogService);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testNewFeatures(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 1 - NOVOS RECURSOS DO SISTEMA");
        System.out.println("-".repeat(40));

        System.out.println("1. Buscando produtos com 'wireless':");
        List<Product> wirelessProducts = catalogService.searchProducts("wireless");
        System.out.println("   Encontrados: " + wirelessProducts.size() + " produtos");
        wirelessProducts.forEach(p -> System.out.println("      - " + p.getName() + " (R$ " + p.getPrice() + ")"));

        System.out.println("\n2. Produtos com alto estoque (promoção):");
        List<Product> saleProducts = catalogService.getProductsOnSale();
        System.out.println("   " + saleProducts.size() + " produtos disponíveis");
        saleProducts.forEach(p -> System.out.println("      - " + p.getName() + " (Estoque: " + p.getStock() + ")"));

        System.out.println("\n3. Aplicando desconto em 'Periféricos':");
        boolean discountApplied = catalogService.applyDiscount("Periféricos", 15.0);
        System.out.println("   Desconto aplicado: " + discountApplied);

        System.out.println("\n4. Estatísticas de preços avançadas:");
        Map<String, Double> priceStats = catalogService.getPriceStatistics();
        System.out.println("   Métricas calculadas:");
        priceStats.forEach((key, value) -> System.out.println("      - " + key + ": " + (key.contains("price") ? "R$ " + String.format("%.2f", value) : String.format("%.2f", value))));
        System.out.println();
    }

    private static void testSequentialCalls(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 2 - MÚLTIPLAS CHAMADAS SEQUENCIAIS");
        System.out.println("-".repeat(45));
        System.out.println("Executando 10 chamadas sequenciais...");
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10; i++) {
            System.out.print("   " + i + ". ");
            switch (i % 4) {
                case 0:
                    List<Product> products = catalogService.getAllProducts();
                    System.out.println("Listagem: " + products.size() + " produtos");
                    break;
                case 1:
                    Product product = catalogService.findProductByName("Smartphone");
                    System.out.println("Busca: " + (product != null ? product.getName() : "Não encontrado"));
                    break;
                case 2:
                    Map<String, Integer> stats = catalogService.getCategoryStatistics();
                    System.out.println("Estatísticas: " + stats.size() + " categorias");
                    break;
                case 3:
                    List<Product> search = catalogService.searchProducts("gamer");
                    System.out.println("Busca 'gamer': " + search.size() + " resultados");
                    break;
            }
            Thread.sleep(100);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("10 chamadas concluídas em " + (endTime - startTime) + "ms\n");
    }

    private static void testConcurrentCalls(ProductCatalogService catalogService) {
        System.out.println("TESTE 3 - CHAMADAS CONCORRENTES");
        System.out.println("-".repeat(35));

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<String>> futures = new ArrayList<>();

        System.out.println("Iniciando 5 clientes simultâneos...");

        for (int i = 1; i <= 5; i++) {
            final int clientId = i;
            Future<String> future = executor.submit(() -> {
                try {
                    String clientName = "Cliente-" + clientId;
                    Random random = new Random();

                    switch (clientId % 3) {
                        case 0:
                            List<Product> results = catalogService.searchProducts("sem fio");
                            return clientName + " → Buscou 'sem fio': " + results.size() + " resultados";
                        case 1:
                            List<Product> allProducts = catalogService.getAllProducts();
                            return clientName + " → Listou: " + allProducts.size() + " produtos";
                        case 2:
                            Map<String, Double> stats = catalogService.getPriceStatistics();
                            return clientName + " → Estatísticas: " + stats.size() + " métricas";
                    }
                    return clientName + " → Operação concluída";
                } catch (Exception e) {
                    return "Cliente-" + clientId + " → Erro: " + e.getMessage();
                }
            });
            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                String result = future.get(10, TimeUnit.SECONDS);
                System.out.println("   " + result);
            } catch (Exception e) {
                System.out.println("   Timeout ou erro: " + e.getMessage());
            }
        }
        executor.shutdown();
        System.out.println("Todos os clientes concorrentes finalizados\n");
    }

    private static void testBackwardCompatibility(ProductCatalogService catalogService) throws Exception {
        System.out.println("TESTE 4 - COMPATIBILIDADE COM CLIENTES ANTIGOS");
        System.out.println("-".repeat(50));

        System.out.println("  Simulando cliente da versão anterior...");

        System.out.println("1. Buscando produto existente...");
        Product laptop = catalogService.findProductByName("Laptop Gamer");
        System.out.println("   " + (laptop != null ? "Encontrado: " + laptop.getName() : "Não encontrado"));

        System.out.println("2. Listando categorias...");
        List<Product> electronics = catalogService.findProductsByCategory("Eletrônicos");
        System.out.println("   Eletrônicos: " + electronics.size() + " produtos");

        System.out.println("3. Verificando saúde...");
        boolean healthy = catalogService.healthCheck();
        System.out.println("   Saúde: " + (healthy ? "Saudável" : "Problemas"));
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}