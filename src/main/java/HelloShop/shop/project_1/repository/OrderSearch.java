package HelloShop.shop.project_1.repository;

import HelloShop.shop.project_1.domain.order.OrderStatus;
import lombok.Data;

@Data
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}