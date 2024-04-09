package HelloShop.shop.project_1.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderBookDTO {
    @JsonIgnore
    private Long orderId;
    private String bookName;
    private String lectureName;
    private String lecturerName;
    private String ISBN;
    private int count;
    private String author;
    private int price;

    public OrderBookDTO(Long orderId, String bookName, String lectureName, String lecturerName, String ISBN, int count, String author, int price) {
        this.orderId = orderId;
        this.bookName = bookName;
        this.lectureName = lectureName;
        this.lecturerName = lecturerName;
        this.ISBN = ISBN;
        this.count = count;
        this.author = author;
        this.price = price;
    }
}
