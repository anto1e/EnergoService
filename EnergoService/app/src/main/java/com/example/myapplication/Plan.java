package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

public class Plan {
    Vector<ImageView> lamps = new Vector<ImageView>();
    private float previousX;        //Предыдущая позиция пальца по Х
    private float previousY;        //Предыдущая позиция пальца по У
    private double prevLength;      //Предыдущая длина отрезка между двумя пальцами

    float x;                    //Текущая позиция пальца по Х
    float y;                    //Текущая позиция пальца по У
    double lenght;              //Текущая длина отрезка между двумя пальцами

    public final String[] lampNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт"
    };

    public final Integer[] imageid = {              //Изображения светильников
            R.drawable.lum4_18, R.drawable.lum2_36,R.drawable.lampnakal
    };
    @SuppressLint("ClickableViewAccessibility")
    public void startDetecting(RelativeLayout plan, LinearLayout imageWrap){     //Отслеживание нажатий на план
        imageWrap.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN:           //Нажатие второго пальца для приближения
                        if (event.getPointerCount()>1) {
                            prevLength = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));
                        }
                        break;
                    case MotionEvent.ACTION_MOVE: // движение пальцев по экрану
                        if (event.getPointerCount()>1){     //Задействовано два пальца - приближение
                            lenght = Math.sqrt(Math.pow((double)(event.getX(0)) - (double)(event.getX(1)),2) + Math.pow((double)(event.getY(0)) - (double)(event.getY(1)),2));

                            double dx = lenght - prevLength;
                            float currentScale = plan.getScaleX();
                            plan.setScaleX(currentScale + (float) dx / 200);
                            plan.setScaleY(currentScale + (float) dx / 200);
                            prevLength = lenght;
                        }else {             //Задействован один палец - перемещение
                            float dx = (x - previousX);
                            float dy = (y - previousY);
                            float currentX = plan.getX();
                            float currentY = plan.getY();

                            plan.setX(currentX + dx);
                            plan.setY(currentY + dy);
                        }
                        break;
                }
                previousX = x;
                previousY = y;
                return true;
            }
        });
    }
    @SuppressLint("ResourceAsColor")
    public void spawnLamp(Integer type, ImageView imageView, RelativeLayout planLayout,Integer pos){        //Функция создания и отображения светильника на плане
        imageView.setImageResource(type);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        imageView.setLayoutParams(params);
        planLayout.addView(imageView);
        setListener(imageView,planLayout);
    }
    @SuppressLint("ClickableViewAccessibility")
    void setListener(ImageView imageView,RelativeLayout planLayout){    //Отслеживание нажатий на светильник
        imageView.setOnTouchListener(new View.OnTouchListener(){
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = imageView.getX();
                float y = imageView.getY();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                            imageView.setX(imageView.getX() + (event.getX()));
                            imageView.setY(imageView.getY() + (event.getY()));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }
}
