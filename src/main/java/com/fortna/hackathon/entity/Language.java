package com.fortna.hackathon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LANGUAGES")
public class Language {
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LANG_GENERATOR")
    @SequenceGenerator(name = "LANG_GENERATOR", sequenceName = "LANG_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "NAME")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
