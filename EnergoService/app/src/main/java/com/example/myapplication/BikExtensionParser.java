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
        currentFile = new File(path);
        BufferedReader reader;
        BufferedWriter writer;
        try {
            if (Variables.building.rooms.size()!=0) {
                {
                    Variables.building.rooms.clear();
                }
            }
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {  //Пока файл не закончился считываем строка за строкой
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
                        Variables.resizeCoeffs();
                        for (int i=0;i<arrX.length();i++){
                            tempX[i] = arrX.getDouble(i)/Variables.resizeCoeffX;
                            tempY[i] = arrY.getDouble(i)/Variables.resizeCoeffY;
                        }
                        Room room = new Room(roomObj.getDouble("number"),tempX,tempY);
                        room.buildPoligon();
                        Variables.building.rooms.add(room);
                    }else if (line.charAt(0)=='H'){
                        String temp = line.substring(2);
                        String[] subStr = temp.split("@");
                        Variables.building.setName(subStr[0]);
                        Variables.building.setFloor(subStr[1]);
                        Variables.building.setAdress(subStr[2]);
                        EditText buildingName = Variables.activity.findViewById(R.id.buildingName);
                        EditText floor = Variables.activity.findViewById(R.id.floorNumber);
                        EditText adress = Variables.activity.findViewById(R.id.adress);
                        buildingName.setText(Variables.building.getName());
                        floor.setText(Variables.building.getFloor());
                        adress.setText(Variables.building.getAdress());
                    }
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
