package com.swingtech.app.tabletracker.service.main;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("\n\n\n**********************************************\n   Startup Complete! \n**********************************************");
		
	}

}
