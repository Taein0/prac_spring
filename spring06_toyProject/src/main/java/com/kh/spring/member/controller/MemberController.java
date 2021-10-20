package com.kh.spring.member.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.CookieGenerator;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.validator.JoinForm;
import com.kh.spring.member.validator.JoinFormValidator;

@Controller
@RequestMapping("member")
public class MemberController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	private MemberService memberService;
	private JoinFormValidator joinFormValidator;
	
	
	public MemberController(MemberService memberService, JoinFormValidator joinFormValidator) {
		super();
		this.memberService = memberService;
		this.joinFormValidator = joinFormValidator;
	}
	
	@InitBinder(value ="joinForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(joinFormValidator);
	}
	
	@GetMapping("join-form")
	public void joinfrom() {}
	
	@PostMapping("join") //@RequestParam 폼타입으로 날라오고 파라미터 이름과 네임이 일치하면 생략가능 
	public String join(@Validated JoinForm form
			,Errors errors //반드시 검증될 객체 바로 뒤에 작성
			) {
		
		if(errors.hasErrors()) {
			return "member/join-form";
		}
		
		memberService.insertMember(form);
		return "index";
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
	public String loginlmpl( Member member, HttpSession session){

		Member certifiedUser = memberService.authenicateUser(member);
		session.setAttribute("authentication", certifiedUser); //세션에 올려주기
		logger.debug(certifiedUser.toString());
		return "redirect:/member/mypage";
	}
	
	@GetMapping("mypage")
	public String mypage(@CookieValue(name = "JSESSIONID") String sessionId
					, @SessionAttribute(name = "authentication") Member member
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
