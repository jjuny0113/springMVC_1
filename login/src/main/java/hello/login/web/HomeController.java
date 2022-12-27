package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentResolver.Login;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor

public class HomeController {


    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
//    @GetMapping("/")
//    public String home() {
//        return "redirect:/items";
//    }


    //    @GetMapping("/")
    public Member homeLogin(@CookieValue(name = "memberId", required = false) Long memberId) {

//        if (memberId == null) {
//            bindingResult.reject("memberId가 없습니다.");
////            throw new BindException(bindingResult);
//        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {

        }
        return loginMember;
    }

    //    @GetMapping("/")
    public Member homeLoginV2(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {

        }
//        Member member = (Member) sessionManager.getSession(request);

        return (Member) Objects.requireNonNull(session).getAttribute(SessionConst.LOGIN_MEMBER);
    }

    //    @GetMapping("/")
    public Member homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) {

        return loginMember;
    }

    @GetMapping("/")
    public Member homeLoginV3ArgumentResolver(@Login Member loginMember) {

        return loginMember;
    }

}