package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 16/03/2018.
 */

public class CategorieAliment {

    private int id;

    private String nom;

    private int image_id;

    public CategorieAliment(){

    }

    public CategorieAliment(int id, String nom, int image_id) {
        this.id = id;
        this.nom = nom;
        this.image_id = image_id;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "CategorieAliment{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
