package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Discount {

    private String description;

    private Predicate<Double> condition;

    private double percentage;


    public Discount(Predicate<Double> condition, String description, double percentage) {
        this.condition = condition;
        this.description = description;
        this.percentage = percentage;
    }


    public double applyDiscount(double totalPrice) {
        if (condition.test(totalPrice)) {
            return totalPrice * (1 - percentage / 100);
        }
        return totalPrice;
    }

    public Predicate<Double> getCondition() {
        return condition;
    }

    public void setCondition(Predicate<Double> condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return
                "--- " + description + '\n' +
                "Wartość Promocji: " + percentage + '\n';
    }
}