package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveFileThread extends Thread {
    @SuppressLint("SimpleDateFormat")
    public void run() {
        try {
            if (!Variables.isExportingToJpg) {
                ImageView rotationElement = Variables.loadingImage;     //Колесо вращения
                Animation an = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); //Анимация
                an.setDuration(1000);               // duration in ms
                an.setRepeatCount(-1);                // -1 = infinite repeated
                an.setFillAfter(true);               // keep rotation after animation

                // Apply animation to image view
                Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                    rotationElement.setVisibility(View.VISIBLE);
                    rotationElement.setAnimation(an);
                });

                double usedLamps = 0;
                double unUsedLamps = 0;
                for (Room room : Variables.current_floor.rooms) {
                    for (Lamp lamp : room.lamps) {
                        usedLamps++;
                    }
                }
                for (Lamp lamp : Variables.current_floor.unusedLamps) {
                    unUsedLamps++;
                }
                double percentage100 = usedLamps + unUsedLamps;
                double percentageUsed = (usedLamps / percentage100) * 100;
                if (percentageUsed > 10 || unUsedLamps==0){
                    Variables.parser.saveFile(Variables.filePath);
                    //more code
                }
                //Variables.refreshLampsToRooms(Variables.current_floor);     //Перепривязка светильников к комнате
                Variables.fileSaved = true;
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                    rotationElement.clearAnimation();
                    rotationElement.setVisibility(View.GONE);
                    Toast.makeText(Variables.activity.getApplicationContext(), "Файл сохранен!", Toast.LENGTH_SHORT).show();
                });
            }
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
