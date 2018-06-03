package com.example.cresu.projectr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.adapters.CategorieAlimentAdapter;
import com.example.cresu.projectr.adapters.ListOfAlimentAdapter;
import com.example.cresu.projectr.modeles.Aliment;
import com.example.cresu.projectr.modeles.Paquet;
import com.example.cresu.projectr.sqlites.AlimentDAO;
import com.example.cresu.projectr.utils.VariablesGlobales;

import java.util.ArrayList;

/**
 * Created by cresu on 17/03/2018.
 */

public class ListOfAlimentActivity extends AppCompatActivity {

    // Graphical Attributes
    private RecyclerView recyclerView;
    private FloatingActionButton btnPanier;

    private ListOfAlimentAdapter mAdapter;

    // Model Attributes
    private ArrayList<Aliment> listOfAliments;

    //Database attributes
    private AlimentDAO alimentDAO;

    // Others

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_aliments);

        // Local variables
        int categorie = 0;

        //Graphical Configurations
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnPanier = (FloatingActionButton)findViewById(R.id.btn_panier);
        btnPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfAlimentActivity.this, PanierActivity.class));
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        // Logical or Calcul
        /*Intent intent = getIntent();

        if(intent == null){
            startActivity(new Intent(this, MainActivity.class));
        }

        categorie = intent.getIntExtra("categorie", 0);
        listOfAliments = new ArrayList<>();*/

        getACategorieOfAlimentsList(VariablesGlobales.getInstance().getCategorie());


        /* Configuration du Customiser et de la liste graphique*/
        mAdapter = new ListOfAlimentAdapter(this, listOfAliments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Ecouteur (Listener) placé sur la liste graphique pour détecter le clic
        recyclerView.addOnItemTouchListener(new CategorieAlimentAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new CategorieAlimentAdapter.ClickListener() {

            Intent intent;

            @Override
            public void onClick(View view, int position) {

                /*if(isInPanier(listOfAliments.get(position))){
                    Toast.makeText(ListOfAlimentActivity.this, "Cet aliment est déja dans le panier", Toast.LENGTH_SHORT).show();
                }else{
                    ajouterAlimentAuPanier(listOfAliments.get(position));
                    Toast.makeText(ListOfAlimentActivity.this, "Aliment Ajouté au panier", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onLongClick(View view, int position) {
                // Prévoir un truc pour l'appui long ou pas.
            }
        }));
    }

    private void getACategorieOfAlimentsList(int categorie){

        alimentDAO = new AlimentDAO(this);
        alimentDAO.openR();

        listOfAliments = new ArrayList<>();

        listOfAliments = alimentDAO.getAliments(categorie);

        if(listOfAliments == null){
            listOfAliments = new ArrayList<>();
        }

        alimentDAO.close();
    }

    private boolean isInPanier(Aliment aliment){

        ArrayList<Paquet> panier = new ArrayList<>();

        panier = VariablesGlobales.getInstance().getPaniers();

        for(int i = 0; i < panier.size(); i++){
            if(aliment.getNom().equals(panier.get(i).getAliment().getNom())){
                return true;
            }
        }

        return false;
    }

    private void ajouterAlimentAuPanier(Aliment aliment){

        ArrayList<Paquet> panier = new ArrayList<>();

        panier = VariablesGlobales.getInstance().getPaniers();

        Paquet paquet = new Paquet();
        paquet.setAliment(aliment);
        paquet.setQuantite(0);

        panier.add(paquet);

        VariablesGlobales.getInstance().setPaniers(panier);
    }
}
