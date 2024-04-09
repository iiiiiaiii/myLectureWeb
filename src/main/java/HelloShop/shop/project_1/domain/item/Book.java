package HelloShop.shop.project_1.domain.item;

import HelloShop.shop.project_1.domain.exception.NotEnoughException;
import HelloShop.shop.project_1.domain.member.Lecturer;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class Book extends Item {
    @Id
    @GeneratedValue
    @Column(name="book_id")
    private Long id;

    private int stockQuantity;
    private String author;
    private String isbn;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock<0){
            throw new NotEnoughException("need more stock");
        }
        this.stockQuantity=restStock;
    }

    protected Book() {
    }

    public Book(int price, Lecture lecture, String name, int stockQuantity, String author, String isbn) {
        super(price, name);
        this.stockQuantity = stockQuantity;
        this.lecture = lecture;
        this.author = author;
        this.isbn = isbn;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
