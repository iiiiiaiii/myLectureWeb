package HelloShop.shop.project_1.repository.item.query;

import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.dto.BookDTO;
import HelloShop.shop.project_1.dto.LectureDTO;
import HelloShop.shop.project_1.repository.order.query.OrderBookDTO;
import HelloShop.shop.project_1.repository.order.query.OrderBookQueryDto;
import HelloShop.shop.project_1.repository.order.query.OrderLectureDTO;
import HelloShop.shop.project_1.repository.order.query.OrderLectureQueryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {
    @PersistenceContext
    private final EntityManager em;

    public List<BookDTO> findAllByDto_optimization_Book() {
        return findAllBook();
    }

    public List<BookDTO> findOneBookDto(Class<? extends Member> entityClass, String id) {
        return findOneBook(entityClass,id);
    }

    public List<LectureDTO> findOneLectureDto(Class<? extends Member> entityClass, String id) {
        return findOneLecture(entityClass,id);
    }

    public List<LectureDTO> findAllByDto_optimization_Lecture() {
        return findAllLecture();
    }

    private Map<Long, List<OrderBookDTO>> findOrderBooksMap(List<Long> orderBookIds) {
        List<OrderBookDTO> orderBookDTOS = em.createQuery(
                        "select new HelloShop.shop.project_1.repository.order.query.OrderBookDTO(ob.id , b.name, b.lecture.name , b.lecturer.name, b.isbn, ob.count, b.author, b.price)" +
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
    private List<BookDTO> findOneBook(Class<?> entityClass,String loginId) {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.dto.BookDTO(b.id ,b.name , b.author , b.price, b.stockQuantity, b.lecture.name, b.lecturer.name , b.isbn)" +
                                " from "+  entityClass.getSimpleName() +" s" +
                                " join s.books b" +
                                " where s.loginId = :loginId", BookDTO.class
                )
                .setParameter("loginId", loginId)
                .getResultList();
    }

    private List<LectureDTO> findOneLecture(Class<?> entityClass,String loginId) {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.dto.LectureDTO(l.id ,l.name, l.price, l.lecturer.name)" +
                                " from "+ entityClass.getSimpleName() +" s" +
                                " join s.lectures l" +
                                " where s.loginId = :loginId", LectureDTO.class
                )
                .setParameter("loginId", loginId)
                .getResultList();
    }


    private List<BookDTO> findAllBook() {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.dto.BookDTO(b.id, b.name , b.author , b.price, b.stockQuantity, b.lecture.name, b.lecturer.name , b.isbn)" +
                                " from Book b" , BookDTO.class
                )
                .getResultList();
    }


    private List<LectureDTO> findAllLecture() {
        return em.createQuery(
                        "select new HelloShop.shop.project_1.dto.LectureDTO(l.id ,l.name, l.price, l.lecturer.name)" +
                                " from Lecture l" ,LectureDTO.class
                )
                .getResultList();
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



}
