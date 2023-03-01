package com.example.myapplication;

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
            Variables.rooms.clear();
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {  //Пока файл не закончился считываем строка за строкой
                if (line.length()>3 && line.charAt(0)=='@' && line.charAt(1)=='%' && line.charAt(2)=='@') {
                    String temp = line.substring(3);
                    if (temp.charAt(0)=='%') {  //Если это информация о размерах изображения
                        temp = temp.substring(1);
                        String[] subStr = temp.split("/");
                        Variables.lastHeight = Double.parseDouble(subStr[0]);
                        Variables.lastWidth = Double.parseDouble(subStr[1]);
                    }else {     //Иначе это информация о комнатах
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
                        Variables.rooms.add(room);
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
