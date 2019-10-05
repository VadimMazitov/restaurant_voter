package ru.vadimmazitov.voter.model.to;

public class MealTO extends BaseTO {

    private int price;

    private String name;

    public MealTO() {
    }

    public MealTO(Integer id, int price, String name) {
        super(id);
        this.price = price;
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MealTO{" +
                ", id=" + id +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}
