package com.example.myapplication;

import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BikExtensionParser {
    File currentFile;
    public void parseFile(String path) throws FileNotFoundException {   //Парсинг файла
        Floor floor = new Floor();          //Создание нового этажа
        if (Variables.current_floor!=null) {        //Если есть предыдущий этаж - записываем в него текущие позицию и приближение
            Variables.current_floor.cordX = Variables.planLay.getX();
            Variables.current_floor.cordY = Variables.planLay.getY();
            Variables.current_floor.scale = Variables.planLay.getScaleX();
        }
        Variables.planLay.setX(floor.cordX);        //Задаем этажу позицию Х
        Variables.planLay.setY(floor.cordY);        //Задаем этажу позицию У
        Variables.planLay.setScaleX(floor.scale);   //Задаем этажу приближение
        Variables.planLay.setScaleY(floor.scale);   //Задаем этажу приближение
        if (Variables.typeOpening==0 && Variables.current_floor!=null) {        //Если открываем в текущей вкладке - переназначаем текущий этаж
            Variables.floors.set(Variables.floors.indexOf(Variables.current_floor), floor);
        }
        currentFile = new File(path);
        BufferedReader reader;
        BufferedWriter writer;
        try {           //Если открываем в текущей вкладке - очищаем все комнаты текущео этажа
            if (Variables.current_floor!=null && Variables.floors.size()>0 && Variables.typeOpening==0) {
                {
                    Variables.current_floor.rooms.clear();
                }
            }
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {  //Пока файл не закончился считываем строка за строкой
                Variables.current_floor = floor;
                if (line.length()>2 && (line.charAt(0)=='H' || line.charAt(0)=='S' || line.charAt(0)=='R') && line.charAt(1)=='@') {
                    if (line.charAt(0)=='S') {  //Если это информация о размерах изображения
                        String temp = line.substring(2);
                        String[] subStr = temp.split("/");
                        Variables.lastHeight = Double.parseDouble(subStr[0]);
                        Variables.lastWidth = Double.parseDouble(subStr[1]);
                    }else if (line.charAt(0)=='R') {     //Иначе это информация о комнатах
                        String temp = line.substring(2);
                        JSONObject roomObj = new JSONObject(temp.substring(temp.indexOf("{"), temp.lastIndexOf("}") + 1));
                        JSONArray arrX = roomObj.getJSONArray("arrayX");
                        JSONArray arrY = roomObj.getJSONArray("arrayY");
                        double[] tempX = new double[arrX.length()];
                        double[] tempY = new double[arrY.length()];
                        floor.resizeCoeffs();
                        for (int i=0;i<arrX.length();i++){
                            tempX[i] = arrX.getDouble(i)/floor.resizeCoeffX;
                            tempY[i] = arrY.getDouble(i)/floor.resizeCoeffY;
                        }
                        Room room = new Room(roomObj.getDouble("number"),tempX,tempY);
                        room.buildPoligon();
                        floor.rooms.add(room);
                    }else if (line.charAt(0)=='H'){     //Информация о зданиии
                        String temp = line.substring(2);
                        String[] subStr = temp.split("@");
                        floor.setName(subStr[0]);
                        floor.setFloor(subStr[1]);
                        floor.setAdress(subStr[2]);
                        EditText buildingName = Variables.activity.findViewById(R.id.buildingName);
                        EditText floor1 = Variables.activity.findViewById(R.id.floorNumber);
                        EditText adress = Variables.activity.findViewById(R.id.adress);
                        buildingName.setText(floor.getName());
                        floor1.setText(floor.getFloor());
                        adress.setText(floor.getAdress());
                    }
                }
                line = reader.readLine();
            }
            floor.setImage(Variables.selectedfile);     //Передаем картинку этажа в созданный этаж
            Variables.floors.add(floor);
            if (Variables.typeOpening==0){      //Если открываем в текущей вкладке - меняем вкладку
            if (Buttons.active==null){
                Variables.buttons.addPanel(floor.getFloor());
            }else{
                Variables.FloorPanelsVec.remove(Buttons.active);
                Variables.floorsPanels.removeView(Buttons.active);
                Variables.buttons.addPanel(floor.getFloor());
            }
            }else{              //Если создаем новую вкладку - добавляем новую вкладку
                Variables.buttons.addPanel(floor.getFloor());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
