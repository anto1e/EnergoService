package com.example.myapplication;
import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import java.util.Vector;

public class Room {
    private double number;      //Номер помещения
    private double height=0.0;      //Высота помещения
    private int roofType=0;         //Тип потолка
    private String comments="";     //Комментарии

    private int type=0;     //Индекс типа помещения в спиннере
    private int days=0;         //Дни работы
    private int hoursPerDay=0;     //Часы работы по будням

    private int hoursPerWeekend=0;     //Часы работы по выходным
    private Polygon polygon;                //Многоугольник, построенный по координатам точек помещения
    public double[] arrayX;                 //Массив координат Х точек помещения
    public double[] arrayY;                 //Массив координат У точек помещения

    public Room()
    {
        super();
    }


    ////Геттеры и сеттеры////

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRoofType() {
        return roofType;
    }

    public void setRoofType(int roofType) {
        this.roofType = roofType;
    }

    public Vector<Lamp> lamps = new Vector<>();       //Список светильников в помещении



    public int getType_pos() {
        return type;
    }

    public void lampPush(Lamp lamp){
        lamps.add(lamp);
    }

    public void lampRemove(Lamp lamp){
        lamps.remove(lamp);
    }

    public Vector<Lamp> getLamps(){
        return lamps;
    }

    public void setType_pos(int type_pos) {
        this.type = type_pos;
    }


    public double getNumber() {
        return number;
    }


    public void setNumber(double number) {
        this.number = number;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(int hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public int getHoursPerWeekend() {
        return hoursPerWeekend;
    }

    public void setHoursPerWeekend(int hoursPerWeekend) {
        this.hoursPerWeekend = hoursPerWeekend;
    }

    ////Конец геттеров и сеттеров////


    //Конструктор комнаты
    public Room(double number, double[] arrayX, double[] arrayY){
        this.number=number;
        this.arrayX=arrayX;
        this.arrayY=arrayY;

    }

    public void buildPoligon(){     //Постройка полигона комнаты по размеченным координатам
        Polygon.Builder builder = new Polygon.Builder();
        for (int i=0;i<arrayX.length;i++){
           builder =  builder.addVertex(new Point(arrayX[i],arrayY[i]));
        }
        polygon = builder.build();
    }
    public boolean detectTouch(double x, double y){     //Определение попадания точки в полигон
        Point temp = new Point(x,y);
        return polygon.contains(temp);
    }

}
