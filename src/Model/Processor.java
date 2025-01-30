package Model;

public enum Processor {

    INTEL_I3(500),

    INTEL_I5(800),

    INTEL_I7(1200),

    INTEL_I9(2000),

    AMD_RYZEN_5(750),

    AMD_RYZEN_7(1100),

    AMD_RYZEN_9(1800);



    private final double price;

    Processor(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
