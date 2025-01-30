package Model;

public enum RAM {

    RAM_8GB(200),

    RAM_16GB(400),

    RAM_32GB(800),

    RAM_64GB(1600);


    private final double price;

    RAM(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
