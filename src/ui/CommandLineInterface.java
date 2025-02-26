package ui;

import model.*;
import services.*;
import util.InputUtil;

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

        loadCart();
        loadDiscounts();
        loadProducts();

        while (true) {
            System.out.println("\n1. Przeglądaj produkty");
            System.out.println("2. Dodaj do koszyka");
            System.out.println("3. Wyświetl zawartość koszyka");
            System.out.println("4. Usuń produkty z koszyka");
            System.out.println("5. Złóż zamówienie");
            System.out.println("6. Skonfiguruj sprzęt");
            System.out.println("7. Aktualne promocje");
            System.out.println("8. Tryb administratora");
            System.out.println("9. Wyjście");
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
                    removeFromCart();
                    break;
                case 5:
                    placeOrder();
                    break;
                case 6:
                    configSpecs();
                    break;
                case 7:
                    showDiscounts();
                    break;
                case 8:
                    adminMode();
                    break;
                case 9:
                    saveFiles();
                    return;
                default:
                    System.out.println("Wybrana opcja niedostępna, spróbuj jeszcze raz.");
            }
        }
    }

    private void viewProducts() {
        System.out.println("""
                
                Dostępne produkty:
                """);
        for (Product product : productManager.getAllProducts()) {
            System.out.println(product);
        }
    }

    private void addToCart() {
        while (true) {
            productManager.getAllProducts()
                    .forEach(v -> System.out.printf("%-20s | Cena: %-10.2f | ID: %-5d%n", v.getName(), v.getPrice(), v.getId()));
            System.out.print("Wprowadź ID produktu, aby dodać go do koszyka: ");
            System.out.println("Wybierz 1. aby wrócić do panelu klienta");
            int id = scanner.nextInt();
            if (id != 1) {
                productManager.getProduct((long) id).
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

    private void removeFromCart() {
        cart.getItems().forEach(v -> System.out.printf("%-20s | Cena: %-10.2f | ID: %-5d%n", v.getName(), v.getPrice(), v.getId()));
        System.out.println("Całkowita wartość koszyka: " + cart.getTotalPrice());
        int choice = (int) InputUtil.validateInput(scanner, "Wpisz ID produktu, który chcesz usunąć, wybierz 1 - aby opróżnić cały koszyk, lub 0 - aby zakończyć edycję.");

        while (choice != 0) {
            if (choice == 1) {
                cart.clear();
                System.out.println("Zawartość koszyka usunięta.");
                choice = 0;
                saveCart();
            }
            if (!cart.getItems().isEmpty()) {
                for (Product product : cart.getItems()) {
                    if (choice == product.getId()) {
                        cart.removeProduct(product);
                        System.out.println("Produkt " + product.getName() + " usunięty.");
                        saveCart();
                    }
                }
            }
        }
    }

    private void placeOrder() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Twój koszyk jest pusty.");
            return;
        }

        System.out.print("Wprowadź imię i nazwisko: ");
        String customerName = scanner.nextLine();


        discount.getApplicableDiscountInfo(cart.getTotalPrice());

        Order order = new Order(System.currentTimeMillis(), customerName, cart.getItems(), discount.applyDiscount(cart.getTotalPrice()));

        orderProcessor.processOrder(order, cart).thenRun(() -> {
            System.out.println("Zamowienie złożone pomyślnie!");
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
        for (DiscountRule discount : discount.getDiscountRules()) {
            System.out.println("Od: " + discount.getThreshold() + "% Promocji: " + discount.getPercentageOff() + "\n");
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
        long id = random.nextLong(999999);
        System.out.println("Podaj nazwę produktu");
        String name = scanner.nextLine();
        double price = InputUtil.validateInputDouble(scanner, "Podaj cenę produktu.");
        int quantity = (int) InputUtil.validateInput(scanner, "Podaj ilość produktów.");


        boolean looper = true;

        while (looper) {

            int choice = (int) InputUtil.validateInput
                    (scanner, "Wybierz typ produktu" + "\n" +
                            "1. Komputer" + "\n" +
                            "2. Smartfon" + "\n" +
                            "3. Elektronika");

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
                    double totalPriceComputer = selectedProcessor.getPrice() + selectedRam.getPrice() + price;

                    productManager.getProducts().add(new Computer(id, name, totalPriceComputer, quantity, selectedProcessor, selectedRam));
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

                    double totalPriceSmartPhone = selectedAccessories.getPrice() + selectedBattery.getPrice() + selectedColor.getPrice() + price;

                    productManager.getProducts().add(new Smartphone(id, name, totalPriceSmartPhone, quantity, selectedColor, selectedBattery, selectedAccessories));
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
        System.out.println("1. Edytuj nazwę produktu.");
        System.out.println("2. Edytuj cenę produktu.");
        System.out.println("3. Edytuj cały produkt.");
        int editChoice = (int) InputUtil.validateInput(scanner, "Wybierz opcję edycji");

        switch (editChoice) {
            case 1:
                productManager.getAllProducts()
                        .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));
                long choiceName = InputUtil.validateInput(scanner, "Podaj id produktu, którego nazwę chcesz edytować.");

                for (Product product : productManager.getAllProducts()) {
                    if (choiceName == product.getId()) {
                        System.out.println("Podaj zaktualizowaną nazwę produktu");
                        scanner.nextLine();
                        product.setName(scanner.nextLine());

                    }
                }
                break;
            case 2:
                productManager.getAllProducts()
                        .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));
                long choicePrice = InputUtil.validateInput(scanner, "Podaj id produktu, którego cenę chcesz edytować.");

                for (Product product : productManager.getAllProducts()) {
                    if (choicePrice == product.getId()) {
                        System.out.println("Podaj zaktualizowaną cenę produktu");
                        product.setPrice(scanner.nextDouble());
                    }
                }
                break;
            case 3:
                productManager.getAllProducts()
                        .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));
                long choice = InputUtil.validateInput(scanner, "Podaj id produktu, który chcesz edytować.");

                for (Product product : productManager.getAllProducts()) {
                    if (choice == product.getId()) {
                        System.out.println("Podaj zaktualizowaną nazwę produktu");
                        product.setName(scanner.nextLine());
                        product.setPrice(InputUtil.validateInputDouble(scanner, "Podaj zaktualizowaną cenę produktu."));
                        product.setQuantity((int) InputUtil.validateInput(scanner, "Podaj zaktualizowaną ilość."));
                        if (product instanceof Computer) {
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
                            ((Computer) product).setProcessor(selectedProcessor);
                            ((Computer) product).setRamGB(selectedRam);
                        } else if (product instanceof Smartphone) {

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
                            ((Smartphone) product).setAccessories(selectedAccessories);
                            ((Smartphone) product).setColor(selectedColor);
                            ((Smartphone) product).setBatteryCapacity(selectedBattery);
                        }
                    }
                }
                break;
            default:
                System.out.println("Wybrana opcja niedostępna, spróbuj jeszcze raz.");
        }
        saveProducts();
    }

    private void removeProductAdminMode() {
        productManager.getAllProducts()
                .forEach(product -> System.out.printf("%-20s | %-10.2f | %-5d%n", product.getName(), product.getPrice(), product.getId()));

        productManager.removeProduct(InputUtil.validateInput(scanner, "Podaj ID produktu, który chcesz usunąć:"));
        System.out.println("Usunięto produkt.");
        saveProducts();
    }

    private void addDiscountAdminMode() {
        double threshold = InputUtil.validateInputDouble(scanner, "Podaj wartość progową promocji");
        double percentageOff = InputUtil.validateInputDouble(scanner, "Podaj wartość procentową obniżki");

        discount.addDiscountRule(threshold, percentageOff);

        System.out.println("Dodano promocję.");

        saveDiscounts();
    }

    private void removeDiscountAdminMode() {
        discount.getDiscountRules()
                .forEach(discount -> System.out.printf("Od: %-10s | Procent zniżki: %-10.2f | ID: %-10s" + "\n", discount.getThreshold(), discount.getPercentageOff(), discount.getId()));

        long id = InputUtil.validateInput(scanner, "Wpisz ID promocji, którą chcesz usunąć");

        discount.getDiscountRules().removeIf(discountRule -> discountRule.getId() == id);

        System.out.println("Usunięto promocję.");
        saveDiscounts();
    }


    private void configComputer() {
        double basePrice = 2000;


        System.out.println("Wybierz Procesor: ");
        for (Processor processor : Processor.values()) {
            System.out.println(processor.ordinal() + 1 + ". " + processor + " - Cena: " + processor.getPrice() + " PLN");
        }
        int processorChoice = (int) InputUtil.validateInput(scanner, "Wpisz numer procesora");
        Processor selectedProcessor = Processor.values()[processorChoice - 1];

        System.out.println("Wybierz pamięć RAM:");
        for (RAM ram : RAM.values()) {
            System.out.println(ram.ordinal() + 1 + ". " + ram + " - Cena: " + ram.getPrice() + " PLN");
        }
        int ramChoice = (int) InputUtil.validateInput(scanner, "Wpisz numer RAMu");
        RAM selectedRam = RAM.values()[ramChoice - 1];

        System.out.println("Wpisz nazwę:");
        String customName = scanner.nextLine();


        double totalPrice = basePrice + selectedProcessor.getPrice() + selectedRam.getPrice();


        long randomId = random.nextLong(999999);


        Product computer = new Product(randomId, customName, totalPrice, 1, Product.ProductType.COMPUTER);

        System.out.println("Skonfigurowany komputer:");
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
        int colorChoice = (int) InputUtil.validateInput(scanner, "Wpisz numer koloru");
        Colors selectedColor = Colors.values()[colorChoice - 1];

        System.out.println("Wybierz pojemność baterii:");
        for (BatteryCapacity battery : BatteryCapacity.values()) {
            System.out.println(battery.ordinal() + 1 + ". " + battery + " - Cena: " + battery.getPrice() + " PLN");
        }
        int batteryChoice = (int) InputUtil.validateInput(scanner, "Wpisz numer baterii");
        BatteryCapacity selectedBattery = BatteryCapacity.values()[batteryChoice - 1];

        System.out.println("Wybierz akcesoria:");
        for (Accessories accessory : Accessories.values()) {
            System.out.println(accessory.ordinal() + 1 + ". " + accessory + " - Cena: " + accessory.getPrice() + " PLN");
        }
        int accessoryChoice = (int) InputUtil.validateInput(scanner, "Wpisz numer akcesorii");
        Accessories selectedAccessories = Accessories.values()[accessoryChoice - 1];

        System.out.println("Wpisz nazwę:");
        String customName = scanner.nextLine();

        double totalPrice = basePrice + selectedColor.getPrice() + selectedBattery.getPrice() + selectedAccessories.getPrice();

        long randomId = random.nextLong(999);

        Product smartphone = new Product(randomId, customName, totalPrice, 1, Product.ProductType.SMARTPHONE);

        System.out.println("Skonfigurowany smartfon:");
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
        if (!directory.exists() && directory.mkdir()) {
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
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać promocji");
        }
    }

    private void loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Files/products.dat"));) {
            List<Product> products = (List<Product>) ois.readObject();
            productManager.setProducts(products);
            System.out.println("Wczytano listę produktów.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nie udało się wczytać produktów z pliku.");
        }
    }

    private void loadCart() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Files/cart.dat"));) {
            List<Product> cartItems = (List<Product>) ois.readObject();
            cart.setItems(cartItems);
            System.out.println("Wczytano zawartość koszyka.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nie udało się wczytać zawartości koszyka z pliku.");
        }
    }

    private void loadDiscounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Files/discounts.dat"));) {
            List<DiscountRule> discounts = (List<DiscountRule>) ois.readObject();
            discount.setDiscountRules(discounts);
            System.out.println("Wczytano promocje.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nie udało się wczytać promocji z pliku.");
        }
    }

}
