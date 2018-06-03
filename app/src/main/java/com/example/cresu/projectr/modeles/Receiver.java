package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 08/05/2018.
 */

public class Receiver {

    private int id;

    private String id_receiver;

    private String nom;

    public Receiver() {
    }

    public Receiver(int id, String id_receiver, String nom) {
        this.id = id;
        this.id_receiver = id_receiver;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
