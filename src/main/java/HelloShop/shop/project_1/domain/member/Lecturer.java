package HelloShop.shop.project_1.domain.member;

import HelloShop.shop.project_1.domain.myClass;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Lecture;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Lecturer extends Member {
    @Id
    @GeneratedValue
    @Column(name="lecturer_id")
    private Long id;

    @OneToMany(mappedBy = "lecturer")
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer")
    private List<Book> books = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private myClass myClass;

    protected Lecturer() {
    }

    public Lecturer(int age, String name, String password, String loginId, myClass myClass) {
        super(age, name, password, loginId);
        this.myClass = myClass;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setLecturer(this);
    }
}
