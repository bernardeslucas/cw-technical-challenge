package br.com.cwi.technicalchallenge.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="pautas")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private String title;

    public Pauta(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
