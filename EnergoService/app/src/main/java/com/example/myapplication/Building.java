package com.example.myapplication;

import java.util.Vector;

public class Building {
    private String name;
    private String floor;
    private String adress;
    public Vector<Room> rooms = new Vector<Room>();     //Хранение размеченных помещений

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
}
