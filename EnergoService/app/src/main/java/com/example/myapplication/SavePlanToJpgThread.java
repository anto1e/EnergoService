package com.example.myapplication;

import static com.example.myapplication.MainActivity.maxSize;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class SavePlanToJpgThread extends Thread{
    public void run() {
        try {
            Variables.isExportingToJpg=true;
            Variables.planLayCleared = true;
            Variables.exportingJpg=true;
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
            File directory = new File(Variables.path1+"/"+Variables.current_floor.getName());
            if (! directory.exists()){
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            View layout = LayoutInflater.from(Variables.activity).inflate(R.layout.activity_main, null, false);
            RelativeLayout mContImage = Variables.planLay;
            Variables.activity.runOnUiThread(() -> {        //Включаем вращение

            //reference View with image
            float firstWidth = Variables.planLay.getWidth();
            float firstHeight = Variables.planLay.getHeight();
            mContImage.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                float secondWidth = mContImage.getMeasuredWidth();
                float secondHeight = mContImage.getMeasuredHeight();
                for (Room room:Variables.current_floor.rooms){
                    for (Lamp lamp:room.lamps){
                        float oldX = lamp.getImage().getX();
                        float oldY = lamp.getImage().getY();
                        float newX = oldX*(secondWidth/firstWidth);
                        float newY = oldY * (secondHeight / firstHeight);
                        //lamp.getImage().setPivotX(0);
                        //lamp.getImage().setPivotY(0);
                        if (secondHeight-firstHeight>1) {
                            lamp.getImage().setScaleX(lamp.getImage().getScaleX() + (secondWidth / firstWidth) * 0.8f);
                            lamp.getImage().setScaleY(lamp.getImage().getScaleY() + (secondHeight / firstHeight) * 0.8f);
                            lamp.getImage().setX(newX);
                            lamp.getImage().setY(newY);
                        }
                    }
                }
                for (Lamp lamp:Variables.current_floor.unusedLamps){
                    float oldX = lamp.getImage().getX();
                    float oldY = lamp.getImage().getY();
                    float newX = oldX*(secondWidth/firstWidth);
                    float newY = oldY * (secondHeight / firstHeight);
                    //lamp.getImage().setPivotX(0);
                    //lamp.getImage().setPivotY(0);
                    if (secondHeight-firstHeight>1) {
                        lamp.getImage().setX(newX + lamp.getImage().getWidth() / 2);
                        lamp.getImage().setY(newY + lamp.getImage().getHeight() / 2);
                        lamp.getImage().setScaleX(lamp.getImage().getScaleX() + (secondWidth / firstWidth) * 0.8f);
                        lamp.getImage().setScaleY(lamp.getImage().getScaleY() + (secondHeight / firstHeight * 0.8f));
                    }
                }
            Bitmap bitmap = Bitmap.createBitmap(mContImage.getMeasuredWidth(), mContImage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            mContImage.layout(0, 0, mContImage.getMeasuredWidth(), mContImage.getMeasuredHeight());
            mContImage.draw(canvas);

            //save to File
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File mediaStorageDir = new File(Variables.path1+"/"+Variables.current_floor.getName());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageName = Variables.current_floor.getAdress()+";"+Variables.current_floor.getFloor()+";" + timeStamp + ".jpg";

            File f = new File(mediaStorageDir + File.separator + imageName);
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                Log.d("Error File:"  , "" + e);
            }
                Variables.planLay.removeAllViews();
                Variables.planLay.addView(Variables.image);
                    Variables.typeOpening=0;
                    Variables.image.setImageResource(0);
                Variables.planLayCleared=false;
                try {
                    InputStream is = Variables.activity.getContentResolver().openInputStream(Variables.selectedfile);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    if (bm.getWidth()>maxSize || bm.getHeight()>maxSize){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                bm, maxSize, maxSize, false);
                        Variables.image.setImageBitmap(resizedBitmap);
                    }else{
                        Variables.image.setImageURI(Variables.selectedfile);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                rotationElement.clearAnimation();
                rotationElement.setVisibility(View.GONE);
                Toast.makeText(Variables.activity.getApplicationContext(),"План экспортирован в JPG!",Toast.LENGTH_SHORT).show();
                });
            /*ImageView rotationElement = Variables.loadingImage;     //Колесо вращения
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
                    if (lamp.getPower().equals("2*58Вт")){
                        System.out.println("");
                    }
                    int index = Variables.findIndexOfLamp(lamp.getTypeImage(),lamp.getGroupIndex());
                    if (index!=-1) {
                        lamp.getImage().setImageResource(0);
                        switch (lamp.getGroupIndex()) {
                            case 0:
                                lamp.getImage().setImageResource(Variables.VstraivaemieImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.VstraivaemieImageIdBold[index]);
                                break;
                            case 1:
                                lamp.getImage().setImageResource(Variables.NakladnieImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.NakladnieImageIdBold[index]);
                                break;
                            case 2:
                                lamp.getImage().setImageResource(Variables.LampsImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.LampsImageIdBold[index]);
                                break;
                            case 3:
                                lamp.getImage().setImageResource(Variables.DiodsImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.DiodsImageIdBold[index]);
                                break;
                            case 4:
                                lamp.getImage().setImageResource(Variables.OthersImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.OthersImageIdBold[index]);
                                break;
                            case 5:
                                lamp.getImage().setImageResource(Variables.OutsideImageIdBold[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.OutsideImageIdBold[index]);
                                break;
                        }
                    }
                    });
                }
            }
            for (Lamp lamp: Variables.current_floor.unusedLamps){
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                lamp.getImage().setImageResource(0);
                int index = Variables.findIndexOfLamp(lamp.getTypeImage(),lamp.getGroupIndex());
                    switch (lamp.getGroupIndex()){
                        case 0:
                            lamp.getImage().setImageResource(Variables.VstraivaemieImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.VstraivaemieImageIdBold[index]);
                            break;
                        case 1:
                            lamp.getImage().setImageResource(Variables.NakladnieImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.NakladnieImageIdBold[index]);
                            break;
                        case 2:
                            lamp.getImage().setImageResource(Variables.LampsImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.LampsImageIdBold[index]);
                            break;
                        case 3:
                            lamp.getImage().setImageResource(Variables.DiodsImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.DiodsImageIdBold[index]);
                            break;
                        case 4:
                            lamp.getImage().setImageResource(Variables.OthersImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.OthersImageIdBold[index]);
                            break;
                        case 5:
                            lamp.getImage().setImageResource(Variables.OutsideImageIdBold[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.OutsideImageIdBold[index]);
                            break;
                    }
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

            //if (bWidth > bHeight) {
                //int imageHeight = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
                //bitmap = Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true);
            //} else {
                //int imageWidth = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
                //bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true);
            //}
            Variables.planLay.setDrawingCacheEnabled(false);
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
                    int index = Variables.findIndexOfLamp(lamp.getTypeImage(),lamp.getGroupIndex());
                    if (index!=-1) {
                        lamp.getImage().setImageResource(0);
                        switch (lamp.getGroupIndex()) {
                            case 0:
                                lamp.getImage().setImageResource(Variables.VstraivaemieImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.VstraivaemieImageId[index]);
                                break;
                            case 1:
                                lamp.getImage().setImageResource(Variables.NakladnieImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.NakladnieImageId[index]);
                                break;
                            case 2:
                                lamp.getImage().setImageResource(Variables.LampsImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.LampsImageId[index]);
                                break;
                            case 3:
                                lamp.getImage().setImageResource(Variables.DiodsImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.DiodsImageId[index]);
                                break;
                            case 4:
                                lamp.getImage().setImageResource(Variables.OthersImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.OthersImageId[index]);
                                break;
                            case 5:
                                lamp.getImage().setImageResource(Variables.OutsideImageId[index]);
                                Variables.plan.rotateImg(lamp.getRotationAngle(), lamp.getImage(), "", Variables.OutsideImageId[index]);
                                break;
                        }
                    }
                    });
                }
            }
            for (Lamp lamp: Variables.current_floor.unusedLamps){
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                lamp.getImage().setImageResource(0);
                int index = Variables.findIndexOfLamp(lamp.getTypeImage(),lamp.getGroupIndex());
                    switch (lamp.getGroupIndex()){
                        case 0:
                            lamp.getImage().setImageResource(Variables.VstraivaemieImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.VstraivaemieImageId[index]);
                            break;
                        case 1:
                            lamp.getImage().setImageResource(Variables.NakladnieImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.NakladnieImageId[index]);
                            break;
                        case 2:
                            lamp.getImage().setImageResource(Variables.LampsImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.LampsImageId[index]);
                            break;
                        case 3:
                            lamp.getImage().setImageResource(Variables.DiodsImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.DiodsImageId[index]);
                            break;
                        case 4:
                            lamp.getImage().setImageResource(Variables.OthersImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.OthersImageId[index]);
                            break;
                        case 5:
                            lamp.getImage().setImageResource(Variables.OutsideImageId[index]);
                            Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),"",Variables.OutsideImageId[index]);
                            break;
                    }
                });
            }
            Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                rotationElement.clearAnimation();
                rotationElement.setVisibility(View.GONE);
                Toast.makeText(Variables.activity.getApplicationContext(),"План экспортирован в JPG!",Toast.LENGTH_SHORT).show();
            });*/
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }
}
