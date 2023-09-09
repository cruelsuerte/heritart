package com.heritart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
public class HeritartApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(HeritartApplication.class, args);
	}

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
	}

	@Override
	public void run(String... args) {
		System.out.println("HeritArt");
	}
}
