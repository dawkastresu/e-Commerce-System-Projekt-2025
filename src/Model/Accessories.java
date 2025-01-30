package Model;

public enum Accessories {

    FOIL_COVER(50),

    CASE(100),

    HEADPHONES(400),

    HOLDER(100),

    INSURANCE(500);


    public final double price;

    Accessories(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
