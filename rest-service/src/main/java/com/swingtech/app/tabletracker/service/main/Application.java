package com.swingtech.app.tabletracker.service.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SpringBootApplication
@ComponentScan({"com.swingtech.app.tabletracker.service.rest"})
public class Application {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("\n\n\n**********************************************\n   Startup Complete! \n**********************************************");
		
	}
	
	@Bean
	public Module jodaModule() {
	  return new JodaModule();
	}
	
}
