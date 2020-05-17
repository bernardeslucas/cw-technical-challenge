package br.com.cwi.technicalchallenge;

import br.com.cwi.technicalchallenge.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CwiTechnicalChallengeApplication {


    public static void main(String[] args) {

        SpringApplication.run(CwiTechnicalChallengeApplication.class, args);

    }
}
