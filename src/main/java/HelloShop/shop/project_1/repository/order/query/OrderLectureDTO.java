package HelloShop.shop.project_1.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderLectureDTO {

    @JsonIgnore
    private Long orderId;
    private String LectureName;
    private LocalDateTime localDateTime;
    private int orderPrice;

    public OrderLectureDTO(Long orderId, String lectureName, LocalDateTime localDateTime, int orderPrice) {
        this.orderId = orderId;
        LectureName = lectureName;
        this.localDateTime = localDateTime;
        this.orderPrice = orderPrice;
    }
}
