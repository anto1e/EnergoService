package com.example.myapplication;

import android.widget.ImageView;

public class Lamp {
    private String type="";     //Тип светильника
    private float rotationAngle=0;

    private String lampRoom="-1";

    private String power="";        //Мощность светильника
    private ImageView image=null;       //Картинка светильника

    private int typeImage;
    private String comments;            //Комментарии к светильнику

    ////Геттеры и сеттеры////
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

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
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
