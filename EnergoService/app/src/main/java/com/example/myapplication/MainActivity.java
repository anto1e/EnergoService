package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.LampsList;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import java.io.FileNotFoundException;
import java.math.*;

public class MainActivity extends AppCompatActivity {
    BikExtensionParser parser = new BikExtensionParser();
    String path;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        setContentView(R.layout.activity_main);

        Variables.activity = MainActivity.this;         //Сохранение activity
        path =  String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        Variables.plan.startDetecting(); //Начало отслеживания перемещения на плане

        Bitmap bmImg = BitmapFactory.decodeFile(path+"/plan.bik");

        Variables.image.setImageBitmap(bmImg);
        ListView listView=(ListView)findViewById(R.id.LampsListView);           //Лист со списком светильников
        LampsList customCountryList = new LampsList(this, Variables.plan.lampNames, Variables.plan.imageid);        //Заполнение списка светильников
        listView.setAdapter(customCountryList);
        Buttons buttons = new Buttons();

        /*Polygon polygon = Polygon.Builder()
                .addVertex(new Point(1, 1))
                .addVertex(new Point(1, 6))
                .addVertex(new Point(5, 6))
                .addVertex(new Point(5, 3))
                .addVertex(new Point(6, 3))
                .addVertex(new Point(6, 2))
                .addVertex(new Point(5, 2))
                .addVertex(new Point(5, 0))
                .addVertex(new Point(4, 0))
                .addVertex(new Point(4, 1))
                .build();
        Point point = new Point(1, 5);
        boolean contains = polygon.contains(point);
        Log.d("Ansver: ",Boolean.toString(contains));*/
        /*Drawable d = getResources().getDrawable(R.drawable.yourimage);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();*/
        /*BitmapDrawable b = (BitmapDrawable)this.getResources().getDrawable(R.drawable.cake);
        Log.d("MainActivity", "Image Width: " + b.getBitmap().getWidth());
        */
        //Bitmap bmImg = BitmapFactory.decodeFile(path+"/plan.jpg");
        //Variables.image.setImageBitmap(bmImg);

        buttons.startDetecting();       //Начало отслеживания нажатия кнопок
        listView.setOnItemClickListener((adapterView, view, position, l) -> {       //Обработка нажатия на один из элементов списка светильников
            Integer itemSelected = Variables.plan.imageid[position];
            Variables.plan.spawnLamp(itemSelected);         //Создание светильника
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        try {
            //Log.d("123", String.valueOf(Variables.image.getWidth()));
            String path1 = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            //Log.d("123",path1);
            Variables.currentHeight = findViewById(R.id.imageView).getHeight();
            Variables.currentWidth = findViewById(R.id.imageView).getWidth();
            parser.parseFile(path,"/plan.bik");
            Log.d("123", String.valueOf(Variables.rooms.elementAt(0).arrayX[0]));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Room temp:Variables.rooms){
            System.out.println(temp);
        }
        /*Log.d("Image size:",Float.toString(this.findViewById(R.id.imageView).getWidth())+" , "+ Float.toString(this.findViewById(R.id.imageView).getHeight()));
        float[] mas1 = {402.09375f,507.09375f,507.09375f,402.09375f,293.09375f,346.09375f,347.09375f,292.09375f,293.09375f,394.09375f,394.09375f,293.09375f};
        float[] mas2 = {272,272,502,502,512,512,615,615,622,622,735,735};
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable b = (BitmapDrawable)this.getResources().getDrawable(R.drawable.plan);
        float coef1 = (float) (1653.0/(this.findViewById(R.id.imageView).getWidth()));
        float coef2 = (float) (2337.0/(this.findViewById(R.id.imageView).getHeight()));
        for (int i=0;i<12;i++){
            View view1 = new View(MainActivity.this);
            view1.setBackgroundColor(R.color.black);
            view1.setX(mas1[i]/coef1);
            view1.setY(mas2[i]/coef2);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
            view1.setLayoutParams(params);
            Variables.planLay.addView(view1);

        }*/
    }
}


