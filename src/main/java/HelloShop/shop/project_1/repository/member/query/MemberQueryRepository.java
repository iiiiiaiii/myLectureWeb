package HelloShop.shop.project_1.repository.member.query;

import HelloShop.shop.project_1.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;

    public List<? extends Member> findAllMember(Class<?> entityClass) {
        String ClassName = entityClass.getSimpleName() + "DTO";
        return em.createQuery("select new HelloShop.shop.project_1.dto." + ClassName + "(m.name, m.age)" +
                " from Member m", Member.class
        ).getResultList();
    }
}
