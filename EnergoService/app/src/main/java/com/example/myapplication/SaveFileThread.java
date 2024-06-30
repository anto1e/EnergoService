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
                Variables.savingFlag=true;
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




                String[] split_path = Variables.filePath.split("/");        //Получаем путь к текущей папке

                StringBuilder builder = new StringBuilder();
                for(int i=0;i<split_path.length-1;i++) {
                    if (i!=0) {
                        builder.append("/");
                    }
                    builder.append(split_path[i]);
                }
                String folderPath = builder.toString();

                File directory = new File(folderPath  + "/saves");      //Создаем папку для бэкапов если ее нет изначально
                if (!directory.exists()) {
                    directory.mkdir();
                    // If you require it to make the entire directory path including parents,
                    // use directory.mkdirs(); here instead.
                }

                String path = String.valueOf(folderPath  + "/saves/"+Variables.current_floor.getName()+"_"+Variables.current_floor.getFloor()+"_"+System.currentTimeMillis()+"_save.bik");
                //Variables.copyFile(Variables.current_floor.getImage(), path);       //Копирование файла

                Variables.copy(new File(FileHelper.getRealPathFromURI(Variables.activity,Variables.current_floor.getImage())),new File(path));
                Variables.savingFlag=false;
            }
            } catch (IOException ex) {
            Variables.savingFlag=false;
        }

    }
}
