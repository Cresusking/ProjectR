package com.example.cresu.projectr.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cresu.projectr.R;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.utils.MyMqtt;
import com.example.cresu.projectr.utils.VariablesGlobales;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransfertActivity extends AppCompatActivity {

    private final static String TAG = TransfertActivity.class.getSimpleName();

    private EditText input_dest_num;

    private EditText input_montant;

    private EditText input_password;

    private Button btn_confirmation;

    private MqttAndroidClient client;

    private MyMqtt myMqtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if(intent.getStringExtra("id_destinataire") == null){
            startActivity(new Intent(this, SelectReceiverActivity.class));
            finish();
        }

        input_dest_num = (EditText)findViewById(R.id.input_dest_num);
        input_dest_num.setText(intent.getStringExtra("id_destinataire"));
        input_dest_num.setEnabled(false);

        input_montant = (EditText)findViewById(R.id.input_montant);

        input_password = (EditText)findViewById(R.id.input_password);

        btn_confirmation = (Button)findViewById(R.id.btn_confirmation);
        btn_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transfert();
            }
        });

        myMqtt = new MyMqtt(getApplicationContext(), HttpRequestConstantes.TOPIC);
        myMqtt.connect( null);

    }

    public void transfert(){

        String identifiant = input_dest_num.getText().toString();
        String password = input_password.getText().toString();
        String montant = input_montant.getText().toString();

        if(TextUtils.isEmpty(identifiant) || TextUtils.isEmpty(password) || TextUtils.isEmpty(montant)){

            Toast.makeText(this, "Tous les champs ne sont pas remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.valueOf(montant) > VariablesGlobales.getInstance().getUser().recupererUtilisateur().getSolde()){

            Toast.makeText(this, "Vos resources sont insuffisantes pour effectuer cette opération.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.valueOf(montant) < 0){

            Toast.makeText(this, "Vous ne pouvez pas envoyé un montant négatif.", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if(!password.equals(VariablesGlobales.getInstance().getUser().recupererUtilisateur().getMot_de_passe())){

            Toast.makeText(this, "Ce n'est pas un bon mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }*/

        askForConfirmation(identifiant, montant, password);
    }

    public void askForConfirmation(final String identifiant, final String montant, final String password){

        // Creation de l'AlertDialog
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        // On lui donne un titre
        //adb.setTitle(getResources().getString(R.string.information_title));
        adb.setTitle("ATTENTION !!!");

        adb.setMessage("Confirmez vous que vous voulez bien effectuer cette opération ?");

        // On modifie l'icône de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_menu_info_details);

        adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                effectuerLeTransfert(identifiant, montant, password);

                startActivity(new Intent(TransfertActivity.this, SelectReceiverActivity.class));
                finish();
            }
        });

        adb.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.show();
    }

    public void effectuerLeTransfert(final String identifiant, final String montant, final String password){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                HttpRequestConstantes.TRANSFERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if(obj.getBoolean("statut") == true){
                        long retrait = (-1) * Long.valueOf(montant);
                        Log.e("Transfert", ""+retrait);
                        VariablesGlobales.getInstance().getUser().addSolde(retrait);

                        myMqtt.send(identifiant, "OK");
                    }

                    Toast.makeText(getApplicationContext(), "" + obj.getString("resultat"), Toast.LENGTH_LONG).show();

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
                params.put("id_emetteur", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId());
                params.put("id_destinataire", identifiant);
                params.put("new_solde", montant);
                params.put("password", password);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);
    }
}
