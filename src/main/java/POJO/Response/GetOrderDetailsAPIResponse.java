package POJO.Response;

public class GetOrderDetailsAPIResponse {
    private Order data;
    private String message;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
