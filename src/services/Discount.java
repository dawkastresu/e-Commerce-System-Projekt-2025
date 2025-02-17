package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Discount {
    private List<DiscountRule> discountRules;

    public Discount() {
        this.discountRules = new ArrayList<>();
        // Initialize with default discount rules
        addDiscountRule(3000, 10);
        addDiscountRule(5000, 20);
    }

    public List<DiscountRule> getDiscountRules() {
        return discountRules;
    }

    public void setDiscountRules(List<DiscountRule> discountRules) {
        this.discountRules = discountRules;
    }

    public void addDiscountRule(double threshold, double percentageOff) {
        discountRules.add(new DiscountRule(threshold, percentageOff));
    }

    public void removeDiscountRule(DiscountRule discountRule) {
        discountRules.remove(discountRule);
    }

    public double applyDiscount(double totalPrice) {
        double highestDiscount = 0;
        for (DiscountRule rule : discountRules) {
            if (totalPrice >= rule.getThreshold() && rule.getPercentageOff() > highestDiscount) {
                highestDiscount = rule.getPercentageOff();
            }
        }
        return totalPrice * (1 - (highestDiscount / 100));
    }

    public void getApplicableDiscountInfo(double totalPrice) {
        for (DiscountRule rule : discountRules) {
            if (totalPrice >= rule.getThreshold()) {
                System.out.println("zastosowana zniżka: " + rule.getPercentageOff());
            }
        }
        System.out.println("Brak zniżki.");
    }

    public static class DiscountRule implements Serializable {
        private final double threshold;
        private final double percentageOff;

        public DiscountRule(double threshold, double percentageOff) {
            this.threshold = threshold;
            this.percentageOff = percentageOff;
        }

        public double getThreshold() {
            return threshold;
        }

        public double getPercentageOff() {
            return percentageOff;
        }

        @Override
        public String toString() {
            return "------------------------------------" + "\n" +
                    "Wartość procentowa obniżki: " + percentageOff + "\n" +
                    "Promocja na zakupy powyżej:  " + threshold;
        }
    }
}
