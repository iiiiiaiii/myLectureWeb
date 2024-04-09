package HelloShop.shop.project_1.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String name;
    private String author;
    private int price;
    private int stock;
    private String lecture;
    private String lecturer;
    private String isbn;


    public BookDTO(Long id, String name, String author, int price, int stock, String lecture, String lecturer, String isbn) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.lecture = lecture;
        this.lecturer = lecturer;
        this.isbn = isbn;
    }
}
