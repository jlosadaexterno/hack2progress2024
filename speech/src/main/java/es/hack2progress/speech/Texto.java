package es.hack2progress.speech;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Texto {
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Id
    private Long id;

    private String english;
    private boolean translated;
    private String dutch;

    public Texto() {
    }

    public Texto(String english, boolean translated, String dutch) {
        this.english = english;
        this.translated = translated;
        this.dutch = dutch;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEnglish() {
        return english;
    }
    public void setEnglish(String english) {
        this.english = english;
    }   
    public boolean isTranslated() {
        return translated;
    }   
    public void setTranslated(boolean translated) {
        this.translated = translated;
    }
    public String getDutch() {
        return dutch;
    }
    public void setDutch(String dutch) {
        this.dutch = dutch;
    }
}
