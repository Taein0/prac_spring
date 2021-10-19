package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

@Controller
@RequestMapping("member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("join-form")
	public void joinfrom() {}
	
	@PostMapping("join") //@RequestParam 폼타입으로 날라오고 파라미터 이름과 네임이 일치하면 생략가능 
	public String join(@ModelAttribute Member member) {
		 memberService.insertMember(member);
		return "index";
	}
}
