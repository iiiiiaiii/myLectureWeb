package HelloShop.shop.project_1.domain.member;

import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.order.OrderBase;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class Student extends Member {

    @Id
    @GeneratedValue
    @Column(name="student_id")
    private Long id;

    @Embedded
    private Address address;

    private int pay;
    private int mileage;
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @OneToMany(fetch = LAZY)
    @JoinColumn(name="lectures")
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(fetch = LAZY)
    @JoinColumn(name="books")
    private List<Book> books = new ArrayList<>();


    @OneToMany(mappedBy = "student")
    private List<OrderBase> orderBases = new ArrayList<>();

    public Student() {
    }

    public Student(int age, String name, String password, String loginId, Address address) {
        super(age, name, password, loginId);
        this.address = address;
        this.grade = Grade.일반;
        this.pay=0;
    }

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
