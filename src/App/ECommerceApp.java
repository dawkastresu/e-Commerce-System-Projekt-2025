package App;

import Services.DiscountProcessor;
import Services.OrderProcessor;
import Services.ProductManager;
import UI.CommandLineInterface;

public class ECommerceApp {
    public static void main(String[] args) {
        ProductManager productManager = new ProductManager();
        OrderProcessor orderProcessor = new OrderProcessor();
        DiscountProcessor discountProcessor = new DiscountProcessor();

        CommandLineInterface cli = new CommandLineInterface(productManager, orderProcessor, discountProcessor);
        cli.start();
    }
}