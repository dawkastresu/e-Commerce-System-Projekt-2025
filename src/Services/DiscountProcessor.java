package Services;

import java.util.ArrayList;
import java.util.List;

public class DiscountProcessor {

    private List<Discount> discountList = new ArrayList<>();


    public List<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<Discount> discountList) {
        this.discountList = discountList;
    }

    public void addDiscount(Discount discount) {
        discountList.add(discount);
    }

    public void removeDiscountById(long id) {
        for (Discount discount : discountList) {
            discountList.remove(discount);
        }
    }

}
