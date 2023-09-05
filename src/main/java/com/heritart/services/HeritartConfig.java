package com.heritart.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Configuration
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.heritart")
@EnableTransactionManagement
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class HeritartConfig extends AbstractMongoClientConfiguration {

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return "heritArt";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString("mongodb+srv://cruel:cruel@heritart.dnbe8we.mongodb.net/");
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject
                (AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(new AuthenticationService())
                .passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760);
        return multipartResolver;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()

                .authorizeRequests()
                .antMatchers("/","/general/**","/error","/logout","/loginFailure",
                            "/newCliente","/newGestore","/Confirm/**","/Resend/**").permitAll()
                .antMatchers("/Home","/Catalog/**").hasAnyAuthority("GESTORE","CLIENTE")
                .antMatchers("/Gestore/**").hasAuthority("GESTORE")
                .antMatchers("/myCollection/**").hasAuthority("CLIENTE")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .usernameParameter("email")
                .defaultSuccessUrl("/Home")
                .failureForwardUrl("/loginFailure")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")


                .deleteCookies("JSESSIONID");


        return http.build();
    }

}