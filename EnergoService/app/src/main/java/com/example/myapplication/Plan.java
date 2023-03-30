package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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

    private double sumXY=0;

    boolean moveType=true;      //Вкл. или выкл. перемещение светильников

     Room touchedRoom;       //Текущая нажатая комната
    private Room lastRoom;          //Предыдущая нажатая комната
     Lamp touchedLamp;          //Последний нажатый светильник
    float x,y;                    //Текущая позиция пальца по Х,Y.
    double lenght;              //Текущая длина отрезка между двумя пальцами
    boolean isReleased=true;    //Разрешено ли перемещение пальцем по плану(Если было приближение, функция заблокирована пока все пальцы не оторвутся от экрана)
    float pivotX=0.f;           //Пивот Х для корректного приближения плана(Не работает)
    float pivotY=0.f;           //Пивот У для корректного приближения плана(Не работает)
    Vector<Lamp> unusedLamps = new Vector<Lamp>();      //Вектор непривязанных никуда ламп

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
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                            tempView.setLayoutParams(params);
                            tempView.setScaleX(1.5f);
                            tempView.setScaleY(1.5f);
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
        for (Room room:Variables.current_floor.rooms){
            if (room.detectTouch(x,y)) {
                if (type) { //Если нам нужно отследить положение при перемещении светильника
                    lastRoom = touchedRoom;
                    touchedRoom = room;
                    setTouchedRoomInfo();
                    return;
                }else{  //Если нужно отследить первое нажатие на светильник
                    touchedRoom = room;
                    lastRoom=touchedRoom;
                    setTouchedRoomInfo();
                    return;
                }
            }
        }
        touchedRoom=null;
    }

    public void setTouchedRoomInfo(){
        Variables.roomNumber.setText(Double.toString(touchedRoom.getNumber()));
        Variables.roomHeight.setText(Double.toString(touchedRoom.getHeight()));
        Variables.type.setSelection(touchedRoom.getType_pos());
        Variables.daysPerWeek.setSelection(touchedRoom.getDays());
        Variables.hoursPerDay.setSelection(touchedRoom.getHoursPerDay());
        Variables.hoursPerWeekend.setSelection(touchedRoom.getHoursPerWeekend());
        Variables.roofType.setSelection(touchedRoom.getRoofType());
        Variables.roomComments.setText(touchedRoom.getComments());
        EditText tempText = Variables.activity.findViewById(R.id.roomLamps);
        tempText.setText(Integer.toString(touchedRoom.lamps.size()));
    }

    @SuppressLint("SetTextI18n")
    public void detectRoomTouch(float x, float y){      //Функиця определения нажатия на комнату и вывода информации о ней
        for (Room room:Variables.current_floor.rooms){
            if (room.detectTouch(x,y)) {
                Variables.RoomInfo.setVisibility(View.VISIBLE);     //Отображаем данные о комнате
                touchedRoom = room;
                    Variables.roomNumber.setText(Double.toString(touchedRoom.getNumber()));
                    Variables.roomHeight.setText(Double.toString(touchedRoom.getHeight()));
                    Variables.type.setSelection(touchedRoom.getType_pos());
                    Variables.daysPerWeek.setSelection(touchedRoom.getDays());
                    Variables.hoursPerDay.setSelection(touchedRoom.getHoursPerDay());
                    Variables.hoursPerWeekend.setSelection(touchedRoom.getHoursPerWeekend());
                    Variables.roofType.setSelection(touchedRoom.getRoofType());
                    Variables.roomComments.setText(touchedRoom.getComments());
                    EditText tempText = Variables.activity.findViewById(R.id.roomLamps);
                    tempText.setText(Integer.toString(touchedRoom.lamps.size()));
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

    public void rotateImg(float angle,ImageView imageView,int type){
        Bitmap myImg = BitmapFactory.decodeResource(Variables.activity.getResources(), type);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                matrix, true);

        imageView.setImageBitmap(rotated);
    }
    @SuppressLint("ResourceAsColor")
    //Функция создания и отображения светильника на плане
    public void spawnLamp(Integer type, int pos,int typeLamp){
        if (tempView!=null) {                   //Если активная функция добавления светильника
                ImageView imageView = new ImageView(Variables.activity);
                imageView.setImageResource(type);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                imageView.setLayoutParams(params);
                imageView.setX(tempView.getX());
                imageView.setY(tempView.getY());
                imageView.setScaleX(Variables.lastScaletype);
                imageView.setScaleY(Variables.lastScaletype);
                setListener(imageView);
                Variables.planLay.removeView(tempView);
            if (touchedRoom!=null) {        //Если нажатая комната размечена
                Lamp lamp = new Lamp();     //Создаем новый светильник
                if (typeLamp==1){
                    lamp.setType("Люминесцентный");
                }
                lamp.setTypeImage(type);
                lamp.setPower(Variables.lampNames[pos]);
                lamp.setImage(imageView);
                touchedRoom.lampPush(lamp);     //Добавляем светильник в вектор светильников нажатой комнаты
                lamp.setView();                 //Добавляем картинку светильника на экран
            }
            else{                           //Иначе, если нажатая комната не размечена
                Lamp lamp = new Lamp();
                if (typeLamp==1){
                    lamp.setType("Люминесцентный");
                }
                lamp.setTypeImage(type);
                lamp.setPower(Variables.lampNames[pos]);
                lamp.setImage(imageView);
                unusedLamps.add(lamp);          //Добавляем светильник в вектор непривязанных светильников
                lamp.setView();                 //Добавляем картинку светильника на экран
            }
                tempView = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    //Деактивация всех слушателей нажатий у ламп
    public void stopLampsTouchListener(){
        for (Room room:Variables.current_floor.rooms){
            Vector<Lamp> temp = room.getLamps();
            for (int i=0;i<temp.size();i++){
                temp.elementAt(i).getImage().setOnTouchListener(null);
            }
        }
    }

    //Активация всех слушателей нажатий у ламп
    public void setLampsTouchListener(){
        for (Room room:Variables.current_floor.rooms){
            Vector<Lamp> temp = room.getLamps();
            for (int i=0;i<temp.size();i++){
                setListener(temp.elementAt(i).getImage());
            }
        }
    }




    private float getDegreesFromTouchEvent(MotionEvent event,ImageView view,float x, float y){
        double var = Math.atan2((view.getY() + (event.getY()))-y, (view.getX() + (event.getX()))-x);
        float degrees = (float) Math.toDegrees(var);
        if (degrees>80 && degrees <100)
            degrees=90;
        else if (degrees>170 && degrees<-170)
            degrees=170;
        else if (degrees>-10 && degrees<10)
            degrees=0;
        System.out.println(degrees);
        return degrees;
        /*double delta_x = (view.getX() + (event.getX())) - (view.getWidth())/2;
        double delta_y = view.getHeight()/2 - (view.getY() + (event.getY()));
        double radians = Math.atan2(delta_y, delta_x);
        System.out.println(Math.toDegrees(radians));
        return Math.toDegrees(radians);*/
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
                if (Variables.removeMode) {
                    setTouchedRoom(x, y, false);   //Первичное нажатие на светильник
                    touchedLamp = getLampByTouch(imageView);
                    if (touchedLamp!=null) {
                        Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                            Variables.planLay.removeView(touchedLamp.getImage());
                        });
                        touchedRoom.lamps.remove(touchedLamp);
                    }
                }else{
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:       //Получаем нажатый светильнк
                        sumXY = (imageView.getX() + (event.getX())) + (imageView.getY() + (event.getY()));
                        setTouchedRoom(x, y, false);   //Первичное нажатие на светильник
                        touchedLamp = getLampByTouch(imageView);
                        setInfoLamp(touchedLamp);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Variables.scalemode) {
                            float currentScale = imageView.getScaleX();
                            double newSumXY = (imageView.getX() + (event.getX())) + (imageView.getY() + (event.getY()));
                            double dx = newSumXY - sumXY;
                            imageView.setPivotX(imageView.getWidth());
                            imageView.setPivotY(imageView.getHeight());
                            if ((currentScale + (float) dx / 50) > 0.5) {
                                imageView.setScaleX(currentScale + (float) dx / 50);
                                imageView.setScaleY(currentScale + (float) dx / 50);
                                Variables.lastScaletype = currentScale + (float) dx / 50;
                                sumXY = newSumXY;
                            }
                            /*touchedLamp = getLampByTouch(imageView);
                            lenght = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));
                            double dx = lenght - prevLength;
                            float currentScale = imageView.getScaleX();
                            if ((currentScale + (float) dx / 100) > 0.5) {             //Если размер слишком маленький - останавливаем уменьшение
                                imageView.setScaleX(currentScale + (float) dx / 100);
                                imageView.setScaleY(currentScale + (float) dx / 100);
                                prevLength = lenght;
                            } else {
                                Log.d("current scale:", Float.toString(imageView.getScaleX()) + " , " + Float.toString(imageView.getScaleY()));

                            }*/
                        } else if (Variables.rotateMode) {
                            float angle = getDegreesFromTouchEvent(event, imageView, x, y);
                            touchedLamp.setRotationAngle(angle);
                            rotateImg(angle, imageView, touchedLamp.getTypeImage());
                            //imageView.setRotation((float) angle);
                        } else {
                            //Иначе обычное перемещение
                            //angle=imageView.getRotation();
                            //imageView.setRotation(0);
                            setTouchedRoom(x + imageView.getWidth() / 2, y + imageView.getHeight() / 2, true);  //Перемещение светильника
                            imageView.setX((imageView.getX() + (event.getX())) - imageView.getWidth() / 2);
                            imageView.setY((imageView.getY() + (event.getY())) - imageView.getHeight() / 2);
                            //imageView.setRotation((float) angle);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        lastRoom = touchedRoom;
                        break;
                }

                if (!Variables.scalemode) {
                    if (touchedRoom != lastRoom) {   //Если светильник в процессе перемещения оказался в другой комнате, то убираем его из старой комнаты и привязываем к новой
                        if (lastRoom != null) {

                            lastRoom.lampRemove(touchedLamp);
                            unusedLamps.add(touchedLamp);
                        }
                        if (touchedRoom != null) {
                            if (!touchedRoom.lamps.contains(imageView)) {
                                unusedLamps.remove(touchedLamp);
                                touchedRoom.lampPush(touchedLamp);
                            }
                        }
                    }
                }
            }

                return true;
            }
        });
    }

    private void setInfoLamp(Lamp lamp){        //Функция установки информации о светильнке
        if (lamp!=null) {
            Variables.lampType.setText(lamp.getType());
            Variables.lampPower.setText(lamp.getPower());
            Variables.lampComments.setText(lamp.getComments());
        }
    }

    private Lamp getLampByTouch(ImageView image){       //Функция получения информации о светильнике по нажатию на него
        if (touchedRoom!=null) {
            for (int i = 0; i < touchedRoom.lamps.size(); i++) {    //Сначала ищем в размеченных комнатах
                if (touchedRoom.lamps.elementAt(i)!=null) {
                    if (touchedRoom.lamps.elementAt(i).getImage() == image)
                        return touchedRoom.lamps.elementAt(i);
                }
            }
        }
        for (int i=0;i<unusedLamps.size();i++){         //Если не нашли - в непривязанных светильниках
            if (unusedLamps.elementAt(i)!=null) {
                if (unusedLamps.elementAt(i).getImage() == image)
                    return unusedLamps.elementAt(i);
            }
        }
        return null;            //Иначе - светильник не найден
    }
}
