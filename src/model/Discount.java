package model;

import services.DiscountRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Discount {
    private List<DiscountRule> discountRules;

    Random random = new Random();

    public Discount() {
        this.discountRules = new ArrayList<>();
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

        discountRules.add(new DiscountRule(threshold, percentageOff, random.nextLong(999999)));
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

}
