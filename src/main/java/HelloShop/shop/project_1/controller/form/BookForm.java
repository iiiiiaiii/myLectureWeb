package HelloShop.shop.project_1.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class BookForm {
    private Long id;
    @NotEmpty(message="이름을 입력하세요")
    private String name;
    @Positive(message = "가격을 입력하세요")
    private int price;
    @Positive(message = "재고를 입력하세요")
    private int stockQuantity;
    @NotEmpty(message = "강의는 필수입니다")
    private String lectureName;

    @NotEmpty(message = "저자를 입력하세요")
    private String author;
    @NotEmpty(message = "isbn을 입력하세요")
    private String isbn;
}
