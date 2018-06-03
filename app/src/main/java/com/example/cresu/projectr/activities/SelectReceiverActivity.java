package com.example.cresu.projectr.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.adapters.PanierAdapter;
import com.example.cresu.projectr.adapters.ReceiverUIAdapter;
import com.example.cresu.projectr.modeles.Receiver;
import com.example.cresu.projectr.sqlites.ReceiverDAO;

import java.util.ArrayList;

public class SelectReceiverActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FloatingActionButton btn_add_receiver;

    private ReceiverUIAdapter mAdapter;

    private ArrayList<Receiver> receiverArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_receiver);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        getListeDesReceivers();

        /* Configuration du Customiser et de la liste graphique*/
        mAdapter = new ReceiverUIAdapter(this, receiverArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Ecouteur (Listener) placé sur la liste graphique pour détecter le clic
        recyclerView.addOnItemTouchListener(new PanierAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new PanierAdapter.ClickListener() {

            Intent intent;

            @Override
            public void onClick(View view, int position) {

                intent = new Intent(SelectReceiverActivity.this, TransfertActivity.class);
                intent.putExtra("id_destinataire", receiverArrayList.get(position).getId_receiver());

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

                askForSuppression(position);
            }
        }));

        btn_add_receiver = (FloatingActionButton)findViewById(R.id.btn_add_receiver);
        btn_add_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddAReceiver();
            }
        });
    }

    public void getListeDesReceivers(){

        ReceiverDAO receiverDAO = new ReceiverDAO(this);
        receiverDAO.openR();

        receiverArrayList = new ArrayList<>();
        receiverArrayList = receiverDAO.getReceivers();

        if(receiverArrayList == null){
            receiverArrayList = new ArrayList<>();
        }
    }

    public void showAddAReceiver(){

        // Instanciation de notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View creationDialog = factory.inflate(R.layout.receiver_add_ui, null);

        final EditText input_id = (EditText) creationDialog.findViewById(R.id.input_id);
        final EditText input_pseudo = (EditText) creationDialog.findViewById(R.id.input_pseudo);

        // Creation de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        // On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(creationDialog);

        // On lui donne un titre
        adb.setTitle("Ajouter un destinataire");

        // On modifie l'icône de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_menu_edit);

        adb.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (TextUtils.isEmpty(input_id.getText().toString()) || TextUtils.isEmpty(input_pseudo.getText().toString())) {
                    Toast.makeText(SelectReceiverActivity.this, "Tous les champs n'ont pas été remplis", Toast.LENGTH_SHORT).show();
                } else {

                    // Déclaration et Initialisation d'une DAO
                    ReceiverDAO receiverDAO = new ReceiverDAO(SelectReceiverActivity.this);

                    // Ouverture de la BDD en Mode Ecriture
                    receiverDAO.openW();

                    // Création de la Room
                    Receiver receiver;

                    if(receiverArrayList.size() <= 0)
                        receiver = new Receiver(1 ,input_id.getText().toString(), input_pseudo.getText().toString());
                    else{
                        receiver = new Receiver((Integer.valueOf(receiverArrayList.get(receiverArrayList.size() - 1).getId()) + 1) ,input_id.getText().toString(), input_pseudo.getText().toString());
                    }

                    //Ajout de la Room dans la BDD
                    receiverDAO.ajouterUnReceiver(receiver);

                    // Ajout de la Room dans la liste de Room
                    receiverArrayList.add(receiver);

                    // Mise à jour de l'Affichage
                    mAdapter.notifyDataSetChanged();

                    // Fermeture de la BDD
                    receiverDAO.close();
                }
            }
        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        adb.show();
    }

    public void askForSuppression(final int position){

        // Creation de l'AlertDialog
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        // On lui donne un titre
        //adb.setTitle(getResources().getString(R.string.information_title));
        adb.setTitle("Suppresion de : "+receiverArrayList.get(position).getNom());

        adb.setMessage("Confirmez vous que vous voulez bien effectuer cette opération ?");

        // On modifie l'icône de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_menu_info_details);

        adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Déclaration et Initialisation d'une DAO
                ReceiverDAO receiverDAO = new ReceiverDAO(SelectReceiverActivity.this);

                // Ouverture de la BDD en Mode Ecriture
                receiverDAO.openW();

                receiverDAO.supprimerUnReceiver(position + 1);

                // Supprimer de la liste
                receiverArrayList.remove(position);

                // Mise à jour de l'Affichage
                mAdapter.notifyDataSetChanged();

                receiverDAO.close();

            }
        });

        adb.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.show();
    }
}
