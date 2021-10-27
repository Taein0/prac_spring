package com.kh.spring.member.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.CookieGenerator;

import com.kh.spring.code.ErrorCode;
import com.kh.spring.common.exception.HandlableException;
import com.kh.spring.common.mybatis.validator.ValidateResult;
import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.validator.JoinForm;
import com.kh.spring.member.validator.JoinFormValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	private final MemberService memberService;
	private JoinFormValidator joinFormValidator;
		
	
	/* 모델 명명 규칙
	 * com.myapp.Product becomes "product" 
	 * com.myapp.MyProduct becomes "myProduct"
	 * com.myapp.UKProduct becomes "UKProduct"
	 */

	
	
	@InitBinder(value ="joinForm") //value = 모델 속성명 또는 파라미터명
									//model의 속성 중 속성명이 joinForm인 속성이 있는 경우 initBinder메서드 실행
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(joinFormValidator);
	}
	
	@GetMapping("join")
	public void joinfrom(Model model) {
		model.addAttribute(new JoinForm()).addAttribute("error",new ValidateResult().getError());
		
	}
	
	@PostMapping("join") //@RequestParam 폼타입으로 날라오고 파라미터 이름과 네임이 일치하면 생략가능 
	public String join(@Validated JoinForm form
			,Errors errors //반드시 검증될 객체 바로 뒤에 작성
			,Model model
			,HttpSession session
			,RedirectAttributes redirectAttr
			) {
		
		ValidateResult vr = new ValidateResult();
		model.addAttribute("error",vr.getError());
		if(errors.hasErrors()) {
			vr.addError(errors);			
						
			return "member/join";
		}
		
		//token 생성
		String token = UUID.randomUUID().toString();
		session.setAttribute("persistUser", form);
		session.setAttribute("persistToken", token);
		
		memberService.authenicateByEmail(form, token);
		
		redirectAttr.addFlashAttribute("message","이메일이 발송되었습니다.");
		
		return "redirect:/";
	}
	
	@GetMapping("join-impl/{token}")
	public String joinImpl(@PathVariable String token
							,@SessionAttribute(value = "persistToken", required = false) String persistToken
							,@SessionAttribute(value = "persistUser", required = false) JoinForm form
							,HttpSession session
							,RedirectAttributes redirectAttrs) {
		
		if(!token.equals(persistToken)) {
			throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
		}		
		memberService.insertMember(form);
		redirectAttrs.addFlashAttribute("message","회원가입을 환영합니다. 로그인 해주세요.");
		session.removeAttribute("persistToken");
		session.removeAttribute("persistUser");
		return "redirect:/member/login";
	}
	
	
	
	@PostMapping("join-json")
	public String joinWithJson(@RequestBody Member member) {
		logger.debug(member.toString());
		return "index";
	}
	
	//로그인 페이지 이동메서드
	//메서드명 : login
	@GetMapping("login")
	public void login() {
		
	}
	//로그인 실행 메서드
	//메서드명 : loginlmpl
	//재지정할 jsp : mypage
	@PostMapping("login")
	public String loginlmpl( Member member, HttpSession session, RedirectAttributes redirctAttr){

		Member certifiedUser = memberService.authenicateUser(member);
		
		if(certifiedUser == null) {
			redirctAttr.addFlashAttribute("message","아이디나 비밀번호가 정확하지 않습니다.");
			return "redirect:/member/login";
		}
		
		
		session.setAttribute("authentication", certifiedUser); //세션에 올려주기
		logger.debug(certifiedUser.toString());
		return "redirect:/member/mypage";
	}
	
	@GetMapping("mypage")
	public String mypage(@CookieValue(name = "JSESSIONID", required = false) String sessionId
					, @SessionAttribute(name = "authentication", required = false) Member member
					, HttpServletResponse response) {
		//Cookie 생성 및 응답헤더에 추가
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName("testCookie");
		cookieGenerator.addCookie(response, "test_cookie");
		
		logger.debug("@CookieValue : "+ sessionId);
		logger.debug("@SessionAttribute : " + member);
		
		return"member/mypage";
	}
	
	@GetMapping("id-check")
	@ResponseBody
	public String idCheck(String userId) {
		Member member = memberService.selectMemberByUserId(userId);
		
		if(member == null) {
			return "available";
		}else {
			return "disable";
		}
	}
		
	
}
