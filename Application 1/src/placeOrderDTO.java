import java.util.Date;

public class placeOrderDTO {
    private String OrderId;
    private String CustomerId;
    private String ItemCode;
    private int qty;
    private double unitPrice;

    private String date;

    public placeOrderDTO(String orderId, String customerId, String itemCode, int qty, double unitPrice, String date) {
        OrderId = orderId;
        CustomerId = customerId;
        ItemCode = itemCode;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.date = date;
    }
    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "placeOrderDTO{" +
                "OrderId='" + OrderId + '\'' +
                ", CustomerId='" + CustomerId + '\'' +
                ", ItemCode='" + ItemCode + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", date='" + date + '\'' +
                '}';
    }
}
