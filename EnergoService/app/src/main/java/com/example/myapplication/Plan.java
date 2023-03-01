package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

//Класс для хранения плана здания, и нанесенных на него светильников
public class Plan {
    View tempView=null;         //Временный view для отображения позиции, куда будет добавлен светильник     //Вектор для хранения светильников на плане
    private float previousX;        //Предыдущая позиция пальца по Х
    private float previousY;        //Предыдущая позиция пальца по У
    private double prevLength;      //Предыдущая длина отрезка между двумя пальцами

    private Room touchedRoom;
    private Room lastRoom;
    float x,y;                    //Текущая позиция пальца по Х,Y.
    double lenght;              //Текущая длина отрезка между двумя пальцами

    public final String[] lampNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт"
    };

    public final Integer[] imageid = {              //Изображения светильников
            R.drawable.lum4_18, R.drawable.lum2_36,R.drawable.lampnakal
    };

    @SuppressLint("ClickableViewAccessibility")
    public void disableListenerFromPlan(){              //Отключение слушателя нажатий на план для добавления светильников
        Variables.planLay.setOnTouchListener(null);
        Variables.planLay.removeView(tempView);
        tempView=null;
    }
    //Включение слушателя нажатий на план для добавления светильников
    @SuppressLint("ClickableViewAccessibility")
    public void setListenerToPlan(){
        Variables.planLay.setOnTouchListener(new View.OnTouchListener(){                 //Отслеживание нажатия для создания светильника в этой точке
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (tempView==null){
                    tempView = new View(Variables.activity);
                    Variables.planLay.addView(tempView);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
                    tempView.setLayoutParams(params);
                    tempView.setBackgroundColor(Color.parseColor("#808080"));
                }
                tempView.setX(event.getX());
                tempView.setY(event.getY());
               // Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListenerSubmitBtn(){
        Variables.submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchedRoom.setNumber(Double.parseDouble(String.valueOf(Variables.roomNumber.getText())));
                touchedRoom.setHeight(Double.parseDouble(String.valueOf(Variables.roomHeight.getText())));
                touchedRoom.setType(Variables.spinner.getSelectedItem().toString());
                touchedRoom.setDays(Integer.parseInt(String.valueOf(Variables.daysPerWeek.getText())));
                touchedRoom.setHoursPerDay(Float.parseFloat(String.valueOf(Variables.hoursPerDay.getText())));
                touchedRoom.setHoursPerWeekend(Float.parseFloat(String.valueOf(Variables.hoursPerWeekend.getText())));
                touchedRoom.setType_pos(Variables.spinner.getSelectedItemPosition());
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListenerToImage(){
        Variables.image = Variables.activity.findViewById(R.id.imageView);
        Variables.image.setOnTouchListener(new View.OnTouchListener(){                 //Отслеживание нажатия для создания светильника в этой точке
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                detectRoomTouch(event.getX(),event.getY());
                return false;
            }
        });
    }

    public void setTouchedRoom(float x,float y,boolean type){
        for (Room room:Variables.rooms){
            if (room.detectTouch(x,y)) {
                if (type) {
                    lastRoom = touchedRoom;
                    touchedRoom = room;
                    return;
                }else{
                    touchedRoom = room;
                    lastRoom=touchedRoom;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void detectRoomTouch(float x, float y){
        for (Room room:Variables.rooms){
            if (room.detectTouch(x,y)) {
                Variables.RoomInfo.setVisibility(View.VISIBLE);
                touchedRoom = room;
                    Variables.roomNumber.setText(Double.toString(touchedRoom.getNumber()));
                    Variables.roomHeight.setText(Double.toString(touchedRoom.getHeight()));
                    Variables.spinner.setSelection(touchedRoom.getType_pos());
                    Variables.daysPerWeek.setText(Integer.toString(touchedRoom.getDays()));
                    Variables.hoursPerDay.setText(Float.toString(touchedRoom.getHoursPerDay()));
                    Variables.hoursPerWeekend.setText(Float.toString(touchedRoom.getHoursPerWeekend()));
                    EditText tempText = Variables.activity.findViewById(R.id.roomLamps);
                    tempText.setText(Integer.toString(touchedRoom.lamps.size()));
                    setListenerSubmitBtn();
                    return;
                    //CharSequence text = "Помещение номер: "+Double.toString(temp.getNumber());
                //int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(Variables.activity, text, duration);
                //toast.show();
                //Log.d("Touch at number:", Double.toString(temp.getNumber()));
            }
        }
        Variables.RoomInfo.setVisibility(View.GONE);
    }


    @SuppressLint("ClickableViewAccessibility")
    //Отслеживание нажатий на план для перемещения/приближения
    public void startDetecting(){     //Отслеживание нажатий на план
        Variables.planLay = Variables.activity.findViewById(R.id.planLayout);
        LinearLayout imageWrap = Variables.activity.findViewById(R.id.imageWrap);
        setListenerToPlan();
        setListenerToImage();
        imageWrap.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {     //Отслеживание нажатий на план
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
                            float currentScale = Variables.planLay.getScaleX();
                            if (currentScale + (float) dx / (200 - Variables.planLay.getScaleX())>0.2) {
                                Variables.planLay.setScaleX(currentScale + (float) dx / (200 - Variables.planLay.getScaleX()));
                                Variables.planLay.setScaleY(currentScale + (float) dx / (200 - Variables.planLay.getScaleX()));

                            }
                            prevLength = lenght;
                        }else {             //Задействован один палец - перемещение
                            float dx = (x - previousX);
                            float dy = (y - previousY);
                            float currentX = Variables.planLay.getX();
                            float currentY = Variables.planLay.getY();

                            Variables.planLay.setX(currentX + dx);
                            Variables.planLay.setY(currentY + dy);

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
    //Функция создания и отображения светильника на плане
    public void spawnLamp(Integer type){
        if (tempView!=null) {                   //Если активная функция добавления светильника
            ImageView imageView = new ImageView(Variables.activity);
            imageView.setImageResource(type);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
            imageView.setLayoutParams(params);
            Variables.planLay.addView(imageView);
            imageView.setX(tempView.getX());
            imageView.setY(tempView.getY());
            setListener(imageView);
            Variables.planLay.removeView(tempView);
            touchedRoom.lampPush(imageView);
            touchedRoom.lamp[0] = imageView;
            tempView=null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    //Деактивация всех слушателей нажатий у ламп
    public void stopLampsTouchListener(){
        for (Room room:Variables.rooms){
            Vector<ImageView> temp = room.getLamps();
            for (int i=0;i<temp.size();i++){
                temp.elementAt(i).setOnTouchListener(null);
            }
        }
    }

    //Активация всех слушателей нажатий у ламп
    public void setLampsTouchListener(){
        for (Room room:Variables.rooms){
            Vector<ImageView> temp = room.getLamps();
            for (int i=0;i<temp.size();i++){
                setListener(temp.elementAt(i));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
        //Отслеживание нажатий на светильники
    void setListener(ImageView imageView){
        imageView.setOnTouchListener(new View.OnTouchListener(){
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = imageView.getX();
                float y = imageView.getY();
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        //prevLength = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));
                        setTouchedRoom(x, y,false);
                        break;
                        case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount()>1){     //Задействовано два пальца - приближение
                            lenght = Math.sqrt(Math.pow((double)(event.getX(0)) - (double)(event.getX(1)),2) + Math.pow((double)(event.getY(0)) - (double)(event.getY(1)),2));

                            double dx = lenght - prevLength;
                            float currentScale = imageView.getScaleX();
                            if (currentScale + (float) dx / 100>0.55) {             //Если размер слишком маленький - останавливаем уменьшение
                                imageView.setScaleX(currentScale + (float) dx / 100);
                                imageView.setScaleY(currentScale + (float) dx / 100);
                            }
                            //Log.d("current scale:",Float.toString(imageView.getScaleX())+" , "+Float.toString(imageView.getScaleY()));
                            prevLength = lenght;
                        }else {             //Иначе обычное перемещение
                            setTouchedRoom(x, y,true);
                            imageView.setX(imageView.getX() + (event.getX()));
                            imageView.setY(imageView.getY() + (event.getY()));
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        lastRoom=touchedRoom;
                        break;
                    case MotionEvent.ACTION_UP:
                        lastRoom=touchedRoom;
                        break;
                }
                if (touchedRoom!=lastRoom && lastRoom!=null){
                    lastRoom.lampRemove(imageView);
                    touchedRoom.lampPush(imageView);
                }
                return true;
            }
        });
    }
}
