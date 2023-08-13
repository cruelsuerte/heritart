package com.heritart;

import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.dao.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.util.Date;

@SpringBootApplication
public class HeritartApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(HeritartApplication.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {

		System.out.println("HeritArt");

	}
}
