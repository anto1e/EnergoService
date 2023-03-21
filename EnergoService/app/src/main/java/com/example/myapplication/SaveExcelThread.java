package com.example.myapplication;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class SaveExcelThread extends Thread{
    public void run() {
        try {
            ImageView rotationElement = Variables.loadingImage;
                Animation an = new RotateAnimation(0.0f, 360.0f,40,40);
                an.setDuration(1000);               // duration in ms
                an.setRepeatCount(-1);                // -1 = infinite repeated
                an.setFillAfter(true);               // keep rotation after animation

                // Aply animation to image view
            Variables.activity.runOnUiThread(() -> {
                rotationElement.setVisibility(View.VISIBLE);
                rotationElement.setAnimation(an);
            });
            Variables.exporter.exportToExel();
            Variables.isExpotedExcel=true;
            Variables.activity.runOnUiThread(() -> {
                rotationElement.clearAnimation();
                rotationElement.setVisibility(View.GONE);
                Toast.makeText(Variables.activity.getApplicationContext(),"Экспортировано в Excel!",Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
