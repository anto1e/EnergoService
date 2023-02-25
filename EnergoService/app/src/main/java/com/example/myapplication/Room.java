package com.example.myapplication;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

public class Room {
    private double number;
    private double height=0.0;

    public int getType_pos() {
        return type_pos;
    }

    public void setType_pos(int type_pos) {
        this.type_pos = type_pos;
    }

    private String type="";
    private int type_pos=0;
    private int days=0;
    private float hoursPerDay=0.0f;

    public void setNumber(double number) {
        this.number = number;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public float getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(float hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public float getHoursPerWeekend() {
        return hoursPerWeekend;
    }

    public void setHoursPerWeekend(float hoursPerWeekend) {
        this.hoursPerWeekend = hoursPerWeekend;
    }

    private float hoursPerWeekend=0.0f;
    private Polygon polygon;
    public double[] arrayX;
    public double[] arrayY;
    public Room()
    {
        super();
    }

    public double getNumber() {
        return number;
    }



    public Room(double number, double[] arrayX, double[] arrayY){
        this.number=number;
        this.arrayX=arrayX;
        this.arrayY=arrayY;

    }

    public void buildPoligon(){
        Polygon.Builder builder = new Polygon.Builder();
        /*polygon= Polygon.Builder()
                .addVertex(new Point(1, 1))
                .addVertex(new Point(1, 6))
                .addVertex(new Point(5, 6))
                .addVertex(new Point(5, 3))
                .addVertex(new Point(6, 3))
                .addVertex(new Point(6, 2))
                .addVertex(new Point(5, 2))
                .addVertex(new Point(5, 0))
                .addVertex(new Point(4, 0))
                .addVertex(new Point(4, 1))
                .build();*/
        for (int i=0;i<arrayX.length;i++){
           builder =  builder.addVertex(new Point(arrayX[i],arrayY[i]));
        }
        polygon = builder.build();
    }
    public boolean detectTouch(double x, double y){
        Point temp = new Point(x,y);
        return polygon.contains(temp);
    }
}
