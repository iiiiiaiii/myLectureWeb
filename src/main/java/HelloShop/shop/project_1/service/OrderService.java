package HelloShop.shop.project_1.service;

import HelloShop.shop.project_1.domain.delivery.Delivery;
import HelloShop.shop.project_1.domain.delivery.DeliveryStatus;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Item;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.domain.order.OrderBase;
import HelloShop.shop.project_1.domain.order.OrderBook;
import HelloShop.shop.project_1.domain.order.OrderLecture;
import HelloShop.shop.project_1.pay.DiscountPolicy;
import HelloShop.shop.project_1.pay.PolicyGrade;
import HelloShop.shop.project_1.pay.Save;
import HelloShop.shop.project_1.repository.ItemRepository;
import HelloShop.shop.project_1.repository.MemberRepository;
import HelloShop.shop.project_1.repository.OrderRepository;
import HelloShop.shop.project_1.repository.OrderSearch;
import HelloShop.shop.project_1.repository.order.query.OrderBookQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderItemQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderLectureQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderQueryRepository orderQueryRepository;

    @Transactional
    public Long orderBase(String memberId, Long id, int count, Class<? extends Item> entityClass) {
        Optional<Student> byLoginId = memberRepository.findByLoginId(Student.class, memberId);
        Student findMember = byLoginId.get();
        Item findItem = itemRepository.findByLongItems(entityClass,id);
        if (findItem.getClass() == Lecture.class) {
            Lecture findLecture = (Lecture) findItem;
            findMember.addLecture(findLecture);
            System.out.println("findMember = " + findMember.getLectures().get(0).getName());
            int price = discountSet(findLecture.getPrice(), findMember);
            System.out.println("price = " + price);
            OrderLecture orderLecture = OrderLecture.createOrderLecture(findLecture, price);
            System.out.println("orderLecture = " + orderLecture);
            OrderBase orderBase = OrderBase.createLecture(findMember, orderLecture);
            System.out.println("orderBase2 = " + orderBase);
            orderRepository.save(orderBase);
            System.out.println("orderBase = " + orderBase);
            return orderBase.getId();
        }
        Book book = (Book) findItem;
        Delivery delivery=new Delivery();
        delivery.setStatus(DeliveryStatus.준비);
        delivery.setAddress(findMember.getAddress());

        int price = discountSet(book.getPrice(), findMember);
        OrderBook orderBook = OrderBook.createOrderBook(book, count, price);
        OrderBase orderBase = OrderBase.createBook(findMember, delivery, orderBook);
        findMember.getBooks().add(book);
        orderRepository.save(orderBase);
        return orderBase.getId();
    }


    /**
     * 주문검색*/
    public List<OrderBase> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }


    @Transactional
    public void cancelOrderLecture(Long orderId) {
        OrderBase order = orderRepository.findOne(orderId);
        Student student = order.getStudent();
        rollback(order);
        student.getLectures().remove(order.getOrderLecture().getLecture());

        order.cancelLecture();
        orderRepository.cancelOrderLecture(order,order.getOrderLecture());
    }
    @Transactional
    public void cancelOrderBook(Long orderId) {
        //주문 엔티티 조회
        System.out.println("orderId = " + orderId);
        OrderBase order = orderRepository.findOne(orderId);
        List<Book> studentBook = order.getStudent().getBooks();
        System.out.println("studentBook.get(0).getId() = " + studentBook.get(0).getId());
        //정보수정
        rollback(order);
        List<OrderBook> orderBooks = order.getOrderBooks();
        for (OrderBook orderBook : orderBooks) {
            studentBook.remove(orderBook.getBook());
        }
        //주문 취소
        order.cancelBooks();
        orderRepository.cancelOrderBook(order,order.getOrderBooks());
    }




    public List<OrderLectureQueryDto> findLectureDto() {
        return orderQueryRepository.findAllByDto_optimization_Lecture();
    }

    public List<OrderBookQueryDto> findBookDto() {
        return orderQueryRepository.findAllByDto_optimization_Book();
    }


    public List<OrderBookQueryDto> findBookOne(String id) {
        return orderQueryRepository.findBookDtoByStudentId(id);
    }

    public List<OrderLectureQueryDto> findLectureOne(String id) {
        return orderQueryRepository.findLectureByStudentId(id);
    }

    public List<OrderItemQueryDto> findOrders(List<Long> ids) {
        return orderQueryRepository.findOrders(ids);
    }

    private static void rollback(OrderBase order) {
        Student student = order.getStudent();
        int current = student.getPay();
        int newPrice = order.getPrice();
        student.setPay(current-newPrice);
        Save.gradeSet(student);
    }

    private static int discountSet(int findLecture, Student findMember) {
        DiscountPolicy discountPolicy = new PolicyGrade();
        return discountPolicy.DiscountPrice(findLecture, findMember);
    }


}
