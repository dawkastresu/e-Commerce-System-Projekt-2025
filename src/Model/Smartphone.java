//package Model;
//
//public class Smartphone extends Product{
//
//    private Colors color;
//
//    private BatteryCapacity batteryCapacity;
//
//    private Accessories accessories;
//
//
//    public Smartphone(Long id, String name, double price, int quantity, Colors color, BatteryCapacity batteryCapacity, Accessories accessories) {
//        super(id, name, price, quantity, ProductType.SMARTPHONE);
//        this.color = color;
//        this.batteryCapacity = batteryCapacity;
//    }
//
//    public Smartphone(Colors color, BatteryCapacity batteryCapacity, Accessories accessories) {
//        super();
//        this.color = color;
//        this.batteryCapacity = batteryCapacity;
//        this.accessories = accessories;
//    }
//
//    public Accessories getAccessories() {
//        return accessories;
//    }
//
//    public void setAccessories(Accessories accessories) {
//        this.accessories = accessories;
//    }
//
//    public BatteryCapacity getBatteryCapacity() {
//        return batteryCapacity;
//    }
//
//    public void setBatteryCapacity(BatteryCapacity batteryCapacity) {
//        this.batteryCapacity = batteryCapacity;
//    }
//
//    public Colors getColor() {
//        return color;
//    }
//
//    public void setColor(Colors color) {
//        this.color = color;
//    }
//
//    @Override
//    public String toString() {
//        return super.toString() + ", " +
//                "Kolor: " + color + ", " +
//                "Pojemność baterii: " + batteryCapacity + ", " +
//                "Akcesoria: " + accessories + "\n" +
//                "-----------------------------------" + "\n";
//    }
//}