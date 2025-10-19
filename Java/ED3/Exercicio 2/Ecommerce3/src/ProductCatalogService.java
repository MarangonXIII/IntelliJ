import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ProductCatalogService extends Remote {

    Product findProductByName(String name) throws RemoteException;
    Product findProductById(String id) throws RemoteException;
    List<Product> getAllProducts() throws RemoteException;
    List<Product> findProductsByCategory(String category) throws RemoteException;
    boolean updateStock(String productId, int quantity) throws RemoteException;
    Map<String, Integer> getCategoryStatistics() throws RemoteException;
    boolean healthCheck() throws RemoteException;
    List<Product> searchProducts(String keyword) throws RemoteException;
    List<Product> getProductsOnSale() throws RemoteException;
    boolean applyDiscount(String category, double discountPercent) throws RemoteException;
    Map<String, Double> getPriceStatistics() throws RemoteException;
}