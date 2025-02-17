package app;

import services.Discount;
import services.OrderProcessor;
import services.ProductManager;
import ui.CommandLineInterface;

public class ECommerceApp {
    public static void main(String[] args) {
        ProductManager productManager = new ProductManager();
        OrderProcessor orderProcessor = new OrderProcessor();
        Discount discount = new Discount();

        CommandLineInterface cli = new CommandLineInterface(productManager, orderProcessor, discount);
        cli.start();
    }
}