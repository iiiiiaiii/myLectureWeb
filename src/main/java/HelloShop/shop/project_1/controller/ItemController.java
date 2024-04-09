package HelloShop.shop.project_1.controller;

import HelloShop.shop.project_1.controller.form.BookForm;
import HelloShop.shop.project_1.controller.form.LectureForm;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Lecturer;
import HelloShop.shop.project_1.domain.member.Parent;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.dto.BookDTO;
import HelloShop.shop.project_1.dto.LectureDTO;
import HelloShop.shop.project_1.service.ItemService;
import HelloShop.shop.project_1.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/items/new")
    public String FormList() {
        return "items/createItemForm";
    }

    @GetMapping("/items/newBook")
    public String createBookForm(Model model) {
        model.addAttribute("BookForm", new BookForm());
        return "items/createBookForm";
    }

    @PostMapping("/items/newBook")
    public String createItem(@Valid BookForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "/items/newBook";
        }
        Book book = new Book(form.getPrice(), itemService.findByName(Lecture.class, form.getLectureName()), form.getName(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.save(book);
        return "redirect:/";
    }

    @GetMapping("/booklist")
    public String bookList(Model model) {
        List<BookDTO> allBook = itemService.findAllBookDto();
        model.addAttribute("allBook", allBook);
        return "items/bookList";
    }

    @GetMapping("/{lecturerId}/lecturerHome/booklist")
    public String myBooks(Model model,
                          @PathVariable("lecturerId") String lecturerId) {
        List<BookDTO> allBook = itemService.findOneBookDto(Lecturer.class, lecturerId);
        model.addAttribute("allBook", allBook);
        return "items/bookList";
    }

    //createItem

    @GetMapping("/{lecturerId}/items/newBook")
    public String createBookForm(
            @PathVariable("lecturerId") String lecturerId,
            Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/createBookForm";

    }

    @PostMapping("/{lecturerId}/items/newBook")
    public String createBook(
            @PathVariable("lecturerId") String lecturerId,
            @Valid @ModelAttribute BookForm bookForm,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "items/createBookForm";
        }


        if (itemService.findByName(Book.class, bookForm.getName()) != null) {
            result.reject("nameError", "중복된 이름입니다");
            return "items/createBookForm";
        }


        Optional<Lecturer> id = memberService.findId(Lecturer.class, lecturerId);
        Lecturer lecturer = id.orElse(null);

        String lectureName = bookForm.getLectureName();


        Lecture findLecture = itemService.findByName(Lecture.class, lectureName);

        if (findLecture == null) {
            result.reject("error", "강의 이름을 확인해주세요");
            return "items/createBookForm";
        }


        Book book = new Book(bookForm.getPrice(), findLecture, bookForm.getName(), bookForm.getStockQuantity(), bookForm.getAuthor(), bookForm.getIsbn());
        assert lecturer != null;
        lecturer.addBook(book);
        itemService.save(book);
        return "redirect:/" + lecturerId + "/lecturerHome";
    }

    @GetMapping("/{lecturerId}/items/newLecture")
    public String createLectureForm(
            @PathVariable("lecturerId") String lecturerId,
            Model model) {
        model.addAttribute("lectureForm", new LectureForm());
        return "items/createLectureForm";
    }

    @PostMapping("/{lecturerId}/items/newLecture")
    public String createLecture(@Valid LectureForm lectureForm, BindingResult result
            , @PathVariable("lecturerId") String lecturerId) {
        if (result.hasErrors()) {
            return "items/createLectureForm";
        }
        Optional<Lecturer> id = memberService.findId(Lecturer.class, lecturerId);
        Lecturer lecturer = id.orElse(null);

        if (lectureForm.getLectureName() == null) {
            result.reject("noneLectureName", "강의 이름을 작성해주세요");
            return "items/createLectureForm";
        }
        if (itemService.findByName(Lecture.class, lectureForm.getLectureName()) != null) {
            result.reject("duplicateError", "이미 있는 강의 이름입니다");
            return "items/createLectureForm";
        }


        Lecture lecture = new Lecture(lectureForm.getPrice(), lectureForm.getLectureName(), lecturer);

        assert lecturer != null;
        lecturer.getLectures().add(lecture);
        itemService.save(lecture);
        return "redirect:/" + lecturerId + "/lecturerHome";
    }


    @GetMapping("/items")
    public String AllItems(Model model) {
        List<LectureDTO> allLecture = itemService.findAllLectureDto();
        List<BookDTO> allBook = itemService.findAllBookDto();
        model.addAttribute("allBook", allBook);
        model.addAttribute("allLecture", allLecture);
        return "items/itemList";
    }

    @GetMapping("/lecturelist")
    public String AllLecture(Model model) {
        List<LectureDTO> allLecture = itemService.findAllLectureDto();
        model.addAttribute("allLecture", allLecture);
        return "items/lectureList";
    }

    @GetMapping("/{lectureId}/lecturerHome/lecturelist")
    public String myLectures(Model model,
                             @PathVariable("lectureId") String Id) {
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Lecturer.class, Id);
        model.addAttribute("allLecture", allLecture);

        return "items/lectureList";
    }

    @GetMapping("/{studentId}/studentHome/lecturelist")
    public String myLecture(Model model,
                            @PathVariable("studentId") String Id) {
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Student.class, Id);
        model.addAttribute("allLecture", allLecture);
        return "items/lectureList";
    }

    @GetMapping("/{studentId}/studentHome/booklist")
    public String myBook(Model model,
                         @PathVariable("studentId") String Id) {
        List<BookDTO> allBook = itemService.findOneBookDto(Student.class, Id);
        model.addAttribute("allBook", allBook);
        return "items/bookList";
    }

    @GetMapping("/{studentId}/studentHome/orderlist")
    public String myOrder(Model model,
                          @PathVariable("studentId") String Id) {
        List<BookDTO> allBook = itemService.findOneBookDto(Student.class, Id);
        model.addAttribute("allBook", allBook);
        return "items/bookList";
    }


    //parent
    @GetMapping("/{parentId}/parentHome/lecturelist")
    public String studentLecture(@PathVariable("parentId") String parentId,
                                 Model model) {
        Optional<Parent> id = memberService.findId(Parent.class, parentId);
        Parent parent = id.get();
        Student student = parent.getStudent();
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Student.class, student.getLoginId());
        model.addAttribute("allLecture", allLecture);
        return "items/lectureList";
    }


    //update

    @GetMapping("/{lecturerId}/updateLecture")
    public String updateLecture(@PathVariable("lecturerId") String lecturerId,
                                Model model) {
        List<?> findLecture = itemService.findOneItems(Lecturer.class, Lecture.class, lecturerId);
        model.addAttribute("findLecture", findLecture);
        model.addAttribute("lectureForm", new LectureForm());
        return "items/updateLectureForm";
    }

    @PostMapping("/{lecturerId}/updateCompleteLecture")
    public String updateLecture2(@PathVariable("lecturerId") String lecturerId,
                                 @ModelAttribute LectureForm lectureForm,
                                 Model model) {
        itemService.updateLecture(lectureForm);
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Lecturer.class, lecturerId);
        model.addAttribute("allLecture", allLecture);
        return "redirect:/" + lecturerId + "/lecturerHome/lecturelist";
    }


    @GetMapping("/{lecturerId}/updateBook")
    public String updateBook(@PathVariable("lecturerId") String lecturerId,
                             Model model) {
        List<?> findBook = itemService.findOneItems(Lecturer.class, Book.class, lecturerId);
        model.addAttribute("findBook", findBook);
        model.addAttribute("bookForm", new BookForm());
        return "items/updateBookForm";
    }

    @PostMapping("/{lecturerId}/updateCompleteBook")
    public String updateBook2(@PathVariable("lecturerId") String lecturerId,
                              @ModelAttribute BookForm bookForm,
                              Model model) {
        itemService.updateBook(bookForm);
        List<BookDTO> allBook = itemService.findOneBookDto(Lecturer.class, lecturerId);
        model.addAttribute("allBook", allBook);
        return "redirect:/" + lecturerId + "/lecturerHome/booklist";
    }

    //delete item
    @GetMapping("/{lecturerId}/cancelLecture")
    public String deleteLecture(@PathVariable("lecturerId") String lecturerId,
                                Model model) {
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Lecturer.class, lecturerId);
        model.addAttribute("allLecture", allLecture);
        return "items/cancelLecture";
    }

    @PostMapping("/{lecturerId}/cancelLecture")
    public String deleteLecture2(@PathVariable("lecturerId") String lecturerId,
                                 @RequestParam("lectureId") Long lectureId,
                                 Model model) {
        Lecture lecture = itemService.findByIdLecture(lectureId);
        itemService.delete(lecture);
        List<LectureDTO> allLecture = itemService.findOneLectureDto(Lecturer.class, lecturerId);
        model.addAttribute("allLecture", allLecture);
        return "redirect:/{lecturerId}/lecturerHome/lecturelist";
    }

    @GetMapping("/{lecturerId}/cancelBook")
    public String deleteBook(@PathVariable("lecturerId") String lecturerId,
                                Model model) {
        List<BookDTO> allBook = itemService.findOneBookDto(Lecturer.class, lecturerId);
        model.addAttribute("allBook", allBook);
        return "items/cancelBook";
    }

    @PostMapping("/{lecturerId}/cancelBook")
    public String deleteBook2(@PathVariable("lecturerId") String lecturerId,
                                 @RequestParam("bookId") Long bookId,
                                 Model model) {
        Book book = itemService.findByIdBook(bookId);
        itemService.delete(book);
        List<BookDTO> allBook = itemService.findOneBookDto(Lecturer.class, lecturerId);
        model.addAttribute("allBook", allBook);
        return "redirect:/{lecturerId}/lecturerHome/booklist";
    }
}