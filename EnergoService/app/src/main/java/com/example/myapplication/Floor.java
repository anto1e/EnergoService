package com.example.myapplication;

import android.net.Uri;

import java.net.URI;
import java.util.Vector;

public class Floor {
    private String name;                //Наименование здания

    double resizeCoeffX;                //Коэффициент ресайза по Х
    double resizeCoeffY;                //Коэффициент ресайза по У


    private Uri image;                  //Путь к плану этажа
    private String floor;               //Название этажа
    private String adress;              //Адрес здания
    public Vector<Room> rooms = new Vector<Room>();     //Хранение размеченных помещений

    ////Геттеры и сеттеры////
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    ////Конец геттеров и сеттеров////

    public void resizeCoeffs(){              //Определение коэффициента ресайза
        resizeCoeffY = Math.abs(Variables.lastHeight/Variables.currentHeight);
        resizeCoeffX = Math.abs(Variables.lastWidth/Variables.currentWidth);
    }
}
