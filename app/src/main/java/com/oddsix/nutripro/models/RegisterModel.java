package com.oddsix.nutripro.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 16/10/16.
 */

public class RegisterModel extends RealmObject {
    @PrimaryKey
    private String mail;

    private String password;

    private String name;
    private int age;
    private String gender;
    private int height;
    private float weight;

    public RegisterModel(String mail, String password, String name, int age, String gender, int height, float weight) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    public RegisterModel() {
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }
}
