package POJO.Response;

public class Errors {
    private FieldError productName;
    private FieldError productCategory;
    private FieldError productSubCategory;
    private FieldError productPrice;
    private FieldError productDescription;

    // Getters and Setters
    public FieldError getProductName() {
        return productName;
    }

    public void setProductName(FieldError productName) {
        this.productName = productName;
    }

    public FieldError getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(FieldError productCategory) {
        this.productCategory = productCategory;
    }

    public FieldError getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(FieldError productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public FieldError getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(FieldError productPrice) {
        this.productPrice = productPrice;
    }

    public FieldError getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(FieldError productDescription) {
        this.productDescription = productDescription;
    }
}
