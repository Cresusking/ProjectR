package com.example.cresu.projectr.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cresu.projectr.R;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.modeles.Utilisateur;
import com.example.cresu.projectr.utils.VariablesGlobales;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParametresActivity extends AppCompatActivity {

    private final static String TAG = InscriptionActivity.class.getSimpleName();

    private TextView identifiant_txt;

    private TextView pseudo_txt;

    private TextView solde_txt;

    private ImageView qr_image;

    private EditText input_pseudo;

    private EditText input_password;

    private EditText input_telephone;

    private EditText input_paiement;

    private EditText input_pays;

    private EditText input_ville;

    private EditText input_quartier;

    private Button btn_valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur = VariablesGlobales.getInstance().getUser().recupererUtilisateur();

        // Arrangement de l'identifiant de l'utilisateur
        String id_to_display = "";
        String id = VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId();

        for (int i = 0; i < id.length(); i++){
            id_to_display += id.charAt(i);

            if((i+1) % 2 == 0 && i != (id.length() - 1)){
                id_to_display += "   ";
            }
        }

        /*identifiant_txt = (TextView)findViewById(R.id.identifiant_txt);
        identifiant_txt.setText(id_to_display);

        pseudo_txt = (TextView)findViewById(R.id.pseudo_txt);
        pseudo_txt.setText(utilisateur.getPseudo());

        solde_txt = (TextView)findViewById(R.id.solde_txt);
        solde_txt.setText(""+utilisateur.getSolde()+"  "+ AppConstantes.DEVISE);

        qr_image = (ImageView)findViewById(R.id.qr_image);
        generateQRCode(utilisateur.getId());*/

        input_pseudo = (EditText)findViewById(R.id.input_pseudo);
        input_pseudo.setText(utilisateur.getPseudo());

        /*input_password = (EditText)findViewById(R.id.input_password);
        input_password.setText(utilisateur.getMot_de_passe());*/

        input_telephone = (EditText)findViewById(R.id.input_telephone);
        input_telephone.setText(utilisateur.getNumero_de_telephone());

        input_paiement = (EditText)findViewById(R.id.input_paiement);
        input_paiement.setText(utilisateur.getNumero_de_paiement());

        input_pays = (EditText)findViewById(R.id.input_pays);
        input_pays.setText(utilisateur.getPays());

        input_ville = (EditText)findViewById(R.id.input_ville);
        input_ville.setText(utilisateur.getVille());

        input_quartier = (EditText)findViewById(R.id.input_quartier);
        input_quartier.setText(utilisateur.getQuartier());

        btn_valider = (Button)findViewById(R.id.btn_valider);
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }

    private void update(){

        final String pseudo = input_pseudo.getText().toString();
        final String telephone = input_telephone.getText().toString();
        final String paiement = input_paiement.getText().toString();
        final String pays = input_pays.getText().toString();
        final String ville = input_ville.getText().toString();
        final String quartier = input_quartier.getText().toString();

        if (TextUtils.isEmpty(pseudo) || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(paiement) || TextUtils.isEmpty(pays)
                || TextUtils.isEmpty(ville) || TextUtils.isEmpty(quartier)) {
            Toast.makeText(this, "Tous les champs obligatoires ne sont pas remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                HttpRequestConstantes.UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("statut") == true) {
                        // user successfully logged in

                        Utilisateur user = new Utilisateur(pseudo, "", telephone, paiement, pays, ville, quartier);

                        VariablesGlobales.getInstance().getUser().updateUnUtilisateur(user);

                        // storing user in shared preferences and database content
                        // MyApplication.getInstance().getPrefManager().storeUser(user);

                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId());
                params.put("nom", pseudo);
                params.put("numero_de_telephone", telephone);
                params.put("numero_de_paiement", paiement);
                params.put("pays", pays);
                params.put("ville", ville);
                params.put("quartier", quartier);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);
    }

    public void generateQRCode(String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{

            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 100, 100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap  = barcodeEncoder.createBitmap(bitMatrix);

            qr_image.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }
    }
}
