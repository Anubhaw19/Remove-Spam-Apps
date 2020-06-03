package com.example.removedragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StartAnimation extends AppCompatActivity {

    ImageView logoForAnimation;
    Button startScanning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);

        logoForAnimation = findViewById(R.id.logo);

//        Animation animation_image = AnimationUtils.loadAnimation(this, R.anim.logo_anim);

//        logoForAnimation.startAnimation(animation_image);
        TextView textLink = findViewById(R.id.textLink);
        textLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/Team-Voyager-100284605056678"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        startScanning = findViewById(R.id.start_scanning);
        startScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartAnimation.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
