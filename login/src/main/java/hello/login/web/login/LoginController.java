package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;


    public Member login(@Validated @RequestBody LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) throws BindException {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아아디 또는 비밀번호가 맞지 않습니다.");
            throw new BindException(bindingResult);
        }
        Cookie idCookies = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookies);
        log.info("loginMember={}", loginMember);
        return loginMember;
    }

    //    @PostMapping("/login")
    public Member loginV2(@Validated @RequestBody LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) throws BindException {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아아디 또는 비밀번호가 맞지 않습니다.");
            throw new BindException(bindingResult);
        }
        sessionManager.createSession(loginMember, response);
        return loginMember;
    }

    @PostMapping("/login")
    public Member loginV3(@Validated @RequestBody LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) throws BindException {
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아아디 또는 비밀번호가 맞지 않습니다.");
            throw new BindException(bindingResult);
        }

//        세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return loginMember;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
//        sessionManager.expire(request);

    }
}
