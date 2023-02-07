package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

//Класс, обрабатывающий нажатия кнопок из правого тулбара

public class Buttons {
    @SuppressLint("ClickableViewAccessibility")
    public void startDetecting(){   //Начало отслеживания нажатия кнопок
        ImageView addBtn = Variables.activity.findViewById(R.id.addBtn);        //Нажата кнопка добавления светильника
        addBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
        ImageView moveBtn = Variables.activity.findViewById(R.id.moveBtn);      //Нажата кнопка активации перемещения светильникв
        moveBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
        addBtn.setOnTouchListener(new View.OnTouchListener()                    //Слушатель нажатий на кнопку добавления
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                   Variables.invertAddFlag();
                   if (Variables.getAddFlag()){                                 //Активация добавления
                       addBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
                       Variables.plan.setListenerToPlan();
                   }else{                                                       //Деактивация добавления
                       addBtn.setBackgroundColor(Color.parseColor("#858585"));
                       Variables.plan.disableListenerFromPlan();
                   }
                   return false;
            }
        });

        moveBtn.setOnTouchListener(new View.OnTouchListener()                       //Слушатель нажатий на кнопку активации перемещения
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Variables.invertMoveFlag();
                if (Variables.getMoveFlag()){                                       //Активация перемещения
                    moveBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
                    Variables.plan.setLampsTouchListener();
                }else{                                                              //Деактивация перемещения
                    moveBtn.setBackgroundColor(Color.parseColor("#858585"));
                    Variables.plan.stopLampsTouchListener();
                }
                return false;
            }
        });

    }
}
