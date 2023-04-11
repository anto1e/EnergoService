package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Property;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.poi.hssf.util.HSSFColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

//Класс, обрабатывающий нажатия кнопок из правого тулбара

public class Buttons {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    static LinearLayout active=null;

    ImageView addBtn;       //Кнопка добавления светильника
    ImageView moveBtn;      //Кнопка передвижения светильников
    ImageButton uploadBtn;      //Кнопка открытия файла
    TextView roomInfo;          //Поле текста информации о комнате
    TextView buildingInfo;      //Поле теста информации о здании
    TextView lampInfo;          //Поле текста информации о светильнике
    ImageButton addPanel;       //Кнопка добавления новой вкладки
    ImageButton removePanel;    //Кнопка удаления вкладки
    ImageView scaleBtn;         //Кнопка изменения масштаба
    ImageButton saveFile;       //Кнопка сохранения файла
    ImageButton rotateLamp;     //Кнопка поворота светильника
    ImageView removeLamp;       //Кнопка удаления светильника
    ImageView addMultipleBtn;   //Кнопка добавления множества светильников
    ImageView addMultipleRows;      //Кнопка добавления светильников по рядам и столбцам
    Button multipleRowsSubmit;      //Кнопка подтверждения добавления светильников по рядам и столбцам
    ImageView copyBtn;          //Кнопка копирования
    ImageView confirmBtn;       //Кнопка подтверждения
    ImageButton cancelBtn;      //Кнопка отмены
    ImageButton selectZone;     //Кнопка выделения зоны
    ImageButton copyToBufBtn;   //Кнопка копирования в буфер
    ImageButton pasteBtn;       //Кнопка вставки
    ImageView takePicBtn;       //Кнопка активации камеры



