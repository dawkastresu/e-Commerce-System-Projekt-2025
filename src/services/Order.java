package services;

import model.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {

    private Long id;

    private String customerName;

    private List<Product> products;

    private double totalAmount;

    private LocalDateTime orderTime;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Order(Long id, String customerName, List<Product> products, double totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.products = products;
        this.totalAmount = totalAmount;
        this.orderTime = LocalDateTime.now();
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    @Override
    public String toString() {
        return "Zamówienie: " +
                "ID: " + id  + "\n" +
                "Imie klienta: " + customerName + '\n' +
                "Produkty: " + products + "\n" +
                "Cena całkowita: " + totalAmount + "\n" +
                "Czas złożenia zamówienia: " + orderTime.format(formatter);
    }
}
