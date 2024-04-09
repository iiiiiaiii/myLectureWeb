package HelloShop.shop.project_1.controller;

import HelloShop.shop.project_1.controller.form.LoginForm;
import HelloShop.shop.project_1.domain.member.Lecturer;
import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.domain.member.Parent;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.service.LoginService;
import HelloShop.shop.project_1.session.SessionConst;
import HelloShop.shop.project_1.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        String selectedClass = request.getParameter("selectedClass");
        Class<? extends Member> memberClass = switch (selectedClass) {
            case "student" -> Student.class;
            case "parent" -> Parent.class;
            case "lecturer" -> Lecturer.class;
            default -> throw new IllegalStateException("Invalid class selected:" + selectedClass);
        };
        Member loginMember = loginService.login(memberClass, form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        String redirectURL = loginMember.getLoginId()+"/" +selectedClass +"Home";
        return "redirect:/" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        //캐시제거
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "redirect:/";
    }

    private static String expireCookie(HttpServletResponse response,String CookieId) {
        Cookie cookie = new Cookie(CookieId, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
