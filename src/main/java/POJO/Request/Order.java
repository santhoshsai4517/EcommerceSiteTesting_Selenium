package POJO.Request;

public class Order {
    private String country;
    private String productOrderedId;

    public Order(String country, String productOrderedId) {
        this.country = country;
        this.productOrderedId = productOrderedId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProductOrderedId() {
        return productOrderedId;
    }

    public void setProductOrderedId(String productOrderedId) {
        this.productOrderedId = productOrderedId;
    }
}
