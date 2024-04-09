package HelloShop.shop.project_1.dto;

import lombok.Data;

@Data
public class LecturerDTO {
    private String name;
    private int age;
    private String subject;

    public LecturerDTO(String name, int age, String subject) {
        this.name = name;
        this.age = age;
        this.subject = subject;
    }
}
