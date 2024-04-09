package HelloShop.shop.project_1.controller;

import HelloShop.shop.project_1.controller.form.LecturerForm;
import HelloShop.shop.project_1.controller.form.ParentForm;
import HelloShop.shop.project_1.controller.form.StudentForm;
import HelloShop.shop.project_1.domain.Address;
import HelloShop.shop.project_1.domain.member.Lecturer;
import HelloShop.shop.project_1.domain.member.Parent;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.domain.myClass;
import HelloShop.shop.project_1.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    //student
    @GetMapping("/members/new")
    public String FormList() {
        return "members/createMemberForm";
    }

    @GetMapping("/members/newStudent")
    public String newStudent(Model model) {
        model.addAttribute("studentForm",new StudentForm());
        return "members/createStudentForm";
    }

    @PostMapping("/members/newStudent")
    public String createStudent(@Valid StudentForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createStudentForm";
        }

        Optional<Student> findId = memberService.findId(Student.class, form.getLoginId());
        if (findId.isPresent()) {
            result.reject("IdError", "이미 존재하는 Id입니다");
            return "members/createStudentForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Student student = new Student(form.getAge(), form.getName(), form.getPassword(), form.getLoginId(), address);
        memberService.join(student);
        return "redirect:/";
    }

    //parent
    @GetMapping("/members/newParent")
    public String newParent(Model model) {
        model.addAttribute("parentForm",new ParentForm());
        return "members/createParentForm";
    }
    @PostMapping("/members/newParent")
    public String createParent(@Valid ParentForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createParentForm";

        }
        Optional<Student> findStudent = memberService.findId(Student.class, form.getChildId());
        if(findStudent.isEmpty()){
            result.reject("childIdError", "존재하지 않는 자녀 ID입니다.");
            return "members/createParentForm";
        }

        Student student = findStudent.get();

        Parent parent = new Parent(form.getAge(), form.getName(), form.getPassword(), form.getLoginId(), student);

        Optional<Parent> findId = memberService.findId(Parent.class, form.getLoginId());
        if (findId.isPresent()) {
            result.reject("IdError", "이미 존재하는 Id입니다");
            return "members/createParentForm";
        }


        memberService.join(parent);
        return "redirect:/";
    }

    //lecturer
    @GetMapping("/members/newLecturer")
    public String newLecturer(Model model) {
        model.addAttribute("lecturerForm",new LecturerForm());
        return "members/createLecturerForm";
    }

    @PostMapping("/members/newLecturer")
    public String createLecturer(@Valid LecturerForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createLecturerForm";
        }
        myClass subject = myClass.valueOf(String.valueOf(form.getMyClass()));
        Lecturer lecturer = new Lecturer(form.getAge(), form.getName(), form.getPassword(), form.getLoginId(), subject);

        if (form.getMyClass() == null) {
            result.reject("error", "과목을 선택하세요");
            return "members/createLecturerForm";
        }



        Optional<Lecturer> findId = memberService.findId(Lecturer.class, form.getLoginId());
        if (findId.isPresent()) {
            result.reject("IdError", "이미 존재하는 Id입니다");
            return "members/createLecturerForm";
        }
        memberService.join(lecturer);
        return "redirect:/";
    }


    @GetMapping("/members")
    public String memberList(Model model) {
        List<?> allStudent = memberService.findAllDto(Student.class);
        List<?> allParent = memberService.findAllDto(Parent.class);
        List<?> allLecturer = memberService.findAllDto(Lecturer.class);


        model.addAttribute("allStudent", allStudent);
        model.addAttribute("allParent", allParent);
        model.addAttribute("allLecturer", allLecturer);
        return "members/memberList";
    }

}
