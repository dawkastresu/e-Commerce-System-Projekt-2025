package Services;

import Model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductManager {

    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Long id) {
        products.removeIf(product -> id.equals(product.getId()));
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(updatedProduct.getId())) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    public Optional<Product> getProduct(Long id) {
        return products.stream()
                .filter(product -> id.equals(product.getId())).findFirst();
    }

    public List<Product> getAllProducts() {
        return products;
    }


}
