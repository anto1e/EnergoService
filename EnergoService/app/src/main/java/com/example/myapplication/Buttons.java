package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

//Класс, обрабатывающий нажатия кнопок из правого тулбара

public class Buttons {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    static LinearLayout active=null;        //Активная панель этажа

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
    ImageView photoClose;       //Кнопка закрытия просмотра фотографий
    ImageView photoDelete;      //Кнопка удаления фотографии
    ImageView photoRightArrow;  //Кнопка перехода к следующей фотографии
    ImageView photoLeftArrow;   //Кнопка перехода к предыдущей фотографии
    ImageView takePicLampPhoto;     //Кнопка активации камеры(вкладка светильников)
    ImageView screenShotBtn;        //Кнопка сохранения скриншота экрана
    Button submitHeightFloor;       //Кнопка подтверждения задания стандартной высоты потолка
    int lastIndex=-1;               //Последний индекс типа потолка(для автовысоты)
    int lastWorkdays=6;             //Последний тип режима работы
    int lastType=0;
    int lastDays=0;
    int lastHours=0;
    int lastHoursWeekend=0;
    int lastHoursSunday=0;



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
                    Variables.roofTypeDefaultText.setText("");
                    Variables.roomHeightDefaultCheck.setChecked(false);
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
        photoClose = Variables.activity.findViewById(R.id.photoClose);       //Кнопка закрытия просмотра фотографий
        photoDelete = Variables.activity.findViewById(R.id.photoDelete);      //Кнопка удаления фотографии
        photoRightArrow = Variables.activity.findViewById(R.id.photoArrowRight);  //Кнопка перехода к следующей фотографии
        photoLeftArrow = Variables.activity.findViewById(R.id.photoArrowLeft);   //Кнопка перехода к предыдущей фотографии
        takePicLampPhoto = Variables.activity.findViewById(R.id.takePicLampBtn);    //Кнопка активации камеры(Вкладка со светильниками)
        submitHeightFloor = Variables.activity.findViewById(R.id.submitHeightFloor);    //Кнопка подтверждения задания стандартной высоты потолка
        screenShotBtn = Variables.activity.findViewById(R.id.screenShotBtn);    //Кнопка сохранения скриншота экрана

