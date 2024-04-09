package HelloShop.shop.project_1.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentDTO {
    private String name;
    private int age;
    private List<String> bookList = new ArrayList<>();

    public StudentDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
