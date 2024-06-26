package com.example.myapplication;

import android.widget.ImageView;

import java.util.Vector;

public class Lamp {
    private String type="";     //Тип светильника
    private float rotationAngle=0;      //Угол поворота светильника

    private String lampRoom="-1";           //Помещение, к которому привязан светильник

    private String power="";        //Мощность светильника
    private ImageView image=null;       //Картинка светильника

    private String typeImage;           //Тип светильника

    Vector<String> photoPaths = new Vector<String>();       //Вектор путей к фотографиям светильника
    private String comments;            //Комментарии к светильнику
    private int montagneType=0;         //Тип монтажа


    private int positionOutside=0;      //Позиция(внутри/снаружи)
    private int lampsAmount=0;          //Количество ламп(для люстр)
    private boolean isStolb=false;      //Является ли столбом(для наружного освещения)

    private int placeType;              //Место расположения(для наружного освещения)
    private int groupIndex;             //Групповой индекс
    private int typeRoom=0;
    private int daysWork=0;
    private int hoursWork=0;
    private int hoursWeekendWork=0;

    private int hoursSundayWork=0;



    ////Геттеры и сеттеры////
    public int getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(int typeRoom) {
        this.typeRoom = typeRoom;
    }

    public int getDaysWork() {
        return daysWork;
    }

    public void setDaysWork(int daysWork) {
        this.daysWork = daysWork;
    }

    public int getHoursWork() {
        return hoursWork;
    }

    public void setHoursWork(int hoursWork) {
        this.hoursWork = hoursWork;
    }

    public int getHoursWeekendWork() {
        return hoursWeekendWork;
    }

    public void setHoursWeekendWork(int hoursWeekendWork) {
        this.hoursWeekendWork = hoursWeekendWork;
    }

    public int getHoursSundayWork() {
        return hoursSundayWork;
    }

    public void setHoursSundayWork(int hoursSundayWork) {
        this.hoursSundayWork = hoursSundayWork;
    }
    public boolean isStolb() {
        return isStolb;
    }

    public void setStolb(boolean stolb) {
        isStolb = stolb;
    }

    public int getPositionOutside() {
        return positionOutside;
    }

    public void setPositionOutside(int positionOutside) {
        this.positionOutside = positionOutside;
    }
    public int getLampsAmount() {
        return lampsAmount;
    }

    public void setLampsAmount(int lampsAmount) {
        this.lampsAmount = lampsAmount;
    }
    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }
    public int getMontagneType() {
        return montagneType;
    }

    public void setMontagneType(int montagneType) {
        this.montagneType = montagneType;
    }

    public String getLampRoom() {
        return lampRoom;
    }

    public void setLampRoom(String lampRoom) {
        this.lampRoom = lampRoom;
    }
    public float getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setView(){
        Variables.activity.runOnUiThread(() -> {
            Variables.planLay.addView(image);
        });
    }

    ////Конец геттеров и сеттеров////
}
