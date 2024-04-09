package HelloShop.shop.project_1.session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class SessionRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void sessionSave(String sessionId,String value) {
        SessionEntity session = new SessionEntity(sessionId,value);
        em.persist(session);
    }

    public String getSessionData(String id) {
        SessionEntity session = em.find(SessionEntity.class, id);
        return session != null ? session.getValue() : null;
    }

    public void sessionRemove(String id) {
        SessionRepository session = em.find(SessionRepository.class, id);
        if (session != null) {
            em.remove(session);
        }
    }
}
