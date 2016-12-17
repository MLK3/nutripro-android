package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;

/**
 * Created by filippecl on 17/12/16.
 */

public class RegisterResponse implements Serializable {
    private String name;
    private String gender;
    private int age;
    private String email;
    private String activity;
    private int peso;
    private int altura;

    public RegisterResponse(String name, String gender, int age, String email, String activity, int peso, int altura) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.activity = activity;
        this.peso = peso;
        this.altura = altura;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getActivity() {
        return activity;
    }

    public int getPeso() {
        return peso;
    }

    public int getAltura() {
        return altura;
    }
}
