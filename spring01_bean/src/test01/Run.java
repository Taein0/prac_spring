package test01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Run {
	
	
	public static void main(String[] args) {
	
		ApplicationContext context
			=new ClassPathXmlApplicationContext("test01/applicationContext.xml");
	 
		Address addr = (Address) context.getBean("hong"); //getbean은 오브젝트타입이라 캐스트필요
		System.out.println(addr);
		
		Address addr2 = context.getBean("lee",Address.class); 
		System.out.println(addr2);
		
		Address addr3 = context.getBean("kim",Address.class);
		System.out.println(addr3);
	}
	
}
