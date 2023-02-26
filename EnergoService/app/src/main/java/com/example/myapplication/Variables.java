package com.example.myapplication;

import android.app.Activity;
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
    static Activity activity=null;          //Главное activity
    static boolean opened = false;
    static BikExtensionParser parser = new BikExtensionParser();
    private static boolean addFlag=true;       //Флаг активации режима добавления светильника
    private static boolean moveFlag=true;      //Флаг активации режима перемещния светильника
    static Plan plan = new Plan();          //План этажа
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText roomNumber=null;
    static EditText roomHeight;
    static EditText daysPerWeek;
    static EditText hoursPerDay;
    static EditText hoursPerWeekend;
    static Button submit;
    static Spinner spinner;
    static ScrollView RoomInfo;
    static Vector<Room> rooms = new Vector<Room>();     //Хранение размеченных помещений
    static double lastWidth;
    static double lastHeight;
    static double currentWidth;
    static double currentHeight;
    static double resizeCoeffX;
    static double resizeCoeffY;
    static String[] typesOfRooms = { "Игровая", "Спальная", "Санузел", "Коридор", "Тамбур"};

    public static void resizeCoeffs(){
        resizeCoeffY = Math.abs(lastHeight/currentHeight);
        resizeCoeffX = Math.abs(lastWidth/currentWidth);
    }

    public static void init(){
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
