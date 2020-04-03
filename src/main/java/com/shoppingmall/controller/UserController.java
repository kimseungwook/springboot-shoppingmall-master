package com.shoppingmall.controller;

import com.shoppingmall.dto.NormalUserRequestDto;
import com.shoppingmall.service.NormalUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private NormalUserService normalUserService;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "user/login-register";
    }

    // 일반유저 회원가입
    @PostMapping("/member")
    public String registration(@ModelAttribute @Valid NormalUserRequestDto userRequestDto, RedirectAttributes rttr) {
        normalUserService.userRegistration(userRequestDto);

        rttr.addFlashAttribute("registerComplete", "회원가입이 완료되었습니다.");

        return "redirect:/login";
    }

    @GetMapping("/profiles")
    public String profiles(Model model) {

        model.addAttribute("pageName", "profiles");

        return "user/profiles";
    }

    // form 로그아웃, oauth2 로그아웃 공통
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // remember-me 쿠키도 지워야 함
        new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, authentication);

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }

    @PostMapping("/loginFailure")
    public String loginFailure() throws Exception {

        return "user/login-register";
    }

    // 아이디 중복 로그인
    @GetMapping("/duplicated-login")
    public String duplicatedLogin(RedirectAttributes rttr) {

        rttr.addFlashAttribute("duplicatedLogin", "다른 곳에서 로그인 하였습니다.");

        return "/redirect:/";
    }
}
