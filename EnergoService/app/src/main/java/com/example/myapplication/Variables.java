package com.example.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.Vector;

//Класс для хранения глобальных переменных
public class Variables {
    static ScrollView roomInfoView;

    static ScrollView buildingInfoView;
    static String filePath="";
    static Uri selectedfile;
    static Activity activity=null;          //Главное activity
    static boolean opened = false;          //Если файл был открыт
    static BikExtensionParser parser = new BikExtensionParser();
    private static boolean addFlag=true;       //Флаг активации режима добавления светильника
    private static boolean moveFlag=true;      //Флаг активации режима перемещния светильника
    static Plan plan = new Plan();          //План этажа
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText roomNumber=null;            //Поле для номера помещения
    static EditText roomHeight;            //Поле для высоты помещения
    static EditText daysPerWeek;            //Поле для дней работы помещения
    static EditText hoursPerDay;            //Поле для часов работы в день помещения
    static EditText hoursPerWeekend;            //Поле для часов работы в выходные помещения
    static Button submit;            //Кнопка сохранения изменений в помещении
    static Spinner spinner;            //Выпадающий список(спинер) с типами помещений
    static ScrollView RoomInfo;            //Макет, где хранится информация о помещении

    static Building building;
    static double lastWidth;                //Ширина плана, при разметке на сайте
    static double lastHeight;                //Высота плана, при разметке на сайте
    static double currentWidth;                //Ширина плана в приложении
    static double currentHeight;                //Высота плана в приложении
    static double resizeCoeffX;                //Коэффициент ресайза по Х
    static double resizeCoeffY;                //Коэффициент ресайза по У
    static String[] typesOfRooms = { "Игровая", "Спальная", "Санузел", "Коридор", "Тамбур"};

    public static void resizeCoeffs(){              //Определение коэффициента ресайза
        resizeCoeffY = Math.abs(lastHeight/currentHeight);
        resizeCoeffX = Math.abs(lastWidth/currentWidth);
    }

    public static void init(){                //Инициализация переменных
        roomInfoView = activity.findViewById(R.id.RoomInfoView);
        buildingInfoView = activity.findViewById(R.id.BuildingInfoView);
        building = new Building();
        image = activity.findViewById(R.id.imageView);
        roomNumber = activity.findViewById(R.id.roomNumber);
        spinner = activity.findViewById(R.id.spinTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, typesOfRooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        roomHeight = activity.findViewById(R.id.roomHigh);
        daysPerWeek = activity.findViewById(R.id.roomDays);
        hoursPerDay = activity.findViewById(R.id.roomHours);
        hoursPerWeekend = activity.findViewById(R.id.roomHoursWeekends);
        submit = activity.findViewById(R.id.submit);
        RoomInfo = activity.findViewById(R.id.RoomInfoView);
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
