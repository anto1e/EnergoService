package com.example.myapplication;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//Класс для хранения глобальных переменных
public class Variables {
    static Activity activity=null;          //Главное activity
    private static boolean addFlag=true;       //Флаг активации режима добавления светильника
    private static boolean moveFlag=true;      //Флаг активации режима перемещния светильника
    static Plan plan = new Plan();          //План этажа
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;


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
