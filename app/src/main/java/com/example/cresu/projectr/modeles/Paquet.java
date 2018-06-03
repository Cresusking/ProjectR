package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 27/03/2018.
 */

public class Paquet {

    private int id;

    private Aliment aliment;

    private int quantite;

    public Paquet() {
    }

    public Paquet(int id, Aliment aliment, int quantite) {
        this.id = id;
        this.aliment = aliment;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aliment getAliment() {
        return aliment;
    }

    public void setAliment(Aliment aliment) {
        this.aliment = aliment;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Paquet{" +
                "id=" + id +
                ", aliment=" + aliment +
                ", quantite=" + quantite +
                '}';
    }
}