        screenShotBtn.setOnTouchListener(new View.OnTouchListener() {       //Активация кнопки создания скриншота
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SaveFileThread thread = new SaveFileThread();
                thread.start();
                SavePlanToJpgThread thread1 = new SavePlanToJpgThread(); //Создаем новый поток для экспорта плана в JPG
                thread1.start();     //Запускаем поток
                return false;
            }
        });

        submitHeightFloor.setOnTouchListener(new View.OnTouchListener() {       //Активация подтверждения задания стандартной высоты потолка
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!String.valueOf(Variables.roofTypeDefaultText.getText()).equals("0.0")) {
                    int index = Variables.roofTypeDefault.getSelectedItemPosition();
                    Variables.current_floor.roofHeightDefault.set(index, String.valueOf(Variables.roofTypeDefaultText.getText()));
                }
                return false;
            }
        });

        takePicLampPhoto.setOnTouchListener(new View.OnTouchListener() {        //Кнопка активации сохранения фотографии светильника
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if (Variables.plan.touchedLamp != null) {
                        verifyPermissions(false);
                    }
                return false;
            }
        });

        photoDelete.setOnTouchListener(new View.OnTouchListener() {     //Слушатель нажатий на кнопку удаления фотографии
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.showPhotoFlag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Variables.activity);      //Создаем диалоговое окно
                    builder.setCancelable(true);
                    builder.setTitle("Удалить");
                    builder.setMessage("Вы действительно хотите удалить фотографию?");
                    builder.setPositiveButton("Удалить",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {        //Если подтверждено - удаляем фотографию
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        try {
                                            Files.delete(Paths.get(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto)));
                                            Variables.plan.touchedRoom.photoPaths.remove(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                                            Variables.showAllPhotos(Variables.plan.touchedRoom);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    if (Variables.plan.touchedRoom.photoPaths.size() == 0) {
                                        Variables.indexOfPhoto = -1;
                                        disablePhotoShow();
                                    } else if (Variables.plan.touchedRoom.photoPaths.size() == 1) {
                                        Variables.indexOfPhoto = 0;
                                        File f = new File(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                                    } else {
                                        if (Variables.indexOfPhoto == 0) {
                                            Variables.indexOfPhoto = Variables.plan.touchedRoom.photoPaths.size() - 1;
                                        } else {
                                            Variables.indexOfPhoto--;
                                        }
                                        File f = new File(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                                    }
                                }
                            });
                    builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (Variables.showPhotoLampFlag){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Variables.activity);
                    builder.setCancelable(true);
                    builder.setTitle("Удалить");
                    builder.setMessage("Вы действительно хотите удалить фотографию?");
                    builder.setPositiveButton("Удалить",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        try {
                                            Files.delete(Paths.get(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto)));
                                            Variables.plan.touchedLamp.photoPaths.remove(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                                            Variables.showLampsAllPhotos(Variables.plan.touchedLamp);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    if (Variables.plan.touchedLamp.photoPaths.size() == 0) {
                                        Variables.indexOfPhoto = -1;
                                        disablePhotoShow();
                                    } else if (Variables.plan.touchedLamp.photoPaths.size() == 1) {
                                        Variables.indexOfPhoto = 0;
                                        File f = new File(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                                    } else {
                                        if (Variables.indexOfPhoto == 0) {
                                            Variables.indexOfPhoto = Variables.plan.touchedLamp.photoPaths.size() - 1;
                                        } else {
                                            Variables.indexOfPhoto--;
                                        }
                                        File f = new File(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                                    }
                                }
                            });
                    builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return false;
            }
        });


        photoLeftArrow.setOnTouchListener(new View.OnTouchListener() {      //Слушатель нажатий на кнопку пролистывания влево светильников
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.showPhotoFlag) {
                    if (Variables.plan.touchedRoom.photoPaths.size() > 1) {
                        if (Variables.indexOfPhoto == 0) {
                            Variables.indexOfPhoto = Variables.plan.touchedRoom.photoPaths.size() - 1;
                        } else {
                            Variables.indexOfPhoto--;
                        }
                        File f = new File(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                    }
                } else if (Variables.showPhotoLampFlag){
                    if (Variables.plan.touchedLamp.photoPaths.size() > 1) {
                        if (Variables.indexOfPhoto == 0) {
                            Variables.indexOfPhoto = Variables.plan.touchedLamp.photoPaths.size() - 1;
                        } else {
                            Variables.indexOfPhoto--;
                        }
                        File f = new File(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                    }
                }
                return false;
            }
        });

        photoRightArrow.setOnTouchListener(new View.OnTouchListener() {     //Слушатель нажатий на кнопку пролистывания вправо светильников
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.showPhotoFlag) {
                    if (Variables.plan.touchedRoom.photoPaths.size() > 1) {
                        if (Variables.indexOfPhoto == Variables.plan.touchedRoom.photoPaths.size() - 1) {
                            Variables.indexOfPhoto = 0;
                        } else {
                            Variables.indexOfPhoto++;
                        }
                        File f = new File(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                    }
                }else if (Variables.showPhotoLampFlag){
                    if (Variables.plan.touchedLamp.photoPaths.size() > 1) {
                        if (Variables.indexOfPhoto == Variables.plan.touchedLamp.photoPaths.size() - 1) {
                            Variables.indexOfPhoto = 0;
                        } else {
                            Variables.indexOfPhoto++;
                        }
                        File f = new File(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                        Variables.photoImage.setImageURI(Uri.fromFile(f));
                    }
                }
                return false;
            }
        });

        photoClose.setOnTouchListener(new View.OnTouchListener() {      //Прослушиватель нажатий на кнопку закрытия фотографии
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        disablePhotoShow();
                return false;
            }
        });

        takePicBtn.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активируем камеру
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.plan.touchedRoom!=null) {
                    Variables.takePhotoFlag = true;
                    verifyPermissions(true);
                }
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
                                lamp.getImage().post(new Runnable() {
                                    @Override
                                    public void run() {boolean found = false;
                                            for (Room room : Variables.current_floor.rooms) {
                                                if (room.detectTouch(lamp.getImage().getX()+(lamp.getImage().getWidth()/2), lamp.getImage().getY()+(lamp.getImage().getHeight()/2))) {
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
                                });
                                Variables.plan.rotateImg(lamp.getRotationAngle(),lamp.getImage(),lamp.getTypeImage(),-1);      //Поворачиваем
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
                                tempLamp.setStolb(lamp.isStolb());
                                tempLamp.setMontagneType(lamp.getMontagneType());
                                tempLamp.setPositionOutside(lamp.getPositionOutside());
                                tempLamp.setGroupIndex(lamp.getGroupIndex());
                                tempLamp.setPlaceType(lamp.getPlaceType());
                                tempLamp.setLampsAmount(lamp.getLampsAmount());
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
                                tempLamp.setStolb(Variables.plan.touchedLamp.isStolb());
                                tempLamp.setMontagneType(Variables.plan.touchedLamp.getMontagneType());
                                tempLamp.setPositionOutside(Variables.plan.touchedLamp.getPositionOutside());
                                tempLamp.setGroupIndex(Variables.plan.touchedLamp.getGroupIndex());
                                tempLamp.setPlaceType(Variables.plan.touchedLamp.getPlaceType());
                                tempLamp.setLampsAmount(Variables.plan.touchedLamp.getLampsAmount());
                                ImageView imageView = new ImageView(Variables.activity);
                                Resources resources = Variables.activity.getResources();
                                final int resourceId = resources.getIdentifier(Variables.plan.touchedLamp.getTypeImage(), "drawable",
                                        Variables.activity.getPackageName());
                                imageView.setImageResource(resourceId);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                imageView.setLayoutParams(params);
                                imageView.setScaleX(Variables.plan.touchedLamp.getImage().getScaleX());
                                imageView.setScaleY(Variables.plan.touchedLamp.getImage().getScaleY());
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
                            //Variables.plan.touchedLamp=null;
                            Variables.lampRoom.setText("");
                            Variables.lampType.setText("");
                            Variables.lampPower.setText("");
                            Variables.lampComments.setText("");
                            Variables.lampAmountEdit.setText("0");
                            Variables.montagneTypeTxt.setVisibility(View.GONE);
                            Variables.montagneType.setVisibility(View.GONE);
                            Variables.montagneTypeTxtTwo.setVisibility(View.VISIBLE);
                            Variables.montagneTypeTwo.setVisibility(View.VISIBLE);
                            Variables.lampAmountText.setVisibility(View.VISIBLE);
                            Variables.lampAmountEdit.setVisibility(View.VISIBLE);
                            showLampDop();
                            Variables.montagneTypeTwo.setSelection(0);
                            String[] typeArr = new String[Variables.type.getCount()+1];
                            String[] daysArr = new String[Variables.daysPerWeek.getCount()+1];
                            String[] hoursArr = new String[Variables.hoursPerDay.getCount()+1];
                            String[] hoursWeekendArr = new String[Variables.hoursPerWeekend.getCount()+1];
                            String[] hoursSundayArr = new String[Variables.hoursPerSunday.getCount()+1];
                            String[] placeTypeArr = new String[Variables.placeType.getCount()+1];
                            typeArr[0] = "Не указано";
                            for (int i=0;i<Variables.type.getCount();i++){
                                typeArr[i+1] = String.valueOf(Variables.type.getItemAtPosition(i));
                            }
                            daysArr[0] = "Не указано";
                            for (int i=0;i<Variables.daysPerWeek.getCount();i++){
                                daysArr[i+1] = String.valueOf(Variables.daysPerWeek.getItemAtPosition(i));
                            }
                            hoursArr[0] = "Не указано";
                            for (int i=0;i<Variables.hoursPerDay.getCount();i++){
                                hoursArr[i+1] = String.valueOf(Variables.hoursPerDay.getItemAtPosition(i));
                            }
                            hoursWeekendArr[0] = "Не указано";
                            for (int i=0;i<Variables.hoursPerWeekend.getCount();i++){
                                hoursWeekendArr[i+1] = String.valueOf(Variables.hoursPerWeekend.getItemAtPosition(i));
                            }
                            hoursSundayArr[0] = "Не указано";
                            for (int i=0;i<Variables.hoursPerSunday.getCount();i++){
                                hoursSundayArr[i+1] = String.valueOf(Variables.hoursPerSunday.getItemAtPosition(i));
                            }
                            placeTypeArr[0]= "Не указано";
                            for (int i=0;i<Variables.placeType.getCount();i++){
                                placeTypeArr[i+1] = String.valueOf(Variables.placeType.getItemAtPosition(i));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, typeArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.typeLamp.setAdapter(adapter);
                            adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,daysArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.daysLamp.setAdapter(adapter);
                            adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,hoursArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.hoursLamp.setAdapter(adapter);
                            adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,hoursWeekendArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.hoursPerWeekendLamp.setAdapter(adapter);
                            adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,hoursSundayArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.hoursPerSundayLamp.setAdapter(adapter);
                            adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,placeTypeArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Variables.placeType.setAdapter(adapter);
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
                                    if (room.detectTouch(lamp.getImage().getX()+(lamp.getImage().getWidth()/2), lamp.getImage().getY()+(lamp.getImage().getHeight()/2))) {
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
                            if (!Variables.moveOnlySelectedZone) {  //Выключение функцию выделения и подтверждающей кнопки
                                disableConfirmBtn();
                                disableSelectZone();
                                disableCancelBtn();
                            }else{ //Иначе - выключаем функцию веделения
                                Variables.selectZoneFlag=false;
                                selectZone.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                                Variables.plan.disableListenerFromPlan();
                            }
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
                            if (Variables.montagneTypeTwo.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setMontagneType(Variables.montagneTypeTwo.getSelectedItemPosition()-1);
                                }
                            }
                            if (!String.valueOf(Variables.lampType.getText()).equals("")) {
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setType(String.valueOf(Variables.lampType.getText()));
                                }
                            }
                            if (!String.valueOf(Variables.lampPower.getText()).equals("")) {
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setPower(String.valueOf(Variables.lampPower.getText()));
                                }
                            }
                            if (!String.valueOf(Variables.lampPower.getText()).equals("")) {
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setPower(String.valueOf(Variables.lampPower.getText()));
                                }
                            }
                            if (!String.valueOf(Variables.lampAmountEdit.getText()).equals("0")) {
                                for (Lamp lamp:Variables.copyVector){
                                    if (lamp.getTypeImage().equals("lustradiod") || lamp.getTypeImage().equals("lustranakal") || lamp.getTypeImage().equals("lustrakll")){
                                        lamp.setLampsAmount(Integer.parseInt(String.valueOf(Variables.lampAmountEdit.getText())));
                                    }
                                }
                            }
                            if (!String.valueOf(Variables.lampRoom.getText()).equals("")){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setLampRoom(String.valueOf(Variables.lampRoom.getText()));
                                }
                            }
                            if (Variables.placeType.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setPlaceType(Variables.placeType.getSelectedItemPosition()-1);
                                }
                            }
                            if (Variables.typeLamp.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setTypeRoom(Variables.typeLamp.getSelectedItemPosition()-1);
                                }
                            }
                            if (Variables.daysLamp.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setDaysWork(Variables.daysLamp.getSelectedItemPosition()-1);
                                }
                            }
                            if (Variables.hoursLamp.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setHoursWork(Variables.hoursLamp.getSelectedItemPosition()-1);
                                }
                            }
                            if (Variables.hoursPerWeekendLamp.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setHoursWeekendWork(Variables.hoursPerWeekendLamp.getSelectedItemPosition()-1);
                                }
                            }
                            if (Variables.hoursPerSundayLamp.getSelectedItemPosition()!=0){
                                for (Lamp lamp:Variables.copyVector){
                                    lamp.setHoursSundayWork(Variables.hoursPerSundayLamp.getSelectedItemPosition()-1);
                                }
                            }
                            if (!Variables.moveOnlySelectedZone) {  //Выключение функцию выделения и подтверждающей кнопки
                                disableConfirmBtn();
                                disableSelectZone();
                            }else{ //Иначе - выключаем функцию веделения
                                Variables.selectZoneFlag=false;
                                selectZone.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                                Variables.plan.disableListenerFromPlan();
                            }
                            if (!Variables.lampDopShown)
                                hideLampDop();
                            else
                                showLampDop();
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
                                    tempLamp.setStolb(lamp.isStolb());
                                    tempLamp.setMontagneType(lamp.getMontagneType());
                                    tempLamp.setPositionOutside(lamp.getPositionOutside());
                                    tempLamp.setGroupIndex(lamp.getGroupIndex());
                                    tempLamp.setPlaceType(lamp.getPlaceType());
                                    tempLamp.setLampsAmount(lamp.getLampsAmount());
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
                                    Variables.plan.rotateImg(tempLamp.getRotationAngle(),tempLamp.getImage(),tempLamp.getTypeImage(),-1);
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
                                            if (room.detectTouch((lamp.getImage().getX())+(lamp.getImage().getWidth()/2), (lamp.getImage().getY())+(lamp.getImage().getHeight()/2))) {
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
                                        switch (Variables.currentLampsPanelIndex){      //В зависимости от типа светильника добавляем соответствующие
                                            case 0:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsVstraivaemieName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 1:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsNakladnieName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 2:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsLampsName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 3:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsDiodsName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 4:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsDoskiName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 5:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsPodvesName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 6:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOthersName[Variables.multiplepos],0,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                            case 7:
                                                Variables.plan.spawnLamp(Variables.multipleType, Variables.multiplepos, Variables.lampsOutsideName[Variables.multiplepos],1,Variables.currentLampsPanelIndex,cordX + j * width_step, cordY + i * height_step, true, angle,scaleType);
                                                break;
                                        }
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
                    if (Variables.selectZoneFlag){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Variables.activity);
                        builder.setCancelable(true);
                        builder.setTitle("Удалить");
                        builder.setMessage("Вы действительно хотите удалить группу светильников?");
                        builder.setPositiveButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {        //Если выбрано удалить - удаляем светильник с экрана
                                        for (Lamp lamp:Variables.copyVector){
                                            Room temp = Variables.getRoomByNumber(lamp.getLampRoom(),lamp.getImage().getX(),lamp.getImage().getY(),lamp.getImage().getScaleX(),Variables.current_floor);
                                            if (temp!=null) {
                                                temp.lamps.remove(lamp);
                                            }else
                                                if (Variables.current_floor.unusedLamps.contains(lamp)){
                                                    Variables.current_floor.unusedLamps.remove(lamp);
                                                }else{
                                                    Variables.plan.removeFromEveryWhere(lamp);
                                                }

                                            Variables.activity.runOnUiThread(() -> {        //Удаляем светильник с экрана
                                                Variables.planLay.removeView(lamp.getImage());
                                            });
                                        }
                                    }
                                });
                        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {       //Иначе - отменяем удаление
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else
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
                        if (!Variables.scalemode) {     //Активация изменения размера
                            Variables.scalemode = true;
                            scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            disableRemoveBtn();
                            disableRotateBtn();
                        }else{      //Деактивация изменения размера
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
                        if (!Variables.rotateMode){         //Активация поворота светильника
                            Variables.rotateMode=true;
                            rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.red));
                            disableScaleBtn();
                            disableRemoveBtn();
                        }else{          //Деактивация поворота светильника
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
                                Variables.filePath = FileHelper.getRealPathFromURI(Variables.activity,Variables.current_floor.getImage());
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
                                Variables.floors.clear();
                                Variables.floorsPanels.removeAllViews();
                                Variables.planLayCleared=true;
                                Variables.image.setImageResource(0);
                                Variables.current_floor=null;
                                Variables.filePath="";
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
                        case MotionEvent.ACTION_UP:
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


        Variables.lampDop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        if (!Variables.lampDopShown) {
                            if (Variables.plan.touchedLamp!=null && Variables.plan.touchedLamp.getPlaceType()!=1){
                                showLampDop();
                                Variables.lampDopShown=true;
                            }
                        } else {
                            if (Variables.plan.touchedLamp!=null && Variables.plan.touchedLamp.getPlaceType()!=1){
                                hideLampDop();
                                Variables.lampDopShown=false;
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
                    for (Lamp lamp:Variables.plan.touchedRoom.lamps){
                        if (lamp.getTypeRoom()==lastType) {
                            lamp.setTypeRoom(Variables.type.getSelectedItemPosition());
                        }
                    }
                    lastType=Variables.plan.touchedRoom.getType_pos();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.typeLamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedLamp!=null && !Variables.selectZoneFlag) {
                    Variables.plan.touchedLamp.setTypeRoom(Variables.typeLamp.getSelectedItemPosition());
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
                    for (Lamp lamp:Variables.plan.touchedRoom.lamps) {
                        if (lamp.getDaysWork() == lastDays) {
                            lamp.setDaysWork(Variables.daysPerWeek.getSelectedItemPosition());
                        }
                    }
                    lastDays=Variables.plan.touchedRoom.getDays();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.daysLamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedLamp!=null  && !Variables.selectZoneFlag) {
                    Variables.plan.touchedLamp.setDaysWork(Variables.daysLamp.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.roofTypeDefault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (Variables.current_floor!=null) {
                        int index = Variables.roofTypeDefault.getSelectedItemPosition();
                        Variables.roofTypeDefaultText.setText(Variables.current_floor.roofHeightDefault.elementAt(index));
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
                    for (Lamp lamp:Variables.plan.touchedRoom.lamps) {
                        if (lamp.getHoursWork() == lastHours) {
                            lamp.setHoursWork(Variables.hoursPerDay.getSelectedItemPosition());
                        }
                    }
                    lastHours = Variables.plan.touchedRoom.getHoursPerDay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.hoursLamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedLamp!=null  && !Variables.selectZoneFlag) {
                    Variables.plan.touchedLamp.setHoursWork(Variables.hoursLamp.getSelectedItemPosition());
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
                    for (Lamp lamp:Variables.plan.touchedRoom.lamps) {
                        if (lamp.getHoursWeekendWork() == lastHoursWeekend) {
                            lamp.setHoursWeekendWork(Variables.hoursPerWeekend.getSelectedItemPosition());
                        }
                    }
                    lastHoursWeekend = Variables.plan.touchedRoom.getHoursPerWeekend();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.hoursPerWeekendLamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedLamp!=null  && !Variables.selectZoneFlag) {
                    Variables.plan.touchedLamp.setHoursWeekendWork(Variables.hoursPerWeekendLamp.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.hoursPerSunday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedRoom!=null) {
                    Variables.plan.touchedRoom.setHoursPerSunday(Variables.hoursPerSunday.getSelectedItemPosition());
                    for (Lamp lamp:Variables.plan.touchedRoom.lamps) {
                        if (lamp.getHoursSundayWork() == lastHoursSunday) {
                            lamp.setHoursSundayWork(Variables.hoursPerSunday.getSelectedItemPosition());
                        }
                    }
                    lastHoursSunday=Variables.plan.touchedRoom.getHoursPerSunday();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.hoursPerSundayLamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.plan.touchedLamp!=null  && !Variables.selectZoneFlag) {
                    Variables.plan.touchedLamp.setHoursSundayWork(Variables.hoursPerSundayLamp.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.typeOfBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (String.valueOf(Variables.typeOfBuilding.getSelectedItem()).equals("Детский сад")){
                    ArrayAdapter<String> adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsDetSad);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Variables.type.setAdapter(adapter);
                    Variables.typeLamp.setAdapter(adapter);
                    Variables.daysOfWorkDefault.setSelection(6);
                }else if (String.valueOf(Variables.typeOfBuilding.getSelectedItem()).equals("Школа")){
                    ArrayAdapter<String> adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsSchools);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Variables.type.setAdapter(adapter);
                    Variables.typeLamp.setAdapter(adapter);
                    Variables.daysOfWorkDefault.setSelection(7);
                }else if (String.valueOf(Variables.typeOfBuilding.getSelectedItem()).equals("Больница")){
                    ArrayAdapter<String> adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsHospitals);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Variables.type.setAdapter(adapter);
                    Variables.typeLamp.setAdapter(adapter);
                    Variables.daysOfWorkDefault.setSelection(8);
                }else if (String.valueOf(Variables.typeOfBuilding.getSelectedItem()).equals("Другое")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsOthers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Variables.type.setAdapter(adapter);
                    Variables.typeLamp.setAdapter(adapter);
                    Variables.daysOfWorkDefault.setSelection(6);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.daysOfWorkDefault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (Variables.current_floor!=null) {
                    for (Room room : Variables.current_floor.rooms) {
                            room.setDays(Variables.daysOfWorkDefault.getSelectedItemPosition());
                    }
                    lastWorkdays = Variables.daysOfWorkDefault.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Variables.placeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.plan.touchedLamp!=null && !Variables.selectZoneFlag){
                    Variables.plan.touchedLamp.setPlaceType(Variables.placeType.getSelectedItemPosition());
                    if (Variables.plan.touchedLamp.getPlaceType()==1){
                        if (Variables.plan.touchedLamp.getGroupIndex()==7) {
                            Variables.montagneOutsideTypeTxt.setVisibility(View.VISIBLE);
                            Variables.montagneOutsideType.setVisibility(View.VISIBLE);
                            Variables.positionOutsideTxt.setVisibility(View.VISIBLE);
                            Variables.positionOutside.setVisibility(View.VISIBLE);
                            Variables.isStolbTxt.setVisibility(View.VISIBLE);
                            Variables.isStolbCheck.setVisibility(View.VISIBLE);
                            Variables.montagneTypeTxt.setVisibility(View.GONE);
                            Variables.montagneType.setVisibility(View.GONE);
                            Variables.montagneOutsideType.setSelection(Variables.plan.touchedLamp.getMontagneType());
                            Variables.positionOutside.setSelection(Variables.plan.touchedLamp.getPositionOutside());
                            Variables.isStolbCheck.setChecked(Variables.plan.touchedLamp.isStolb());
                        }else{
                            Variables.positionOutsideTxt.setVisibility(View.VISIBLE);
                            Variables.positionOutside.setVisibility(View.VISIBLE);
                            Variables.positionOutside.setSelection(Variables.plan.touchedLamp.getPositionOutside());
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.montagneType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.plan.touchedLamp!=null){
                    Variables.plan.touchedLamp.setMontagneType(Variables.montagneType.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Variables.roofType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (Variables.roomHeightDefaultCheck.isChecked()) {
                    if (Variables.plan.touchedRoom != null) {
                        if (lastIndex == -1) {
                            lastIndex = Variables.current_floor.roofHeightDefault.indexOf(String.valueOf(Variables.roomHeight.getText()));
                        }
                        Variables.plan.touchedRoom.setRoofType(Variables.roofType.getSelectedItemPosition());
                        int index = Variables.roofType.getSelectedItemPosition();
                        if (String.valueOf(Variables.roomHeight.getText()).equals("0.0") || (lastIndex != -1 && String.valueOf(Variables.roomHeight.getText()).equals(Variables.current_floor.roofHeightDefault.elementAt(lastIndex)))) {
                            if (!Objects.equals(Variables.current_floor.roofHeightDefault.elementAt(index), "0.0")) {
                                Variables.plan.touchedRoom.setHeight(Variables.current_floor.roofHeightDefault.elementAt(index));
                                Variables.roomHeight.setText(Variables.current_floor.roofHeightDefault.elementAt(index));
                            }
                        }
                        lastIndex = index;
                    }
                }else{
                    if (Variables.plan.touchedRoom!=null) {
                        Variables.plan.touchedRoom.setRoofType(Variables.roofType.getSelectedItemPosition());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        Variables.montagneOutsideType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.plan.touchedLamp!=null && Variables.plan.touchedLamp.getGroupIndex()==7){
                    Variables.plan.touchedLamp.setMontagneType(Variables.montagneOutsideType.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.positionOutside.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.plan.touchedLamp!=null){
                    Variables.plan.touchedLamp.setPositionOutside(Variables.positionOutside.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.isStolbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Variables.plan.touchedLamp!=null && Variables.plan.touchedLamp.getGroupIndex()==7){
                    if (Variables.isStolbCheck.isChecked()){
                        Variables.plan.touchedLamp.setStolb(true);
                    }else{
                        Variables.plan.touchedLamp.setStolb(false);
                    }
                }
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
                if (!Variables.selectZoneFlag && !Variables.getMoveFlag()) {
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
                if (!Variables.selectZoneFlag && !Variables.getMoveFlag()) {
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
                if (!Variables.selectZoneFlag && !Variables.getMoveFlag()) {
                    if (Variables.plan.touchedLamp != null) {
                        Variables.plan.touchedLamp.setPower(String.valueOf(Variables.lampPower.getText()));
                    }
                }
            }
        });
        Variables.lampAmountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.plan.touchedLamp != null) {
                    try {
                        if (Variables.lampAmountEdit.length()>0) {
                            Variables.plan.touchedLamp.setLampsAmount(Integer.parseInt(String.valueOf(Variables.lampAmountEdit.getText())));
                        }
                    } catch (Exception ex){

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
                if (!Variables.selectZoneFlag && !Variables.getMoveFlag()) {
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
        if (Variables.copyType==1) {
            for (Lamp lamp : Variables.copyVector) {
                if (lamp.getImage() != null)
                    Variables.planLay.removeView(lamp.getImage());
            }
        }
        Variables.copyVector.clear();
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
        Variables.lampAmountText.setVisibility(View.GONE);
        Variables.lampAmountEdit.setVisibility(View.GONE);
        Variables.montagneTypeTxt.setVisibility(View.VISIBLE);
        Variables.montagneType.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = null;
        switch (Variables.typeOfBuilding.getSelectedItemPosition()){
            case 0:
                adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsDetSad);
                break;
            case 1:
                adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsSchools);
                break;
            case 2:
                adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsHospitals);
                break;
            case 3:
                adapter = new ArrayAdapter(Variables.activity, R.layout.spinner_item, Variables.typesOfRoomsOthers);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.typeLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,Variables.daysPerWeekArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.daysLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,Variables.hoursPerDayArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.hoursLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,Variables.hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.hoursPerWeekendLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,Variables.hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.hoursPerSundayLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(Variables.activity,R.layout.spinner_item,Variables.placeTypeArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Variables.placeType.setAdapter(adapter);
        if (Variables.plan.touchedLamp!=null){
            Variables.montagneType.setSelection(Variables.plan.touchedLamp.getMontagneType());
        }
        Variables.montagneTypeTxtTwo.setVisibility(View.GONE);
        Variables.montagneTypeTwo.setVisibility(View.GONE);
    }

    private void verifyPermissions(boolean type){       //Получение разрешений на использование камеры
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent(type);
        }else{
            ActivityCompat.requestPermissions(Variables.activity,
                    permissions,
                    CAMERA_PERM_CODE);
        }
    }
    public static void dispatchTakePictureIntent(boolean type) {        //Функция создания изображения
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Variables.activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(type);
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

    public static void showLampDop(){
        Variables.typeLampTxt.setVisibility(View.VISIBLE);
        Variables.typeLamp.setVisibility(View.VISIBLE);
        Variables.daysLampTxt.setVisibility(View.VISIBLE);
        Variables.daysLamp.setVisibility(View.VISIBLE);
        Variables.hoursLampTxt.setVisibility(View.VISIBLE);
        Variables.hoursLamp.setVisibility(View.VISIBLE);
        Variables.hoursWeekendLampTxt.setVisibility(View.VISIBLE);
        Variables.hoursPerWeekendLamp.setVisibility(View.VISIBLE);
        Variables.hoursSundayLampTxt.setVisibility(View.VISIBLE);
        Variables.hoursPerSundayLamp.setVisibility(View.VISIBLE);
    }

    public static void hideLampDop(){
        Variables.typeLampTxt.setVisibility(View.GONE);
        Variables.typeLamp.setVisibility(View.GONE);
        Variables.daysLampTxt.setVisibility(View.GONE);
        Variables.daysLamp.setVisibility(View.GONE);
        Variables.hoursLampTxt.setVisibility(View.GONE);
        Variables.hoursLamp.setVisibility(View.GONE);
        Variables.hoursWeekendLampTxt.setVisibility(View.GONE);
        Variables.hoursPerWeekendLamp.setVisibility(View.GONE);
        Variables.hoursSundayLampTxt.setVisibility(View.GONE);
        Variables.hoursPerSundayLamp.setVisibility(View.GONE);
    }

    private static File createImageFile(boolean type) throws IOException {          //Функция создания фотографии
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="temp";
        if (type) {
            if (Variables.plan.touchedRoom != null) {
                imageFileName = Variables.current_floor.getFloor() + ";Помещение:" + Variables.plan.touchedRoom.getNumber() + ";";
            }
        }else{
            if (Variables.plan.touchedLamp != null) {
                if (Variables.plan.touchedRoom!=null) {
                    imageFileName = Variables.current_floor.getFloor() + ";Помещение:" + Variables.plan.touchedRoom.getNumber() + ";" + Variables.plan.touchedLamp.getType() + " " + Variables.plan.touchedLamp.getPower() + ";";
                }else{
                    imageFileName = Variables.current_floor.getFloor() + ";Наружное освещение:"+Variables.plan.touchedLamp.getType()+" "+Variables.plan.touchedLamp.getPower()+";";
                }
                }
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
        if (type)
        Variables.plan.touchedRoom.photoPaths.add(image.getAbsolutePath());
        else
            Variables.plan.touchedLamp.photoPaths.add(image.getAbsolutePath());
        return image;
    }

    private static void disablePhotoShow(){         //Отключение отображения фотографий
        Variables.photoFrame.setVisibility(View.GONE);
        Variables.floorPanelLay.setVisibility(View.VISIBLE);
        Variables.planLay.setVisibility(View.VISIBLE);
        Variables.photoImage.setImageResource(0);
        Variables.indexOfPhoto=-1;
        Variables.showPhotoFlag=false;
        Variables.showPhotoLampFlag=false;
    }


    public static void createNewPhotoRoom(File f,boolean type){     //Создание новой фотографии комнаты(светильника)
        ImageView view = new ImageView(Variables.activity);
        view.setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics())));
        //lay.setBackgroundResource(R.drawable.txtviewborder);
        view.setImageURI(Uri.fromFile(f));
        if (type){      //Если сфотографирована комната
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.scalemode) {
                    Variables.showPhotoLampFlag=false;
                        Variables.showPhotoFlag=true;
                        for (int i = 0; i < Variables.roomGrid.getChildCount(); i++) {
                            if (Variables.roomGrid.getChildAt(i) == view) {
                                Variables.indexOfPhoto = i;
                                break;
                            }
                        }
                        if (Variables.indexOfPhoto != -1) {
                            Variables.photoFrame.setVisibility(View.VISIBLE);
                            Variables.floorPanelLay.setVisibility(View.GONE);
                            Variables.planLay.setVisibility(View.GONE);
                            File f = new File(Variables.plan.touchedRoom.photoPaths.elementAt(Variables.indexOfPhoto));
                            Variables.photoImage.setImageURI(Uri.fromFile(f));
                        }
                }
                return false;
            }
        });Variables.roomGrid.addView(view);}
        else{   //иначе - светильник
            view.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (Variables.scalemode) {
                        Variables.showPhotoFlag=false;
                        Variables.showPhotoLampFlag=true;
                        for (int i = 0; i < Variables.lampGrid.getChildCount(); i++) {
                            if (Variables.lampGrid.getChildAt(i) == view) {
                                Variables.indexOfPhoto = i;
                                break;
                            }
                        }
                        if (Variables.indexOfPhoto != -1) {
                            Variables.photoFrame.setVisibility(View.VISIBLE);
                            Variables.floorPanelLay.setVisibility(View.GONE);
                            Variables.planLay.setVisibility(View.GONE);
                            File f = new File(Variables.plan.touchedLamp.photoPaths.elementAt(Variables.indexOfPhoto));
                            Variables.photoImage.setImageURI(Uri.fromFile(f));
                        }
                    }
                    return false;
                }
            });Variables.lampGrid.addView(view);
        }
    }
    //Конец деактивации функций
}
