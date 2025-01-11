package POJO.Request;

import java.io.File;

public class AddProductAPIRequest {
    private String productName;
    private String productAddedBy;
    private String productCategory;
    private String productSubCategory;
    private String productPrice;
    private String productDescription;
    private String productFor;
    private File productImage;

    public AddProductAPIRequest(String productName, String productAddedBy, String productCategory, String productSubCategory, String productPrice, String productDescription, String productFor, File productImage) {
        this.productName = productName;
        this.productAddedBy = productAddedBy;
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productFor = productFor;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAddedBy() {
        return productAddedBy;
    }

    public void setProductAddedBy(String productAddedBy) {
        this.productAddedBy = productAddedBy;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductFor() {
        return productFor;
    }

    public void setProductFor(String productFor) {
        this.productFor = productFor;
    }

    public File getProductImage() {
        return productImage;
    }

    public void setProductImage(File productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "AddProductAPIRequest{" +
                "productName='" + productName + '\'' +
                ", productAddedBy='" + productAddedBy + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productSubCategory='" + productSubCategory + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productFor='" + productFor + '\'' +
                ", productImage=" + productImage +
                '}';
    }
}
