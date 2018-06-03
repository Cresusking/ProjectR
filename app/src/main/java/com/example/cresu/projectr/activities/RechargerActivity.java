package com.example.cresu.projectr.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cresu.projectr.R;
import com.example.cresu.projectr.constantes.AppConstantes;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.utils.UserData;
import com.example.cresu.projectr.utils.VariablesGlobales;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class RechargerActivity extends AppCompatActivity implements  ZXingScannerView.ResultHandler{

    private Button btn_reader;

    private ZXingScannerView mScannerView;

    private int resultat;

    private String message;

    private final static String TAG = RechargerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppConstantes.CAMERA_PERMISSION);
        } else {
            readQR();
        }

        //btn_reader = (Button)findViewById(R.id.btn_reader);
        /*btn_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readQR();
            }
        });*/
    }

    public void readQR(){

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            readQR();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void handleResult(Result result) {

        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Résultat du scan : ");
        //builder.setMessage(result.getText().toString());
        //AlertDialog alertDialog = builder.create();
        //alertDialog.show();

        int sortie = analyser_numero(result.toString());

        // Envoi de confirmation si c'est le cas ou envoi d'infimation dans le cas contraire
    }

    /**
     * Récupérer toutes les demarches
     * */
    private int analyser_numero(String numero) {

        String endPoint = HttpRequestConstantes.GET_CARTE;

        Log.e(TAG, "endPoint: " + endPoint.replace("_ID_", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId()).replace("_NUMERO_", numero));

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint.replace("_ID_", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId())
                        .replace("_NUMERO_", numero), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                Intent intent = new Intent(RechargerActivity.this, ConfirmationActivity.class);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check statut
                    if (obj.getBoolean("statut") == true) {

                        JSONObject carteObj = obj.getJSONObject("carte");

                        UserData userData = VariablesGlobales.getInstance().getUser();

                        Log.e(TAG, "" + carteObj.getLong("montant"));

                        userData.addSolde(carteObj.getLong("montant"));

                        VariablesGlobales.getInstance().setUser(userData);

                        Log.e(TAG, "" + obj.getString("message"));

                        resultat = 0;

                    } else {
                        Log.e(TAG, "" + obj.getString("message"));
                        resultat = -1;
                    }

                    message = obj.getString("message");

                    Log.e(TAG, "" + resultat);
                    Log.e(TAG, "" + message);
                    intent.putExtra("recharge", resultat);
                    intent.putExtra("message", message);

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    resultat = 1;
                    message = "Une erreur s'est produite. Votre carte est toujours active. Réessayer plus tard";

                    e.printStackTrace();
                    intent.putExtra("recharge", resultat);
                    intent.putExtra("message", message);

                }finally {

                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);

                resultat = 2;
                message = "Votre connexion n'est pas bonne. Réessayer un peu plus tard";

                Intent intent = new Intent(RechargerActivity.this, ConfirmationActivity.class);
                intent.putExtra("recharge", resultat);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);

        return resultat;
    }
}
