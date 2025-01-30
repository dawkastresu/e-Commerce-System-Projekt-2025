package Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class OrderProcessor {

    private List<Order> orders = new ArrayList<>();

    Random random = new Random();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public CompletableFuture<Void> processOrder(Order order) {
        return CompletableFuture.runAsync(() -> {
            // Simulate order processing
            orders.add(order);
            generateInvoice(order);
        });
    }

    private void generateInvoice(Order order) {
        System.out.println
                ("\n" + "----Faktura---------------------------------" + "\n" +
                "Imie klienta: " + order.getCustomerName() + "\n" +
                "Produkty: " + "\n" +
                order.getProducts() + "\n" +
                "Data transakcji: " + order.getOrderTime().format(formatter) + "\n" +
                "Wartość transakcji: " + order.getTotalAmount());

        String invoice = "\n" + "----Faktura---------------------------------" + "\n" +
                "Imie klienta: " + order.getCustomerName() + "\n" +
                "Produkty: " + "\n" +
                order.getProducts() + "\n" +
                "Data transakcji: " + order.getOrderTime().format(formatter) + "\n" +
                "Wartość transakcji: " + order.getTotalAmount();

        File directory = new File("Invoices");
        if (!directory.exists() && directory.mkdir()){
            System.out.println("Utworzono folder z fakturami");
        }
        int randomInt = random.nextInt(100);

        //Zapisanie faktury do pliku
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Invoices/Faktura nr" + randomInt + ", " + order.getCustomerName() + ".txt"))){
            writer.append(invoice);
            System.out.println("Wygenerowano fakturę nr " + randomInt);
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać faktury");
        }
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

}
