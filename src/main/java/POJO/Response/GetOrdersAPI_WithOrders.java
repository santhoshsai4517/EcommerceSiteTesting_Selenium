package POJO.Response;

import java.util.List;

public class GetOrdersAPI_WithOrders {
    private List<Order> data;
    private int count;
    private String message;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
