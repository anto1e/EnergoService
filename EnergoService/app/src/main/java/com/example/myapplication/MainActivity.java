package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
    String filePath=""; //Путь файла для открытия

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        setContentView(R.layout.activity_main);

        Variables.activity = MainActivity.this;         //Сохранение activity
        Variables.init();                               //Инициализация переменныъ
        Variables.plan.startDetecting(); //Начало отслеживания перемещения на плане
        ListView listView=(ListView)findViewById(R.id.LampsListView);           //Лист со списком светильников
        LampsList lampsList = new LampsList(this, Variables.plan.lampNames, Variables.plan.imageid);        //Заполнение списка светильников
        listView.setAdapter(lampsList);
        Buttons buttons = new Buttons();        //Создание класса с кнопками


        buttons.startDetecting();       //Начало отслеживания нажатия кнопок
        listView.setOnItemClickListener((adapterView, view, position, l) -> {       //Обработка нажатия на один из элементов списка светильников
            Integer itemSelected = Variables.plan.imageid[position];
            Variables.plan.spawnLamp(itemSelected);         //Создание светильника
        });

        Variables.image.addOnLayoutChangeListener( new View.OnLayoutChangeListener()        //В момент изменения размеров изображения
            //Т.е. загрузки его из файла мы парсим файл и определяем коэффициенты изменения размера изображения
        {
            public void onLayoutChange( View v,
                                        int left,    int top,    int right,    int bottom,
                                        int leftWas, int topWas, int rightWas, int bottomWas )
            {
                int widthWas = rightWas - leftWas; // Right exclusive, left inclusive
                int heightWas = bottomWas - topWas; // Bottom exclusive, top inclusive
                if( v.getHeight() != heightWas && v.getWidth() != widthWas )    //Если размер изображения изменился
                {
                    Variables.currentHeight = findViewById(R.id.imageView).getHeight();
                    Variables.currentWidth = findViewById(R.id.imageView).getWidth();
                    try {   //Парсим файл
                        //Variables.parser.parseFile(filePath);
                        Variables.parser.parseFile("/storage/emulated/0/Download/plan.bik");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Variables.opened=false;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //После выбора пользователем файла путем диалогового окна
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
                final String[] split = selectedfile.getPath().split(":");//split the path.
                filePath = split[1];
                Variables.image.setImageURI(selectedfile);
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
