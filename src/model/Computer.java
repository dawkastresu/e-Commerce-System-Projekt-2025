package model;

public class Computer extends Product{

    private Processor processor;

    private RAM ramGB;


    public Computer(Long id, String name, double price, int quantity, Processor processor, RAM ramGB) {
        super(id, name, price, quantity, ProductType.COMPUTER);
        this.processor = processor;
        this.ramGB = ramGB;
    }

    public Computer(Processor processor, RAM ramGB) {
        super();
        this.processor = processor;
        this.ramGB = ramGB;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public RAM getRamGB() {
        return ramGB;
    }

    public void setRamGB(RAM ramGB) {
        this.ramGB = ramGB;
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                "Procesor: " + processor + ", " +
                "Ram GB: " + ramGB + "\n" +
                "-----------------------" + "\n";
    }
}
