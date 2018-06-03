package com.example.cresu.projectr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cresu.projectr.R;

import static com.example.cresu.projectr.utils.Encryptor.decrypt;
import static com.example.cresu.projectr.utils.Encryptor.encrypt;

public class CreateCarteActivity extends AppCompatActivity {

    private EditText text;

    private Button btn_encrypt;

    private Button btn_decrypt;

    private TextView encrypted;

    private TextView decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carte);

        text = (EditText)findViewById(R.id.text_to_enc);

        btn_encrypt = (Button)findViewById(R.id.btn_enc);
        btn_decrypt = (Button)findViewById(R.id.btn_dec);

        encrypted = (TextView)findViewById(R.id.text_encrypted);
        decrypted = (TextView)findViewById(R.id.text_decrypted);

        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_to_encrypt = text.getText().toString();

                if(!TextUtils.isEmpty(text_to_encrypt)){
                    try {
                        encrypted.setText(encrypt("Mouton89Mouton98", "RandomInitVector", text_to_encrypt));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CreateCarteActivity.this, "Vous devez ecrire quelque chose avant", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_to_decrypt = encrypted.getText().toString();

                Toast.makeText(CreateCarteActivity.this, text_to_decrypt, Toast.LENGTH_SHORT).show();
                if(!TextUtils.isEmpty(text_to_decrypt)){
                    try {
                        decrypted.setText(decrypt("Mouton89Mouton98", "RandomInitVector", text_to_decrypt));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CreateCarteActivity.this, "Vous devez d'abord hasher quelqie chose", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
