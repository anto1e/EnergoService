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
    private boolean ifReleased=false;       //Если один палец отпущен с экрана(блокировка движения при приближении)



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
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Variables.lampSize, Variables.lampSize);
                                    tempView.setLayoutParams(params);
                                    tempView.setPivotX(0);
                                    tempView.setPivotY(0);
                                    tempView.setScaleX(Variables.lastScaletype);
                                    tempView.setScaleY(Variables.lastScaletype);
                                    tempView.setBackgroundColor(Color.parseColor("#808080"));
                                    tempView.getBackground().setAlpha(128);
                                    tempView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println(event.getX()+";"+event.getY());
                                            tempView.setX(event.getX() - (tempView.getWidth()/2)*tempView.getScaleX());
                                            tempView.setY(event.getY() - (tempView.getHeight()/2)*tempView.getScaleY());
                                        }
                                    });
                                } else {      //Иначе устанавливаем ему координаты нажатия
                                    tempView.setX(event.getX() - (tempView.getWidth()/2)*tempView.getScaleX());
                                    tempView.setY(event.getY() - (tempView.getHeight()/2)*tempView.getScaleY());
                                }
                                // Log.d("Touched at: ",Float.toString(event.getX())+" , " + Float.toString(event.getY()));
                            } else if (Variables.addMultiple_flag && Variables.multiplepos != -1 && Variables.multipleType != -1 && Variables.multiplelampType != -1) {     //Если выбрана функция добавления множества светильников по нажатию
                                if (tempView == null) {        //Если маркера появления светильника нет - отрисовываем его
                                    tempView = new View(Variables.activity);
                                    tempView.setPivotX(0);
                                    tempView.setPivotY(0);
                                    Variables.planLay.addView(tempView);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Variables.lampSize, Variables.lampSize);
                                    tempView.setLayoutParams(params);
                                    tempView.setScaleX(Variables.lastScaletype);
                                    tempView.setScaleY(Variables.lastScaletype);
                                    tempView.setBackgroundColor(Color.parseColor("#808080"));
                                }       //Иначе устанавливаем ему координаты нажатия
                                tempView.post(new Runnable() {      //После появления маркера - создаем светильник
                                    @Override
                                    public void run() {
                                        tempView.setX(event.getX() - (tempView.getWidth()/2)*tempView.getScaleX());
                                        tempView.setY(event.getY() - (tempView.getHeight()/2)*tempView.getScaleY());
                                        int placeType=0;
                                        switch (Variables.currentLampsPanelIndex){      //Создаем светильник в зависимости от его типа
                                            case 0:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsVstraivaemieName[Variables.multiplepos], placeType,0, 0,0, false, 0,0);
                                                break;
                                            case 1:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsNakladnieName[Variables.multiplepos],placeType, 1, 0,0, false, 0,0);
                                                break;
                                            case 2:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsLampsName[Variables.multiplepos], placeType, 2,0, 0,false, 0,0);
                                                break;
                                            case 3:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsDiodsName[Variables.multiplepos], placeType,3, 0,0, false, 0,0);
                                                break;
                                            case 4:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsDoskiName[Variables.multiplepos], placeType, 4,0,0, false, 0,0);
                                                break;
                                            case 5:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsPodvesName[Variables.multiplepos], placeType,5, 0,0, false, 0,0);
                                                break;
                                            case 6:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOthersName[Variables.multiplepos], placeType, 6,0,0, false, 0,0);
                                                break;
                                            case 7:
                                                spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOutsideName[Variables.multiplepos], placeType,7, 0,0, false, 0,0);
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
                                    Variables.buttons.showTempLamps();
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
                            if (Variables.addMultipleRowsFlag){
                                Variables.buttons.showTempLamps();
                            }
                            if ((Variables.copyFlag && Variables.copyType == 0) || Variables.selectZoneFlag) {
                                for (Lamp lamp : Variables.copyVector) {
                                    lamp.getImage().setBackgroundResource(0);
                                }
                                Variables.copyVector.clear();       //Очищаем временный вектор
                                for (int i = 0; i < Variables.current_floor.rooms.size(); i++) {
                                    Room temp = Variables.current_floor.rooms.elementAt(i);
                                    for (int j = 0; j < temp.lamps.size(); j++) {
                                        if (((temp.lamps.elementAt(j).getImage().getX()+temp.lamps.elementAt(j).getImage().getWidth()/2.f) >= selectionZone.getX() && (temp.lamps.elementAt(j).getImage().getX()+temp.lamps.elementAt(j).getImage().getWidth()/2.f) <= (selectionZone.getX() + selectionZone.getWidth())) && ((temp.lamps.elementAt(j).getImage().getY()+temp.lamps.elementAt(j).getImage().getHeight()/2.f) >= selectionZone.getY() && (temp.lamps.elementAt(j).getImage().getY()+temp.lamps.elementAt(j).getImage().getHeight()/2.f) <= (selectionZone.getY() + selectionZone.getHeight()))) {
                                            Variables.copyVector.add(temp.lamps.elementAt(j));
                                            temp.lamps.elementAt(j).getImage().setBackgroundResource(R.color.blue);
                                        }
                                    }
                                }
                                for (int i=0;i<Variables.current_floor.unusedLamps.size();i++){
                                    if (((Variables.current_floor.unusedLamps.elementAt(i).getImage().getX()+Variables.current_floor.unusedLamps.elementAt(i).getImage().getWidth()/2.f) >= selectionZone.getX() && (Variables.current_floor.unusedLamps.elementAt(i).getImage().getX()+Variables.current_floor.unusedLamps.elementAt(i).getImage().getWidth()/2.f) <= (selectionZone.getX() + selectionZone.getWidth())) && ((Variables.current_floor.unusedLamps.elementAt(i).getImage().getY()+Variables.current_floor.unusedLamps.elementAt(i).getImage().getHeight()/2.f) >= selectionZone.getY() && (Variables.current_floor.unusedLamps.elementAt(i).getImage().getY()+Variables.current_floor.unusedLamps.elementAt(i).getImage().getHeight()/2.f) <= (selectionZone.getY() + selectionZone.getHeight()))) {
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

    public void setTouchedRoomInfo(){           //Отображения информации о комнате
        if (touchedRoom!=null) {
            Variables.buttons.lastType = touchedRoom.getType_pos();
            Variables.buttons.lastDays =touchedRoom.getDays();
            Variables.buttons.lastHours = touchedRoom.getHoursPerDay();
            Variables.buttons.lastHoursWeekend=touchedRoom.getHoursPerWeekend();
            Variables.buttons.lastHoursSunday=touchedRoom.getHoursPerSunday();
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
            if (Variables.roomHeightDefaultCheck.isChecked()) {     //Задание высоты по умолчанию
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
            if (room!=null && room.detectTouch(x,y)) {
                if (room!=touchedRoom) {
                    clearInfoLamp();
                    Variables.RoomInfo.setVisibility(View.VISIBLE);     //Отображаем данные о комнате
                    touchedRoom = room;
                    Variables.buttons.lastType = touchedRoom.getType_pos();
                    Variables.buttons.lastDays =touchedRoom.getDays();
                    Variables.buttons.lastHours = touchedRoom.getHoursPerDay();
                    Variables.buttons.lastHoursWeekend=touchedRoom.getHoursPerWeekend();
                    Variables.buttons.lastHoursSunday=touchedRoom.getHoursPerSunday();
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


    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private float pivX1,pivY1,pivX2,pivY2;
    private float mAngle;
    float lastAngle = 0;
    public float getAngle() {
        return mAngle;
    }

    private float angleBetweenLines (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY)
    {
        float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
        float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

        float angle = ((float)Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return angle;
    }

    @SuppressLint("ClickableViewAccessibility")
    //Отслеживание нажатий на план для перемещения/приближения
    public void startDetecting(){     //Отслеживание нажатий на план
        Variables.planLay = Variables.activity.findViewById(R.id.planLayout);
        LinearLayout imageWrap = Variables.activity.findViewById(R.id.imageWrap);
        setListenerToImage();   //Ставим слушатель для получения информации о комнате
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;


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
                                if (Variables.allowRotationPlanFlag) {
                                    sX = event.getX(0);
                                    sY = event.getY(0);
                                    fX = event.getX(1);
                                    fY = event.getY(1);
                                    lastAngle = Variables.planLay.getRotation();
                                }
                                //pivotX = (event.getX(0)+event.getX(1))/2.f;
                                //pivotY = (event.getY(0)+event.getY(1))/2.f;
                                //Variables.planLay.setPivotX(pivotX);
                                //Variables.planLay.setPivotY(pivotY);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE: // движение пальцев по экрану
                            if (event.getPointerCount() > 1) {     //Задействовано два пальца - приближение
                                if (Variables.allowRotationPlanFlag) {
                                    float nfX, nfY, nsX, nsY;
                                    nsX = event.getX(0);
                                    nsY = event.getY(0);
                                    nfX = event.getX(1);
                                    nfY = event.getY(1);
                                    mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
                                    mAngle *= -1;
                                    float angle = lastAngle + mAngle;
                                    Variables.planLay.setRotation(angle);
                                }
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
                        case MotionEvent.ACTION_UP:         //Если второй палец отпущен - включение перемещения по плану
                            if (event.getPointerCount() == 1)
                                isReleased = true;
                            break;
                    }
                }
                previousX = x;      //Предыдущее значение X
                previousY = y;      //Предыдущее значение Y
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
            imageView.setPivotX(0);
            imageView.setPivotY(0);
            imageView.setImageResource(type);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Variables.lampSize, Variables.lampSize);
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
                //imageView.setPivotX(0);
                //imageView.setPivotY(0);
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
            if (Variables.rotateMode){
                rotateImg(90, imageView, lampName,-1);
                rotation=90;
            }
            lamp.setPlaceType(placeType);
            if (touchedRoom != null) {        //Если нажатая комната размечена
                lamp.setRotationAngle(rotation);
                lamp.setTypeImage(lampName);
                lamp.setLampRoom(touchedRoom.getNumber());
                lamp.setImage(imageView);
                lamp.setTypeRoom(touchedRoom.getType_pos());
                lamp.setDaysWork(touchedRoom.getDays());
                lamp.setHoursWork(touchedRoom.getHoursPerDay());
                lamp.setHoursWeekendWork(touchedRoom.getHoursPerWeekend());
                lamp.setHoursSundayWork(touchedRoom.getHoursPerSunday());
                if (touchedRoom!=null && touchedRoom.lamps.size()==0 && ((groupIndex==0) || (lampName.equals("diod36vstr")) || (lampName.equals("diod40vstr")) || (lampName.equals("lampdiodspot12")) || (lampName.equals("lampdiodspot10")) || (lampName.equals("lampdiodspot15")))){     //Если добавленный светильника встраиваемый - задаем тип потолка армстронг
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
                if (groupIndex==7) {
                    lamp.setPlaceType(1);
                }
                lamp.setRotationAngle(rotation);
                lamp.setLampRoom("-1");
                imageView.setBackgroundResource(R.color.blue);
                lamp.setTypeImage(lampName);
                lamp.setImage(imageView);
                Variables.current_floor.unusedLamps.add(lamp);          //Добавляем светильник в вектор непривязанных светильников
                lamp.setView();                 //Добавляем картинку светильника на экран
            }
            if (groupIndex==0) {
                lamp.setType("Люминесцентный");
                lamp.setMontagneType(1);
                switch (lampName) {
                    case "lampnakal60spot":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("60Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll15spot":
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampgalogen35":
                        lamp.setType("Лампа галогенная");
                        lamp.setPower("35Вт");
                        lamp.setComments("Цоколь G5.3");
                        escapePowerSet = true;
                        break;
                    case "lampnakal40spot":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("40Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll18spot":
                        lamp.setType("КЛЛ");
                        lamp.setPower("18Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll20spot":
                        lamp.setType("КЛЛ");
                        lamp.setPower("20Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll25spot":
                        lamp.setType("КЛЛ");
                        lamp.setPower("25Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll30spot":
                        lamp.setType("КЛЛ");
                        lamp.setPower("30Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampnakal75spot":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("75Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampnakal95spot":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("95Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                }
            }
            else if (groupIndex == 1) {           //Если это встраиваемый или накладной светильник
                lamp.setType("Люминесцентный");
                switch (lampName) {
                    case "lampnakal60":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("60Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampnakal40":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("40Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampnakal75":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("75Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampnakal95":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("95Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll15":
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                }
            }
            else if(groupIndex==2) {                    //Если это лампы
                switch (lampName) {
                    case "lampkll18":
                        lamp.setType("КЛЛ");
                        lamp.setPower("18Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll20":
                        lamp.setType("КЛЛ");
                        lamp.setPower("20Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll25":
                        lamp.setType("КЛЛ");
                        lamp.setPower("25Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll30":
                        lamp.setType("КЛЛ");
                        lamp.setPower("30Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampdiod12":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("12Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampdiod10":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("10Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampdiod15":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("15Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampdiod50":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("50Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                }
            }
            else if (groupIndex==3){               //Если это светодиодный светильник
                lamp.setType("Светодиодный");
                switch (lampName) {
                    case "diod36vstr":
                    case "diod36nakl":
                    case "diod40vstr":
                    case "diod40nakl":
                        lamp.setComments("Аналог 4*18");
                        if (lampName.equals("diod36vstr") || lampName.equals("diod40vstr")) {
                            lamp.setMontagneType(1);
                        }
                        break;
                    case "diod36long":
                    case "diod40long":
                        lamp.setComments("Аналог 2*36");
                        break;
                    case "diod18nakl":
                        lamp.setComments("Аналог 2*18");
                        break;
                    case "lustradiod":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("12Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        lamp.setLampsAmount(3);
                        break;
                    case "lampdiodspot12":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("12Вт");
                        lamp.setMontagneType(1);
                        escapePowerSet=true;
                        break;
                    case "lampdiodspot10":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("10Вт");
                        lamp.setMontagneType(1);
                        escapePowerSet=true;
                        break;
                    case "lampdiodspot15":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("15Вт");
                        lamp.setMontagneType(1);
                        escapePowerSet=true;
                        break;
                }
            }else if (groupIndex==4){
                lamp.setMontagneType(2);
                lamp.setComments("Над доской");
                if (touchedRoom!=null){
                    if (touchedRoom.getDays()==6){
                        lamp.setHoursWork(3);
                    }else if (touchedRoom.getDays()==7){
                        lamp.setHoursWork(3);
                        lamp.setHoursWeekendWork(2);
                    }
                }
                if (lampName.equals("diod18dosk") || lampName.equals("diod24dosk") || lampName.equals("diod20dosk")){
                    lamp.setType("Светодиодный");
                }else{
                    lamp.setType("Люминесцентный");
                }
            }
            else if (groupIndex==5){
                lamp.setMontagneType(3);
                switch (lampName) {
                    case "lampnakal60podves":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("60Вт");
                        escapePowerSet=true;
                        break;
                    case "lampnakal40podves":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("40Вт");
                        escapePowerSet=true;
                        break;
                    case "lampnakal75podves":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("75Вт");
                        escapePowerSet=true;
                        break;
                    case "lampnakal95podves":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("95Вт");
                        escapePowerSet=true;
                        break;
                    case "lampkll15podves":
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                        escapePowerSet=true;
                        break;
                    case "lampkll18podves":
                        lamp.setType("КЛЛ");
                        lamp.setPower("18Вт");
                        escapePowerSet=true;
                        break;
                    case "lampkll20podves":
                        lamp.setType("КЛЛ");
                        lamp.setPower("20Вт");
                        escapePowerSet=true;
                        break;
                    case "lampkll25podves":
                        lamp.setType("КЛЛ");
                        lamp.setPower("25Вт");
                        escapePowerSet=true;
                        break;
                    case "lampkll30podves":
                        lamp.setType("КЛЛ");
                        lamp.setPower("30Вт");
                        escapePowerSet=true;
                        break;
                    case "lampdiod12podves":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("12Вт");
                        escapePowerSet=true;
                        break;
                    case "lampdiod10podves":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("10Вт");
                        escapePowerSet=true;
                        break;
                    case "lampdiod15podves":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("15Вт");
                        escapePowerSet=true;
                        break;
                }
            }
            else if (groupIndex==6){           //Если это другой тип светильника
                if (lampName.equals("unknowntype") || lampName.equals("unknowntypediod")) {
                    if (lampName.equals("unknowntypediod")){
                        lamp.setType("Светодиодный");
                    }else{
                        lamp.setType("Люминесцентный");
                    }
                    lamp.setPower("?");
                    escapePowerSet=true;
                }else
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
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("60Вт");
                    }else if (lampName.equals("lustrakll")){
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                    }
                    lamp.setLampsAmount(3);
                    escapePowerSet=true;
                }
            }else if (groupIndex==7){           //Если это наружное освещение
                escapePowerSet=true;
                switch (lampName){
                    case "lampnakal60":
                        lamp.setType("Лампа накаливания");
                        lamp.setPower("60Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampkll15":
                        lamp.setType("КЛЛ");
                        lamp.setPower("15Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "lampdiod12":
                        lamp.setType("Лампа светодиодная");
                        lamp.setPower("12Вт");
                        //lamp.setComments("Спот");
                        escapePowerSet = true;
                        break;
                    case "drl250":
                        lamp.setType("ДРЛ");
                        lamp.setPower("250Вт");
                        break;
                    case "drl400":
                        lamp.setType("ДРЛ");
                        lamp.setPower("400Вт");
                        break;
                    case "drl125":
                        lamp.setType("ДРЛ");
                        lamp.setPower("125Вт");
                        break;
                    case "drl700":
                        lamp.setType("ДРЛ");
                        lamp.setPower("700Вт");
                        break;
                    case "drl1000":
                        lamp.setType("ДРЛ");
                        lamp.setPower("1000Вт");
                        break;
                    case "dnat250":
                        lamp.setType("ДНаТ");
                        lamp.setPower("250Вт");
                        break;
                    case "dnat400":
                        lamp.setType("ДНаТ");
                        lamp.setPower("400Вт");
                        break;
                    case "dnat70":
                        lamp.setType("ДНаТ");
                        lamp.setPower("70Вт");
                        break;
                    case "dnat100":
                        lamp.setType("ДНаТ");
                        lamp.setPower("100Вт");
                        break;
                    case "dnat150":
                        lamp.setType("ДНаТ");
                        lamp.setPower("150Вт");
                        break;
                    case "mgl250":
                        lamp.setType("МГЛ");
                        lamp.setPower("250Вт");
                        lamp.setMontagneType(1);
                        break;
                    case "mgl400":
                        lamp.setType("МГЛ");
                        lamp.setPower("400Вт");
                        lamp.setMontagneType(1);
                        break;
                    case "mgl500":
                        lamp.setType("МГЛ");
                        lamp.setPower("500Вт");
                        lamp.setMontagneType(1);
                        break;
                    case "mgl1000":
                        lamp.setType("МГЛ");
                        lamp.setPower("1000Вт");
                        lamp.setMontagneType(1);
                        break;
                    case "diod50":
                        lamp.setType("Светодиодный");
                        lamp.setPower("50Вт");
                        break;
                    case "diod30":
                        lamp.setType("Светодиодный");
                        lamp.setPower("30Вт");
                        break;
                    case "diod70":
                        lamp.setType("Светодиодный");
                        lamp.setPower("70Вт");
                        break;
                    case "diod100":
                        lamp.setType("Светодиодный");
                        lamp.setPower("100Вт");
                        break;
                    case "shar125":
                        lamp.setType("ДРЛ");
                        lamp.setPower("125Вт");
                        lamp.setComments("Шар E14");
                        lamp.setPositionOutside(2);
                        lamp.setMontagneType(2);
                        lamp.setStolb(true);
                        break;
                    case "shar250":
                        lamp.setType("ДРЛ");
                        lamp.setPower("250Вт");
                        lamp.setComments("Шар");
                        lamp.setPositionOutside(2);
                        lamp.setMontagneType(2);
                        lamp.setStolb(true);
                        break;
                }
            }
            //lamp.setPlaceType(placeType);
            lamp.setGroupIndex(groupIndex);
            if (!escapePowerSet) {      //Если не задан режим игнорирования задания мощности - задаем мощность светильника
                switch (groupIndex) {       //В зависимости от группы светильника
                    case 0:
                        lamp.setPower(Variables.lampVstraivaemieNames[pos]);
                        break;
                    case 1:
                        lamp.setPower(Variables.lampNakladnieNames[pos]);
                        break;
                    case 2:
                        //lamp.setPower(Variables.lampLampsNamesForOutput[pos]);
                        break;
                    case 3:
                        lamp.setPower(Variables.lampDiodsNames[pos]);
                        break;
                    case 4:
                        lamp.setPower(Variables.lampDoskiNames[pos]);
                        break;
                    case 5:
                        lamp.setPower(Variables.lampPodvesNames[pos]);
                        break;
                    case 6:
                        lamp.setPower(Variables.lampOthersNames[pos]);
                        break;
                    case 7:
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
        if (degrees>70 && degrees <110)
            degrees=90;
        else if (degrees>160 && degrees<200)
            degrees=180;
        else if (degrees>-20 && degrees<20)
            degrees=0;
        System.out.println(degrees);
        return degrees;
    }


    @SuppressLint("ClickableViewAccessibility")
        //Отслеживание нажатий на светильники
    void setListener(ImageView imageView){
        imageView.setOnTouchListener(new View.OnTouchListener(){
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.selectByClickFlag && !Variables.moveOnlySelectedZone){
                    if (!Variables.copyVector.contains(touchedLamp = getLampByTouch(imageView))) {
                        Variables.copyVector.add(touchedLamp = getLampByTouch(imageView));
                        imageView.setBackgroundResource(R.color.blue);
                    }
                }else {
                    float x = imageView.getX()+imageView.getWidth();
                    float y = imageView.getY()+imageView.getHeight();
                    if (Variables.removeMode) {     //Если функция удаления светильника
                        setTouchedRoom(x, y, false);   //Первичное нажатие на светильник
                        touchedLamp = getLampByTouch(imageView);
                        if (touchedLamp != null) {
                            Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                                Variables.planLay.removeView(touchedLamp.getImage());
                            });
                            if (!Objects.equals(touchedLamp.getLampRoom(), "-1")) {
                                Room room = Variables.getRoomByNumber(touchedLamp.getLampRoom(), touchedLamp.getImage().getX(), touchedLamp.getImage().getY(), touchedLamp.getImage().getScaleX(), Variables.current_floor);
                                if (room != null && room.lamps.contains(touchedLamp)) {        //Если светильник привязан - удаляем из привязанной комнаты
                                    room.lamps.remove(touchedLamp);
                                } else if (Variables.current_floor.unusedLamps.contains(touchedLamp)) {          //Иначе удаляем из неиспользуемых светильников
                                    Variables.current_floor.unusedLamps.remove(touchedLamp);
                                } else {
                                    removeFromEveryWhere(touchedLamp);
                                }
                            } else {      //Иначе удаляем из неиспользуемых светильников
                                if (Variables.current_floor.unusedLamps.contains(touchedLamp)) {
                                    Variables.current_floor.unusedLamps.remove(touchedLamp);
                                } else {
                                    removeFromEveryWhere(touchedLamp);
                                }
                            }
                        } else if (touchedLamp == null) {
                            Variables.planLay.removeView(imageView);
                            Variables.removeLampByView(imageView);
                        }
                    } else {
                        switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {        //Иначе если не функция удаления
                            case MotionEvent.ACTION_DOWN:       //Получаем нажатый светильнк
                                if (!Variables.moveOnlySelectedZone) {      //Если не перемещение по выделенной зоне - двигаем светильник
                                    sumXY = (imageView.getX() + +imageView.getWidth()) + (imageView.getY() + +imageView.getHeight());
                                    setTouchedRoom(x, y, false);   //Первичное нажатие на светильник
                                    touchedLamp = getLampByTouch(imageView);
                                    setInfoLamp(touchedLamp);
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (Variables.scalemode) {          //Если режим изменения масштаба - меняем масштаб
                                    float currentScale = imageView.getScaleX();
                                    double newSumXY = (imageView.getX() + (event.getX())) + (imageView.getY() + (event.getY()));
                                    double dx = newSumXY - sumXY;
                                    //imageView.setPivotX(imageView.getWidth());
                                    //imageView.setPivotY(imageView.getHeight());
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
                                } else if (Variables.rotateMode && !Variables.addMultiple_flag) {      //Если режим поворота - поворачиваем
                                    isReleased = false;
                                    float angle = getDegreesFromTouchEvent(event, imageView, x, y);
                                    if (!Variables.selectZoneFlag && touchedLamp != null) {
                                        touchedLamp.setRotationAngle(angle);        //Устанавливаем в светильник угол поворота
                                        rotateImg(angle, imageView, touchedLamp.getTypeImage(), -1);        //Выполняем поворот картинки
                                    } else {
                                        for (Lamp lamp : Variables.copyVector) {
                                            lamp.setRotationAngle(angle);
                                            rotateImg(angle, lamp.getImage(), lamp.getTypeImage(), -1);
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
                                    if (imageView.getX() + (event.getX())>=Variables.currentWidth || imageView.getY() + (event.getY())>=Variables.currentHeight){

                                    }else {
                                        imageView.setX((imageView.getX() + (event.getX())) - imageView.getWidth() / 2);
                                        imageView.setY((imageView.getY() + (event.getY())) - imageView.getHeight() / 2);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                isReleased = true;
                                setTouchedRoom(x, y, true);  //Перемещение светильника
                                //lastRoom = touchedRoom;
                                break;
                        }

                        if (Variables.getMoveFlag()) {       //Если обычное перемещение светильника - перепривязка светильников к комнатам
                            if (!Variables.scalemode && !Variables.removeMode && !Variables.moveOnlySelectedZone && !Variables.rotateMode) {
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
                                        touchedLamp.setTypeRoom(touchedRoom.getType_pos());
                                        Variables.typeLamp.setSelection(touchedLamp.getTypeRoom());
                                        touchedLamp.setDaysWork(touchedRoom.getDays());
                                        Variables.daysLamp.setSelection(touchedLamp.getDaysWork());
                                        touchedLamp.setHoursWork(touchedRoom.getHoursPerDay());
                                        Variables.hoursLamp.setSelection(touchedLamp.getHoursWork());
                                        touchedLamp.setHoursWeekendWork(touchedRoom.getHoursPerWeekend());
                                        Variables.hoursPerWeekendLamp.setSelection(touchedLamp.getHoursWeekendWork());
                                        touchedLamp.setHoursSundayWork(touchedRoom.getHoursPerSunday());
                                        Variables.hoursPerSundayLamp.setSelection(touchedLamp.getHoursSundayWork());
                                        Variables.lampRoom.setText(touchedLamp.getLampRoom());
                                        Variables.lampType.setText(touchedLamp.getType());
                                        Variables.lampPower.setText(touchedLamp.getPower());
                                        Variables.montagneType.setSelection(touchedLamp.getMontagneType());
                                        //}
                                    }
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
            if (lamp.getPlaceType()==1){
                Buttons.hideLampDop();
            }else{
                if (Variables.lampDopShown) {
                    Buttons.showLampDop();
                }
            }
            Variables.typeLamp.setSelection(lamp.getTypeRoom());
            Variables.daysLamp.setSelection(lamp.getDaysWork());
            Variables.hoursLamp.setSelection(lamp.getHoursWork());
            Variables.hoursPerWeekendLamp.setSelection(lamp.getHoursWeekendWork());
            Variables.hoursPerSundayLamp.setSelection(lamp.getHoursSundayWork());
            Variables.showLampsAllPhotos(lamp);
            //Если это люстры - отображаем информацию о количестве лампочек
            if (lamp.getTypeImage().equals("lustranakal") || lamp.getTypeImage().equals("lustrakll") || lamp.getTypeImage().equals("lustradiod")){
                Variables.lampAmountText.setVisibility(View.VISIBLE);
                Variables.lampAmountEdit.setVisibility(View.VISIBLE);
                Variables.lampAmountEdit.setText(String.valueOf(lamp.getLampsAmount()));
            }else{
                Variables.lampAmountText.setVisibility(View.GONE);
                Variables.lampAmountEdit.setVisibility(View.GONE);
            }
            if (lamp.getPlaceType()==1){        //Если это наружное освещение
                if (lamp.getGroupIndex()==7) {                      //Если это прожектора
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
            }else{              //Иначе это другие типы светильников
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

    private void clearInfoLamp(){           //Очистка полей информации о светильнике
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
        Variables.typeLamp.setSelection(0);
        Variables.daysLamp.setSelection(0);
        Variables.hoursLamp.setSelection(0);
        Variables.hoursPerWeekendLamp.setSelection(0);
        Variables.hoursPerSundayLamp.setSelection(0);
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

    void removeFromEveryWhere(Lamp lamp_to_del){        //Удаление светильника отовсюду
        for (Room room:Variables.current_floor.rooms){
            for (Lamp lamp: room.lamps){
                if (lamp==lamp_to_del){
                    room.lamps.remove(lamp);
                    return;
                }
            }
        }
        for (int i=0;i<Variables.current_floor.unusedLamps.size();i++){         //Если не нашли - в непривязанных светильниках
            if (Variables.current_floor.unusedLamps.elementAt(i)!=null) {
                if (Variables.current_floor.unusedLamps.elementAt(i)==lamp_to_del) {
                    Variables.current_floor.unusedLamps.remove(lamp_to_del);
                    return;
                }
            }
        }
        return;
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
        for (Room room:Variables.current_floor.rooms){
            for (Lamp lamp: room.lamps){
                if (lamp.getImage()==image){
                    return lamp;
                }
            }
        }
        return null;            //Иначе - светильник не найден
    }

    public void spawnTempLamp(Integer type, int pos,String lampName,int placeType,int groupIndex,float cordX,float cordY,boolean type_spawning,float rotation,float scaleType) {
        if (tempView != null || type_spawning) {                   //Если активная функция добавления светильника
            ImageView imageView = new ImageView(Variables.activity);
            Variables.multipleAddTempVector.add(imageView);
            imageView.setPivotX(0);
            imageView.setPivotY(0);
            //imageView.getBackground().setAlpha(200);
            imageView.setImageResource(type);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Variables.lampSize, Variables.lampSize);
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
                //imageView.setPivotX(0);
                //imageView.setPivotY(0);
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
            if (Variables.rotateMode){
                rotateImg(90, imageView, lampName,-1);
                rotation=90;
            }
            Variables.planLay.addView(imageView);
        }
    }
}