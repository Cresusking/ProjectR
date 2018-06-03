package com.example.cresu.projectr.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.adapters.CategorieAlimentAdapter;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.modeles.Aliment;
import com.example.cresu.projectr.modeles.CategorieAliment;
import com.example.cresu.projectr.services.MainService;
import com.example.cresu.projectr.sqlites.AlimentDAO;
import com.example.cresu.projectr.sqlites.CategorieAlimentDAO;
import com.example.cresu.projectr.utils.MyMqtt;
import com.example.cresu.projectr.utils.VariablesGlobales;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;

    private TextView solde;

    private FloatingActionButton fab_edit_picture;

    private ImageView qr_code_image;

    private CategorieAlimentAdapter mAdapter;

    private ArrayList<CategorieAliment> categorieAlimentArrayList;

    private FloatingActionButton btnPanier;

    private CategorieAlimentDAO categorieAlimentDAO;

    private AlimentDAO alimentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        /**********************************************************************
         * TOOL BAR CONFIGS
         */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*btnPanier = (FloatingActionButton)findViewById(R.id.btn_panier);
        btnPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PanierActivity.class));
            }
        });*/

        /**********************************************************************
         * NAVIGATION BAR CONFIGS
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        qr_code_image = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.qr_code_id);
        //chargerProfilImage();
        generateQRCode(VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId());

        /*fab_edit_picture = (FloatingActionButton) navigationView.getHeaderView(0).findViewById(R.id.fab_edit_picture);
        fab_edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.pick_picture)), 1);
            }
        });

        solde = (TextView)navigationView.getHeaderView(0).findViewById(R.id.solde);
        solde.setText(""+VariablesGlobales.getInstance().getUser().recupererUtilisateur().getSolde());*/

        /**********************************************************************
         * OTHER CONFIGS
         */
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        categorieAlimentArrayList = new ArrayList<>();

        getCategorieAliments();

        if(categorieAlimentArrayList.size() == 0) {

            ajouterUtilisateur();

            addCategorieAliment();

            getCategorieAliments();

            addAliments();
        }

        /* Configuration du Customiser et de la liste graphique*/
        mAdapter = new CategorieAlimentAdapter(this, categorieAlimentArrayList);
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

                CategorieAliment categorieAliment = categorieAlimentArrayList.get(position);

                intent = new Intent(MainActivity.this, ListOfAlimentActivity.class);
                VariablesGlobales.getInstance().setCategorie(categorieAliment.getId());
                //intent.putExtra("categorie", categorieAliment.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                // Prévoir un truc pour l'appui long ou pas.
            }

        }));

        // Connexion au broker Mqtt
        MyMqtt myMqtt =  new MyMqtt(this, HttpRequestConstantes.TOPIC);
        myMqtt.connect(solde);

        // Mettre à jour le solde au démarrage de l'Application
        VariablesGlobales.getInstance().updateSolde(solde);

        startService(new Intent(this, MainService.class));
        checkProtectionOnApp();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_historique){
            startActivity(new Intent(this, WalletActivity.class));

        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(this, ChatActivity.class));

        }else if (id == R.id.nav_parametres){
            startActivity(new Intent(this, ParametresActivity.class));

        }else if (id == R.id.nav_deconnexion){

            VariablesGlobales.getInstance().getUser().nettoyerPreference();

            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int RC, int RQC, final Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap.createScaledBitmap(bitmap, 200, 200, false);

                qr_code_image.setImageBitmap(bitmap);

                sauvegarderProfilImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ajouterUtilisateur(){

        //VariablesGlobales.getInstance().getUser().enregistrerUnUtilisateur();
    }

    private void addCategorieAliment(){

        categorieAlimentDAO = new CategorieAlimentDAO(this);
        categorieAlimentDAO.openW();

        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(1, "Fruits et Légumes", R.drawable.legumes));
        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(2, "Viandes et Poissons", R.drawable.viande_poisson));
        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(3, "Céréales et Tubercules", R.drawable.cereales));
        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(4, "Huiles et Epices", R.drawable.huile));
        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(5, "Liqueurs et Canettes", R.drawable.boissons));
        categorieAlimentDAO.ajouterUneCategorieAliment(new CategorieAliment(6, "Divers", R.drawable.divers));

        categorieAlimentDAO.close();
    }

    private void getCategorieAliments(){

        categorieAlimentDAO = new CategorieAlimentDAO(this);
        categorieAlimentDAO.openR();

        categorieAlimentArrayList = new ArrayList<>();

        categorieAlimentArrayList = categorieAlimentDAO.getCategorieAliments();

        if(categorieAlimentArrayList == null){
            categorieAlimentArrayList = new ArrayList<>();
        }

        categorieAlimentDAO.close();
    }

    private void addAliments(){

        alimentDAO = new AlimentDAO(this);
        alimentDAO.openW();

        for(int i = 0; i < categorieAlimentArrayList.size(); i++){

            for(int j = 0; j < 10; j++){
                alimentDAO.ajouterUnAliment(new Aliment(1, categorieAlimentArrayList.get(i).getNom()+"_"+j, categorieAlimentArrayList.get(i).getId(), categorieAlimentArrayList.get(i).getImage_id(), 100, 100));
            }
        }
    }

    public void checkProtectionOnApp(){

        final SharedPreferences settings = getSharedPreferences("protected", MODE_PRIVATE);

        if("huawei".equalsIgnoreCase(Build.MANUFACTURER) && !settings.getBoolean("protected", false)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification de Protection")
                    .setMessage("Votre Application n'est pas protégée. Si vous ne la protégez pas vous n'aurez pas de notification.")
                    .setPositiveButton("Protéger", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                            startActivity(intent);

                            // Put false when applicatoin is closed and check if the MainService Run before put the true value
                            settings.edit().putBoolean("protected",true).commit();
                        }
                    }).create().show();
        }
    }

    public void sauvegarderProfilImage(Bitmap bitmap){

        FileOutputStream out = null;

        try {
            out = new FileOutputStream("/sdcard/ProjectR/Images/"+""
                    + File.separator+""+VariablesGlobales.getInstance().getUser().recupererUtilisateur().getPseudo()+".png");

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void chargerProfilImage(){

        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/ProjectR/Images/" + VariablesGlobales.getInstance().getUser().recupererUtilisateur().getPseudo() + ".png");

        if(bitmap != null){
            qr_code_image.setImageBitmap(bitmap);
        }
    }

    public void generateQRCode(String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{

            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 100, 100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap  = barcodeEncoder.createBitmap(bitMatrix);

            qr_code_image.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }
    }

}
