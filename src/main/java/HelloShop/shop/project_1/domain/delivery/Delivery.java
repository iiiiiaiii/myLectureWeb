package HelloShop.shop.project_1.domain.delivery;

import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.order.OrderBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;


    @OneToOne(mappedBy = "delivery",fetch = LAZY)
    private OrderBase orderBase;

    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public void setOrderBase(OrderBase orderBase) {
        this.orderBase = orderBase;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
