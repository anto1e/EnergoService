package com.example.myapplication;

import android.os.Environment;
import android.os.FileUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.StandardCopyOption;

public class BackupThread extends Thread{
    public void run() {
        try {
                ImageView rotationElement = Variables.loadingImage;     //Колесо вращения
                Animation an = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); //Анимация
                an.setDuration(1000);               // duration in ms
                an.setRepeatCount(-1);                // -1 = infinite repeated
                an.setFillAfter(true);               // keep rotation after animation
                Variables.fileBackuping=true;

                // Apply animation to image view
                Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                    rotationElement.setVisibility(View.VISIBLE);
                    rotationElement.setAnimation(an);
                });

            File directory = new File(Variables.path1 + "/" + Variables.current_floor.getName());       //Путь к папке объекта
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

           directory = new File(Variables.path1 + "/" + Variables.current_floor.getName() + "/backup");     //Путь к папке с бэкапами
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

            String path = Variables.path1 + "/" + Variables.current_floor.getName() + "/backup/"+Variables.current_floor.getName()+"_"+Variables.current_floor.getFloor()+"_"+System.currentTimeMillis()+"_backup.bik";
            //Variables.copyFile(Variables.current_floor.getImage(), path);       //Копирование файла
            Variables.copy(new File(FileHelper.getRealPathFromURI(Variables.activity,Variables.current_floor.getImage())),new File(path));


            String[] split_path = Variables.filePath.split("/");        //Получаем путь к текущей папке

            StringBuilder builder = new StringBuilder();
            for(int i=0;i<split_path.length-1;i++) {
                if (i!=0) {
                    builder.append("/");
                }
                builder.append(split_path[i]);
            }
            String folderPath = builder.toString();

            directory = new File(folderPath  + "/backup");      //Создаем папку для бэкапов если ее нет изначально
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

            path = String.valueOf(folderPath  + "/backup/"+Variables.current_floor.getName()+"_"+Variables.current_floor.getFloor()+"_"+System.currentTimeMillis()+"_backup.bik");
            //Variables.copyFile(Variables.current_floor.getImage(), path);       //Копирование файла

            Variables.copy(new File(FileHelper.getRealPathFromURI(Variables.activity,Variables.current_floor.getImage())),new File(path));


            Variables.fileBackuping=false;


                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                    rotationElement.clearAnimation();
                    rotationElement.setVisibility(View.GONE);
                    Toast.makeText(Variables.activity.getApplicationContext(), "Копия файла сохранена!", Toast.LENGTH_SHORT).show();
                });
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
