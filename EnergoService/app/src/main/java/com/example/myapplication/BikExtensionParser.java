package com.example.myapplication;

import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BikExtensionParser {
    boolean buildingInfo=false;
    boolean lampsInfo = false;
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
                if (line.equals("///INFORMATION ABOUT BUILDING AND ROOMS///")) {
                    buildingInfo = true;
                }else if (line.equals("///INFORMATION ABOUT LAMPS///")){
                    buildingInfo=false;
                    lampsInfo=true;
                }
                if (buildingInfo){
                    if (line.length() > 2 && (line.charAt(0) == 'H' || line.charAt(0) == 'S' || line.charAt(0) == 'R') && line.charAt(1) == '@') {
                        if (line.charAt(0) == 'S') {  //Если это информация о размерах изображения
                            String temp = line.substring(2);
                            String[] subStr = temp.split("/");
                            Variables.lastHeight = Double.parseDouble(subStr[0]);
                            Variables.lastWidth = Double.parseDouble(subStr[1]);
                        } else if (line.charAt(0) == 'R') {     //Иначе это информация о комнатах
                            String temp = line.substring(2);
                            JSONObject roomObj = new JSONObject(temp.substring(temp.indexOf("{"), temp.lastIndexOf("}") + 1));
                            JSONArray arrX = roomObj.getJSONArray("arrayX");
                            JSONArray arrY = roomObj.getJSONArray("arrayY");
                            double[] tempX = new double[arrX.length()];
                            double[] tempY = new double[arrY.length()];
                            floor.resizeCoeffs();
                            for (int i = 0; i < arrX.length(); i++) {
                                tempX[i] = arrX.getDouble(i) / floor.resizeCoeffX;
                                tempY[i] = arrY.getDouble(i) / floor.resizeCoeffY;
                            }
                            Room room = new Room(roomObj.getDouble("number"), tempX, tempY);
                            room.buildPoligon();
                            floor.rooms.add(room);
                        } else if (line.charAt(0) == 'H') {     //Информация о зданиии
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
            }
                else if (lampsInfo){
                    if (line.length()>7 && line.charAt(0) != '/'){
                        String[] split_number = line.split("%");
                        if (split_number.length>1){
                            float number = Float.parseFloat(split_number[0]);
                            String[] split_room_info = split_number[1].split("@");
                            String type = split_room_info[0];
                            String power = split_room_info[1];
                            int type_image = Integer.parseInt(split_room_info[2]);
                            String comments = split_room_info[3];
                            if (comments==null)
                                comments="";
                            float cordX = Float.parseFloat(split_room_info[4]);
                            float cordY = Float.parseFloat(split_room_info[5]);
                            float scale = Float.parseFloat(split_room_info[6]);
                            Room room = Variables.getRoomByNumber(number);
                            if (room!=null){
                                Lamp lamp = new Lamp();
                                lamp.setType(type);
                                lamp.setPower(power);
                                lamp.setTypeImage(type_image);
                                lamp.setComments(comments);
                                ImageView imageView = new ImageView(Variables.activity);
                                imageView.setImageResource(type_image);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                imageView.setLayoutParams(params);
                                imageView.setX(cordX);
                                imageView.setY(cordY);
                                imageView.setScaleX(scale);
                                imageView.setScaleY(scale);
                                Variables.plan.setListener(imageView);
                                lamp.setImage(imageView);
                                //lamp.setView();
                                room.lamps.add(lamp);
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
            buildingInfo=false;
            lampsInfo=false;
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
    public void saveFile(String pathFile) throws IOException {
        String filename = pathFile;

        File oldFile = new File( pathFile);
        File newFile = new File( pathFile );

        try {
            String str1= "///INFORMATION ABOUT LAMPS///";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte[] b1 = Files.readAllBytes(Paths.get(pathFile));
                byte[] b2 = str1.getBytes();
                long bytesCount=0;
                long countMatches=0;
                for (int i=0;i<b1.length;i++){
                    if (b1[i]==b2[0]){
                        if (i+b2.length<=b1.length){
                            for (int j=0;j<b2.length;j++){
                                if (b1[i+j]==b2[j])
                                    countMatches++;
                            }
                        }
                    }
                    if (countMatches==b2.length)
                        break;
                    bytesCount++;
                    countMatches=0;
                }
                byte[] temp_ar = new byte[(int) bytesCount];
                for (int i=0;i<temp_ar.length;i++){
                    temp_ar[i] = b1[i];
                }
                FileOutputStream fos = new FileOutputStream(newFile);

                fos.write(temp_ar);
                fos.close();
                //str1 = Base64.encodeBase64URLSafeString(b);
                try(FileWriter fw = new FileWriter(pathFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))

                {
                    out.println("\n///INFORMATION ABOUT LAMPS///");
                    Floor tempFloor = Variables.current_floor;
                    for (int i=0;i<tempFloor.rooms.size();i++){
                        for (int j=0;j<tempFloor.rooms.elementAt(i).lamps.size();j++){
                            Lamp temp = tempFloor.rooms.elementAt(i).lamps.elementAt(j);
                            out.println(tempFloor.rooms.elementAt(i).getNumber()+"%"+temp.getType()+"@"+temp.getPower()+"@"+temp.getTypeImage()+"@"+temp.getComments()+"@"+temp.getImage().getX()+"@"+temp.getImage().getY()+"@"+temp.getImage().getScaleX());
                        }
                    }
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            }
            //writer.write(str);
            //fos.write(temp);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
