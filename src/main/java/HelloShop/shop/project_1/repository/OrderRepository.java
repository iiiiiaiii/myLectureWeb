package HelloShop.shop.project_1.repository;

import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.domain.order.OrderBase;
import HelloShop.shop.project_1.domain.order.OrderBook;
import HelloShop.shop.project_1.domain.order.OrderLecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(OrderBase orderBase) {
        em.persist(orderBase);
    }
    public OrderBase findOne(Long id) {
        return em.find(OrderBase.class, id);
    }

    public List<OrderBase> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o From Order_Base o join o.student s";
        boolean isFirstCondition=true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition=false;
            }else{
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else{
                jpql += " and";
            }
            jpql += " s.name like :name";
        }
        TypedQuery<OrderBase> query = em.createQuery(jpql, OrderBase.class)
                .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<OrderBase> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderBase> cq = cb.createQuery(OrderBase.class);
        Root<OrderBase> o = cq.from(OrderBase.class);
        Join<OrderBase, Member> m = o.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName()
                    + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<OrderBase> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<OrderBase> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", OrderBase.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<OrderBase> findAllWithItem() {
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", OrderBase.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    public void cancelOrderLecture(OrderBase orderBase, OrderLecture orderLecture) {
        em.remove(orderLecture);
        em.remove(orderBase);
    }
    public void cancelOrderBook(OrderBase orderBase, List<OrderBook> orderBook) {
        em.remove(orderBook);
        em.remove(orderBase);
    }
}
