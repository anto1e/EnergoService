package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;

//Класс, обрабатывающий нажатия кнопок из правого тулбара

public class Buttons {
    @SuppressLint("ClickableViewAccessibility")
    public void startDetecting(){   //Начало отслеживания нажатия кнопок
        ImageView addBtn = Variables.activity.findViewById(R.id.addBtn);        //Нажата кнопка добавления светильника
        addBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
        ImageView moveBtn = Variables.activity.findViewById(R.id.moveBtn);      //Нажата кнопка активации перемещения светильникв
        moveBtn.setBackgroundColor(Color.parseColor("#ff0f0f"));
        ImageButton uploadBtn = Variables.activity.findViewById(R.id.openFile);
        ImageButton exportExel = Variables.activity.findViewById(R.id.excelExport);
        TextView roomInfo = Variables.activity.findViewById(R.id.roomInfo);
        TextView buildingInfo = Variables.activity.findViewById(R.id.buildingInfo);
        TextView lampInfo = Variables.activity.findViewById(R.id.lampInfo);

        exportExel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        SaveExcelThread thread = new SaveExcelThread();
                        thread.start();
                        break;
                }
                return false;
            }
        });

        Variables.submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {         //Установка данных для выбранной комнаты
                        if (Variables.plan!=null && Variables.plan.touchedRoom!=null) {
                            Variables.plan.touchedRoom.setNumber(Double.parseDouble(String.valueOf(Variables.roomNumber.getText())));
                            Variables.plan.touchedRoom.setHeight(Double.parseDouble(String.valueOf(Variables.roomHeight.getText())));
                            Variables.plan.touchedRoom.setDays(Variables.daysPerWeek.getSelectedItemPosition());
                            Variables.plan.touchedRoom.setHoursPerDay(Variables.hoursPerDay.getSelectedItemPosition());
                            Variables.plan.touchedRoom.setRoofType(Variables.roofType.getSelectedItemPosition());
                            Variables.plan.touchedRoom.setHoursPerWeekend(Variables.hoursPerWeekend.getSelectedItemPosition());
                            Variables.plan.touchedRoom.setComments(String.valueOf(Variables.roomComments.getText()));
                            Variables.plan.touchedRoom.setType_pos(Variables.type.getSelectedItemPosition());
                        }
                        return false;
            }
        });

        Variables.submitLampInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {         //Установка данных для выбранной комнаты
                        if (Variables.plan.touchedLamp!=null) {
                            Variables.plan.touchedLamp.setType(String.valueOf(Variables.lampType.getText()));
                            Variables.plan.touchedLamp.setPower(String.valueOf(Variables.lampPower.getText()));
                            Variables.plan.touchedLamp.setComments(String.valueOf(Variables.lampComments.getText()));
                        }
                return false;
            }
        });

        Variables.submitBuildingInfo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {         //Установка данных для выбранной комнаты
                    if (Variables.plan!=null) {
                        Variables.building.setAdress(String.valueOf(Variables.buildingAdress.getText()));
                        Variables.building.setFloor(String.valueOf(Variables.buidlingFloor.getText()));
                        Variables.building.setAdress(String.valueOf(Variables.buildingAdress.getText()));
                    }
                    return false;
                }
            });

        roomInfo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (Variables.roomInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.roomInfoView,1);
                            /*ViewGroup.LayoutParams params = Variables.roomInfoView.getLayoutParams();
                            params.height = 1;
                            Variables.roomInfoView.setLayoutParams(params);*/
                        } else {
                            Variables.roomInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.roomInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.roomInfoView,height);
                            /*ViewGroup.LayoutParams params = Variables.roomInfoView.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            Variables.roomInfoView.setLayoutParams(params);*/
                        }
                        break;
                }
                return true;
            }
        });

        lampInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (Variables.lampInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.lampInfoView,1);
                            /*ViewGroup.LayoutParams params = Variables.lampInfoView.getLayoutParams();
                            params.height = 1;
                            Variables.lampInfoView.setLayoutParams(params);*/
                        } else {
                            Variables.lampInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.lampInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.lampInfoView,height);
                            /*ViewGroup.LayoutParams params = Variables.lampInfoView.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            Variables.lampInfoView.setLayoutParams(params);*/
                        }
                        break;
                }
                return true;
            }
        });

        buildingInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (Variables.buildingInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.buildingInfoView,1);
                            /*ViewGroup.LayoutParams params = Variables.buildingInfoView.getLayoutParams();
                            params.height = 1;
                            Variables.buildingInfoView.setLayoutParams(params);*/
                        } else {
                            Variables.buildingInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.buildingInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.buildingInfoView,height);
                            /*ViewGroup.LayoutParams params = Variables.buildingInfoView.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            Variables.buildingInfoView.setLayoutParams(params);*/
                        }
                        break;
                }
                return true;
            }
        });
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
                    Variables.plan.moveType=true;
                }else{                                                              //Деактивация перемещения
                    moveBtn.setBackgroundColor(Color.parseColor("#858585"));
                    Variables.plan.moveType=false;
                }
                return false;
            }
        });

        uploadBtn.setOnTouchListener(new View.OnTouchListener() {   //Кнопка загрузки файла с планом
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Variables.opened) {
                    Variables.image.setImageResource(0);
                    Variables.filePath="";
                    Variables.opened=true;
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);
                    Variables.activity.startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                }
                return false;
            }
        });

    }

    private void animateHeightTo(@NonNull View view, int height) {
        final int currentHeight = view.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofInt(view, new HeightProperty(), currentHeight, height);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }


    static class HeightProperty extends Property<View, Integer> {

        public HeightProperty() {
            super(Integer.class, "height");
        }

        @Override public Integer get(View view) {
            return view.getHeight();
        }

        @Override public void set(View view, Integer value) {
            view.getLayoutParams().height = value;
            view.setLayoutParams(view.getLayoutParams());
        }
    }
}
