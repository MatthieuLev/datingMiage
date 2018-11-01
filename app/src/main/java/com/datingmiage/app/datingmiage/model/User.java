package com.datingmiage.app.datingmiage.model;

import android.location.Location;

import java.util.Objects;

public class User {
    private double id;
    private String nom;
    private String prenom;
    private String numTel;
    private Location position;

    public User(double id, String nom, String prenom, String numTel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.position = null;
    }

    public double getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumTel() {
        return numTel;
    }

    public Location getPosition() {
        return position;
    }


    public void setId(double id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public void setPosition(Location position) {
        this.position = position;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numTel='" + numTel + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.id, id) == 0 &&
                Objects.equals(nom, user.nom) &&
                Objects.equals(prenom, user.prenom) &&
                Objects.equals(numTel, user.numTel) &&
                Objects.equals(position, user.position);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nom, prenom, numTel, position);
    }
}
