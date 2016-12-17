package com.oddsix.nutripro.rest.models.responses;

/**
 * Created by filippecl on 17/12/16.
 */

public class RegisterResponse {
    private String name;
    private String gender;
    private int age;
    private String email;
    private String activity;
    private int peso;
    private int altura;

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
