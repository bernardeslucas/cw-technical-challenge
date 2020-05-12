package br.com.cwi.technicalchallenge.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class Test {

    @GetMapping
    private String teste(){
        return "Gustavo help me";
    }



}
