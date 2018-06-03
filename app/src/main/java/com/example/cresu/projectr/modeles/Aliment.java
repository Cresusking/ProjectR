package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 16/03/2018.
 */

public class Aliment {

    private int id;

    private String nom;

    private int categorie;

    private int image_id;

    private int prix;

    private int pas_de_prix;

    public Aliment() {

    }

    public Aliment(int id, String nom, int categorie, int image_id, int prix, int pas_de_prix) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.image_id = image_id;
        this.prix = prix;
        this.pas_de_prix = pas_de_prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCategorie() {
        return categorie;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPas_de_prix() {
        return pas_de_prix;
    }

    public void setPas_de_prix(int pas_de_prix) {
        this.pas_de_prix = pas_de_prix;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie=" + categorie +
                ", image_id=" + image_id +
                ", prix=" + prix +
                '}';
    }
}
