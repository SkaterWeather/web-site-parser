package model;

import java.util.Set;

public class Product {
    private String articleID;
    private String name;
    private String brand;
    private Double basePrice;
    private Double salePrice;
    private String color;
    private Set<String> availableSizes;

    public Product() {
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<String> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(Set<String> availableSizes) {
        this.availableSizes = availableSizes;
    }
}
