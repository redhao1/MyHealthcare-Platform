package com.mhp.api_gateway.service;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final WebClient webClient;

    public MyUserDetailsService(WebClient.Builder webClientBuilder,
         @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // call the auth service to get userDetails by email
        UserDetails userDetails;
        try {
            userDetails = webClient.get()
                    .uri("/getUserDetails")
                    .retrieve()
                    .toEntity(UserDetails.class)
                    .block()
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("No UserDetails found" + e);
        }
        return userDetails;


//        Optional<com.example.KoreraProject.DatabaseModels.User> optional = userRep.findByName(username);
//        if(optional.isEmpty()){
//            throw new NoSuchElementException("User is not exist");
//        }else{
//            com.example.KoreraProject.DatabaseModels.User realUser= optional.get();
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(realUser.getRole().toString()));
//            return new User(realUser.getName(),realUser.getPassword(),authorities);
//        }
    }
}