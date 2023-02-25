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
import android.view.LayoutInflater;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        path = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

        Variables.activity = MainActivity.this;         //Сохранение activity
        Variables.init();
        Variables.plan.startDetecting(); //Начало отслеживания перемещения на плане

        Bitmap bmImg = BitmapFactory.decodeFile(path+"/plan.bik");

        Variables.image.setImageBitmap(bmImg);
        ListView listView=(ListView)findViewById(R.id.LampsListView);           //Лист со списком светильников
        LampsList customCountryList = new LampsList(this, Variables.plan.lampNames, Variables.plan.imageid);        //Заполнение списка светильников
        listView.setAdapter(customCountryList);
        Buttons buttons = new Buttons();

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
            //String path1 = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
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
    }
}



/*        path =  String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        String path1 = path+"/plan.bik";
        String filename = "/output.jpg";
        String path2 = path;

        File oldFile = new File( path1);
        File newFile = new File( path2, filename );

        try
        {
            FileInputStream fis = new FileInputStream( oldFile );
            FileOutputStream fos = new FileOutputStream( newFile );

            try
            {
                int currentByte = fis.read();
                while( currentByte != -1 )
                {
                    fos.write( currentByte );
                    currentByte = fis.read();
                }
            }
            catch( IOException exception )
            {
                System.err.println( "IOException occurred!" );
                exception.printStackTrace();
            }
            finally
            {
                fis.close();
                fos.close();
                System.out.println( "Copied file!" );
            }
        }
        catch( IOException exception )
        {
            System.err.println( "Problems with files!" );
            exception.printStackTrace();
        }

 */
