package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 19/05/2018.
 */

public class EYamMessage {

    private int id;

    private String text;

    private String date;

    private String nom_sender;

    public EYamMessage() {
    }

    public EYamMessage(String text, String date, String nom_sender) {
        this.text = text;
        this.date = date;
        this.nom_sender = nom_sender;
    }

    public EYamMessage(int id, String text, String date, String nom_sender) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.nom_sender = nom_sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNom_sender() {
        return nom_sender;
    }

    public void setNom_sender(String nom_sender) {
        this.nom_sender = nom_sender;
    }
}
