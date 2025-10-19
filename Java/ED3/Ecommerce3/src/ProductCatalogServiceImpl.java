import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ProductCatalogServiceImpl extends UnicastRemoteObject implements ProductCatalogService {
    private static final long serialVersionUID = 1L;

    private final Map<String, Product> products;
    private final Map<String, Double> categoryDiscounts;

    public ProductCatalogServiceImpl() throws RemoteException {
        super();
        this.products = new HashMap<>();
        this.categoryDiscounts = new HashMap<>();
        initializeSampleData();
        System.out.println("Serviço de Catálogo expandido instanciado");
    }

    private void initializeSampleData() {
        addProduct(new Product("P001", "Laptop Gamer", "Laptop para jogos de alta performance", 2500.00, "Eletrônicos", 10, "Dell"));
        addProduct(new Product("P002", "Smartphone", "Smartphone Android 128GB", 1200.00, "Eletrônicos", 25, "Samsung"));
        addProduct(new Product("P003", "Livro Java", "Programação Java Avançada", 89.90, "Livros", 50, "Editora Tech"));
        addProduct(new Product("P004", "Headphone", "Fone de ouvido sem fio", 299.90, "Áudio", 15, "Sony"));
        addProduct(new Product("P005", "Tablet", "Tablet 10 polegadas", 899.90, "Eletrônicos", 8, "Apple"));
        addProduct(new Product("P006", "Mouse Sem Fio", "Mouse óptico sem fio", 79.90, "Periféricos", 30, "Logitech"));
        addProduct(new Product("P007", "Teclado Mecânico", "Teclado mecânico RGB", 349.90, "Periféricos", 12, "Razer"));
        addProduct(new Product("P008", "Monitor 24\"", "Monitor Full HD 24 polegadas", 699.90, "Eletrônicos", 5, "LG"));
        addProduct(new Product("P009", "Webcam HD", "Webcam 1080p para reuniões", 199.90, "Periféricos", 20, "Logitech"));
        addProduct(new Product("P010", "SSD 1TB", "SSD NVMe 1TB alta velocidade", 499.90, "Componentes", 15, "Samsung"));
        System.out.println("Catálogo expandido: " + products.size() + " produtos carregados");
    }

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public boolean healthCheck() throws RemoteException {
        System.out.println("[SERVER] Health check solicitado - Verificando saúde do serviço");
        boolean isHealthy = !products.isEmpty() && products.size() > 0 && !Thread.currentThread().isInterrupted();
        System.out.println("Health check: " + (isHealthy ? "SAUDÁVEL" : "COM PROBLEMAS"));
        return isHealthy;
    }

    @Override
    public Product findProductByName(String name) throws RemoteException {
        System.out.println("[SERVER] Buscando: '" + name + "'");
        return products.values().stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public Product findProductById(String id) throws RemoteException {
        System.out.println("[SERVER] Buscando produto por ID: " + id);
        return products.get(id);
    }

    @Override
    public List<Product> getAllProducts() throws RemoteException {
        System.out.println("[SERVER] Listando todos os produtos");
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> findProductsByCategory(String category) throws RemoteException {
        System.out.println("[SERVER] Buscando produtos da categoria: " + category);
        return products.values().stream().filter(p -> p.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    @Override
    public boolean updateStock(String productId, int quantity) throws RemoteException {
        System.out.println("[SERVER] Atualizando estoque: " + productId + " -> " + quantity);
        Product product = products.get(productId);
        if (product != null) {
            product.setStock(quantity);
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Integer> getCategoryStatistics() throws RemoteException {
        System.out.println("[SERVER] Gerando estatísticas por categoria");
        Map<String, Integer> stats = new HashMap<>();
        for (Product product : products.values()) {
            String category = product.getCategory();
            stats.put(category, stats.getOrDefault(category, 0) + 1);
        }
        System.out.println("Estatísticas geradas: " + stats);
        return stats;
    }

    @Override
    public List<Product> searchProducts(String keyword) throws RemoteException {
        System.out.println("[SERVER] Busca por palavra-chave: '" + keyword + "'");
        List<Product> results = products.values().stream().filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) || p.getDescription().toLowerCase().contains(keyword.toLowerCase()) || p.getCategory().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        System.out.println("Busca retornou " + results.size() + " resultados");
        return results;
    }

    @Override
    public List<Product> getProductsOnSale() throws RemoteException {
        System.out.println("[SERVER] Buscando produtos em 'promoção' (estoque > 20)");
        List<Product> saleProducts = products.values().stream().filter(p -> p.getStock() > 20).collect(Collectors.toList());
        System.out.println(saleProducts.size() + " produtos em 'promoção'");
        return saleProducts;
    }

    @Override
    public boolean applyDiscount(String category, double discountPercent) throws RemoteException {
        System.out.println("💰 [SERVER] Aplicando desconto de " + discountPercent + "% na categoria: " + category);
        if (discountPercent < 0 || discountPercent > 100) {
            System.out.println("Percentual de desconto inválido");
            return false;
        }
        categoryDiscounts.put(category, discountPercent);
        int updatedCount = 0;
        for (Product product : products.values()) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                updatedCount++;
            }
        }
        System.out.println("Desconto aplicado a " + updatedCount + " produtos");
        return true;
    }

    @Override
    public Map<String, Double> getPriceStatistics() throws RemoteException {
        System.out.println("[SERVER] Calculando estatísticas de preços avançadas");
        List<Product> productList = new ArrayList<>(products.values());
        Map<String, Double> stats = new HashMap<>();

        double averagePrice = productList.stream().mapToDouble(Product::getPrice).average().orElse(0.0);
        stats.put("average_price", averagePrice);
        double maxPrice = productList.stream().mapToDouble(Product::getPrice).max().orElse(0.0);
        stats.put("max_price", maxPrice);
        double minPrice = productList.stream().mapToDouble(Product::getPrice).min().orElse(0.0);
        stats.put("min_price", minPrice);
        double totalInventoryValue = productList.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum();
        stats.put("total_inventory_value", totalInventoryValue);
        double variance = productList.stream().mapToDouble(p -> Math.pow(p.getPrice() - averagePrice, 2)).average().orElse(0.0);
        stats.put("price_std_dev", Math.sqrt(variance));

        System.out.println("Estatísticas de preço calculadas: " + stats.size() + " métricas");
        return stats;
    }
}