package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import java.math.*;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        setContentView(R.layout.activity_main);
        Variables.activity = MainActivity.this;         //Сохранение activity
        Variables.plan.startDetecting(); //Начало отслеживания перемещения на плане
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
}


