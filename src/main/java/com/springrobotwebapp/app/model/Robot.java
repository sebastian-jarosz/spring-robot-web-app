package com.springrobotwebapp.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Robot {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
