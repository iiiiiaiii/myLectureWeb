package HelloShop.shop.project_1.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class SessionEntity {

    @Id
    private String sessionId;

    private String value;
    protected SessionEntity() {
    }

    public SessionEntity(String sessionId,String value) {
        this.sessionId = sessionId;
        this.value = value;
    }
}
