package HelloShop.shop.project_1;

import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Lecturer;
import HelloShop.shop.project_1.domain.member.Parent;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.domain.myClass;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Student student = createStudent("1", "1", 1, "1", "1", "1", "1");
            Parent parent = createParent("1", "1", 1, "1", student);
            Lecturer lecturer = createLecturer("1", "1", 1, "1", myClass.국어);
            Lecture lecture = createLecture(1, "1", lecturer);
            Book book = createBook(1, lecture, "1", 1, "1", "1");

        }

        private  Student createStudent(String name, String password, int age, String loginId, String city, String zipcode, String street) {
            Student student = new Student(age, name, password, loginId, new Address(city, zipcode, street));
            em.persist(student);
            return student;
        }

        private  Parent createParent(String name, String password, int age, String loginId,Student student) {
            Parent parent = new Parent(age, name, password, loginId, student);
            em.persist(parent);
            return parent;
        }

        private  Lecturer createLecturer(String name, String password, int age, String loginId, myClass myClass) {
            Lecturer lecturer = new Lecturer(age, name, password, loginId, myClass);
            em.persist(lecturer);
            return lecturer;
        }

        private  Lecture createLecture(int price,String name,Lecturer lecturer)  {
            Lecture lecture = new Lecture(price, name, lecturer);
            em.persist(lecture);
            return lecture;
        }

        private  Book createBook(int price, Lecture lecture, String name, int stockQuantity, String author, String isbn) {
            Book book = new Book(price, lecture, name, stockQuantity, author, isbn);
            lecture.getLecturer().addBook(book);
            em.persist(book);
            return book;
        }
    }
}
