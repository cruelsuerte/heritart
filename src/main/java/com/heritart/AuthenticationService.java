package com.heritart;

import com.heritart.dao.UtentiRepository;
import com.heritart.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    UtentiRepository utentiRepository;

    private Utente utente;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utente utente = utentiRepository.findByEmail(email);

        if (utente != null && utente.isEnabled()){
            List ruolo = Arrays.asList(new SimpleGrantedAuthority(utente.getRuolo().name()));

            System.out.println("ACCESSO EFFETTUATO");

            this.utente = utente;
            return new User(utente.getEmail(), utente.getPassword(), ruolo);
        }

        else{
            throw new UsernameNotFoundException("Not found");

        }
    }

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public Utente getUser(){
        return utente;
    }

}