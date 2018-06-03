package com.example.cresu.projectr.modeles;

/**
 * Created by cresu on 02/04/2018.
 */

public class Utilisateur {

    private String id;

    private String pseudo;

    private String mot_de_passe;

    private String numero_de_telephone;

    private String numero_de_paiement;

    private long solde;

    private String pays;

    private String ville;

    private String quartier;

    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String mot_de_passe, String numero_de_telephone, String numero_de_paiement, String pays, String ville, String quartier) {
        this.pseudo = pseudo;
        this.mot_de_passe = mot_de_passe;
        this.numero_de_telephone = numero_de_telephone;
        this.numero_de_paiement = numero_de_paiement;
        this.pays = pays;
        this.ville = ville;
        this.quartier = quartier;
    }

    public Utilisateur(String id, String pseudo, String mot_de_passe, String numero_de_telephone, String numero_de_paiement, long solde, String pays, String ville, String quartier) {
        this.id = id;
        this.pseudo = pseudo;
        this.mot_de_passe = mot_de_passe;
        this.numero_de_telephone = numero_de_telephone;
        this.numero_de_paiement = numero_de_paiement;
        this.solde = solde;
        this.pays = pays;
        this.ville = ville;
        this.quartier = quartier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getNumero_de_telephone() {
        return numero_de_telephone;
    }

    public void setNumero_de_telephone(String numero_de_telephone) {
        this.numero_de_telephone = numero_de_telephone;
    }

    public String getNumero_de_paiement() {
        return numero_de_paiement;
    }

    public void setNumero_de_paiement(String numero_de_paiement) {
        this.numero_de_paiement = numero_de_paiement;
    }

    public long getSolde() {
        return solde;
    }

    public void setSolde(long solde) {
        this.solde = solde;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }
}
