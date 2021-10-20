package com.kh.spring.member;

import org.apache.ibatis.annotations.Param;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.spring.member.model.dto.Member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*-context.xml" })
public class MemberControllerTest {
	// MockMVC : http 요청 및 응답 상황 테스트를 위한 객체

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	WebApplicationContext wac;
	MockMvc mockMvc;

	@Before
	public void set() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void joinTest() throws Exception {
		mockMvc.perform(post("/member/join")
				.param("userId", "testMethod4")
				.param("password", "1234")
				.param("tell", "010-2222-3333")
				.param("email", "aaa@gbb.com"))
		.andExpect(status().isOk()) // 200번아닐경우 에러뜸
		.andDo(print());
	}

	@Test
	public void joinWithJson() throws Exception {
		Member member = new Member();
		member.setUserId("testJson");
		member.setPassword("1234");
		member.setEmail("json@pclass.com");
		member.setTell("010-3333-4444");

		ObjectMapper mapper = new ObjectMapper();
		String memberJson = mapper.writeValueAsString(member);
		logger.debug(memberJson);
		// logger.debug(mapper.readValue(memberJson, Map.class).toString());

		mockMvc.perform(post("/member/join-json").contentType(MediaType.APPLICATION_JSON).content(memberJson))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void loginlmpl() throws Exception {
		mockMvc.perform(post("/member/login").param("userId", "pclass").param("password", "1234"))
				.andExpect(status().is3xxRedirection()).andDo(print());
	}

	@Test
	public void mypage() throws Exception {
		Member member = new Member();
		member.setUserId("testJson");
		member.setPassword("1234");
		member.setEmail("json@pclass.com");
		member.setTell("010-3333-4444");
		
		mockMvc.perform(get("/member/mypage")
				.cookie(new Cookie("JSESSIONID", "1123123124")) //쿠키 생성
				.sessionAttr("authentication", member)) //세션에 속성, 값 추가
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	@Test
	public void idCheck() throws Exception{
		mockMvc.perform(get("/member/id-check?userId=test"))
		.andExpect(status().isOk())
		.andDo(print());
	}
}
