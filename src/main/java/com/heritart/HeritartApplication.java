package com.heritart;

import com.heritart.dao.OpereRepository;
import com.heritart.dao.TokenRepository;
import com.heritart.dao.UtentiRepository;
import com.heritart.model.Opera.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class HeritartApplication implements CommandLineRunner{

	@Autowired
	UtentiRepository utentiRepository;

	@Autowired
	OpereRepository opereRepository;

	@Autowired
	TokenRepository tokenRepository;

	public static void main(String[] args) {
		SpringApplication.run(HeritartApplication.class, args);
	}

	@Override
	public void run(String... args) throws ParseException {

		System.out.println("HeritArt");

//		opereRepository.deleteAll();

//		Opera opera = new Opera("Nascita di Venere", "Sandro Botticelli",1996,"Napoli", Tipologia.Dipinto,"26 x 15",Proprieta.COLLEZIONE_PRIVATA, Condizioni.NUOVA);
//		opera.setCondizioni(Condizioni.get("In ottime condizioni"));
//		opera.setTecnica(Tecnica.get("Olio su tela"));
//		opera.setCorrenteArtistica(CorrenteArtistica.RINASCIMENTO);
//		opera.setProprieta(Proprieta.COLLEZIONE_PUBBLICA);
//		opereRepository.save(opera);
//		opera = opereRepository.findByTitolo("Nascita di Venere");
//		System.out.println(opera.getArtista());



//		utentiRepository.deleteAll();
//		tokenRepository.deleteAll();

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


	}
}
