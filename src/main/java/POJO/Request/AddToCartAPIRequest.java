package POJO.Request;

import POJO.Response.Product;

public class AddToCartAPIRequest {
    private String _id;
    private Product product;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}