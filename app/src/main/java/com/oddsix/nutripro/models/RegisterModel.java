package com.oddsix.nutripro.models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 16/10/16.
 */

public class RegisterModel extends RealmObject implements Serializable{
    @PrimaryKey
    private String mail;

    private String password;

    private String name;
    private int age;
    private String gender;
    private float height;
    private float weight;
    private DietModel dietModel;

    public RegisterModel(String mail, String password, String name, int age, String gender, float height, float weight) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setDietModel(DietModel dietModel) {
        this.dietModel = dietModel;
    }

    public DietModel getDietModel() {
        return dietModel;
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

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }
}
