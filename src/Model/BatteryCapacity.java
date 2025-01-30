package Model;

public enum BatteryCapacity {

    MAH2000(0),

    MAH2800(400),

    MAH3400(550),

    MAH4800(1200);


    public final double price;

    BatteryCapacity(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

}
