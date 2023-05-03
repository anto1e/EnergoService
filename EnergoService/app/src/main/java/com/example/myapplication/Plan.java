package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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

import java.util.Objects;
import java.util.Vector;

//Класс для хранения плана здания, и нанесенных на него светильников
public class Plan {
    View tempView=null;         //Временный view для отображения позиции, куда будет добавлен светильник     //Вектор для хранения светильников на плане
    private float previousX;        //Предыдущая позиция пальца по Х
    private float previousY;        //Предыдущая позиция пальца по У
    private double prevLength;      //Предыдущая длина отрезка между двумя пальцами

    private double sumXY=0;         //Сумма координат
    View selectionZone=null;        //Зона выделения


     Room touchedRoom=null;       //Текущая нажатая комната
     Room lastRoom=null;          //Предыдущая нажатая комната
     Lamp touchedLamp;          //Последний нажатый светильник
    float x,y;                    //Текущая позиция пальца по Х,Y.
    double lenght;              //Текущая длина отрезка между двумя пальцами
    boolean isReleased=true;    //Разрешено ли перемещение пальцем по плану(Если было приближение, функция заблокирована пока все пальцы не оторвутся от экрана)
    float pivotX=0.f;           //Пивот Х для корректного приближения плана(Не работает)
    float pivotY=0.f;           //Пивот У для корректного приближения плана(Не работает)
    private boolean ifReleased=false;



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
                if (!Variables.moveOnlySelectedZone) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            if (Variables.copyFlag && Variables.copyType == 1) {        //Если выбрано копирование по выделению зоны
                                //float angle = getDegreesFromTouchEvent(event, imageView, x, y);
                                Variables.moveCopiedVector(x, y);           //Двигаем скопированные светильники
                            }
                            if (Variables.getAddFlag()) {       //Если выбрана функция добавления светильников по маркеру
                                if (tempView == null) {        //Если маркера появления светильника нет - отрисовываем его
                                    tempView = new View(Variables.activity);
                                    Variables.planLay.addView(tempView);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                    tempView.setLayoutParams(params);
                                    tempView.setScaleX(Variables.lastScaletype);
                                    tempView.setScaleY(Variables.lastScaletype);
                                    tempView.setBackgroundColor(Color.parseColor("#808080"));
                                    tempView.getBackground().setAlpha(128);
                                    tempView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tempView.setX(event.getX() - tempView.getWidth() / 2);
                                            tempView.setY(event.getY() - tempView.getHeight() / 2);
                                        }
                                    });
                                } else {      //Иначе устанавливаем ему координаты нажатия
                                    tempView.setX(event.getX() - tempView.getWidth() / 2);
                                    tempView.setY(event.getY() - tempView.getHeight() / 2);
                                }
                                // Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                            } else if (Variables.addMultiple_flag && Variables.multiplepos != -1 && Variables.multipleType != -1 && Variables.multiplelampType != -1) {     //Если выбрана функция добавления множества светильников по нажатию
                                if (tempView == null) {        //Если маркера появления светильника нет - отрисовываем его
                                    tempView = new View(Variables.activity);
                                    Variables.planLay.addView(tempView);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                    tempView.setLayoutParams(params);
                                    tempView.setScaleX(Variables.lastScaletype);
                                    tempView.setScaleY(Variables.lastScaletype);
                                    tempView.setBackgroundColor(Color.parseColor("#808080"));
                                }       //Иначе устанавливаем ему координаты нажатия
                                tempView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tempView.setX(event.getX() - tempView.getWidth() / 2);
                                        tempView.setY(event.getY() - tempView.getHeight() / 2);
                                        switch (Variables.currentLampsPanelIndex){
                                            case 0:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsVstraivaemieName[Variables.multiplepos], 0,0, 0,0, false, 0,0);
                                                break;
                                            case 1:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsNakladnieName[Variables.multiplepos],0, 1, 0,0, false, 0,0);
                                                break;
                                            case 2:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsLampsName[Variables.multiplepos], 0, 2,0, 0,false, 0,0);
                                                break;
                                            case 3:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsDiodsName[Variables.multiplepos], 0,3, 0,0, false, 0,0);
                                                break;
                                            case 4:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOthersName[Variables.multiplepos], 0, 4,0,0, false, 0,0);
                                                break;
                                            case 5:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOutsideName[Variables.multiplepos], 1,5, 0,0, false, 0,0);
                                                break;
                                        }
                                        //spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.multiplelampType, Variables.lampsName[Variables.multiplepos], 0, 0, false, 0,0);
                                    }
                                });
                            } else if (Variables.addMultipleRowsFlag || (Variables.copyFlag && Variables.copyType == 0) || Variables.selectZoneFlag) {      //Отрисовка зоны выделения
                                if (!Variables.firstTouch) {        //Если первое нажатие - создаем зону выделения
                                    Variables.firstPointX = event.getX();
                                    Variables.firstPointY = event.getY();
                                    Variables.firstTouch = true;
                                    selectionZone = new View(Variables.activity);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1, 1);
                                    selectionZone.setLayoutParams(params);
                                    selectionZone.setBackgroundColor(Color.parseColor("#808080"));
                                    selectionZone.setX(event.getX());
                                    selectionZone.setY(event.getY());
                                    selectionZone.getBackground().setAlpha(128);
                                    Variables.planLay.addView(selectionZone);
                                } else {            //Иначе - изменяем размер в соответствии с положением пальца/стилуса на экране
                                    float temp1 = event.getX() - Variables.firstPointX;
                                    float temp2 = event.getY() - Variables.firstPointY;
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) temp1, (int) temp2);
                                    selectionZone.setLayoutParams(params);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if ((Variables.addMultipleRowsFlag || Variables.copyFlag || Variables.selectZoneFlag) && Variables.firstTouch) {        //Изменяем размер в соответствии с положением пальца/стилуса на экране
                                float temp1 = event.getX() - Variables.firstPointX;
                                float temp2 = event.getY() - Variables.firstPointY;
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) temp1, (int) temp2);
                                selectionZone.setLayoutParams(params);
                                selectionZone.setRotationY(180);
                            }
                            if (Variables.copyFlag && Variables.copyType == 1) {
                                Variables.moveCopiedVector(event.getX(), event.getY());
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:         //Выполняем поиск светильников внутри этой зоны, убираем зону, светильники добавляем во временным вектор
                            if ((Variables.copyFlag && Variables.copyType == 0) || Variables.selectZoneFlag) {
                                for (Lamp lamp : Variables.copyVector) {
                                    lamp.getImage().setBackgroundResource(0);
                                }
                                Variables.copyVector.clear();       //Очищаем временный вектор
                                for (int i = 0; i < Variables.current_floor.rooms.size(); i++) {
                                    Room temp = Variables.current_floor.rooms.elementAt(i);
                                    for (int j = 0; j < temp.lamps.size(); j++) {
                                        if ((temp.lamps.elementAt(j).getImage().getX() >= selectionZone.getX() && temp.lamps.elementAt(j).getImage().getX() <= (selectionZone.getX() + selectionZone.getWidth())) && (temp.lamps.elementAt(j).getImage().getY() >= selectionZone.getY() && temp.lamps.elementAt(j).getImage().getY() <= (selectionZone.getY() + selectionZone.getHeight()))) {
                                            Variables.copyVector.add(temp.lamps.elementAt(j));
                                            temp.lamps.elementAt(j).getImage().setBackgroundResource(R.color.blue);
                                        }
                                    }
                                }
                                for (int i=0;i<Variables.current_floor.unusedLamps.size();i++){
                                    if ((Variables.current_floor.unusedLamps.elementAt(i).getImage().getX() >= selectionZone.getX() && Variables.current_floor.unusedLamps.elementAt(i).getImage().getX() <= (selectionZone.getX() + selectionZone.getWidth())) && (Variables.current_floor.unusedLamps.elementAt(i).getImage().getY() >= selectionZone.getY() && Variables.current_floor.unusedLamps.elementAt(i).getImage().getY() <= (selectionZone.getY() + selectionZone.getHeight()))) {
                                        Variables.copyVector.add(Variables.current_floor.unusedLamps.elementAt(i));
                                        Variables.current_floor.unusedLamps.elementAt(i).getImage().setBackgroundResource(R.color.blue);
                                    }
                                }
                                Variables.planLay.removeView(selectionZone);
                                Variables.firstTouch = false;
                            }
                            break;
                    }
                }
                if (Variables.getAddFlag()){
                    return false;
                }else {
                    return true;
                }
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
                if (room!=touchedRoom)
                    clearInfoLamp();
                if (!type) { //Если нам нужно отследить положение при перемещении светильника
                    //lastRoom = touchedRoom;
                    touchedRoom = room;
                    lastRoom=touchedRoom;
                    setTouchedRoomInfo();
                    return;
                }else{  //Если нужно отследить первое нажатие на светильник
                    lastRoom=touchedRoom;
                    touchedRoom = room;
                    setTouchedRoomInfo();
                    return;
                }
            }
        }
        //lastRoom=touchedRoom;
        touchedRoom=null;
    }

    public void setTouchedRoomInfo(){
        if (touchedRoom!=null) {
            Variables.RoomInfo.setVisibility(View.VISIBLE);     //Отображаем данные о комнате
            Variables.roomNumber.setText(touchedRoom.getNumber());
            Variables.roomHeight.setText(touchedRoom.getHeight());
            Variables.type.setSelection(touchedRoom.getType_pos());
            Variables.daysPerWeek.setSelection(touchedRoom.getDays());
            Variables.hoursPerDay.setSelection(touchedRoom.getHoursPerDay());
            Variables.hoursPerSunday.setSelection(touchedRoom.getHoursPerSunday());
            Variables.hoursPerWeekend.setSelection(touchedRoom.getHoursPerWeekend());
            Variables.roofType.setSelection(touchedRoom.getRoofType());
            Variables.roomComments.setText(touchedRoom.getComments());
            EditText tempText = Variables.activity.findViewById(R.id.roomLamps);
            tempText.setText(Integer.toString(touchedRoom.lamps.size()));
            Variables.roomGrid.removeAllViews();
            Variables.showAllPhotos(touchedRoom);
            if (Variables.roomHeightDefaultCheck.isChecked()) {
                if (String.valueOf(Variables.roomHeight.getText()).equals("0.0")) {
                    int index = Variables.roofType.getSelectedItemPosition();
                    Variables.roomHeight.setText(Variables.current_floor.roofHeightDefault.elementAt(index));
                    touchedRoom.setHeight(Variables.current_floor.roofHeightDefault.elementAt(index));
                    Variables.buttons.lastIndex = -1;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void detectRoomTouch(float x, float y){      //Функиця определения нажатия на комнату и вывода информации о ней
        for (Room room:Variables.current_floor.rooms){
            if (room.detectTouch(x,y)) {
                if (room!=touchedRoom) {
                    clearInfoLamp();
                    Variables.RoomInfo.setVisibility(View.VISIBLE);     //Отображаем данные о комнате
                    touchedRoom = room;
                    Variables.roomNumber.setText(touchedRoom.getNumber());
                    Variables.roomHeight.setText(touchedRoom.getHeight());
                    Variables.type.setSelection(touchedRoom.getType_pos());
                    Variables.daysPerWeek.setSelection(touchedRoom.getDays());
                    Variables.hoursPerDay.setSelection(touchedRoom.getHoursPerDay());
                    Variables.hoursPerSunday.setSelection(touchedRoom.getHoursPerSunday());
                    Variables.hoursPerWeekend.setSelection(touchedRoom.getHoursPerWeekend());
                    Variables.roofType.setSelection(touchedRoom.getRoofType());
                    Variables.roomComments.setText(touchedRoom.getComments());
                    EditText tempText = Variables.activity.findViewById(R.id.roomLamps);
                    tempText.setText(Integer.toString(touchedRoom.lamps.size()));
                    Variables.showAllPhotos(touchedRoom);
                    if (Variables.roomHeightDefaultCheck.isChecked()) {
                        if (String.valueOf(Variables.roomHeight.getText()).equals("0.0")) {
                            int index = Variables.roofType.getSelectedItemPosition();
                            Variables.roomHeight.setText(Variables.current_floor.roofHeightDefault.elementAt(index));
                            touchedRoom.setHeight(Variables.current_floor.roofHeightDefault.elementAt(index));
                            Variables.buttons.lastIndex = -1;
                        }
                    }
                }
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
        setListenerToImage();   //Ставим слушатель для получения информации о комнате
        imageWrap.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {     //Отслеживание нажатий на план
                x = event.getX();
                y = event.getY();
                if (!Variables.disableMovingPlan){
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_POINTER_DOWN:           //Нажатие второго пальца для приближения
                            if (event.getPointerCount() > 1) {
                                isReleased = false;
                                prevLength = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));
                                //pivotX = (event.getX(0)+event.getX(1))/2.f;
                                //pivotY = (event.getY(0)+event.getY(1))/2.f;
                                //Variables.planLay.setPivotX(pivotX);
                                //Variables.planLay.setPivotY(pivotY);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE: // движение пальцев по экрану
                            if (event.getPointerCount() > 1) {     //Задействовано два пальца - приближение

                                lenght = Math.sqrt(Math.pow((double) (event.getX(0)) - (double) (event.getX(1)), 2) + Math.pow((double) (event.getY(0)) - (double) (event.getY(1)), 2));

                                double dx = lenght - prevLength;
                                float currentScale = Variables.planLay.getScaleX();
                                if (currentScale + (float) dx / (200 - Variables.planLay.getScaleX()) > 0.2) {
                                    Variables.planLay.setScaleX(currentScale + (float) dx / (200 - Variables.planLay.getScaleX()));
                                    Variables.planLay.setScaleY(currentScale + (float) dx / (200 - Variables.planLay.getScaleX()));
                                }
                                prevLength = lenght;
                            } else {             //Задействован один палец - перемещение
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
                            if (event.getPointerCount() == 1)
                                isReleased = true;
                            break;
                    }
            }
                previousX = x;
                previousY = y;
                return true;
            }
        });
    }

    public void rotateImg(float angle,ImageView imageView,String type, int resource){     //Функция поворота светильника
        if (imageView!=null) {      //Если светильник существует
            Resources resources = Variables.activity.getResources();
            int resourceId;
            if (resource!=-1){
                resourceId=resource;
            }else {
                resourceId = resources.getIdentifier(type, "drawable",
                        Variables.activity.getPackageName());
            }
            Bitmap myImg = BitmapFactory.decodeResource(Variables.activity.getResources(), resourceId);     //Получаем картинку светильника
            if (myImg!=null) {
                Matrix matrix = new Matrix();
                matrix.postRotate(angle);

                Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),      //Выполняем поворот
                        matrix, true);

                imageView.setImageBitmap(rotated);      //Присваиваем повернутую картинку светильнику
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    //Функция создания и отображения светильника на плане
    public void spawnLamp(Integer type, int pos,String lampName,int placeType,int groupIndex,float cordX,float cordY,boolean type_spawning,float rotation,float scaleType) {
        boolean escapePowerSet=false;
        if (tempView != null || type_spawning) {                   //Если активная функция добавления светильника
                ImageView imageView = new ImageView(Variables.activity);
                imageView.setImageResource(type);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                imageView.setLayoutParams(params);
                if (type_spawning){     //Если создание светильника не по нажатию кнопки добавления - берем данные из переданных параметров
                    imageView.setScaleX(scaleType);
                    imageView.setScaleY(scaleType);
                }else {         //Иначе берем данные из последних
                    imageView.setScaleX(Variables.lastScaletype);
                    imageView.setScaleY(Variables.lastScaletype);
                }
                setListener(imageView);
                if (type_spawning){     //Если тип появления - не по нажатию кнопки добавления - берем переданные в метод параметры
                    imageView.setX(cordX);
                    imageView.setY(cordY);
                    rotateImg(rotation, imageView, lampName,-1);
                }else {         //Иначе берем данные из маркера
                    imageView.setX(tempView.getX());
                    imageView.setY(tempView.getY());
                }
                if (!type_spawning) {       //Удаляем маркер если добавление светильника по нажатию кнопки добавления
                    Variables.planLay.removeView(tempView);
                }
                Lamp lamp = new Lamp();     //Создаем новый светильник
                if (touchedRoom != null) {        //Если нажатая комната размечена
                    lamp.setRotationAngle(rotation);
                    lamp.setTypeImage(lampName);
                    lamp.setLampRoom(touchedRoom.getNumber());
                    lamp.setImage(imageView);
                    if (touchedRoom!=null && touchedRoom.lamps.size()==0 && groupIndex==0){
                        touchedRoom.setRoofType(1);
                        Variables.roofType.setSelection(1);
                        if (Variables.roomHeightDefaultCheck.isChecked()){
                            touchedRoom.setHeight((String) Variables.current_floor.roofHeightDefault.elementAt(1));
                            Variables.roomHeight.setText(touchedRoom.getHeight());
                        }
                    }
                    touchedRoom.lampPush(lamp);     //Добавляем светильник в вектор светильников нажатой комнаты
                    lamp.setView();                 //Добавляем картинку светильника на экран
                } else {                           //Иначе, если нажатая комната не размечена
                    lamp.setRotationAngle(rotation);
                    lamp.setLampRoom("-1");
                    imageView.setBackgroundResource(R.color.blue);
                    lamp.setTypeImage(lampName);
                    lamp.setImage(imageView);
                    Variables.current_floor.unusedLamps.add(lamp);          //Добавляем светильник в вектор непривязанных светильников
                    lamp.setView();                 //Добавляем картинку светильника на экран
                }
            if (groupIndex == 0 || groupIndex == 1) {
                lamp.setType("Люминесцентный");
                if (groupIndex==0){
                    lamp.setMontagneType(1);
                    if (lampName.equals("lampnakalspot")){
                        lamp.setType("Лампа");
                        lamp.setPower("накаливания 60Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet=true;
                    }else if (lampName.equals("lampkll15spot")){
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet=true;
                    }else if (lampName.equals("lampgalogen35")){
                        lamp.setType("Лампа галогенная");
                        lamp.setPower("35Вт");
                        lamp.setComments("Цоколь G5.3");
                        escapePowerSet=true;
                    }
                }
            }else if(groupIndex==2){
                if (!lampName.equals("lampkll15")) {
                    lamp.setType("Лампа");
                }else{
                    lamp.setType("КЛЛ");
                    lamp.setPower("15Вт");
                    escapePowerSet=true;
                }
            }else if (groupIndex==3){
                lamp.setType("Светодиодный");
                if (lampName.equals("diod4_18") || lampName.equals("diod4_18nakl")){
                    lamp.setComments("Аналог 4*18");
                    if (lampName.equals("diod4_18")) {
                        lamp.setMontagneType(1);
                    }
                }else if (lampName.equals("diod2_36")){
                    lamp.setComments("Аналог 2*36");
                }
                else if (lampName.equals("lampdiodspot") || lampName.equals("lustradiod")){
                    lamp.setType("Лампа");
                    lamp.setPower("светодиодная 12Вт");
                    //lamp.setComments("Спот");
                    escapePowerSet=true;
                    if (lampName.equals("lampdiodspot")) {
                        lamp.setMontagneType(1);
                    }
                    if (lampName.equals("lustradiod")){
                        lamp.setLampsAmount(3);
                    }
                }
            }else if (groupIndex==4){
                if (!lampName.equals("lustranakal") && !lampName.equals("lustrakll")){
                    lamp.setType("Светодиодный");
                    switch (lampName){
                        case "diod4_18lampsvstr":
                            lamp.setComments("Светодиодные лампы в плафоне 4*18Вт");
                            lamp.setMontagneType(1);
                        case "diod4_18lampsnakl":
                            lamp.setComments("Светодиодные лампы в плафоне 4*18Вт");
                            break;
                        case "diod2_36lampsnakl":
                            lamp.setComments("Светодиодные лампы в плафоне 2*36Вт");
                            break;
                        case "diod2_18lampsnakl":
                            lamp.setComments("Светодиодные лампы в плафоне 2*18Вт");
                            break;
                        case "diod1_36lampsnakl":
                            lamp.setComments("Светодиодные лампы в плафоне 1*36Вт");
                            break;
                        case "diod1_18lampsnakl":
                            lamp.setComments("Светодиодные лампы в плафоне 1*18Вт");
                            break;
                    }
                }else{
                    if (lampName.equals("lustranakal")){
                        lamp.setType("Лампа");
                        lamp.setPower("накаливания 60Вт");
                    }else if (lampName.equals("lustrakll")){
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                    }
                        lamp.setLampsAmount(3);
                        escapePowerSet=true;
                }
            }else if (groupIndex==5){
                escapePowerSet=true;
                switch (lampName){
                    case "drlfasad":
                        lamp.setType("ДРЛ");
                        lamp.setPower("250Вт");
                    break;
                    case "dnatfasad":
                        lamp.setType("ДНаТ");
                        lamp.setPower("250Вт");
                        break;
                    case "mglfasad":
                        lamp.setType("МГЛ");
                        lamp.setPower("250Вт");
                        lamp.setMontagneType(1);
                        break;
                    case "diodfasad":
                        lamp.setType("Светодиодный");
                        lamp.setPower("50Вт");
                        break;
                }
            }
                lamp.setPlaceType(placeType);
                lamp.setGroupIndex(groupIndex);
                if (!escapePowerSet) {
                    switch (groupIndex) {
                        case 0:
                            lamp.setPower(Variables.lampVstraivaemieNames[pos]);
                            break;
                        case 1:
                            lamp.setPower(Variables.lampNakladnieNames[pos]);
                            break;
                        case 2:
                            lamp.setPower(Variables.lampLampsNamesForOutput[pos]);
                            break;
                        case 3:
                            lamp.setPower(Variables.lampDiodsNames[pos]);
                            break;
                        case 4:
                            lamp.setPower(Variables.lampOthersNames[pos]);
                            break;
                        case 5:
                            lamp.setPower(Variables.lampOutsideNames[pos]);
                            break;
                    }
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




    private float getDegreesFromTouchEvent(MotionEvent event,ImageView view,float x, float y){      //Получение угла поворота от нажатия
        double var = Math.atan2((view.getY() + (event.getY()))-y, (view.getX() + (event.getX()))-x);
        float degrees = (float) Math.toDegrees(var);
        //Если углы равны определенному значению, светильник в рамках определенного угла не будет поворачиваться
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
                if (Variables.removeMode) {     //Если функция удаления светильника
                    setTouchedRoom(x, y, false);   //Первичное нажатие на светильник
                    touchedLamp = getLampByTouch(imageView);
                    if (touchedLamp!=null) {
                        Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                            Variables.planLay.removeView(touchedLamp.getImage());
                        });
                        if (touchedLamp.getLampRoom()!="-1"){
                            Room room = Variables.getRoomByNumber(touchedLamp.getLampRoom(),Variables.current_floor);
                            if (room!=null && room.lamps.contains(touchedLamp)){        //Если светильник привязан - удаляем из привязанной комнаты
                                room.lamps.remove(touchedLamp);
                            }else{          //Иначе удаляем из неиспользуемых светильников
                                Variables.current_floor.unusedLamps.remove(touchedLamp);
                            }
                        }else{      //Иначе удаляем из неиспользуемых светильников
                            Variables.current_floor.unusedLamps.remove(touchedLamp);
                        }
                    }else if (touchedLamp==null){
                        Variables.planLay.removeView(imageView);
                        Variables.removeLampByView(imageView);
                    }
                }else {
                    switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {        //Иначе если не функция удаления
                        case MotionEvent.ACTION_DOWN:       //Получаем нажатый светильнк
                            if (!Variables.moveOnlySelectedZone) {      //Если не перемещение по выделенной зоне - двигаем светильник
                                sumXY = (imageView.getX() + (event.getX())) + (imageView.getY() + (event.getY()));
                                setTouchedRoom(imageView.getX() + (event.getX()), imageView.getY() + (event.getY()), false);   //Первичное нажатие на светильник
                                touchedLamp = getLampByTouch(imageView);
                                setInfoLamp(touchedLamp);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Variables.scalemode) {          //Если режим изменения масштаба - меняем масштаб
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
                            } else if (Variables.rotateMode) {      //Если режим поворота - поворачиваем
                                isReleased = false;
                                float angle = getDegreesFromTouchEvent(event, imageView, x, y);
                                if (!Variables.selectZoneFlag && touchedLamp!=null) {
                                    touchedLamp.setRotationAngle(angle);        //Устанавливаем в светильник угол поворота
                                    rotateImg(angle, imageView, touchedLamp.getTypeImage(),-1);        //Выполняем поворот картинки
                                }else{
                                    for (Lamp lamp:Variables.copyVector){
                                        lamp.setRotationAngle(angle);
                                        rotateImg(angle,lamp.getImage(),lamp.getTypeImage(),-1);
                                    }
                                }
                                //imageView.setRotation((float) angle);
                            } else if (Variables.moveOnlySelectedZone) {        //Если режим перемещния выбранной зоны
                                if (imageView == Variables.tempCopiedLamp.getImage()) {     //Двигаем светильники во временном векторе
                                    Variables.moveCopiedVector(imageView.getX() + (event.getX()), imageView.getY() + (event.getY()));
                                }
                            } else if (Variables.getMoveFlag()) {       //Иначе - если обычное перемещение - двигаем светильник
                                //Иначе обычное перемещение
                                //setTouchedRoom(imageView.getX() + (event.getX()), imageView.getY() + (event.getY()), true);  //Перемещение светильника
                                imageView.setX((imageView.getX() + (event.getX())) - imageView.getWidth() / 2);
                                imageView.setY((imageView.getY() + (event.getY())) - imageView.getHeight() / 2);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            isReleased = true;
                            setTouchedRoom(imageView.getX() + (event.getX()), imageView.getY() + (event.getY()), true);  //Перемещение светильника
                            //lastRoom = touchedRoom;
                            break;
                    }

                    if (Variables.getMoveFlag()){       //Если обычное перемещение светильника - перепривязка светильников к комнатам
                        if (!Variables.scalemode && !Variables.removeMode && !Variables.moveOnlySelectedZone) {
                            if (touchedRoom != lastRoom && touchedLamp != null && isReleased) {   //Если светильник в процессе перемещения оказался в другой комнате, то убираем его из старой комнаты и привязываем к новой
                                if (lastRoom != null) {
                                    //if (lastRoom.lamps.contains(touchedLamp)) {
                                    lastRoom.lampRemove(touchedLamp);
                                    //}
                                    // if (!Variables.current_floor.unusedLamps.contains(touchedLamp)) {
                                    Variables.current_floor.unusedLamps.add(touchedLamp);
                                    //}
                                    touchedLamp.setLampRoom("-1");
                                    touchedLamp.getImage().setBackgroundResource(R.color.blue);
                                    lastRoom = touchedRoom;
                                }
                                if (touchedRoom != null) {
                                    //if (!touchedRoom.lamps.contains(imageView)) {
                                    Variables.current_floor.unusedLamps.remove(touchedLamp);
                                    touchedRoom.lampPush(touchedLamp);
                                    touchedLamp.setLampRoom(touchedRoom.getNumber());
                                    touchedLamp.getImage().setBackgroundResource(0);
                                    //}
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
            Variables.lampRoom.setText(String.valueOf(lamp.getLampRoom()));
            Variables.lampType.setText(lamp.getType());
            Variables.lampPower.setText(lamp.getPower());
            Variables.lampComments.setText(lamp.getComments());
            Variables.montagneType.setSelection(lamp.getMontagneType());
            Variables.placeType.setSelection(lamp.getPlaceType());
            Variables.showLampsAllPhotos(lamp);
            if (lamp.getTypeImage().equals("lustranakal") || lamp.getTypeImage().equals("lustrakll") || lamp.getTypeImage().equals("lustradiod")){
                Variables.lampAmountText.setVisibility(View.VISIBLE);
                Variables.lampAmountEdit.setVisibility(View.VISIBLE);
                Variables.lampAmountEdit.setText(String.valueOf(lamp.getLampsAmount()));
            }else{
                Variables.lampAmountText.setVisibility(View.GONE);
                Variables.lampAmountEdit.setVisibility(View.GONE);
            }
            if (lamp.getPlaceType()==1){
                if (lamp.getGroupIndex()==5) {
                    Variables.montagneOutsideTypeTxt.setVisibility(View.VISIBLE);
                    Variables.montagneOutsideType.setVisibility(View.VISIBLE);
                    Variables.positionOutsideTxt.setVisibility(View.VISIBLE);
                    Variables.positionOutside.setVisibility(View.VISIBLE);
                    Variables.isStolbTxt.setVisibility(View.VISIBLE);
                    Variables.isStolbCheck.setVisibility(View.VISIBLE);
                    Variables.montagneTypeTxt.setVisibility(View.GONE);
                    Variables.montagneType.setVisibility(View.GONE);
                    Variables.montagneOutsideType.setSelection(lamp.getMontagneType());
                    Variables.positionOutside.setSelection(lamp.getPositionOutside());
                    Variables.isStolbCheck.setChecked(lamp.isStolb());
                }else{
                    Variables.positionOutsideTxt.setVisibility(View.VISIBLE);
                    Variables.positionOutside.setVisibility(View.VISIBLE);
                    Variables.positionOutside.setSelection(lamp.getPositionOutside());
                }
            }else{
                Variables.montagneOutsideTypeTxt.setVisibility(View.GONE);
                Variables.montagneOutsideType.setVisibility(View.GONE);
                Variables.positionOutsideTxt.setVisibility(View.GONE);
                Variables.positionOutside.setVisibility(View.GONE);
                Variables.isStolbTxt.setVisibility(View.GONE);
                Variables.isStolbCheck.setVisibility(View.GONE);
                Variables.montagneTypeTxt.setVisibility(View.VISIBLE);
                Variables.montagneType.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearInfoLamp(){
        if (!Variables.getMoveFlag()){
        touchedLamp=null;
        }
        Variables.lampRoom.setText("");
        Variables.lampType.setText("");
        Variables.lampPower.setText("");
        Variables.lampComments.setText("");
        Variables.lampAmountText.setVisibility(View.GONE);
        Variables.lampAmountEdit.setVisibility(View.GONE);
        Variables.lampAmountEdit.setText("");
        Variables.montagneOutsideTypeTxt.setVisibility(View.GONE);
        Variables.montagneOutsideType.setVisibility(View.GONE);
        Variables.positionOutsideTxt.setVisibility(View.GONE);
        Variables.positionOutside.setVisibility(View.GONE);
        Variables.isStolbTxt.setVisibility(View.GONE);
        Variables.isStolbCheck.setVisibility(View.GONE);
        Variables.montagneTypeTxt.setVisibility(View.VISIBLE);
        Variables.montagneType.setVisibility(View.VISIBLE);
        Variables.clearLampGrid();
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
        for (int i=0;i<Variables.current_floor.unusedLamps.size();i++){         //Если не нашли - в непривязанных светильниках
            if (Variables.current_floor.unusedLamps.elementAt(i)!=null) {
                if (Variables.current_floor.unusedLamps.elementAt(i).getImage() == image)
                    return Variables.current_floor.unusedLamps.elementAt(i);
            }
        }
        return null;            //Иначе - светильник не найден
    }
}
