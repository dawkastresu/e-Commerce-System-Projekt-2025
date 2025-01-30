# 🛒 Online Shop System

## 📝 Project Description
The goal of this project is to create a complete online shop system in Java. The system supports product management, order handling, customer interaction, and is easily configurable for future extensions.

## 🚀 Prerequisites
- **Git**: Initialize a Git repository for the project.
- **GitHub Flow**: Use feature branches for development, and demonstrate pull requests/merge requests for new features.

## 💻 Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/dawkastresu/e-Commerce-system.git
   ```
2. Open the project in your IDE (e.g., IntelliJ IDEA).

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🛠️ Customizable Products | Users can create their own custom devices, choosing parameters such as RAM, processor, color, etc. |
| 💾 Data Management | Automatic saving of shopping cart status and product list updates to file. |
| 📳 Admin Mode | Administrators can manage products from the user interface, adding or editing items directly. |
| 🧮 Extensible Architecture | Designed according to high programming standards for easy future updates and functionality extensions. |
| 😊 Command-line Interface | User-friendly interface for seamless interaction. |

## 📦 Available Product Types

1. 💻 **Computer**
    - Configurable specifications (e.g., processor, RAM)

2. 📱 **Smartphone**
    - Customizable color, battery capacity, and accessories

3. 🔌 **Electronics**
    - No special configuration required

## 🚀 Usage
1. Run the application.
2. Use the command-line interface to:
    - View products
    - Add items to the cart
    - Place orders
    - Configure custom devices
    - Manage admin panel

## 🛠️ Method Functions

### CommandLineInterface Class

| Method | Description |
|--------|-------------|
| `viewProducts()` | Browses and displays all available products. |
| `addToCart()` | Displays products with prices and IDs, allows selection and addition to cart. |
| `viewCart()` | Browses cart products and displays total price. |
| `placeOrder()` | Places an order, saves date/time, processes order, clears cart, and generates invoice. |
| `configSpecs()` | Creates a new custom device using `configComputer()` and `configSmartphone()`. |
| `adminMode()` | Adds new or edits current items using `addProductAdminMode()` and `editProductAdminMode()`. |
| `saveProductsAndCart()` | Saves cart state and updates product list using ObjectOutputStream. |
| `editProductAdminMode()` | Edits all parameters of available products. |
| `addProductAdminMode()` | Adds new devices to the store. |
| `configComputer()` | Creates a custom computer product with configurable components. |
| `configSmartphone()` | Creates a custom smartphone product with configurable components and accessories. |

### OrderProcessor Class

| Method | Description |
|--------|-------------|
| `processOrder(Order order)` | Asynchronously processes orders, adds to order list, and generates invoice. |
| `generateInvoice(Order order)` | Generates, displays, and saves invoice for a specific order. |

### Cart Class

| Method | Description |
|--------|-------------|
| `getTotalPrice()` | Calculates and displays the total price of all products in the cart. |

### Discount Class

| Method | Description |
|--------|-------------|
| `applyDiscount(double totalPrice)` | Applies discount to the total price. |

## 👥 Authors
- **Dawid Malczewski** - [GitHub Profile](https://github.com/dawkastresu)

---