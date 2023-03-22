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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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
import android.widget.ScrollView;
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
    private static final int PERMISSION_STORAGE = 101;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        setContentView(R.layout.activity_main);
        //if (PermissionUtils.hasPermissions(MainActivity.this)) return;
        //PermissionUtils.requestPermissions(MainActivity.this, PERMISSION_STORAGE);
        Variables.activity = MainActivity.this;         //Сохранение activity
        Variables.init();                               //Инициализация переменныъ
        Variables.plan.startDetecting(); //Начало отслеживания перемещения на плане
        ListView listView=(ListView)findViewById(R.id.LampsListView);           //Лист со списком светильников
        LampsList lampsList = new LampsList(this, Variables.lampNames, Variables.plan.imageid);        //Заполнение списка светильников
        listView.setAdapter(lampsList);
        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }


        Variables.buttons.startDetecting();       //Начало отслеживания нажатия кнопок
        listView.setOnItemClickListener((adapterView, view, position, l) -> {       //Обработка нажатия на один из элементов списка светильников
            Variables.plan.spawnLamp(Variables.plan.imageid[position],position,1);         //Создание светильника
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
                if(v.getHeight() != heightWas && v.getWidth() != widthWas)    //Если размер изображения изменился
                {
                    if (Variables.filePath!="") {
                        while (Variables.currentHeight==0 || Variables.currentWidth==0) {
                            Variables.currentHeight = findViewById(R.id.imageView).getHeight();
                            Variables.currentWidth = findViewById(R.id.imageView).getWidth();
                        }
                        try {   //Парсим файл
                            Variables.parser.parseFile(Variables.filePath);
                            //Variables.filePath="";
                            //Variables.parser.parseFile(String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))+"/planTemp-6.bik");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //После выбора пользователем файла путем диалогового окна
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            for (int i = Variables.planLay.getChildCount()-1; i >= 0; i--) {
                View v = Variables.planLay.getChildAt(i);
                if (v!=Variables.image) {
                    Variables.planLay.removeView(v);
                }
            }
            Variables.selectedfile = data.getData(); //The uri with the location of the file
                //final String[] split = Variables.selectedfile.getPath().split(":");//split the path.
               // Variables.filePath = split[1];
            Variables.filePath = FileHelper.getRealPathFromURI(this,Variables.selectedfile);
            Variables.image.setImageURI(Variables.selectedfile);
        }
        else {
            Variables.image.setImageURI(Variables.selectedfile);
        }
            Variables.opened = false;
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
