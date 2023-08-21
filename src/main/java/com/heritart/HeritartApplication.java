package com.heritart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HeritartApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(HeritartApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("HeritArt");
	}
}
