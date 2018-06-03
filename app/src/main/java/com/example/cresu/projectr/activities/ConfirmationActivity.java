package com.example.cresu.projectr.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.utils.SoundsUtils;

public class ConfirmationActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;

    private ImageView icon;

    private TextView msg;

    private Button btn_continuer;

    private SoundsUtils soundsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();

        if(intent == null){
            startActivity(new Intent(ConfirmationActivity.this, MainActivity.class));
        }

        int operation = intent.getIntExtra("recharge", -80);
        String message = intent.getStringExtra("message");

        relativeLayout = (RelativeLayout)findViewById(R.id.layout);
        icon = (ImageView)findViewById(R.id.analyse_icon);
        msg = (TextView)findViewById(R.id.analyse_msg_id);
        btn_continuer = (Button)findViewById(R.id.btn_continuer);
        soundsUtils = new SoundsUtils();

        if(operation < 0){

            relativeLayout.setBackgroundColor(Color.parseColor("#D82816"));
            icon.setBackgroundResource(R.mipmap.ic_wrong);
            btn_continuer.setTextColor(Color.parseColor("#D82816"));
            soundsUtils.playASound("bad_code");
        }else if(operation == 0){
            soundsUtils.playASound("good");
        }else{
            relativeLayout.setBackgroundColor(Color.parseColor("#FFBC31"));
            icon.setBackgroundResource(R.mipmap.ic_info_warning);
            btn_continuer.setTextColor(Color.parseColor("#FFBC31"));
            soundsUtils.playASound("error_problem");
        }

        if(message.equals("")){
            message = "Le code que vous avez scanné n'est pas bon";
        }
        msg.setText(message);

        btn_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmationActivity.this, MainActivity.class));
            }
        });
    }
}
