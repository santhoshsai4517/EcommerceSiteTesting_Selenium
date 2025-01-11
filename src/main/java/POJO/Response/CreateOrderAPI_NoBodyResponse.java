package POJO.Response;

import java.util.Map;

public class CreateOrderAPI_NoBodyResponse {
    private String type;
    private Map<String, Object> message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
