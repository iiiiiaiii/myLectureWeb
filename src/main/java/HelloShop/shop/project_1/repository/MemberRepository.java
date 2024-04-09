package HelloShop.shop.project_1.repository;

import HelloShop.shop.project_1.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    @PersistenceContext
    private final EntityManager em;
    public void save(Member member){
        em.persist(member);
    }

    public <T> T findOne(Class<T> entityClass, Long id) {

        return em.find(entityClass, id);
    }

    public Optional<? extends Member> findByLoginIdV2(Class<?> entityClass, String loginId) {
        try {
            Member member = (Member) em.createQuery("select m from " + entityClass.getSimpleName() + " m where m.loginId = :loginId", entityClass)
                    .setParameter("loginId", loginId)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public <T> Optional<T> findByLoginId(Class<T> entityClass, String loginId) {
        try {
            T result = em.createQuery("select m from " + entityClass.getSimpleName() + " m where m.loginId = :loginId", entityClass)
                    .setParameter("loginId", loginId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            System.out.println("e = " + e);
            return Optional.empty();
        }
    }

    public <T> T findByNameOne(Class<T> entityClass, String name) {
        try {
            return em.createQuery("select m from " + entityClass.getSimpleName() + " m where m.name = :name",
                            entityClass)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<?> findByName(Class<?> entityClass, String name) {
        List<?> resultList = em.createQuery("select m from " + entityClass.getSimpleName() + " m where m.name = :name", entityClass)
                .setParameter("name", name)
                .getResultList();
        return resultList.isEmpty() ? null : resultList;
    }

    public List<?> findAll(Class<?> entityClass) {
        return em.createQuery("select i from " + entityClass.getSimpleName() + " i", entityClass)
                .getResultList();
    }

    public List<? extends Member> findAllMember() {
        return em.createQuery("SELECT m FROM Member m WHERE TYPE(m) != Member", Member.class)
                .getResultList();
    }

    public void delete(Class<?> entityClass,Long id) {
        Object member = findOne(entityClass, id);
        em.remove(member);
    }
}
