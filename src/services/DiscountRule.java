package services;

import java.io.Serializable;

public class DiscountRule implements Serializable {

    private final double threshold;

    private final double percentageOff;

    private final long id;

    public DiscountRule(double threshold, double percentageOff, long id) {
        this.threshold = threshold;
        this.percentageOff = percentageOff;
        this.id = id;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getPercentageOff() {
        return percentageOff;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "------------------------------------" + "\n" +
                "Wartość procentowa obniżki: " + percentageOff + "\n" +
                "Promocja na zakupy powyżej:  " + threshold;
    }
}

