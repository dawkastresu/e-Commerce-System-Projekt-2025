package ui;

import model.*;
import services.*;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CommandLineInterface {

    private ProductManager productManager;

    private Cart cart;

    private OrderProcessor orderProcessor;

    private Scanner scanner;

    private Discount discount;

    Random random = new Random();


    public CommandLineInterface(ProductManager productManager, OrderProcessor orderProcessor, Discount discount) {
        this.productManager = productManager;
        this.orderProcessor = orderProcessor;
        this.scanner = new Scanner(System.in);
        this.discount = discount;
        this.cart = new Cart();
    }


    public void start() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Files/products.dat"));
             ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("Files/cart.dat"));
             ObjectInputStream ois3 = new ObjectInputStream(new FileInputStream("Files/discounts.dat"))) {

            // Wczytanie listy produktów
            List<Product> products = (List<Product>) ois.readObject();
            productManager.setProducts(products);

            // Wczytanie koszyka
            List<Product> cartItems = (List<Product>) ois2.readObject();
            cart.setItems(cartItems);

            // Wczytanie promocji
            List<Discount.DiscountRule> discounts = (List<Discount.DiscountRule>) ois3.readObject();
            discount.setDiscountRules(discounts);

            System.out.println("Produkty, promocje oraz zawartość koszyka zostały wczytane z plików.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nie udało się wczytać listy produktów, promocji lub zawartości koszyka");
        }

        while (true) {
            System.out.println("\n1. Przeglądaj produkty");
            System.out.println("2. Dodaj do koszyka");
            System.out.println("3. Wyświetl zawartość koszyka");
            System.out.println("4. Złóż zamówienie");
            System.out.println("5. Skonfiguruj sprzęt");
            System.out.println("6. Aktualne promocje");
            System.out.println("7. Tryb administratora");
            System.out.println("8. Wyjście");
            System.out.print("Wybierz opcję: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewProducts();
                    break;
                case 2:
                    addToCart();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    placeOrder();
                    break;
                case 5:
                    configSpecs();
                    break;
                case 6:
                    showDiscounts();
                    break;
                case 7:
                    adminMode();
                    break;
                case 8:
                    saveFiles();
                    return;
                default:
                    System.out.println("Wybrana opcja niedostępna, spróbuj jeszcze raz.");
            }
        }
    }

    private void viewProducts() {
        System.out.println("""
                
                Available Products:
                """);
        for (Product product : productManager.getAllProducts()) {
            System.out.println(product);
        }
    }

    private void addToCart() {
        while (true) {
            productManager.getAllProducts()
                    .forEach(v -> System.out.printf("%-20s | %-10.2f | %-5d%n", v.getName(), v.getPrice(), v.getId()));
            System.out.print("Wprowadź ID produktu, aby dodać go do koszyka: ");
            System.out.println("Wybierz 1. aby wrócić do panelu klienta");
            Long id = scanner.nextLong();
            if (id != 1) {
                productManager.getProduct(id).
                        ifPresentOrElse(product -> {
                                    cart.addProduct(product);
                                    saveProducts();
                                    System.out.println("Produkt " + product + " dodany do koszyka");
                                },
                                () -> System.out.println("Nie znaleziono takiego produktu")
                        );
            } else return;
        }
    }

    private void viewCart() {
        System.out.println("Twój koszyk:");
        cart.getItems().forEach(System.out::println);
        System.out.println("Cena nałkowita: " + cart.getTotalPrice());
    }


    private void placeOrder() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Twój koszyk jest pusty.");
            return;
        }

        System.out.print("Wprowadź imię i nazwisko: ");
        String customerName = scanner.nextLine();


        discount.getApplicableDiscountInfo(cart.getTotalPrice()); //nie wyświetla się

        Order order = new Order(System.currentTimeMillis(), customerName, cart.getItems(), discount.applyDiscount(cart.getTotalPrice()));

        orderProcessor.processOrder(order).thenRun(() -> {
            System.out.println("Zamowienie złożone pomyślnie!");
            cart.clear();
            saveCart();
        });
    }

    private void configSpecs() {
        System.out.println("Wybierz rodzaj: ");
        System.out.println("1. Komputer");
        System.out.println("2. Smartfon");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                configComputer();
                break;
            case 2:
                configSmartphone();
                break;
            default:
                System.out.println("Wybrana opcja niedostępna, spróbuj jeszcze raz.");
        }
    }

    public void showDiscounts() {
        System.out.println("\n" + "Dostępne promocje");
        for (Discount.DiscountRule discount : discount.getDiscountRules()) {
            System.out.println(discount);
        }
    }

    private void adminMode() {
        while (true) {
            System.out.println("1. Dodaj produkt");
            System.out.println("2. Edytuj produkt");
            System.out.println("3. Usuń produkt");
            System.out.println("4. Dodaj promocję");
            System.out.println("5. Usuń promocję");
            System.out.println("6. Powrót do panelu klienta");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProductAdminMode();
                    break;
                case 2:
                    editProductAdminMode();
                    break;
                case 3:
                    removeProductAdminMode();
                    break;
                case 4:
                    addDiscountAdminMode();
                    break;
                case 5:
                    removeDiscountAdminMode();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Wybrana opcja niedostępna, spróbuj jeszcze raz.");
            }
        }
    }

    private void addProductAdminMode() {
        System.out.println("Podaj ID produktu");
        long id = scanner.nextLong();
        System.out.println("Podaj nazwę produktu");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.println("Podaj cenę produktu");
        double price = scanner.nextDouble();
        System.out.println("Podaj ilość");
        int quantity = scanner.nextInt();

        boolean looper = true;

        while (looper == true) {
            System.out.println("Wybierz typ produktu");
            System.out.println("1. Komputer");
            System.out.println("2. Smartfon");
            System.out.println("3. Elektronika");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Processor selectedProcessor = null;
                    RAM selectedRam = null;

                    while (selectedProcessor == null) {
                        System.out.println("Wybierz procesor:");
                        Processor[] processors = Processor.values();
                        for (int i = 0; i < processors.length; i++) {
                            System.out.printf("%d. %s - $%.2f%n", i + 1, processors[i].name(), processors[i].getPrice());
                        }

                        System.out.print("Wprowadź numer procesora: ");
                        int processorChoice = scanner.nextInt();

                        if (processorChoice >= 1 && processorChoice <= processors.length) {
                            selectedProcessor = processors[processorChoice - 1];
                        } else {
                            System.out.println("Zły numer, wprowadź ponownie.");
                        }
                    }

                    while (selectedRam == null) {
                        System.out.println("Wybierz RAM:");
                        RAM[] rams = RAM.values();
                        for (int i = 0; i < rams.length; i++) {
                            System.out.printf("%d. %s - $%.2f%n", i + 1, rams[i].name(), rams[i].getPrice());
                        }

                        System.out.print("Wprowadź numer RAMu: ");
                        int ramChoice = scanner.nextInt();

                        if (ramChoice >= 1 && ramChoice <= rams.length) {
                            selectedRam = rams[ramChoice - 1];
                        } else {
                            System.out.println("Zły numer, wprowadź ponownie.");
                        }
                    }

                    productManager.getProducts().add(new Computer(id, name, price, quantity, selectedProcessor, selectedRam));
                    System.out.println("Dodano nowy komputer.");
                    looper = false;
                    break;
                case 2:
                    Accessories selectedAccessories = null;
                    Colors selectedColor = null;
                    BatteryCapacity selectedBattery = null;

                    while (selectedAccessories == null) {
                        System.out.println("Wybierz akcesoria:");
                        Accessories[] accessories = Accessories.values();
                        for (int i = 0; i < accessories.length; i++) {
                            System.out.printf("%d. %s - $%.2f%n", i + 1, accessories[i].name(), accessories[i].getPrice());
                        }

                        System.out.print("Wprowadź numer akcesorii: ");
                        int accessoryChoice = scanner.nextInt();

                        if (accessoryChoice >= 1 && accessoryChoice <= accessories.length) {
                            selectedAccessories = accessories[accessoryChoice - 1];
                        } else {
                            System.out.println("Zły numer, wprowadź ponownie.");
                        }
                    }

                    while (selectedColor == null) {
                        System.out.println("Wybierz kolor:");
                        Colors[] colors = Colors.values();
                        for (int i = 0; i < colors.length; i++) {
                            System.out.printf("%d. %s - $%.2f%n", i + 1, colors[i].name(), colors[i].getPrice());
                        }

                        System.out.print("Wprowadź numer koloru: ");
                        int colorChoice = scanner.nextInt();

                        if (colorChoice >= 1 && colorChoice <= colors.length) {
                            selectedColor = colors[colorChoice - 1];
                        } else {
                            System.out.println("Zły numer, wprowadź ponownie.");
                        }
                    }

                    while (selectedBattery == null) {
                        System.out.println("Wybierz pojemność baterii:");
                        BatteryCapacity[] batteries = BatteryCapacity.values();
                        for (int i = 0; i < batteries.length; i++) {
                            System.out.printf("%d. %s - $%.2f%n", i + 1, batteries[i].name(), batteries[i].getPrice());
                        }

                        System.out.print("Wprowadź numer baterii: ");
                        int batteryChoice = scanner.nextInt();

                        if (batteryChoice >= 1 && batteryChoice <= batteries.length) {
                            selectedBattery = batteries[batteryChoice - 1];
                        } else {
                            System.out.println("Zły numer, wprowadź ponownie.");
                        }
                    }

                    productManager.getProducts().add(new Smartphone(id, name, price, quantity, selectedColor ,selectedBattery ,selectedAccessories));
                    System.out.println("Dodano nowego smartfona.");
                    looper = false;

                    break;
                case 3:
                    productManager.getProducts().add(new Product(id, name, price, quantity, Product.ProductType.ELECTRONICS));
                    System.out.println("Dodano nowy produkt.");
                    looper = false;
                    break;
                default:
                    System.out.println("Zły wybór, spróbuj jeszcze raz.");
            }
            saveProducts();
        }
    }

    private void editProductAdminMode() {
        productManager.getAllProducts()
                .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));
        System.out.println("Podaj ID produktu który chcesz edytować");
            long choice = scanner.nextLong();

        for (Product product : productManager.getAllProducts()) {
            if (choice == product.getId()) {
                System.out.println("Podaj zaktualizowane ID produktu");
                product.setId(scanner.nextLong());
                System.out.println("Podaj zaktualizowaną nazwę produktu");
                product.setName(scanner.nextLine());
                System.out.println("Podaj zaktualizowaną cenę produktu");
                product.setPrice(scanner.nextDouble());
                System.out.println("Podaj zaktualizowaną ilość");
                product.setQuantity(scanner.nextInt());

                boolean looper = true;

                while (looper) {
                    System.out.println("Wybierz zaktualizowany typ produktu");
                    System.out.println("1. Komputer");
                    System.out.println("2. Smartfon");
                    System.out.println("3. Elektronika");
                    int typeChoice = scanner.nextInt();

                    switch (typeChoice) {
                        case 1:
                            product.setType(Product.ProductType.COMPUTER);
                            looper = false;
                            break;
                        case 2:
                            product.setType(Product.ProductType.SMARTPHONE);
                            looper = false;
                            break;
                        case 3:
                            product.setType(Product.ProductType.ELECTRONICS);
                            looper = false;
                            break;
                        default:
                            System.out.println("Nie ma takiego typu, podaj jeszcze raz");
                    }
                }
            }
        }
        saveProducts();
    }

    private void removeProductAdminMode() {
        productManager.getAllProducts()
                .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));
        System.out.println("Podaj ID produktu który chcesz usunąć:");

        productManager.removeProduct(scanner.nextLong());
        System.out.println("Usunięto produkt.");
        saveProducts();
    }

    private void addDiscountAdminMode() {
        System.out.println("Podaj wartość progową promocji");
        double threshold = scanner.nextDouble();
        System.out.println("Podaj wartość procentową zniżki");
        double percentageOff = scanner.nextDouble();

        discount.addDiscountRule(threshold, percentageOff);

        System.out.println("Dodano promocję.");

        saveDiscounts();
    }

    private void removeDiscountAdminMode() {
        discount.getDiscountRules()
                .forEach(discount -> System.out.printf("%-20s | %-10.2f", discount.getThreshold(), discount.getPercentageOff()));

        System.out.println("Wpisz wartość progową promocji, którą chcesz usunąć.");
        double threshold = scanner.nextDouble();
        System.out.println("Wpisz wartość procentową promocji którą chcesz usunąć.");
        double percentageOff = scanner.nextDouble();

        for (Discount.DiscountRule discountRule : discount.getDiscountRules()) {
            if (discountRule.getThreshold() == threshold && discountRule.getPercentageOff() == percentageOff) {
                discount.getDiscountRules().remove(discountRule);
            }
        }

        System.out.println("Usunięto promocję.");
        saveDiscounts();
    }


    private void configComputer() {
        double basePrice = 2000;


        System.out.println("Wybierz Procesor: ");
        for (Processor processor : Processor.values()) {
            System.out.println(processor.ordinal() + 1 + ". " + processor + " - Cena: " + processor.getPrice() + " PLN");
        }
        int processorChoice = scanner.nextInt();
        Processor selectedProcessor = Processor.values()[processorChoice - 1];

        System.out.println("Wybierz pamięć RAM:");
        for (RAM ram : RAM.values()) {
            System.out.println(ram.ordinal() + 1 + ". " + ram + " - Cena: " + ram.getPrice() + " PLN");
        }
        int ramChoice = scanner.nextInt();
        RAM selectedRam = RAM.values()[ramChoice - 1];

        System.out.println("Wpisz nazwę:");
        String customName = scanner.nextLine();


        double totalPrice = basePrice + selectedProcessor.getPrice() + selectedRam.getPrice();

//        String randomId = UUID.randomUUID().toString(); - losowe 32 znaki pod id

        long randomId = random.nextLong(999);


        Product computer = new Product(randomId, customName, totalPrice, 1, Product.ProductType.COMPUTER);

        System.out.println("\nSkonfigurowany komputer:");
        System.out.println("Nazwa: " + customName);
        System.out.println("Procesor: " + selectedProcessor + "\n" +
                            "RAM: " + selectedRam);
        System.out.println("Całkowita cena: " + totalPrice + " PLN");


        System.out.println("Czy chcesz dodać produkt do koszyka?");
        System.out.println("1. Tak");
        System.out.println("2. Nie");
        System.out.println("Wybierz: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                cart.addProduct(computer);
                saveCart();
                break;
            case 2:
                System.out.println("Produkt usunięty.");
                break;
            default:
                System.out.println("Wybrano nieprawidłową opcję");
        }
    }

    private void configSmartphone() {
        double basePrice = 1200;

        System.out.println("Wybierz Kolor: ");
        for (Colors color : Colors.values()) {
            System.out.println(color.ordinal() + 1 + ". " + color + " - Cena: " + color.getPrice() + " PLN");
        }
        int colorChoice = scanner.nextInt();
        Colors selectedColor = Colors.values()[colorChoice - 1];

        System.out.println("Wybierz pojemność baterii:");
        for (BatteryCapacity battery : BatteryCapacity.values()) {
            System.out.println(battery.ordinal() + 1 + ". " + battery + " - Cena: " + battery.getPrice() + " PLN");
        }
        int batteryChoice = scanner.nextInt();
        BatteryCapacity selectedBattery = BatteryCapacity.values()[batteryChoice - 1];

        System.out.println("Wybierz akcesoria:");
        for (Accessories accessory : Accessories.values()) {
            System.out.println(accessory.ordinal() + 1 + ". " + accessory + " - Cena: " + accessory.getPrice() + " PLN");
        }
        int accessoryChoice = scanner.nextInt();
        Accessories selectedAccessories = Accessories.values()[accessoryChoice - 1];

        System.out.println("Wpisz nazwę:");
        String customName = scanner.nextLine();

        double totalPrice = basePrice + selectedColor.getPrice() + selectedBattery.getPrice() + selectedAccessories.getPrice();

        long randomId = random.nextLong(999);

        Product smartphone = new Product(randomId, customName, totalPrice, 1, Product.ProductType.SMARTPHONE);

        System.out.println("\nSkonfigurowany smartfon:");
        System.out.println("Nazwa: " + customName);
        System.out.println("Kolor: " + selectedColor + "\n" +
                            "Pojemność baterii: " + selectedBattery + "\n" +
                            "Akcesoria: " + selectedAccessories);
        System.out.println("Całkowita cena: " + totalPrice + " PLN");



        System.out.println("Czy chcesz dodać produkt do koszyka?");
        System.out.println("1. Tak");
        System.out.println("2. Nie");
        System.out.println("Wybierz: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                cart.addProduct(smartphone);
                saveCart();
                break;
            case 2:
                System.out.println("Produkt usunięty");
                break;
            default:
                System.out.println("Wybrano nieprawidłową opcję");
        }
    }

    private void saveFiles() {
        File directory = new File("Files");
        if (!directory.exists() && directory.mkdir()){
            System.out.println("Utworzono folder Files");
        }

        saveProducts();
        saveCart();
        saveDiscounts();
    }

    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Files/products.dat"));) {
            oos.writeObject(productManager.getAllProducts());
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać produktu");
        }
    }

    private void saveCart() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Files/cart.dat"));) {
            oos.writeObject(cart.getItems());
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać wartości koszyka");
        }
    }

    private void saveDiscounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Files/discounts.dat"));) {
            oos.writeObject(discount.getDiscountRules());
        }catch (IOException e) {
            System.out.println("Nie udało się zapisać promocji");
        }
    }

}
