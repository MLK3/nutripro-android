package com.oddsix.nutripro.rest.models.requests;

/**
 * Created by filippecl on 03/12/16.
 */

public class RegisterRequest {
    private String name;
    private String gender;
    private int age;
    private String email;
    private String activity;
    private int peso;
    private int altura;

    public RegisterRequest(String name, String gender, int age, String email, String activity, int peso, int altura) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.activity = activity;
        this.peso = peso;
        this.altura = altura;
    }
}
