package com.example.cresu.projectr.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.adapters.PanierAdapter;
import com.example.cresu.projectr.adapters.PanierWalletAdapter;
import com.example.cresu.projectr.constantes.AppConstantes;
import com.example.cresu.projectr.modeles.PanierWallet;
import com.example.cresu.projectr.sqlites.PanierWalletDAO;
import com.example.cresu.projectr.utils.VariablesGlobales;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private PanierWalletAdapter mAdapter;

    private ArrayList<PanierWallet> panierWalletArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        getListeDesPaniersWallet();

        /* Configuration du Customiser et de la liste graphique*/
        mAdapter = new PanierWalletAdapter(this, panierWalletArrayList);
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
               askForConfirmation(panierWalletArrayList.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                // Prévoir un truc pour l'appui long ou pas.
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result OK.d.
        if (requestCode == AppConstantes.WALLET_PANIER_ACTIVITY_CODE) {
            // do something good
        }
    }

    public void getListeDesPaniersWallet(){

        PanierWalletDAO panierWalletDAO = new PanierWalletDAO(this);
        panierWalletDAO.openR();

        panierWalletArrayList = new ArrayList<>();
        panierWalletArrayList = panierWalletDAO.getPaniersWallets();

        if(panierWalletArrayList == null){
            panierWalletArrayList = new ArrayList<>();
        }
    }

    public void askForConfirmation(final PanierWallet panierWallet){

        // Creation de l'AlertDialog
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        // On lui donne un titre
        //adb.setTitle(getResources().getString(R.string.information_title));
        adb.setTitle("COMFIRMATION DE REUTILISATION");

        adb.setMessage("Voulez vous réutiliser ce panier pour votre nouvelle commande ?");

        // On modifie l'icône de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_menu_info_details);

        adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                VariablesGlobales.getInstance().setPaniers(panierWallet.getPaquetArrayList());
                VariablesGlobales.getInstance().setPrix_total(panierWallet.getCoutTotal());

                startActivityForResult(new Intent(WalletActivity.this, PanierActivity.class), AppConstantes.WALLET_PANIER_ACTIVITY_CODE);

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
