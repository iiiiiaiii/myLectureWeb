package HelloShop.shop.project_1.repository.order.query;

import HelloShop.shop.project_1.domain.order.OrderBase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;


    public List<OrderBookQueryDto> findAllByDto_optimization_Book() {
        List<OrderBookQueryDto> ordersBook = findOrdersBook();
        Map<Long, List<OrderBookDTO>> orderBooksMap = findOrderBooksMap(toOrderBookIds(ordersBook));
        ordersBook.forEach(o -> o.setOrderBookDTOS(orderBooksMap.get(o.getOrderId())));
        return ordersBook;
    }

    public List<OrderLectureQueryDto> findAllByDto_optimization_Lecture() {
        List<OrderLectureQueryDto> ordersLecture = findOrdersLecture();
        Map<Long, List<OrderLectureDTO>> orderLecuturesMap = findOrderLecuturesMap(toOrderLectureIds(ordersLecture));
        ordersLecture.forEach(o -> o.setOrderLecture(orderLecuturesMap.get(o.getOrderId())));
        return ordersLecture;
    }

    public List<OrderLectureQueryDto> findLectureByStudentId(String studentId) {
        return findOrdersLecture(studentId);
    }
    public List<OrderBookQueryDto> findBookDtoByStudentId(String studentId) {
        return findOrdersBook(studentId);
    }

    public List<OrderItemQueryDto> findOrders(List<Long> ids) {
        return findOrderItems(ids);
    }

    private List<OrderItemQueryDto> findOrderItems(List<Long> orderId) {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderItemQueryDto(ob.id, ob.price)" +
                                " from OrderBase ob" +
                                " where ob.id in :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private Map<Long, List<OrderBookDTO>> findOrderBooksMap(List<Long> orderBookIds) {
        List<OrderBookDTO> orderBookDTOS = em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderBookDTO(ob.orderBase.id , b.name, b.lecture.name , b.lecturer.name, b.isbn, ob.count, b.author, b.price)" +
                                " from OrderBook ob" +
                                " join ob.book b" +  // 앞에서 가져온 order에 대한것을 한번에 다 가져옴
                                " where ob.id in :orderBookIds", OrderBookDTO.class
                ).setParameter("orderBookIds", orderBookIds)
                .getResultList();
        return orderBookDTOS.stream()
                .collect(Collectors.groupingBy(OrderBookDTO::getOrderId));
    }

    private Map<Long, List<OrderLectureDTO>> findOrderLecuturesMap(List<Long> orderLectureIds) {

        List<OrderLectureDTO> orderLectureDTOS = em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderLectureDto(ol.orderBase.id, i.name, ol.localDateTime, ol.price)" +
                                " from OrderLecture ol" +
                                " join ol.lecture i" +  // 앞에서 가져온 order에 대한것을 한번에 다 가져옴
                                " where ol.orderBase.id in :orderLectureIds", OrderLectureDTO.class
                ).setParameter("orderLectureIds", orderLectureIds)
                .getResultList();
        return orderLectureDTOS.stream()
                .collect(Collectors.groupingBy(OrderLectureDTO::getOrderId));
    }

    private static List<Long> toOrderBookIds(List<OrderBookQueryDto> result) {
        return result.stream()
                .map(OrderBookQueryDto::getOrderId)
                .collect(Collectors.toList());
    }

    private static List<Long> toOrderLectureIds(List<OrderLectureQueryDto> result) {
        return result.stream()
                .map(OrderLectureQueryDto::getOrderId)
                .collect(Collectors.toList());
    }

    private List<OrderBookQueryDto> findOrdersBook() {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderBookQueryDto(o.id, o.orderDate, o.status , d.address, d.status)" +
                                " from OrderBase o" +
                                " join o.delivery d", OrderBookQueryDto.class
                )
                .getResultList();
    }


    private List<OrderBookQueryDto> findOrdersBook(String studentId) {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderBookQueryDto(ob.id, ob.book.name, ob.price, ob.count , ob.orderBase.orderDate, ob.orderBase.status , d.address, d.status)" +
                                " from OrderBook ob" +
                                " join ob.orderBase.delivery d" +
                                " where ob.orderBase.student.id = : studentId", OrderBookQueryDto.class
                )
                .setParameter("studentId", studentId)
                .getResultList();
    }

    private List<OrderLectureQueryDto> findOrdersLecture() {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderLectureQueryDto(o.id, o.orderDate, o.status)" +
                                " from OrderBase o" , OrderLectureQueryDto.class
                )
                .getResultList();
    }
    private List<OrderLectureQueryDto> findOrdersLecture(String studentId) {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderLectureQueryDto(o.id , l.name, l.lecturer.name, ol.price, o.orderDate , o.status)" +
                                " from OrderBase o" +
                                " join o.orderLecture ol" +
                                " join ol.lecture l" +
                                " where o.student.id = : studentId", OrderLectureQueryDto.class
                )
                .setParameter("studentId", studentId)
                .getResultList();
    }
}