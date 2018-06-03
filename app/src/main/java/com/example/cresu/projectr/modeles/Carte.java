package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 23/04/2018.
 */

public class Carte {

    private int id;

    private String numero;

    private int montant;

    public Carte() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }
}
