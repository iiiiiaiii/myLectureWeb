package HelloShop.shop.project_1.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;


@Getter
@MappedSuperclass
public abstract class Item {
    private int price;

    @Column(unique = true)
    private String name;

    public Item(int price, String name) {
        this.price=price;
        this.name=name;
    }

    protected Item() {
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name=name;
    }
}
