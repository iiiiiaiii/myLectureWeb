package HelloShop.shop.project_1.repository.order.query;

import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.delivery.DeliveryStatus;
import HelloShop.shop.project_1.domain.order.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data

@EqualsAndHashCode(of = "orderId")
public class OrderBookQueryDto {
    private Long orderId;
    private String bookName;
    private int price;
    private int count;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private DeliveryStatus deliveryStatus;
    private List<OrderBookDTO> orderBookDTOS;

    public OrderBookQueryDto(Long orderId, LocalDateTime localDateTime, OrderStatus orderStatus, Address address, DeliveryStatus deliveryStatus) {
        this.orderId = orderId;
        this.orderDate=localDateTime;
        this.orderStatus = orderStatus;
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }

    public OrderBookQueryDto(Long orderId, String bookName, int price, int count, LocalDateTime orderDate, OrderStatus orderStatus, Address address, DeliveryStatus deliveryStatus) {
        this.orderId = orderId;
        this.bookName = bookName;
        this.price = price;
        this.count = count;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }

    public OrderBookQueryDto(Long orderId, String bookName, int price, int count, LocalDateTime orderDate, OrderStatus orderStatus, Address address, DeliveryStatus deliveryStatus, List<OrderBookDTO> orderBookDTOS) {
        this.orderId = orderId;
        this.bookName = bookName;
        this.price = price;
        this.count = count;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.deliveryStatus = deliveryStatus;
        this.orderBookDTOS = orderBookDTOS;
    }
}




