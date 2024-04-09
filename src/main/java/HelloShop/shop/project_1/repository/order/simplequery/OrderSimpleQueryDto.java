package HelloShop.shop.project_1.repository.order.simplequery;

import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId =orderId;                        //
        this.name =name;                        ///lazy초기화 -> 영속성컨텍스트가 memberid를 통해 영속성컨텍스트를 찾아보고 데이터를 찾아옴
        this.orderDate =orderDate;                        //
        this.orderStatus =orderStatus;                        //
        this.address =address;                        //;
    }
}