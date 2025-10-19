import java.io.Serializable;

public class ProductStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private double totalInventoryValue;
    private double averagePrice;
    private Product mostExpensive;
    private Product cheapest;
    private int outOfStockCount;
    private int lowStockCount;

    public ProductStatistics(double totalValue, double avgPrice, Product mostExpensive, Product cheapest, int outOfStock, int lowStock) {
        this.totalInventoryValue = totalValue;
        this.averagePrice = avgPrice;
        this.mostExpensive = mostExpensive;
        this.cheapest = cheapest;
        this.outOfStockCount = outOfStock;
        this.lowStockCount = lowStock;
    }

    public double getTotalInventoryValue() { return totalInventoryValue; }
    public double getAveragePrice() { return averagePrice; }
    public Product getMostExpensive() { return mostExpensive; }
    public Product getCheapest() { return cheapest; }
    public int getOutOfStockCount() { return outOfStockCount; }
    public int getLowStockCount() { return lowStockCount; }

    @Override
    public String toString() {
        return String.format("Estatísticas: Valor Total=%.2f, Preço Médio=%.2f, Sem Estoque=%d", totalInventoryValue, averagePrice, outOfStockCount);
    }
}