package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.repository.MemberRepository;
import com.kh.spring.member.validator.JoinForm;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	

	public void insertMember(JoinForm form) {

		memberRepository.insertMebmer(form);
		
	}



	public Member authenicateUser(Member member) {
		
		return memberRepository.authenicateUser(member);
	}



	public Member selectMemberByUserId(String userId) {
		
		return memberRepository.selectMemberByUserId(userId);
	}
}
