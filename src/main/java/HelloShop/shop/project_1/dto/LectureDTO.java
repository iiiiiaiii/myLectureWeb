package HelloShop.shop.project_1.dto;

import lombok.Data;

@Data
public class LectureDTO {
    private Long id;
    private String name;
    private int price;
    private String lecturerName;

    public LectureDTO(Long id, String name, int price, String lecturerName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.lecturerName = lecturerName;
    }
}
