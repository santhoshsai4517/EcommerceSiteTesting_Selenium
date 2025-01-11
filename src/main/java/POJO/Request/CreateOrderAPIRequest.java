package POJO.Request;

import java.util.List;

public class CreateOrderAPIRequest {
    private List<Order> orders;

    public CreateOrderAPIRequest(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
