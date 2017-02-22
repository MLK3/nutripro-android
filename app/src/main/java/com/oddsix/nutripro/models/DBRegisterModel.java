package com.oddsix.nutripro.models;

import com.oddsix.nutripro.rest.models.responses.RegisterResponse;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 16/10/16.
 */

public class DBRegisterModel extends RealmObject implements Serializable{
    @PrimaryKey
    private String email;

    private String name;
    private String gender;
    private int age;
    private String activity;
    private int peso;
    private int altura;

    public DBRegisterModel() {
    }

    public DBRegisterModel(RegisterResponse response) {
        this.email = response.getEmail();
        this.name = response.getName();
        this.gender = response.getGender();
        this.age = response.getAge();
        this.activity = response.getActivity();
        this.peso = response.getPeso();
        this.altura = response.getAltura();
    }

    public DBRegisterModel(String email, String name, String gender, int age, String activity, int peso, int altura) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.activity = activity;
        this.peso = peso;
        this.altura = altura;
    }

    public String getEmail() {
        return email;
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
