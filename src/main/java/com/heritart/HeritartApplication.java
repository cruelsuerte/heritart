package com.heritart;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
