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

    private Room touchedRoom;       //Текущая нажатая комната
    private Room lastRoom;          //Предыдущая нажатая комната
    float x,y;                    //Текущая позиция пальца по Х,Y.
    double lenght;              //Текущая длина отрезка между двумя пальцами
    boolean isReleased=true;
    float pivotX=0.f;
    float pivotY=0.f;

    public final String[] lampNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт"
    };

    public final Integer[] imageid = {              //Изображения светильников
            R.drawable.lum4_18, R.drawable.lum2_36,R.drawable.lampnakal
    };

    @SuppressLint("ClickableViewAccessibility")
    public void disableListenerFromPlan(){              //Отключение слушателя нажатий на план для добавления светильников
        Variables.planLay.setOnTouchListener(null);
        Variables.planLay.removeView(tempView);             //Отключение маркера добавления светильника
        tempView=null;
    }
    //Включение слушателя нажатий на план для добавления светильников
    @SuppressLint("ClickableViewAccessibility")
    public void setListenerToPlan(){
        Variables.planLay.setOnTouchListener(new View.OnTouchListener(){                 //Отслеживание нажатия для создания светильника в этой точке
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (tempView == null) {        //Если маркера появления светильника нет - отрисовываем его
                            tempView = new View(Variables.activity);
                            Variables.planLay.addView(tempView);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
                            tempView.setLayoutParams(params);
                            tempView.setBackgroundColor(Color.parseColor("#808080"));
                        }       //Иначе устанавливаем ему координаты нажатия
                        tempView.setX(event.getX() - tempView.getWidth() / 2);
                        tempView.setY(event.getY() - tempView.getHeight() / 2);
                        // Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                        break;
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListenerSubmitBtn(){             //Слушатель нажатий на кнопку подтверждения
        Variables.submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {         //Установка данных для выбранной комнаты
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        touchedRoom.setNumber(Double.parseDouble(String.valueOf(Variables.roomNumber.getText())));
                        touchedRoom.setHeight(Double.parseDouble(String.valueOf(Variables.roomHeight.getText())));
                        touchedRoom.setType(Variables.spinner.getSelectedItem().toString());
                        touchedRoom.setDays(Integer.parseInt(String.valueOf(Variables.daysPerWeek.getText())));
                        touchedRoom.setHoursPerDay(Float.parseFloat(String.valueOf(Variables.hoursPerDay.getText())));
                        touchedRoom.setHoursPerWeekend(Float.parseFloat(String.valueOf(Variables.hoursPerWeekend.getText())));
                        touchedRoom.setType_pos(Variables.spinner.getSelectedItemPosition());
                        break;
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListenerToImage(){                   //Отслеживание нажатий на план(отслеживание помещений)
        Variables.image = Variables.activity.findViewById(R.id.imageView);
        Variables.image.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {      //Ищем помещение, на которое нажал пользователь
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                        detectRoomTouch(event.getX(), event.getY());
                        break;
                }
                return false;
            }
        });
    }

    public void setTouchedRoom(float x,float y,boolean type){       //Функция для переопределения текущей выбранной комнаты и прошлой выбранной комнаты
        for (Room room:Variables.building.rooms){
            if (room.detectTouch(x,y)) {
                if (type) { //Если нам нужно отследить положение при перемещении светильника
                    lastRoom = touchedRoom;
                    touchedRoom = room;
                    return;
                }else{  //Если нужно отследить первое нажатие на светильник
                    touchedRoom = room;
                    lastRoom=touchedRoom;
                    return;
                }
            }
        }
        touchedRoom=null;
    }

    @SuppressLint("SetTextI18n")
    public void detectRoomTouch(float x, float y){      //Функиця определения нажатия на комнату и вывода информации о ней
        for (Room room:Variables.building.rooms){
            if (room.detectTouch(x,y)) {
                Variables.RoomInfo.setVisibility(View.VISIBLE);     //Отображаем данные о комнате
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
            }
        }
        touchedRoom=null;
        Variables.RoomInfo.setVisibility(View.GONE);        //Если комнат по данным координатам не нашлось - убираем блок с данными о комнате
    }

    @SuppressLint("ClickableViewAccessibility")
    //Отслеживание нажатий на план для перемещения/приближения
    public void startDetecting(){     //Отслеживание нажатий на план
        Variables.planLay = Variables.activity.findViewById(R.id.planLayout);
        LinearLayout imageWrap = Variables.activity.findViewById(R.id.imageWrap);
        setListenerToPlan();    //Ставим слушатель для создания светильника
        setListenerToImage();   //Ставим слушатель для получения информации о комнате
        imageWrap.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {     //Отслеживание нажатий на план
                x = event.getX();
                y = event.getY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN:           //Нажатие второго пальца для приближения
                        if (event.getPointerCount()>1) {
                            isReleased=false;
                            prevLength = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));
                            //pivotX = (event.getX(0)+event.getX(1))/2.f;
                            //pivotY = (event.getY(0)+event.getY(1))/2.f;
                            //Variables.planLay.setPivotX(pivotX);
                            //Variables.planLay.setPivotY(pivotY);
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
                            if (isReleased) {
                                float dx = (x - previousX);
                                float dy = (y - previousY);
                                float currentX = Variables.planLay.getX();
                                float currentY = Variables.planLay.getY();

                                Variables.planLay.setX(currentX + dx);
                                Variables.planLay.setY(currentY + dy);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (event.getPointerCount()==1)
                            isReleased=true;
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
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                imageView.setLayoutParams(params);
                Variables.planLay.addView(imageView);
                imageView.setX(tempView.getX());
                imageView.setY(tempView.getY());
                imageView.setScaleX(2);
                imageView.setScaleY(2);
                setListener(imageView);
                Variables.planLay.removeView(tempView);
            if (touchedRoom!=null) {
                touchedRoom.lampPush(imageView);
            }
                tempView = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    //Деактивация всех слушателей нажатий у ламп
    public void stopLampsTouchListener(){
        for (Room room:Variables.building.rooms){
            Vector<ImageView> temp = room.getLamps();
            for (int i=0;i<temp.size();i++){
                temp.elementAt(i).setOnTouchListener(null);
            }
        }
    }

    //Активация всех слушателей нажатий у ламп
    public void setLampsTouchListener(){
        for (Room room:Variables.building.rooms){
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
                    case MotionEvent.ACTION_POINTER_DOWN:
                        prevLength = Math.sqrt(Math.pow((double)(event.getX(0)) - (double)(event.getX(1)),2) + Math.pow((double)(event.getY(0)) - (double)(event.getY(1)),2));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        setTouchedRoom(x+imageView.getWidth()/2, y+imageView.getHeight()/2,false);   //Первичное нажатие на светильник
                        break;
                        case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount()>1){     //Задействовано два пальца - приближение
                            lenght = Math.sqrt(Math.pow((double)(event.getX(0)) - (double)(event.getX(1)),2) + Math.pow((double)(event.getY(0)) - (double)(event.getY(1)),2));
                            double dx = lenght - prevLength;
                            float currentScale = imageView.getScaleX();
                            if ((currentScale + (float) dx / 100)>0.5) {             //Если размер слишком маленький - останавливаем уменьшение
                                imageView.setScaleX(currentScale + (float) dx / 100);
                                imageView.setScaleY(currentScale + (float) dx / 100);
                                prevLength = lenght;
                            }
                            else {
                                Log.d("current scale:",Float.toString(imageView.getScaleX())+" , "+Float.toString(imageView.getScaleY()));

                            }
                        }else {
                                //Иначе обычное перемещение
                                setTouchedRoom(x + imageView.getWidth() / 2, y + imageView.getHeight() / 2, true);  //Перемещение светильника
                                imageView.setX((imageView.getX() + (event.getX())) - imageView.getWidth() / 2);
                                imageView.setY((imageView.getY() + (event.getY())) - imageView.getHeight() / 2);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        lastRoom=touchedRoom;
                        break;
                }
                if (touchedRoom!=lastRoom) {   //Если светильник в процессе перемещения оказался в другой комнате, то убираем его из старой комнаты и привязываем к новой
                    if (lastRoom != null) {
                        lastRoom.lampRemove(imageView);
                    }
                    if (touchedRoom != null) {
                        if (!touchedRoom.lamps.contains(imageView)) {
                            touchedRoom.lampPush(imageView);
                        }
                    }
                }/*else if (touchedRoom==null){
                    if (lastRoom!=null) {
                        lastRoom.lampRemove(imageView);
                    }
                }*/
                return true;
            }
        });
    }
}
