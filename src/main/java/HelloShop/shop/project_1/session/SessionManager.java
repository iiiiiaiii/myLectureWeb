package HelloShop.shop.project_1.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRepository sessionRepository;
    public static final String SESSION_COOKIE_NAME = "mySessionId";


    public void createSession(String value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionRepository.sessionSave(sessionId,value);

        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(cookie);
    }

    public String getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return sessionRepository.getSessionData(sessionCookie.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    public void expire(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie != null) {
            sessionRepository.sessionRemove(cookie.getValue());
        }
    }

}
