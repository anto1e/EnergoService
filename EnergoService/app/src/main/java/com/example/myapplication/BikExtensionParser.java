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
    public void parseFile(String path,String input_name) throws FileNotFoundException {
        currentFile = new File(path);
        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new FileReader(path+input_name));
            String line = reader.readLine();

            while (line != null) {
                if (line.length()>3 && line.charAt(0)=='@' && line.charAt(1)=='%' && line.charAt(2)=='@') {
                    String temp = line.substring(3);
                    if (temp.charAt(0)=='%') {
                        temp = temp.substring(1);
                        String[] subStr = temp.split("/");
                        Variables.lastHeight = Double.parseDouble(subStr[0]);
                        Variables.lastWidth = Double.parseDouble(subStr[1]);
                    }else {
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
                        //ObjectMapper objectMapper = new ObjectMapper();
                        //objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                        //Room room = objectMapper.readValue(temp, Room.class);
                        //Variables.rooms.add(room);
                        Room room = new Room(roomObj.getDouble("number"),tempX,tempY);
                        room.buildPoligon();
                        Variables.rooms.add(room);
                    }
                }
                // read next line
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
