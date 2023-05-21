package com.example.myapplication;

import static com.example.myapplication.Buttons.CAMERA_PERM_CODE;
import static com.example.myapplication.Buttons.CAMERA_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.LampsList;
import com.google.gson.Gson;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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
        Variables.listView=(ListView)findViewById(R.id.LampsListView);           //Лист со списком светильников
        Variables.lampsList = new LampsList(this, Variables.lampVstraivaemieNames, Variables.VstraivaemieImageId);        //Заполнение списка светильников
        Variables.listView.setAdapter(Variables.lampsList);
        /*if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }*/


        Variables.buttons.startDetecting();       //Начало отслеживания нажатия кнопок
        Variables.listView.setOnItemClickListener((adapterView, view, position, l) -> {       //Обработка нажатия на один из элементов списка светильников
            if (Variables.getAddFlag()) {
                int placeType=0;
                if (Variables.plan.touchedRoom==null){
                    placeType=1;
                }
                switch (Variables.currentLampsPanelIndex){
                    case 0:
                        Variables.plan.spawnLamp(Variables.VstraivaemieImageId[position], position,Variables.lampsVstraivaemieName[position],placeType,0,0,0,false,0,0);         //Создание светильника
                        break;
                    case 1:
                        Variables.plan.spawnLamp(Variables.NakladnieImageId[position], position,Variables.lampsNakladnieName[position],placeType,1,0,0,false,0,0);         //Создание светильника
                        break;
                    case 2:
                        Variables.plan.spawnLamp(Variables.LampsImageId[position], position,Variables.lampsLampsName[position],placeType,2,0,0,false,0,0);         //Создание светильника
                        break;
                    case 3:
                        Variables.plan.spawnLamp(Variables.DiodsImageId[position], position,Variables.lampsDiodsName[position],placeType,3,0,0,false,0,0);         //Создание светильника
                        break;
                    case 4:
                        Variables.plan.spawnLamp(Variables.DoskiImageId[position], position,Variables.lampsDoskiName[position],placeType,4,0,0,false,0,0);         //Создание светильника
                        break;
                    case 5:
                        Variables.plan.spawnLamp(Variables.PodvesImageId[position], position,Variables.lampsPodvesName[position],placeType,5,0,0,false,0,0);         //Создание светильника
                        break;
                    case 6:
                        Variables.plan.spawnLamp(Variables.OthersImageId[position], position,Variables.lampsOthersName[position],placeType,6,0,0,false,0,0);         //Создание светильника
                        break;
                    case 7:
                        Variables.plan.spawnLamp(Variables.OutsideImageId[position], position,Variables.lampsOutsideName[position],placeType,7,0,0,false,0,0);         //Создание светильника
                        break;
                }
            }else if (Variables.addMultiple_flag || Variables.addMultipleRowsFlag){
                Variables.resetListColor();
                view.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                switch (Variables.currentLampsPanelIndex){
                    case 0:
                        Variables.multipleType = Variables.VstraivaemieImageId[position];
                        break;
                    case 1:
                        Variables.multipleType = Variables.NakladnieImageId[position];
                        break;
                    case 2:
                        Variables.multipleType = Variables.LampsImageId[position];
                        break;
                    case 3:
                        Variables.multipleType = Variables.DiodsImageId[position];
                        break;
                    case 4:
                        Variables.multipleType = Variables.DoskiImageId[position];
                        break;
                    case 5:
                        Variables.multipleType = Variables.PodvesImageId[position];
                        break;
                    case 6:
                        Variables.multipleType = Variables.OthersImageId[position];
                        break;
                    case 7:
                        Variables.multipleType = Variables.OutsideImageId[position];
                        break;
                }
                Variables.multiplepos = position;
                Variables.multiplelampType = 1;
            }
        });

        Timer myTimer;
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            public void run() {
                if (Variables.current_floor!=null) {
                    SaveFileThread thread = new SaveFileThread();
                    thread.start();
                }
            }
        }, 0, 60*1000); // каждую 3 минуту-сохранение файла


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
                            Variables.currentHeight = findViewById(R.id.imageView).getHeight();
                            Variables.currentWidth = findViewById(R.id.imageView).getWidth();
                        try {   //Парсим файл
                            if (!Variables.planLayCleared) {
                                Variables.parser.parseFile(Variables.filePath);
                            }
                            //Variables.buttons.drawLamps();
                            //Variables.filePath="";
                            //Variables.parser.parseFile(String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))+"/planTemp-6.bik");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
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
            boolean isOpened=false;
            for (int i=0;i<Variables.floors.size();i++){
                if (Objects.equals(FileHelper.getRealPathFromURI(this, Variables.floors.elementAt(i).getImage()), FileHelper.getRealPathFromURI(this, data.getData())))
                    isOpened=true;
            }
            if (!isOpened) {
                for (int i = Variables.planLay.getChildCount()-1; i >= 0; i--) {
                    View v = Variables.planLay.getChildAt(i);
                    if (v!=Variables.image) {
                        Variables.planLay.removeView(v);
                    }
                }
                Variables.selectedfile = data.getData(); //The uri with the location of the file

                //final String[] split = Variables.selectedfile.getPath().split(":");//split the path.
                // Variables.filePath = split[1];
                Variables.filePath = FileHelper.getRealPathFromURI(this, Variables.selectedfile);
                Variables.image.setImageURI(Variables.selectedfile);
            }else{
                Variables.image.setImageURI(Variables.selectedfile);
            }
        } else if(requestCode == CAMERA_REQUEST_CODE){      //Если был запрос на использование камеры - сохраняем картинку
            if(resultCode == Activity.RESULT_OK){
                File f;
                if (Variables.takePhotoFlag) {
                    f = new File(String.valueOf(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.plan.touchedRoom.photoPaths.size() - 1)));
                    Buttons.createNewPhotoRoom(f, true);      //Создание мини-изображение в layout помещения
                }else {
                    f = new File(String.valueOf(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.plan.touchedLamp.photoPaths.size() - 1)));
                    Buttons.createNewPhotoRoom(f, false);      //Создание мини-изображение в layout помещения
                }
                Variables.takePhotoFlag=false;
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
        else {
            Variables.image.setImageURI(Variables.selectedfile);
        }
            Variables.opened = false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {         //Запрос разрешений для камеры
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Variables.takePhotoFlag) {
                    Buttons.dispatchTakePictureIntent(true);
                }else {
                    Buttons.dispatchTakePictureIntent(false);
                }
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


