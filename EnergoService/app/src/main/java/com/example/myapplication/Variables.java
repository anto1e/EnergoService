package com.example.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Vector;

//Класс для хранения глобальных переменных
public class Variables {
    static boolean isExpotedExcel=true;     //Флаг экспорта в эксель

    static int typeOpening=0;           //Тип открытия нового файла
    static Vector<LinearLayout> FloorPanelsVec = new Vector<LinearLayout>();        //Вектор вкладок на экране
    static ExcelExporter exporter;                  //Экспортер данных в эксель
    static RelativeLayout roomInfoView;             //Панель инфрмации о комнате

    static RelativeLayout buildingInfoView;             //Панель инфрмации о здании
    static RelativeLayout lampInfoView;             //Панель инфрмации о светильнике
    static EditText buildingName;             //Поле наименования здания
    static EditText buidlingFloor;             //Поле номера этажа
    static EditText buildingAdress;             //Поле адреса здания
    static String filePath="";             //Путь к текущему открытому файлу
    static Uri selectedfile;             //Выбранный текущий файл
    static Activity activity=null;          //Главное activity
    static boolean opened = false;          //Если файл был открыт
    static BikExtensionParser parser = new BikExtensionParser();        //Парсер данных из .bik
    private static boolean addFlag=true;       //Флаг активации режима добавления светильника
    private static boolean moveFlag=true;      //Флаг активации режима перемещния светильника
    static ImageView loadingImage;              //Колесо вращения при сохранении
    static LinearLayout floorsPanels;           //Layout для хранения вкладок
    static Plan plan = new Plan();          //План этажа
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText roomNumber=null;            //Поле для номера помещения
    static EditText roomHeight;            //Поле для высоты помещения
    static Spinner daysPerWeek;            //Поле для дней работы помещения
    static Spinner hoursPerDay;            //Поле для часов работы в день помещения
    static Spinner hoursPerWeekend;            //Поле для часов работы в выходные помещения
    static Spinner roofType;                //Поле типа потолка
    static Button submit;            //Кнопка сохранения изменений в информации о помещении
    static Button submitBuildingInfo;       //Кнопка сохранения изменений в информации о здании
    static Button submitLampInfo;       //Кнопка сохранения изменений в информации о светильнике

    static Buttons buttons = new Buttons();        //Создание класса с кнопками
    static Spinner type;            //Выпадающий список(спинер) с типами помещений
    static RelativeLayout RoomInfo;            //Макет, где хранится информация о помещении
    static EditText lampType;                  //Поле типа светильника
    static EditText lampPower;                  //Поле мощности светильника
    static EditText lampComments;              //Поле комментариев к светильнику

    static EditText roomComments;               //Поле комментариев к комнате

    static Vector<Floor> floors= new Vector<Floor>();       //Вектор, хранящий открытые этаж

    static Floor current_floor=null;            //Текущий открытый этаж

    //static Floor building;
    static double lastWidth;                //Ширина плана, при разметке на сайте
    static double lastHeight;                //Высота плана, при разметке на сайте
    static double currentWidth;                //Ширина плана в приложении
    static double currentHeight;                //Высота плана в приложении
    public static final String[] lampNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт"
    };

    static String[] roofTypes = {"Бетон","Армстронг","ПВХ"};        //Типы потолков
    static String[] typesOfRooms = { "Игровая", "Спальная", "Санузел", "Коридор", "Тамбур","Лестница","Кабинет","Пищеблок"};            //Типы помещений
    static String[] daysPerWeekArr = {"0","1","2","3","4","5","6","7"};         //Дней работы в неделю
    static String[] hoursPerDayArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по будням
    static String[] hoursPerWeekendArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по выходным




    public static void init(){                //Инициализация переменных
        floorsPanels = activity.findViewById(R.id.floorPanelsLay);
        loadingImage = activity.findViewById(R.id.LoadingImage);
        loadingImage.bringToFront();
        roomInfoView = activity.findViewById(R.id.RoomInfoView);
        buildingInfoView = activity.findViewById(R.id.BuildingInfoView);
        lampInfoView = activity.findViewById(R.id.lampInfoView);
        lampType = activity.findViewById(R.id.lampType);
        roomComments = activity.findViewById(R.id.roomComments);
        lampPower = activity.findViewById(R.id.lampPower);
        lampComments = activity.findViewById(R.id.lampComments);
        image = activity.findViewById(R.id.imageView);
        roomNumber = activity.findViewById(R.id.roomNumber);
        type = activity.findViewById(R.id.spinTypes);
        roomHeight = activity.findViewById(R.id.roomHigh);
        daysPerWeek = activity.findViewById(R.id.roomDays);
        hoursPerDay = activity.findViewById(R.id.roomHours);
        hoursPerWeekend = activity.findViewById(R.id.roomHoursWeekends);
        roofType = activity.findViewById(R.id.roofType);
        submit = activity.findViewById(R.id.submit);
        submitBuildingInfo = activity.findViewById(R.id.submitBuilding);
        submitLampInfo = activity.findViewById(R.id.submitLamp);
        RoomInfo = activity.findViewById(R.id.RoomInfoView);
        buildingName = activity.findViewById(R.id.buildingName);
        buidlingFloor = activity.findViewById(R.id.floorNumber);
        buildingAdress = activity.findViewById(R.id.adress);
        setSpinners();
        exporter = new ExcelExporter();
    }

    public static void setSpinners(){       //Инициализация спиннеров
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, typesOfRooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,daysPerWeekArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysPerWeek.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerWeekend.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerDayArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerDay.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,roofTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roofType.setAdapter(adapter);
    }



    //Геттеры, сеттеры, инверторы флагов
    public static boolean getAddFlag(){
        return addFlag;
    }
    public static void setAddFlag(boolean addFlagNew){
        addFlag = addFlagNew;
    }
    public static void invertAddFlag(){
        if (addFlag)
            addFlag=false;
        else
            addFlag=true;
    }
    public static boolean getMoveFlag(){
        return moveFlag;
    }
    public static void setMoveFlag(boolean moveFlagNew){
        moveFlag = moveFlagNew;
    }
    public static void invertMoveFlag(){
        if (moveFlag)
            moveFlag=false;
        else
            moveFlag=true;
    }
}
