package com.example.cresu.projectr.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cresu.projectr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratorActivity extends AppCompatActivity {

    private EditText text;

    private Button btn_gen;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        text = (EditText)findViewById(R.id.code_gen);

        btn_gen = (Button)findViewById(R.id.btn_gen);

        image = (ImageView)findViewById(R.id.qr_image);

        btn_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try{

                    BitMatrix bitMatrix = multiFormatWriter.encode(text.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap  = barcodeEncoder.createBitmap(bitMatrix);

                    image.setImageBitmap(bitmap);

                }catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
