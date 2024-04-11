package HelloShop.shop.project_1.controller;

import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.domain.order.OrderBase;
import HelloShop.shop.project_1.domain.order.OrderStatus;
import HelloShop.shop.project_1.dto.BookDTO;
import HelloShop.shop.project_1.dto.LectureDTO;
import HelloShop.shop.project_1.repository.order.query.OrderBookQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderItemQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderLectureQueryDto;
import HelloShop.shop.project_1.service.ItemService;
import HelloShop.shop.project_1.service.MemberService;
import HelloShop.shop.project_1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/{studentId}/studentHome/orderBook")
    public String createBookForm(Model model,
                                 @PathVariable("studentId") String studentId) {
        List<BookDTO> allBooks = itemService.findAllBookDto();
        model.addAttribute("allBooks", allBooks);

        return "order/orderBookForm";
    }

    @PostMapping("/{studentId}/studentHome/orderBook")
    public String orderBook(@PathVariable("studentId") String studentId,
                            @RequestParam("bookId") String bookName,
                            @RequestParam("count") int count) {
        orderService.orderBase(studentId, bookName, count, Book.class);
        return "redirect:/" + studentId + "/orders";
    }

    @GetMapping("/{studentId}/studentHome/orderLecture")
    public String createLectureForm(Model model,
                                    @PathVariable("studentId") String studentId) {
        List<LectureDTO> allLectures = itemService.findAllLectureDto();
        model.addAttribute("allLectures", allLectures);
        return "order/orderLectureForm";
    }


    @PostMapping("/{studentId}/orderBook")
    public String orderListBook(Model model,
                                @PathVariable("studentId") String studentId,
                                @RequestParam("bookId") String bookId,
                                @RequestParam("count") int count) {
        orderService.orderBase(studentId, bookId, count, Book.class);
        List<OrderBookQueryDto> bookDto = orderService.findBookOne(studentId);
        model.addAttribute("bookDto", bookDto);
        return "order/orderList2";
    }

    @PostMapping("/{studentId}/orderLecture")
    public String orderListLecture(@PathVariable("studentId") String studentId,
                                   @RequestParam("lectureId") String lectureId,
                                   Model model){
        if (itemService.duplicateCheck(studentId, lectureId)) {
            model.addAttribute("duplicateError", "이미 신청한 강의입니다");
            List<LectureDTO> allLectures = itemService.findAllLectureDto();
            model.addAttribute("allLectures", allLectures);
            return "order/orderLectureForm";
        }

        orderService.orderBase(studentId, lectureId, 1, Lecture.class);
        List<OrderLectureQueryDto> lectureDto = orderService.findLectureOne(studentId);
        model.addAttribute("lectureDto", lectureDto);
        return "order/orderList";
    }


    @GetMapping("/{studentId}/studentHome/orderStatus")
    public String orderStatus(@PathVariable("studentId") String studentId,
                              @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus,
                              Model model) {
        Optional<Student> studentOptional = memberService.findId(Student.class, studentId);
        if (studentOptional.isEmpty()) {
            return "redirect:/login";
        }
        Student student = studentOptional.get();

        List<OrderBase> orderBases;
        if (orderStatus != null) {
            // 주문 상태에 따라 필터링된 주문 목록 가져오기
            orderBases = student.getOrderBases().stream()
                    .filter(order -> order.getStatus() == orderStatus)
                    .collect(Collectors.toList());
        } else {
            // 주문 상태를 지정하지 않은 경우 모든 주문 목록 가져오기
            orderBases = new ArrayList<>(student.getOrderBases());
        }
        List<Long> ids= orderBases.stream()
                .map(OrderBase::getId)
                .toList();
        List<OrderItemQueryDto> orderBaseDTO = orderService.findOrders(ids);
        model.addAttribute("orderBases", orderBaseDTO);
        return "order/orderStatus";
    }

    //deleteOrder
    @GetMapping("{studentId}/studentHome/cancelOrderLecture")
    public String cancelLecture(@PathVariable("studentId") String studentId,
                             Model model) {
        List<OrderLectureQueryDto> allOrderLecture = orderService.findLectureOne(studentId);
        model.addAttribute("allOrderLecture", allOrderLecture);
        return "order/cancelLectureOrder";
    }
    @PostMapping("{studentId}/studentHome/cancelOrderLecture")
    public String cancelLecture2(@PathVariable("studentId") String studentId,
                             @RequestParam("lectureId")Long lectureId) {
        orderService.cancelOrderLecture(lectureId);
        return "redirect:/{studentId}/studentHome/lecturelist";
    }

    @GetMapping("{studentId}/studentHome/cancelOrderBook")
    public String cancelBook(@PathVariable("studentId") String studentId,
                             Model model) {
        List<OrderBookQueryDto> allOrderBook = orderService.findBookOne(studentId);
        model.addAttribute("allOrderBook", allOrderBook);
        return "order/cancelBookOrder";
    }
    @PostMapping("{studentId}/studentHome/cancelOrderBook")
    public String cancelBook2(@PathVariable("studentId") String studentId,
                              @RequestParam("bookId")Long bookId) {
        orderService.cancelOrderBook(bookId);
        return "redirect:/{studentId}/studentHome/booklist";
    }
}
