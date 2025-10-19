import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ProductCatalogServiceImpl extends UnicastRemoteObject implements ProductCatalogService {
    private static final long serialVersionUID = 1L;
    private final Map<String, Product> products;

    public ProductCatalogServiceImpl() throws RemoteException {
        super();
        this.products = new HashMap<>();
        initializeSampleData();
        System.out.println("Serviço de Catálogo instanciado e stub criado");
    }

    private void initializeSampleData() {
        addProduct(new Product("P001", "Laptop Gamer", "Laptop para jogos de alta performance", 2500.00, "Eletrônicos", 10, "Dell"));
        addProduct(new Product("P002", "Smartphone", "Smartphone Android 128GB", 1200.00, "Eletrônicos", 25, "Samsung"));
        addProduct(new Product("P003", "Livro Java", "Programação Java Avançada", 89.90, "Livros", 50, "Editora Tech"));
        addProduct(new Product("P004", "Headphone", "Fone de ouvido sem fio", 299.90, "Áudio", 15, "Sony"));
        addProduct(new Product("P005", "Tablet", "Tablet 10 polegadas", 899.90, "Eletrônicos", 8, "Apple"));
        System.out.println("Dados de exemplo carregados: " + products.size() + " produtos");
    }

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Product findProductByName(String name) throws RemoteException {
        System.out.println("[SERVER] Recebendo chamada: findProductByName('" + name + "')");
        for (Product product : products.values()) {
            if (product.getName().equalsIgnoreCase(name)) {
                System.out.println("Produto encontrado: " + product);
                return product;
            }
        }
        System.out.println("Produto não encontrado: " + name);
        return null;
    }

    @Override
    public List<Product> getAllProducts() throws RemoteException {
        System.out.println("[SERVER] Recebendo chamada: getAllProducts()");
        List<Product> productList = new ArrayList<>(products.values());
        System.out.println("Retornando " + productList.size() + " produtos");
        return productList;
    }

    @Override
    public List<Product> findProductsByCategory(String category) throws RemoteException {
        System.out.println("[SERVER] Recebendo chamada: findProductsByCategory('" + category + "')");
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        System.out.println("Encontrados " + result.size() + " produtos na categoria " + category);
        return result;
    }

    @Override
    public Product findProductById(String id) throws RemoteException {
        System.out.println("[SERVER] Buscando produto por ID: " + id);
        return products.get(id);
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
    public boolean healthCheck() throws RemoteException {
        System.out.println("[SERVER] Health check solicitado");
        return !products.isEmpty();
    }
}