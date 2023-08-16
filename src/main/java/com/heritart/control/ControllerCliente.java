package com.heritart.control;

import com.heritart.dao.AsteRepository;
import com.heritart.dao.OpereRepository;
import com.heritart.model.aste.Asta;
import com.heritart.model.aste.StatoAsta;
import com.heritart.model.opere.Opera;
import com.heritart.model.opere.Tipologia;
import com.heritart.model.utenti.Utente;
import com.heritart.utils.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class ControllerCliente {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    OpereRepository opereRepository;
    @Autowired
    AsteRepository asteRepository;



}
