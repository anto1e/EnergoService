package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class SavePlanToJpgThread extends Thread{
    public void run() {
        /*try {
            AtomicBoolean isReady1= new AtomicBoolean(false);
            AtomicBoolean isReady2= new AtomicBoolean(false);
            ImageView rotationElement = Variables.loadingImage;     //Колесо вращения
            Animation an = new RotateAnimation(0.0f, 360.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); //Анимация
            an.setDuration(1000);               // duration in ms
            an.setRepeatCount(-1);                // -1 = infinite repeated
            an.setFillAfter(true);               // keep rotation after animation

            // Apply animation to image view
            Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                rotationElement.setVisibility(View.VISIBLE);
                rotationElement.setAnimation(an);
            });
            Variables.refreshLampsToRooms(Variables.current_floor);     //Перепривязка светильников к комнате
            String path1 = String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            for (Room room:Variables.current_floor.rooms) {
                for (Lamp lamp: room.lamps){
                    Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                    lamp.getImage().setImageResource(0);
                    int index = Variables.findIndexOfLamp(lamp.getTypeImage());
                    lamp.getImage().setImageResource(Variables.imageidBold[index]);
                    Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.imageidBold[index]);
                    isReady1.set(true);
                    });
                }
            }
            for (Lamp lamp: Variables.current_floor.unusedLamps){
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                lamp.getImage().setImageResource(0);
                int index = Variables.findIndexOfLamp(lamp.getTypeImage());
                    lamp.getImage().setImageResource(Variables.imageidBold[index]);
                    Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.imageidBold[index]);
                    isReady2.set(true);
                });
            }
            Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
            File mediaStorageDir = new File(path1+"/"+Variables.current_floor.getName());
            // Create a storage directory if it does not exist

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "IMG_" + timeStamp + ".jpg";

            String selectedOutputPath = mediaStorageDir.getPath() + File.separator + imageName;

            Variables.planLay.setDrawingCacheEnabled(true);
            Variables.planLay.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(Variables.planLay.getDrawingCache());

            int maxSize = 1080;

            int bWidth = bitmap.getWidth();
            int bHeight = bitmap.getHeight();
            //bitmap = Bitmap.createScaledBitmap(bitmap, 1920, 1080, true);
            //bitmap = Bitmap.createScaledBitmap(bitmap, bWidth*5, bHeight*5, true);

            /*if (bWidth > bHeight) {
                int imageHeight = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
                bitmap = Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true);
            } else {
                int imageWidth = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
                bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true);
            }*/
            /*Variables.planLay.setDrawingCacheEnabled(false);
            Variables.planLay.destroyDrawingCache();

                OutputStream fOut = null;
                try {
                    File file = new File(selectedOutputPath);
                    fOut = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            for (Room room:Variables.current_floor.rooms) {
                for (Lamp lamp: room.lamps){
                    Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                    lamp.getImage().setImageResource(0);
                    int index = Variables.findIndexOfLamp(lamp.getTypeImage());
                        lamp.getImage().setImageResource(Variables.imageid[index]);
                        Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.imageid[index]);
                    });
                }
            }
            for (Lamp lamp: Variables.current_floor.unusedLamps){
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                lamp.getImage().setImageResource(0);
                int index = Variables.findIndexOfLamp(lamp.getTypeImage());
                    lamp.getImage().setImageResource(Variables.imageid[index]);
                    Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.imageid[index]);
                });
            }
            Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                rotationElement.clearAnimation();
                rotationElement.setVisibility(View.GONE);
                Toast.makeText(Variables.activity.getApplicationContext(),"План экспортирован в JPG!",Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}