    @SuppressLint("ClickableViewAccessibility")
    public void addPanel(String txt1){          //Функция добавления новой панели
        LinearLayout lay = new LinearLayout(Variables.activity);    //Задаем стили
        lay.setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, Variables.activity.getResources().getDisplayMetrics()), ViewGroup.LayoutParams.MATCH_PARENT));
        lay.setBackgroundResource(R.drawable.txtviewborder);
        TextView txt = new TextView(Variables.activity);
        txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        txt.setTextSize(15);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(Variables.activity.getResources().getColor(R.color.black));
        lay.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
        txt.setText(txt1);
        lay.addView(txt);
        Variables.floorsPanels.addView(lay);        //Присоединяем панель к layout
        Variables.FloorPanelsVec.add(lay);          //Добавляем панель в вектор с панелями
        if (active==null) {                         //Если панелей не было - создаем и устанавливаем текующую активно
            active = lay;
            active.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        }else{          //Иначе предыдущую делаем неактивной, новую - активной
            active.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
            active=lay;
            active.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        }
        lay.setOnTouchListener(new View.OnTouchListener() {         //Обработчик нажатий на панели
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (active!=v){             //Если нажата неактивная, делаем ее активной, предыдущую неактивной
                    Variables.planLayCleared=true;
                    Variables.clearFields();
                    active.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                    Variables.current_floor.cordX = Variables.planLay.getX();
                    Variables.current_floor.cordY = Variables.planLay.getY();
                    Variables.current_floor.scale = Variables.planLay.getScaleX();
                    v.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    for (int i = Variables.planLay.getChildCount()-1; i >= 0; i--) {
                        View view = Variables.planLay.getChildAt(i);
                        if (view!=Variables.image) {                //Очищаем светильники с экрана
                            Variables.activity.runOnUiThread(() -> {
                                Variables.planLay.removeView(view);
                            });
                        }
                    }
                    active= (LinearLayout) v;
                    Variables.current_floor = Variables.floors.elementAt(Variables.FloorPanelsVec.indexOf(active));
                    Variables.image.setImageURI(Variables.current_floor.getImage());
                    Variables.planLay.setX(Variables.current_floor.cordX);
                    Variables.planLay.setY(Variables.current_floor.cordY);
                    Variables.planLay.setScaleX(Variables.current_floor.scale);
                    Variables.planLay.setScaleY(Variables.current_floor.scale);
                    Variables.filePath = FileHelper.getRealPathFromURI(Variables.activity,Variables.current_floor.getImage());
                    drawLamps();     //Рисуем светильники текущей комнаты
                    Variables.buildingName.setText(Variables.current_floor.getName());
                    Variables.buidlingFloor.setText(Variables.current_floor.getFloor());
                    Variables.buildingAdress.setText(Variables.current_floor.getAdress());
                    Variables.setAddFlag(false);
                    addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    Variables.plan.disableListenerFromPlan();
                    Variables.setMoveFlag(false);
                    moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    Variables.scalemode=false;
                    scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    Variables.rotateMode=false;
                    rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    Variables.removeMode=false;
                    removeLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                    Variables.plan.touchedRoom=null;
                    Variables.plan.lastRoom=null;
                    Variables.selectedfile = Variables.current_floor.getImage();
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void startDetecting(){   //Начало отслеживания нажатия кнопок
        addBtn = Variables.activity.findViewById(R.id.addBtn);        //Нажата кнопка добавления светильника
        addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        moveBtn = Variables.activity.findViewById(R.id.moveBtn);      //Нажата кнопка активации перемещения светильникв
        moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        uploadBtn = Variables.activity.findViewById(R.id.openFile);     //Кнопка загрузки файла
        ImageButton exportExel = Variables.activity.findViewById(R.id.excelExport); //Кнопка экспорта в Эксель
        roomInfo = Variables.activity.findViewById(R.id.roomInfo);         //Панель информации о комнате
        buildingInfo = Variables.activity.findViewById(R.id.buildingInfo); //Панель информации о здании
        lampInfo = Variables.activity.findViewById(R.id.lampInfo);         //Панель информации о светильнике
        addPanel = Variables.activity.findViewById(R.id.addPanelBtn);   //Кнопка добавления вкладки
        removePanel = Variables.activity.findViewById(R.id.closePanelBtn);  //Кнока удаления вкладки
        scaleBtn = Variables.activity.findViewById(R.id.scaleBtn);      //Кнопка масштабирования
        saveFile = Variables.activity.findViewById(R.id.saveFile);      //Кнопка сохранения файла
        rotateLamp = Variables.activity.findViewById(R.id.rotateLamp);  //Кнопка поворота светильника
        removeLamp = Variables.activity.findViewById(R.id.removeLamp);  //Кнопка удаления светильника
        addMultipleBtn = Variables.activity.findViewById(R.id.addMultipleBtn);  //Кнопка добавления множества светильников
        addMultipleRows = Variables.activity.findViewById(R.id.addMultipleRowsBtn);     //Кнопка добавления светильников по рядам и столбцам
        multipleRowsSubmit = Variables.activity.findViewById(R.id.multipleRowsSubmit);      //Кнопка подтверждения добавления светильников по рядам и столбцам
        copyBtn = Variables.activity.findViewById(R.id.copyBtn);        //Кнопка копирования
        confirmBtn = Variables.activity.findViewById(R.id.confirmBtn);  //Кнопка подтверждения
        cancelBtn = Variables.activity.findViewById(R.id.cancelBtn);    //Кнопка отмены
        selectZone = Variables.activity.findViewById(R.id.selectZone);  //Кнопка выбора зоны
        copyToBufBtn = Variables.activity.findViewById(R.id.copyToBuf); //Кнопка копирования в буфер
        pasteBtn = Variables.activity.findViewById(R.id.pasteBtn);      //Кнопка вставки
        takePicBtn = Variables.activity.findViewById(R.id.takePicBtn);  //Кнопка активации камеры

        takePicBtn.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активируем камеру
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        verifyPermissions();

                return false;
            }
        });

        pasteBtn.setOnTouchListener(new View.OnTouchListener() { //При нажатии - вставляем скопированные ранее элементы
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.copyBuffer.size()>0 && Variables.getAddFlag() && Variables.plan.tempView!=null){
                            Variables.moveCopiedBufVector(Variables.plan.tempView.getX(),Variables.plan.tempView.getY());
                            for (Lamp lamp:Variables.copyBuffer){   //Для каждого светильника в буфере
                                lamp.setView();     //Добавляем на экран
                                Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),lamp.getTypeImage());      //Поворачиваем
                            }
                            for (Lamp lamp : Variables.copyBuffer) {        //Для каждого вставленного светильника ищем комнату куда привязать
                                boolean found = false;
                                for (Room room : Variables.current_floor.rooms) {
                                    if (room.detectTouch(lamp.getImage().getX(), lamp.getImage().getY())) {
                                        room.lamps.add(lamp);
                                        lamp.setLampRoom(room.getNumber());
                                        found = true;
                                    }
                                }
                                if (!found) {       //Если не нашли - комната null
                                    Variables.current_floor.unusedLamps.add(lamp);
                                    lamp.getImage().setBackgroundColor(Variables.activity.getResources().getColor(R.color.blue));
                                }
                            }
                            Variables.copyBuffer.clear();       //Очистка буфера
                            copyToBufBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                            pasteBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                        }
                        break;
                }
                return false;
            }
        });

        copyToBufBtn.setOnTouchListener(new View.OnTouchListener() {        //При нажатии - копируем в буфер светильника(Выделенные ранее, либо один нажатый)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.selectZoneFlag && Variables.copyVector.size()>0){ //Если выделена группа светильников
                            Variables.copyBuffer.clear();       //Очистка буффера копирования
                            for (Lamp lamp:Variables.copyVector){       //Переносим выделенные светильники в буфер копирование
                                Lamp tempLamp = new Lamp();
                                tempLamp.setType(lamp.getType());
                                tempLamp.setRotationAngle(lamp.getRotationAngle());
                                tempLamp.setLampRoom("-1");
                                tempLamp.setPower(lamp.getPower());
                                tempLamp.setTypeImage(lamp.getTypeImage());
                                tempLamp.setComments(lamp.getComments());
                                ImageView imageView = new ImageView(Variables.activity);
                                Resources resources = Variables.activity.getResources();
                                final int resourceId = resources.getIdentifier(lamp.getTypeImage(), "drawable",
                                        Variables.activity.getPackageName());
                                imageView.setImageResource(resourceId);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                imageView.setLayoutParams(params);
                                imageView.setScaleX(Variables.lastScaletype);
                                imageView.setScaleY(Variables.lastScaletype);
                                Variables.plan.setListener(imageView);
                                imageView.setX(lamp.getImage().getX());
                                imageView.setY(lamp.getImage().getY());
                                tempLamp.setImage(imageView);
                                Variables.copyBuffer.add(tempLamp);
                            }   //Находим наиболее левый верхний светильник//
                            float minCordX=Float.MAX_VALUE;
                            for (Lamp lamp:Variables.copyBuffer){
                                if (lamp.getImage().getX()<minCordX){
                                    minCordX=lamp.getImage().getX();
                                    Variables.tempCopiedBufLamp=lamp;
                                }
                            }
                            for (Lamp lamp:Variables.copyBuffer){
                                if (Variables.tempCopiedBufLamp!=null && lamp.getImage().getX()==Variables.tempCopiedBufLamp.getImage().getX()){
                                    if (lamp.getImage().getY()<Variables.tempCopiedBufLamp.getImage().getY()){
                                        Variables.tempCopiedBufLamp=lamp;
                                    }
                                }
                            }
                                //Находим расстояния до других светильников от этого светильника
                            for (int i=0;i<Variables.copyBuffer.size();i++){
                                //if (Variables.copyBuffer.elementAt(i)!=Variables.tempCopiedBufLamp){
                                    float temp1 = Variables.copyBuffer.elementAt(i).getImage().getX()-Variables.tempCopiedBufLamp.getImage().getX();
                                    float temp2 = Variables.copyBuffer.elementAt(i).getImage().getY()-Variables.tempCopiedBufLamp.getImage().getY();
                                    Variables.distBufX.add(temp1);
                                    Variables.distBufY.add(temp2);
                                //}
                            }
                        }else       //Иначе - если зона не выделена
                            if(Variables.plan.touchedLamp!=null){       //Если есть нажатая лампа - копируем ее в буфер
                            Variables.copyBuffer.clear();
                                Lamp tempLamp = new Lamp();
                                tempLamp.setType(Variables.plan.touchedLamp.getType());
                                tempLamp.setRotationAngle(Variables.plan.touchedLamp.getRotationAngle());
                                tempLamp.setLampRoom("-1");
                                tempLamp.setPower(Variables.plan.touchedLamp.getPower());
                                tempLamp.setTypeImage(Variables.plan.touchedLamp.getTypeImage());
                                tempLamp.setComments(Variables.plan.touchedLamp.getComments());
                                ImageView imageView = new ImageView(Variables.activity);
                                Resources resources = Variables.activity.getResources();
                                final int resourceId = resources.getIdentifier(Variables.plan.touchedLamp.getTypeImage(), "drawable",
                                        Variables.activity.getPackageName());
                                imageView.setImageResource(resourceId);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                imageView.setLayoutParams(params);
                                imageView.setScaleX(Variables.lastScaletype);
                                imageView.setScaleY(Variables.lastScaletype);
                                Variables.plan.setListener(imageView);
                                imageView.setX(Variables.plan.touchedLamp.getImage().getX());
                                imageView.setY(Variables.plan.touchedLamp.getImage().getY());
                                tempLamp.setImage(imageView);
                                Variables.copyBuffer.add(tempLamp);
                                Variables.tempCopiedBufLamp = tempLamp;
                        }
                        if (Variables.copyBuffer.size()==0){    //Если размер буфера 0 - меняем цвета на серый
                            copyToBufBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                            pasteBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                        }else {        //Иначе меняем цвет кнопки на белый
                            copyToBufBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                        }
                        break;
                }
                return false;
            }
        });

        selectZone.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активируется выделение зоны
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (!Variables.selectZoneFlag) {        //Активация - установка флага, активация подтверждающих кнопок, очистка полей информации о светильнике
                            Variables.selectZoneFlag=true;
                            selectZone.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            Variables.plan.setListenerToPlan();
                            activateConfirmBtn();
                            Variables.lampRoom.setText("");
                            Variables.lampType.setText("");
                            Variables.lampPower.setText("");
                            Variables.lampComments.setText("");
                        }else{      //Деактивация
                            if (!Variables.moveOnlySelectedZone) {  //Выключение функцию выделения и подтверждающей кнопки
                                disableConfirmBtn();
                                disableSelectZone();
                            }else{ //Иначе - выключаем функцию веделения
                                Variables.selectZoneFlag=false;
                                selectZone.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                                Variables.plan.disableListenerFromPlan();
                            }
                        }
                        break;
                }
                return false;
            }
        });

        cancelBtn.setOnTouchListener(new View.OnTouchListener() {       //При нажатии - выключается какое-либо действие
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.moveOnlySelectedZone){    //Если выбран флаг перемещения выбранной области - сбрасываем позицию
                            Variables.resetCordsCopiedVector();
                            Variables.tempCopiedLamp.getImage().setBackgroundResource(0);
                            Variables.moveOnlySelectedZone=false;
                            Variables.copyVector.clear();
                            Variables.tempCopiedLamp=null;
                            if (Variables.getMoveFlag())
                                moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            else
                                moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                        }else
                        if (Variables.copyFlag){        //Иначе если включена функция копирования - выключаем копирование
                            disableConfirmBtn();
                            disableCancelBtn();
                            disableCopyBtn();
                            Variables.copyType=0;
                        }
                        break;
                }
                return false;
            }
        });

        confirmBtn.setOnTouchListener(new View.OnTouchListener() {      //При нажатии срабатывае подтверждения при определенных режимах
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (Variables.moveOnlySelectedZone){        //Если выбрано перемещение выбранной зоны - сохраняем позицию
                            for (Room room:Variables.current_floor.rooms){
                                for (Lamp lamp: Variables.copyVector){
                                    if (room.lamps.contains(lamp)){
                                        room.lamps.remove(lamp);
                                    }
                                }
                            }       //Ищем комнату куда добавить каждый светильник, если не нашли - добавляем в неиспользуемые
                            for (Lamp lamp : Variables.copyVector) {
                                boolean found = false;
                                for (Room room : Variables.current_floor.rooms) {
                                    if (room.detectTouch(lamp.getImage().getX(), lamp.getImage().getY())) {
                                        room.lamps.add(lamp);
                                        lamp.setLampRoom(room.getNumber());
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    Variables.current_floor.unusedLamps.add(lamp);
                                    lamp.getImage().setBackgroundColor(Variables.activity.getResources().getColor(R.color.blue));
                                }
                            }
                            Variables.moveOnlySelectedZone=false;
                            Variables.copyVector.clear();
                            Variables.tempCopiedLamp.getImage().setBackgroundResource(0);
                            Variables.tempCopiedLamp=null;
                            if (Variables.getMoveFlag())
                            moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            else
                                moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                            disableConfirmBtn();
                            disableCancelBtn();
                        }
                        else if (Variables.selectZoneFlag){     //Если это флаг выбора зоны - сохраняем групповой комментарий к светильникам
                            String txt = Variables.lampComments.getText().toString();
                            for (Lamp lamp:Variables.copyVector){
                                String old_comments = lamp.getComments();
                                if (old_comments!=null && old_comments.length()>0) {
                                    lamp.setComments(old_comments + " "+txt);
                                }else{
                                    lamp.setComments(txt);
                                }
                            }
                        }
                        else if (Variables.copyFlag){       //Если это функция копирования по выбору зоны - сохраняем светильники в вектор, а затем вставляем скопированные светильники
                            if (Variables.copyType==0 && Variables.copyVector.size()>0){        //Сохранение светильников в памяти
                                Vector<Lamp> temp = new Vector(Variables.copyVector);
                                for (Lamp lamp1:Variables.copyVector){
                                    lamp1.getImage().setBackgroundResource(0);
                                }
                                Variables.copyVector.clear();

                                for (Lamp lamp:temp){
                                    Lamp tempLamp = new Lamp();
                                    tempLamp.setType(lamp.getType());
                                    tempLamp.setRotationAngle(lamp.getRotationAngle());
                                    tempLamp.setLampRoom("-1");
                                    tempLamp.setPower(lamp.getPower());
                                    tempLamp.setTypeImage(lamp.getTypeImage());
                                    tempLamp.setComments(lamp.getComments());
                                    ImageView imageView = new ImageView(Variables.activity);
                                    Resources resources = Variables.activity.getResources();
                                    final int resourceId = resources.getIdentifier(lamp.getTypeImage(), "drawable",
                                            Variables.activity.getPackageName());
                                    imageView.setImageResource(resourceId);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                    imageView.setLayoutParams(params);
                                    imageView.setScaleX(lamp.getImage().getScaleX());
                                    imageView.setScaleY(lamp.getImage().getScaleY());
                                    Variables.plan.setListener(imageView);
                                    imageView.setX(lamp.getImage().getX());
                                    imageView.setY(lamp.getImage().getY());
                                    tempLamp.setImage(imageView);
                                    tempLamp.setView();
                                    Variables.plan.rotateImg(tempLamp.getRotationAngle(),tempLamp.getImage(),tempLamp.getTypeImage());
                                    Variables.copyVector.add(tempLamp);
                                }
                                //Ищем крайний левый верхний светильник
                                float minCordX=Float.MAX_VALUE;
                                for (Lamp lamp:Variables.copyVector){
                                    if (lamp.getImage().getX()<minCordX){
                                        minCordX=lamp.getImage().getX();
                                        Variables.tempCopiedLamp=lamp;
                                    }
                                }
                                for (Lamp lamp:Variables.copyVector){
                                    if (Variables.tempCopiedLamp!=null && lamp.getImage().getX()==Variables.tempCopiedLamp.getImage().getX()){
                                        if (lamp.getImage().getY()<Variables.tempCopiedLamp.getImage().getY()){
                                            Variables.tempCopiedLamp=lamp;
                                        }
                                    }
                                }
                                //Рассчитываем расстояния остальных светильников
                                for (int i=0;i<Variables.copyVector.size();i++){
                                    //if (Variables.copyVector.elementAt(i)!=Variables.tempCopiedLamp){
                                        float temp1 = Variables.copyVector.elementAt(i).getImage().getX()-Variables.tempCopiedLamp.getImage().getX();
                                        float temp2 = Variables.copyVector.elementAt(i).getImage().getY()-Variables.tempCopiedLamp.getImage().getY();
                                        Variables.distX.add(temp1);
                                        Variables.distY.add(temp2);
                                    //}
                                }

                                Variables.moveCopiedVector(-1,-1);
                                Variables.copyType=1;
                            }else if (Variables.copyType==1){   //Если выбрано подтверждение - вставляем светильники в выбранную позицию
                                if (Variables.tempCopiedLamp.getImage().getX()==-1 || Variables.tempCopiedLamp.getImage().getY()==-1){
                                    Variables.copyType=0;
                                    disableConfirmBtn();
                                    disableCancelBtn();
                                    disableCopyBtn();
                                }else {     //Ищем комнату к которой привязать светильник, если не нашли - добавляем в неиспользуемые
                                    for (Lamp lamp : Variables.copyVector) {
                                        boolean found = false;
                                        for (Room room : Variables.current_floor.rooms) {
                                            if (room.detectTouch(lamp.getImage().getX(), lamp.getImage().getY())) {
                                                room.lamps.add(lamp);
                                                lamp.setLampRoom(room.getNumber());
                                                found = true;
                                            }
                                        }
                                        if (!found) {
                                            Variables.current_floor.unusedLamps.add(lamp);
                                            lamp.getImage().setBackgroundColor(Variables.activity.getResources().getColor(R.color.blue));
                                        }
                                    }
                                    Variables.copyType = 0;
                                    disableCopyBtn();
                                    disableConfirmBtn();
                                    disableCancelBtn();
                                }
                            }
                        }
                return false;
            }
        });

        copyBtn.setOnTouchListener(new View.OnTouchListener() {     //При нажатии - активация/деактивация копирования выбором зоны
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (!Variables.copyFlag) {      //Активация
                            Variables.copyFlag=true;
                            Variables.copyType = 0;
                            copyBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            activateConfirmBtn();
                            activateCancelBtn();
                            Variables.plan.setListenerToPlan();
                        }else{          //Деактивация
                            disableConfirmBtn();
                            disableCancelBtn();
                            disableCopyBtn();
                            Variables.copyType=0;
                        }
                return false;
            }
        });


        multipleRowsSubmit.setOnTouchListener(new View.OnTouchListener() {          //Кнопка создания светильников по рядам/линиям
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (Variables.addMultipleRowsFlag) {        //Активация функции добавления множества светильников по рядам и столбцам
                            float scaleType = Variables.lastScaletype;  //Получаем коэффициент масштаба
                            EditText column = Variables.activity.findViewById(R.id.columnAmount);   //Получяем колонны
                            EditText rows = Variables.activity.findViewById(R.id.rowsAmount);       //Получаем ряда
                            int column_amount;
                            int rows_amount;
                            //Переводим в int числа
                            if (rows.getText().length()==0 || column.getText().length()==0){
                                column_amount= Integer.parseInt(Variables.spinRows.getSelectedItem().toString());
                                rows_amount = Integer.parseInt(Variables.spinLines.getSelectedItem().toString());
                            }else{
                                column_amount = Integer.parseInt(String.valueOf(column.getText()));
                                rows_amount = Integer.parseInt(String.valueOf(rows.getText()));
                            }
                            CheckBox check = Variables.activity.findViewById(R.id.angleCheckbox);
                            float cordX = Variables.plan.selectionZone.getX();
                            float cordY = Variables.plan.selectionZone.getY();
                            float height = (Variables.plan.selectionZone.getHeight())-(15*scaleType);       //Высчитываем высоту зоны
                            float width = Variables.plan.selectionZone.getWidth()-(15*scaleType);           //Высчитываем ширину зону
                            float height_step = (height / (rows_amount-1));         //Расчитваем шаг по У
                            float width_step = (width / (column_amount-1));         //Расчитываем шаг по Х
                            float angle = 0;        //Угол поворота
                            while ((cordX+15*scaleType)+2 > cordX+width_step){      //Если все светильники не умещаются - изменяем их масштаб
                                scaleType-=0.1f;
                            }
                            while ((cordY+15*scaleType)+2 > cordY+height_step){
                                scaleType-=0.1f;
                            }
                            if (check.isChecked())      //Если активирована функция поворота - поворачиваем на 90 градусов
                                angle = 90;
                            if (column_amount > 0 && column != null && rows_amount > 0 && rows != null && Variables.multipleType != -1) {   //Создаем светильники
                                for (int i = 0; i < rows_amount; i++) {
                                    for (int j = 0; j < column_amount; j++) {
                                        Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.multiplelampType, Variables.plan.lampsName[Variables.multiplepos],cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                    }
                                }
                            }
                            disableMultipleRowsAddBtn();
                            column.setText("");
                            check.setChecked(false);
                            rows.setText("");
                        }
                return false;
            }
        });





        addMultipleRows.setOnTouchListener(new View.OnTouchListener() {     //При нажатии - вызываем функциб добавления множества светильников по рядам и столбцам
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (!Variables.addMultipleRowsFlag) {       //Активация
                            Variables.multipleRowsInfo.setVisibility(View.VISIBLE);     //Показываем скрытый Layout
                            Variables.addMultipleRowsFlag = true;
                            disableAddBtn();
                            disableMultipleAddBtn();
                            Variables.plan.setListenerToPlan();
                            Variables.disableMovingPlan=true;
                            addMultipleRows.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                        } else {            //Деактивация
                            disableMultipleRowsAddBtn();
                        }
                        break;
                }
                return false;
            }
        });

        addMultipleBtn.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активация добавления множества светильников
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (!Variables.addMultiple_flag){       //Активация
                            Variables.addMultiple_flag=true;
                            disableAddBtn();
                            disableMultipleRowsAddBtn();
                            Variables.plan.setListenerToPlan();
                            addMultipleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                        }else {         //Деактивация
                            disableMultipleAddBtn();
                        }
                return false;
            }
        });

        removeLamp.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активация удаления светильников
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (!Variables.removeMode){     //Активация
                            Variables.removeMode=true;
                            removeLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            disableScaleBtn();
                            disableRotateBtn();
                        }else{          //Деактивация
                            disableRemoveBtn();
                        }
                return false;
            }
        });


        saveFile.setOnTouchListener(new View.OnTouchListener() {         //При нажатии - активация функции сохранения в файл
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.fileSaved) {      //Если файл не сохраняется  в данный момент
                            Variables.fileSaved = false;
                            SaveFileThread thread = new SaveFileThread(); //Создаем новый поток для сохранения файла
                            thread.start();     //Запускаем поток
                        }
                        break;
                }
                return false;
            }
        });

        scaleBtn.setOnTouchListener(new View.OnTouchListener() {            //При нажатии - активация/деактивация изменения размера светильника
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (!Variables.scalemode) {     //Активация
                            Variables.scalemode = true;
                            scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            disableRemoveBtn();
                            disableRotateBtn();
                        }else{      //Деактивация
                            disableScaleBtn();
                        }
                return false;
            }
        });

        rotateLamp.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активация/деактивация функции вращения светильника
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (!Variables.rotateMode){         //Активация
                            Variables.rotateMode=true;
                            rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            disableScaleBtn();
                            disableRemoveBtn();
                        }else{          //Деактивация
                            disableRotateBtn();
                        }
                        break;
                }
                return false;
            }
        });


        removePanel.setOnTouchListener(new View.OnTouchListener() {     //Обработчик нажатий на кнопку удаления вкладки
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.floors.size()>0) {
                            int index = Variables.FloorPanelsVec.indexOf(active);
                            LinearLayout lay = Variables.FloorPanelsVec.get(index);
                            if (Variables.FloorPanelsVec.size() > 1) {        //Если на экране больше одной вкладки
                                if (index > 0) {      //Если она не первая - ставим активной вкладку слева
                                    active = Variables.FloorPanelsVec.get(index - 1);
                                } else {      //Иначе вкладку справа
                                    active = Variables.FloorPanelsVec.get(index + 1);
                                }
                                active.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                            } else {     //Иначе, если вкладка была последняя - активных вкладок нет
                                active = null;
                            }
                            for (int i = Variables.planLay.getChildCount() - 1; i >= 0; i--) {
                                View view = Variables.planLay.getChildAt(i);
                                if (view != Variables.image) {                //Очищаем светильники с экрана
                                    Variables.activity.runOnUiThread(() -> {
                                        Variables.planLay.removeView(view);
                                    });
                                }
                            }
                            Variables.activity.runOnUiThread(() -> {
                                Variables.floorsPanels.removeView(lay); //Удаление вкладки из интерфейса
                            });
                            Variables.FloorPanelsVec.remove(lay);   //Удаление вкладки из вектора вкладок
                            Variables.floors.remove(Variables.current_floor);
                            if (active != null) {       //Если в текущий момент не открыта как минимум одна вкладка
                                Variables.current_floor = Variables.floors.elementAt(Variables.FloorPanelsVec.indexOf(active));
                                Variables.image.setImageURI(Variables.current_floor.getImage());
                                Variables.planLay.setX(Variables.current_floor.cordX);
                                Variables.planLay.setY(Variables.current_floor.cordY);
                                Variables.planLay.setScaleX(Variables.current_floor.scale);
                                Variables.planLay.setScaleY(Variables.current_floor.scale);
                                drawLamps();     //Рисуем светильники текущей комнаты
                                Variables.buildingName.setText(Variables.current_floor.getName());
                                Variables.buidlingFloor.setText(Variables.current_floor.getFloor());
                                Variables.buildingAdress.setText(Variables.current_floor.getAdress());
                            } else {            //Иначе удаляем все вкладки
                                Variables.floorsPanels.removeAllViews();
                                Variables.planLayCleared=true;
                                Variables.image.setImageResource(0);
                                Variables.current_floor=null;
                            }
                        }
                        break;
                }
                return false;
            }
        });


        addPanel.setOnTouchListener(new View.OnTouchListener() {            //Обработчик нажатий на кнопку добавления вкладок
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        Variables.planLayCleared=false;
                        if (Variables.FloorPanelsVec.size()==0 || active==null){
                            Variables.typeOpening=0;
                        }else {
                            Variables.typeOpening = 1;
                        }
                        if (!Variables.opened) {            //Открываем меню выбора файла
                            Variables.image.setImageResource(0);
                            Variables.filePath = "";
                            Variables.opened = true;
                            Intent intent = new Intent()
                                    .setType("*/*")
                                    .setAction(Intent.ACTION_GET_CONTENT);
                            Variables.activity.startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                        }
                        break;
                }
                return false;
            }
        });

        exportExel.setOnTouchListener(new View.OnTouchListener() {          //Обработчик нажатий на кнопку экспорта в Эксель
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.isExpotedExcel) {
                    switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            if (Variables.isExpotedExcel) {
                                Variables.isExpotedExcel = false;
                                SaveExcelThread thread = new SaveExcelThread(); //Создаем новый поток для сохранения в Эксель
                                thread.start();     //Запускаем поток
                            }
                            break;
                    }
                }
                return false;
            }
        });

        roomInfo.setOnTouchListener(new View.OnTouchListener() {  //Обработчик нажатий на панель раскрытия информации о комнате

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.roomInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.roomInfoView,1);
                        } else {
                            Variables.roomInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.roomInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.roomInfoView,height);
                        }
                        break;
                }
                return true;
            }
        });

        lampInfo.setOnTouchListener(new View.OnTouchListener() {    //Обработчик нажатий на панель раскрытия информации о светильнике
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.lampInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.lampInfoView,1);
                        } else {
                            Variables.lampInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.lampInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.lampInfoView,height);
                        }
                        break;
                }
                return true;
            }
        });

        buildingInfo.setOnTouchListener(new View.OnTouchListener() {    //Обработчик нажатий на панель раскрытия информации о здании
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (Variables.buildingInfoView.getHeight() > 1) {
                            animateHeightTo(Variables.buildingInfoView,1);
                        } else {
                            Variables.buildingInfoView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int height = Variables.buildingInfoView.getMeasuredHeight();
                            animateHeightTo(Variables.buildingInfoView,height);
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
                       addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                       if (Variables.copyBuffer.size()>0) {
                           pasteBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                       }else{
                           pasteBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                       }
                       disableMultipleAddBtn();
                       disableMultipleRowsAddBtn();
                       Variables.plan.setListenerToPlan();
                   }else{                                                       //Деактивация добавления
                       disableAddBtn();
                   }
                   return false;
            }
        });

        moveBtn.setOnTouchListener(new View.OnTouchListener()                       //Слушатель нажатий на кнопку активации перемещения
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (!Variables.selectZoneFlag) {
                    Variables.invertMoveFlag();
                    if (Variables.getMoveFlag()) {                                       //Активация перемещения
                        moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                        Variables.setMoveFlag(true);
                    } else {                                                              //Деактивация перемещения
                        disableMoveBtn();
                    }
                }else{          //Если двигаем выбранную зону
                    activateCancelBtn();
                    moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.blue));
                    Variables.moveOnlySelectedZone=true;
                    //Высчитываем крайний левый верхний светильник
                    float minCordX=Float.MAX_VALUE;
                    for (Lamp lamp:Variables.copyVector){
                            lamp.getImage().setBackgroundResource(0);
                            Variables.lastMovePosX.add(lamp.getImage().getX());
                            Variables.lastMovePosY.add(lamp.getImage().getY());
                    }
                    for (Lamp lamp:Variables.copyVector){
                        if (lamp.getImage().getX()<minCordX){
                            minCordX=lamp.getImage().getX();
                            Variables.tempCopiedLamp=lamp;
                        }
                    }
                    for (Lamp lamp:Variables.copyVector){
                        if (Variables.tempCopiedLamp!=null && lamp.getImage().getX()==Variables.tempCopiedLamp.getImage().getX()){
                            if (lamp.getImage().getY()<Variables.tempCopiedLamp.getImage().getY()){
                                Variables.tempCopiedLamp=lamp;
                            }
                        }
                    }
                    Variables.tempCopiedLamp.getImage().setBackgroundColor(Variables.activity.getResources().getColor(R.color.blue));
                    //Рассчитываем расстояния светильников от крайнего левого верхнего светильника
                    for (int i=0;i<Variables.copyVector.size();i++){
                        //if (Variables.copyVector.elementAt(i)!=Variables.tempCopiedLamp){
                            float temp1 = Variables.copyVector.elementAt(i).getImage().getX()-Variables.tempCopiedLamp.getImage().getX();
                            float temp2 = Variables.copyVector.elementAt(i).getImage().getY()-Variables.tempCopiedLamp.getImage().getY();
                            Variables.distX.add(temp1);
                            Variables.distY.add(temp2);
                       // }
                    }
                }
                return false;
            }
        });

        uploadBtn.setOnTouchListener(new View.OnTouchListener() {   //Кнопка загрузки файла с планом
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (!Variables.opened) {
                            Variables.planLayCleared=false;
                            Variables.typeOpening=0;
                            Variables.image.setImageResource(0);
                            Variables.filePath = "";
                            Variables.opened = true;
                            Intent intent = new Intent()
                                    .setType("*/*")
                                    .setAction(Intent.ACTION_GET_CONTENT);
                            Variables.activity.startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                        }
                        break;
                }
                return false;
            }
        });

        //Слушатели изменения текста в полях
        Variables.buildingName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null) {
                    Variables.current_floor.setName(String.valueOf(Variables.buildingName.getText()));
                }
            }
        });
        Variables.buidlingFloor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null) {
                    Variables.current_floor.setFloor(String.valueOf(Variables.buidlingFloor.getText()));
                }
            }
        });
        Variables.buildingAdress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null) {
                    Variables.current_floor.setAdress(String.valueOf(Variables.buildingAdress.getText()));
                }
            }
        });
        Variables.roomNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null && Variables.plan.touchedRoom != null) {
                    Variables.plan.touchedRoom.setNumber(String.valueOf(Variables.roomNumber.getText()));
                }
            }
        });
        Variables.roomHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null && Variables.plan.touchedRoom != null) {
                    if (Variables.roomHeight.getText().length()>0)
                    Variables.plan.touchedRoom.setHeight(String.valueOf(Variables.roomHeight.getText()));
                    else
                        Variables.plan.touchedRoom.setHeight("0.0");
                }
            }
        });
        Variables.roomComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan != null && Variables.plan.touchedRoom != null) {
                    Variables.plan.touchedRoom.setComments(String.valueOf(Variables.roomComments.getText()));
                }
            }
        });
        Variables.type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setType_pos(Variables.type.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Variables.daysPerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setDays(Variables.daysPerWeek.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Variables.hoursPerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setHoursPerDay(Variables.hoursPerDay.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Variables.hoursPerWeekend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setHoursPerWeekend(Variables.hoursPerWeekend.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Variables.roofType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setRoofType(Variables.roofType.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.lampRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Variables.selectZoneFlag) {
                    //double lastNumber=-1;
                    if (Variables.plan.touchedLamp != null) {
                        //lastNumber=Variables.plan.touchedLamp.getLampRoom();
                        if (Variables.lampRoom.getText().length() > 0) {
                            try {
                                Variables.plan.touchedLamp.setLampRoom(String.valueOf(Variables.lampRoom.getText()));
                            } catch (NumberFormatException e) {

                            }
                        }

                        if (!Objects.equals(Variables.plan.touchedLamp.getLampRoom(), "-1")) {
                                /*Room room = Variables.getRoomByNumber((float) Double.parseDouble(String.valueOf(Variables.lampRoom.getText())));
                                if (room!=null) {
                                    Variables.current_floor.unusedLamps.remove(Variables.plan.touchedLamp);
                                    room.lamps.add(Variables.plan.touchedLamp);
                                }*/
                            Variables.plan.touchedLamp.getImage().setBackgroundResource(0);
                        } else {
                            Variables.plan.touchedLamp.getImage().setBackgroundResource(R.color.blue);
                        }
                    }
                }
            }
        });
        Variables.lampType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Variables.selectZoneFlag) {
                    if (Variables.plan.touchedLamp != null) {
                        Variables.plan.touchedLamp.setType(String.valueOf(Variables.lampType.getText()));
                    }
                }
            }
        });
        Variables.lampPower.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Variables.selectZoneFlag) {
                    if (Variables.plan.touchedLamp != null) {
                        Variables.plan.touchedLamp.setPower(String.valueOf(Variables.lampPower.getText()));
                    }
                }
            }
        });
        Variables.lampComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Variables.selectZoneFlag) {
                    if (Variables.plan.touchedLamp != null) {
                        Variables.plan.touchedLamp.setComments(String.valueOf(Variables.lampComments.getText()));
                    }
                }
            }
        });
    }
    //Конец слушателей изменения текста в полях

    private void animateHeightTo(@NonNull View view, int height) {      //Функция анимирования изменения высота элемента
        final int currentHeight = view.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofInt(view, new HeightProperty(), currentHeight, height);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }


    static class HeightProperty extends Property<View, Integer> {   //Класс для анимации изменения размера элемента

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

    //Деактивации функций

    public void drawLamps(){            //Функция отрисовки ламп на экране
        for (int i=0;i<Variables.current_floor.rooms.size();i++){
            for (int j=0;j<Variables.current_floor.rooms.elementAt(i).lamps.size();j++){
                ImageView img = Variables.current_floor.rooms.elementAt(i).lamps.elementAt(j).getImage();
                Variables.activity.runOnUiThread(() -> {        //Отрисовка ламп
                    Variables.planLay.addView(img);
                });
                }
        }
        for (int i=0;i<Variables.current_floor.unusedLamps.size();i++){
            ImageView img = Variables.current_floor.unusedLamps.elementAt(i).getImage();
            Variables.activity.runOnUiThread(() -> {        //Отрисовка ламп
                Variables.planLay.addView(img);
            });
        }
    }
    private void disableAddBtn(){
        addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.setAddFlag(false);
        Variables.plan.disableListenerFromPlan();
    }
    private void disableMultipleAddBtn(){
        Variables.addMultiple_flag = false;
        Variables.plan.disableListenerFromPlan();
        addMultipleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.resetListColor();
        Variables.multipleType=-1;
        Variables.multiplepos=-1;
        Variables.multiplelampType=-1;
    }
    private void disableMultipleRowsAddBtn(){
        Variables.addMultipleRowsFlag = false;
        addMultipleRows.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.plan.disableListenerFromPlan();
        Variables.disableMovingPlan=false;
        if (Variables.plan.selectionZone!=null) {
            Variables.planLay.removeView(Variables.plan.selectionZone);
            Variables.plan.selectionZone = null;
        }
        Variables.resetListColor();
        Variables.firstTouch=false;
        Variables.multiplepos=-1;
        Variables.multiplelampType=-1;
        Variables.multipleType=-1;
        Variables.multipleRowsInfo.setVisibility(View.GONE);
    }
    private void disableScaleBtn(){
        Variables.scalemode=false;
        scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
    }
    private void disableRotateBtn(){
        Variables.rotateMode=false;
        rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
    }
    private void disableRemoveBtn(){
        Variables.removeMode=false;
        removeLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
    }
    private void disableMoveBtn(){
        moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.setMoveFlag(false);
    }
    private void activateConfirmBtn(){
        Variables.confirmBtnActive=true;
        confirmBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.green));
    }
    private void activateCancelBtn(){
        Variables.cancelBtnActive=true;
        cancelBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
    }
    private void disableConfirmBtn(){
        Variables.confirmBtnActive=false;
        confirmBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
    }
    private void disableCancelBtn(){
        Variables.cancelBtnActive=false;
        cancelBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
    }
    private void disableCopyBtn(){
        Variables.copyFlag=false;
        copyBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.copyVector.clear();
        if (Variables.copyType==1) {
            for (Lamp lamp : Variables.copyVector) {
                if (lamp.getImage() != null)
                    Variables.planLay.removeView(lamp.getImage());
            }
        }
        Variables.plan.disableListenerFromPlan();
        Variables.distX.clear();
        Variables.distY.clear();
    }
    private void disableSelectZone(){
        Variables.selectZoneFlag=false;
        selectZone.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        for (Lamp lamp : Variables.copyVector) {
                lamp.getImage().setBackgroundResource(0);
        }
        Variables.copyVector.clear();
        Variables.plan.disableListenerFromPlan();
    }

    private void verifyPermissions(){
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }else{
            ActivityCompat.requestPermissions(Variables.activity,
                    permissions,
                    CAMERA_PERM_CODE);
        }
    }
    public static void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Variables.activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Variables.activity,
                        "com.example.myapplication",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Variables.activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="temp";
        if (Variables.plan.touchedRoom!=null) {
            imageFileName = Variables.current_floor.getFloor()+";Помещение:"+Variables.plan.touchedRoom.getNumber()+";";
        }
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        //String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        File directory = new File(path+"/"+Variables.current_floor.getName());
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        File storageDir = Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+"/"+Variables.current_floor.getName());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        Variables.plan.touchedRoom.photoPaths.add(image.getAbsolutePath());
        return image;
    }

    public static void createNewPhotoRoom(File f){
        ImageView view = new ImageView(Variables.activity);
        view.setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics())));
        //lay.setBackgroundResource(R.drawable.txtviewborder);
        view.setImageURI(Uri.fromFile(f));
        Variables.roomGrid.addView(view);
    }
    private static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    //Конец деактивации функций
}
