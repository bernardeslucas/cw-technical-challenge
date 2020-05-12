package br.com.cwi.technicalchallenge.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "musics")
public class Music {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String artist;

    @ManyToMany
    private List<User> users;

    public Music(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



}
