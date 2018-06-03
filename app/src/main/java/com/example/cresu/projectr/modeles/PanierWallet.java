package com.example.cresu.projectr.modeles;

import java.util.ArrayList;

/**
 * Created by cresu on 06/05/2018.
 */

public class PanierWallet {

    private int id;

    private ArrayList<Paquet> paquetArrayList;

    private String dateOperation;

    public PanierWallet() {
        paquetArrayList = new ArrayList<Paquet>();
    }

    public PanierWallet(int id, ArrayList<Paquet> paquetArrayList, String dateOperation) {
        this.id = id;
        this.paquetArrayList = paquetArrayList;
        this.dateOperation = dateOperation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Paquet> getPaquetArrayList() {
        return paquetArrayList;
    }

    public void setPaquetArrayList(ArrayList<Paquet> paquetArrayList) {
        this.paquetArrayList = paquetArrayList;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public int getCoutTotal(){
        int cout_total = 0;
        for(int i = 0; i < this.paquetArrayList.size(); i++){
            cout_total += this.paquetArrayList.get(i).getAliment().getPrix();
        }

        return cout_total;
    }

    public String getListeDesProduits(){
        String liste = "";

        for(int i = 0; i < this.paquetArrayList.size(); i++){
            liste += this.paquetArrayList.get(i).getAliment().getNom()+"\n";
        }

        return liste;
    }

    public String getListeDesPrix(){
        String liste = "";

        for(int i = 0; i < this.paquetArrayList.size(); i++){
            liste += this.paquetArrayList.get(i).getAliment().getPrix()+"\n";
        }

        return liste;
    }
}
