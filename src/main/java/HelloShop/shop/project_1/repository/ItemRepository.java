package HelloShop.shop.project_1.repository;


import HelloShop.shop.project_1.domain.item.Item;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    @PersistenceContext
    private final EntityManager em;
    public void save(Item item){
        em.persist(item);
    }
    public <T> List<T> findAll(Class<T> entityClass) {
        return em.createQuery("select i from " + entityClass.getSimpleName() + " i", entityClass)
                .getResultList();
    }

    public <T> T findOne(Class<T> entityClass, Long id) {
        return em.find(entityClass, id);
    }

    public <T> T findByName(Class<T> entityClass, String name) {
        try {
            return em.createQuery("select m from " + entityClass.getSimpleName() + " m where m.name = :name", entityClass)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }


    public List<?> findByIdItems(Class<? extends Member> memberClass, Class<? extends Item> itemClass , String id) {
        String itemClassSimpleName = itemClass.getSimpleName();
        try {
            return em.createQuery("select l from " + memberClass.getSimpleName() + " m" +
                            " join m."+itemClassSimpleName.toLowerCase() +"s l" +
                            " where m.loginId = :loginId", Lecture.class)
                    .setParameter("loginId", id)
                    .getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Item findByLongItems(Class<? extends Item> itemClass,Long id) {
        try {
            return em.createQuery("select m from " + itemClass.getSimpleName() + " m" +
                            " where m.id = :id", Lecture.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    public void delete(Item item) {
        em.remove(item);
    }

    public boolean checkDuplicate(String studentId, Long lectureId) {
        try {
            System.out.println("lectureId = " + lectureId);
            Long singleResult = em.createQuery("select l.id " +
                            "from Student s" +
                            " join s.lectures l" +
                            " where s.loginId = :studentId AND l.id = :lectureId", Long.class)
                    .setParameter("studentId", studentId)
                    .setParameter("lectureId", lectureId)
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult);
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }
}
