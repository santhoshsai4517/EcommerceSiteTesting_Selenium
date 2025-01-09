package POJO.Response;

import java.util.List;

public class GetOrdersAPI_NoOrders {
    private List<Order> data;
    private String message;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
