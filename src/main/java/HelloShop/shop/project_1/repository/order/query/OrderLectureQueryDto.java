package HelloShop.shop.project_1.repository.order.query;

import HelloShop.shop.project_1.domain.order.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderLectureQueryDto {
    private Long orderId;
    private String LectureName;
    private String LecturerName;
    private int price;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private List<OrderLectureDTO> orderLecture;

    public OrderLectureQueryDto(Long orderId, String lectureName, String lecturerName, int price, LocalDateTime orderDate, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.LectureName = lectureName;
        this.LecturerName = lecturerName;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public OrderLectureQueryDto(Long orderId, String lectureName, String lecturerName, int price, LocalDateTime orderDate, OrderStatus orderStatus, List<OrderLectureDTO> orderLecture) {
        this.orderId = orderId;
        this.LectureName = lectureName;
        this.LecturerName = lecturerName;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderLecture = orderLecture;
    }
}
