package aop02;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Run {

	public static void main(String[] args) {
		
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("aop02/applicationContext.xml");
		
		
		Developer man = context.getBean("man",Developer.class);
		Developer woman = context.getBean("woman",Developer.class);

		woman.develop();
		System.out.println("==========================");
		man.develop();
	}

}
