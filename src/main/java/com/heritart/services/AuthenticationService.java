package com.heritart.services;

import com.heritart.dao.UtentiRepository;
import com.heritart.model.utenti.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    UtentiRepository utentiRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utente utente = utentiRepository.findByEmail(email);

        if (utente != null && utente.isEnabled()){
            List<SimpleGrantedAuthority> ruolo = List.of(new SimpleGrantedAuthority(utente.getRuolo().name()));

            return new User(utente.getEmail(), utente.getPassword(), ruolo);
        }

        else{
            throw new UsernameNotFoundException("Not found");

        }
    }

    public Utente getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return utentiRepository.findByEmail(email);
    }

}