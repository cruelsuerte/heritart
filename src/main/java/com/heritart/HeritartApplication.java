package com.heritart;

import com.heritart.dao.UtentiRepository;
import com.heritart.model.Ruolo;
import com.heritart.model.Utente;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class HeritartApplication implements CommandLineRunner{

	@Autowired
	UtentiRepository utentiRepository;

	public static void main(String[] args) {
		SpringApplication.run(HeritartApplication.class, args);
	}

	@Override
	public void run(String... args) {

		utentiRepository.deleteAll();

		System.out.println("Data creation started...");

//		Utente utente2 = new Utente("lucacampanile3@gmail.com", new BCryptPasswordEncoder().encode("luca"),"Luca", "Campanile", Ruolo.CLIENTE);
//		utentiRepository.save(utente2);
//
//
//		Utente user = utentiRepository.findByEmail("lucacampanile3@gmail.com");
//
//		if (user != null) {
//			System.out.println("Email: " + user.getEmail());
//			if (user.getRuolo() == Ruolo.CLIENTE){
//				System.out.println("sono cliente");
//				System.out.println(user.getTelefono());
//				System.out.println(user.getIndirizzo());
//			}
//		}
//
//		else {
//			System.out.println("Non trovato");
//		}


		System.out.println(utentiRepository.count());

	}
}
