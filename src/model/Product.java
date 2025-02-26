package model;

import java.io.Serializable;
import java.util.Random;

public class Product implements Serializable {

    private Long id;

    private String name;

    private double price;

    private int quantity;

    private ProductType type;

    Random random;

    public enum ProductType {
        COMPUTER, SMARTPHONE, ELECTRONICS
    }

    public Product(Long id, String name, double price, int quantity, ProductType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public Product() {
        this.id = random.nextLong(999999);
        this.name = "Custom PC";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "ID: " + id + ", " +
                "Nazwa: " + name + ", " +
                "Cena: " + price + ", " +
                "Ilość w magazynie: " + quantity + ", " + "\n";
    }
}
