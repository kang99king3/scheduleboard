package com.hk.calboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class ScheduleboardSpringboot2013Application {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleboardSpringboot2013Application.class, args);
	}

}
