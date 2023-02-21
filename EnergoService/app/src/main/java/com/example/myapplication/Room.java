package com.example.myapplication;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

public class Room {
    private double number;
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
