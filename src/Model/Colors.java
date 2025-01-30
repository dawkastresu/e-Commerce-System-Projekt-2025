package Model;

public enum Colors {

    GREY(0),

    NAVY(0),

    GOLDEN_ROSE(500),

    WHITE_PEARL(300),

    BLACK_ONYX(300);


    public final double price;

    Colors(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
