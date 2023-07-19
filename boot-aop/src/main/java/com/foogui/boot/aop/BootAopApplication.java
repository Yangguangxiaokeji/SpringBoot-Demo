package com.foogui.boot.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BootAopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootAopApplication.class, args);
	}

}
