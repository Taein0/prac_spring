package aop02;

import org.springframework.stereotype.Component;

@Component
public class Woman implements Developer{
	
	public void develop() {
		System.out.println("루비로 개발한다.");
	}
}
