package com.example.myapplication;

import android.content.res.Resources;
import android.os.Build;
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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class BikExtensionParser {
    boolean buildingInfo=false;     //Флаг начала информации о здании
    boolean lampsInfo = false;      //Флаг начала информации о светильниках
    boolean roomInfo = false;       //Флаг начала информации о комнатах
    File currentFile;
    public void parseFile(String path) throws FileNotFoundException {   //Парсинг файла
        Variables.setAddFlag(false);
        Variables.plan.disableListenerFromPlan();
        Variables.buttons.addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.setMoveFlag(false);
        Variables.buttons.moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.scalemode=false;
        Variables.buttons.scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.rotateMode=false;
        Variables.buttons.rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.removeMode=false;
        Variables.buttons.removeLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.plan.touchedRoom=null;
        Variables.plan.lastRoom=null;
        Floor floor = new Floor();          //Создание нового этажа
        for (String type:Variables.roofTypes){
            floor.roofHeightDefault.add("0.0");
        }
        Variables.roofTypeDefaultText.setText("");
        Variables.roomHeightDefaultCheck.setChecked(false);
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
                if (line.equals("///INFORMATION ABOUT BUILDING AND ROOMS///")) {        //Если информация о разметке
                    buildingInfo = true;
                }else if (line.equals("///INFORMATION ABOUT ROOMS///")){                //Если информация о комнатах
                    buildingInfo=false;
                    roomInfo=true;
                }

                else if (line.equals("///INFORMATION ABOUT LAMPS///")){             //Если информация о светильниках
                    buildingInfo=false;
                    roomInfo=false;
                    lampsInfo=true;
                }
                if (buildingInfo){      //Если информация о здании
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
                            for (int i = 0; i < arrX.length(); i++) {       //Изменяем координаты точек разметки в зависимости от размера экрана
                                tempX[i] = arrX.getDouble(i) / floor.resizeCoeffX;
                                tempY[i] = arrY.getDouble(i) / floor.resizeCoeffY;
                            }
                            Room room = new Room(roomObj.getString("number"), tempX, tempY);
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
                else if (roomInfo){         //Если информация о комнатах
                    if (line.length()>7 && line.charAt(0) != '/'){
                        String[] split_number = line.split("%");
                        if (split_number.length>1){
                            String number = split_number[0];
                            String[] split_room_info = split_number[1].split("@");
                            String height = split_room_info[0];
                            int typeRoom = Integer.parseInt(split_room_info[1]);
                            int days = Integer.parseInt(split_room_info[2]);
                            int hours = Integer.parseInt(split_room_info[3]);
                            int hoursPerWeekend = Integer.parseInt(split_room_info[4]);
                            int roofType = Integer.parseInt(split_room_info[5]);
                            String comments;
                            if (split_room_info.length==6){
                                comments="";
                            }else {
                                comments = split_room_info[6];

                                if (Objects.equals(comments, "null"))
                                    comments = "";
                            }
                            Room room = Variables.getRoomByNumber(number);
                            if (room!=null) {
                                if (split_room_info.length>7){      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                    String paths = split_room_info[7];
                                    String[] split_room_photos = paths.split("!");
                                    for (int i=0;i<split_room_photos.length;i++){
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            if (Files.exists(Paths.get(split_room_photos[i]))){
                                                room.photoPaths.add(split_room_photos[i]);
                                            }
                                        }
                                    }
                                    //room.photoPaths.addAll(Arrays.asList(split_room_photos));
                                }
                                room.setHeight(height);
                                room.setType_pos(typeRoom);
                                room.setDays(days);
                                room.setHoursPerDay(hours);
                                room.setHoursPerWeekend(hoursPerWeekend);
                                room.setRoofType(roofType);
                                room.setComments(comments);
                            }
                        }
                    }
                }
                else if (lampsInfo){        //Если информация о светильниках
                    if (line.length()>9 && line.charAt(0) != '/'){
                        String[] split_number = line.split("%");
                        if (split_number.length>1){
                            String number = split_number[0];
                            String[] split_room_info = split_number[1].split("@");
                            String type = split_room_info[0];
                            String power = split_room_info[1];
                            String type_image = split_room_info[2];
                            String comments = split_room_info[3];
                            if (Objects.equals(comments, "null"))
                                comments="";
                            float cordX = Float.parseFloat(split_room_info[4]);
                            float cordY = Float.parseFloat(split_room_info[5]);
                            float scale = Float.parseFloat(split_room_info[6]);
                            float rotationAngle = Float.parseFloat(split_room_info[7]);
                            String lampRoom = split_room_info[8];
                            String usedOrNot = split_room_info[9];
                            Room room=null;
                            if (!Objects.equals(number, "-1")) {
                                room = Variables.getRoomByNumber(number);
                            }
                                Lamp lamp = new Lamp();
                                lamp.setType(type);
                                lamp.setPower(power);
                                lamp.setTypeImage(type_image);
                                lamp.setLampRoom(lampRoom);
                                lamp.setComments(comments);
                                lamp.setRotationAngle(rotationAngle);
                                ImageView imageView = new ImageView(Variables.activity);
                                Resources resources = Variables.activity.getResources();
                                final int resourceId = resources.getIdentifier(type, "drawable",
                                    Variables.activity.getPackageName());
                                imageView.setImageResource(resourceId);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(15, 15);
                                imageView.setLayoutParams(params);
                                imageView.setX(cordX);
                                imageView.setY(cordY);
                                imageView.setScaleX(scale);
                                imageView.setScaleY(scale);
                                Variables.plan.setListener(imageView);
                                lamp.setImage(imageView);
                                Variables.plan.rotateImg(rotationAngle,imageView,type_image);
                            if (split_room_info.length>10){      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                String paths = split_room_info[10];
                                String[] split_room_photos = paths.split("!");
                                for (int i=0;i<split_room_photos.length;i++){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (Files.exists(Paths.get(split_room_photos[i]))){
                                            lamp.photoPaths.add(split_room_photos[i]);
                                        }
                                    }
                                }
                                //room.photoPaths.addAll(Arrays.asList(split_room_photos));
                            }
                                if (Objects.equals(lampRoom, "-1") && !Objects.equals(usedOrNot, "used")){  //Если светильник не привязан никуда, пытаемся привязать по координатам, если не выходит - не привязываем
                                    Room detectedRoom=null;
                                    for (Room temp:Variables.current_floor.rooms){
                                        if (temp.detectTouch(cordX,cordY)) {
                                            detectedRoom=temp;
                                        }
                                    }
                                    if (detectedRoom==null){
                                        Variables.current_floor.unusedLamps.add(lamp);
                                            imageView.setBackgroundResource(R.color.blue);
                                    }else{
                                        detectedRoom.lamps.add(lamp);
                                    }
                                }else{
                                    room.lamps.add(lamp);
                                }
                        }
                    }
                }
                line = reader.readLine();
            }
            buildingInfo=false;
            roomInfo=false;
            lampsInfo=false;
            floor.setImage(Variables.selectedfile);     //Передаем картинку этажа в созданный этаж
            Variables.floors.add(floor);            //Добавляем этаж в вектор этажей
            if (Variables.typeOpening==0){      //Если открываем в текущей вкладке - меняем вкладку
            if (Buttons.active==null){      //Если нужно добавить новую вкладку
                Variables.buttons.addPanel(floor.getFloor());
            }else{      //Иначе заменяем текущую
                Variables.FloorPanelsVec.remove(Buttons.active);
                Variables.floorsPanels.removeView(Buttons.active);
                Variables.buttons.addPanel(floor.getFloor());
            }
            }else{              //Если создаем новую вкладку - добавляем новую вкладку
                Variables.buttons.addPanel(floor.getFloor());
            }
            reader.close();
            if (Variables.current_floor!=null) {
                for (Room room : Variables.current_floor.rooms) {
                    if (room.getDays() == 0) {
                        room.setDays(Integer.parseInt(String.valueOf(Variables.daysOfWorkDefault.getSelectedItem())));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveFile(String pathFile) throws IOException {      //Сохранение файла
        String filename = pathFile;

        File oldFile = new File( pathFile);
        File newFile = new File( pathFile );

        try {       //Находим информацию о комнатах и стираем все после нее
            String str1= "///INFORMATION ABOUT ROOMS///";
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

                {       //Начинаем записывать информацию о комнатах
                    Floor tempFloor = Variables.current_floor;
                    out.println();
                    out.println("///INFORMATION ABOUT ROOMS///");
                    for (int i=0;i<tempFloor.rooms.size();i++){
                        String str12 = tempFloor.rooms.elementAt(i).getNumber()+"%"+tempFloor.rooms.elementAt(i).getHeight()+"@"+tempFloor.rooms.elementAt(i).getType_pos()+"@"+tempFloor.rooms.elementAt(i).getDays()+"@"+tempFloor.rooms.elementAt(i).getHoursPerDay()+"@"+tempFloor.rooms.elementAt(i).getHoursPerWeekend()+"@"+tempFloor.rooms.elementAt(i).getRoofType()+"@"+tempFloor.rooms.elementAt(i).getComments();
                        String str2="@";
                        if (tempFloor.rooms.elementAt(i).photoPaths.size()!=0){
                            for (String str:tempFloor.rooms.elementAt(i).photoPaths){
                                str2+=str;
                                str2+="!";
                            }
                        }
                        out.println(str12+str2);

                    }


                    out.println("///INFORMATION ABOUT LAMPS///");   //Начинаем записывать информацию о светильниках
                    for (int i=0;i<tempFloor.rooms.size();i++){
                        for (int j=0;j<tempFloor.rooms.elementAt(i).lamps.size();j++){
                            Lamp temp = tempFloor.rooms.elementAt(i).lamps.elementAt(j);
                            String str12 = tempFloor.rooms.elementAt(i).getNumber()+"%"+temp.getType()+"@"+temp.getPower()+"@"+temp.getTypeImage()+"@"+temp.getComments()+"@"+temp.getImage().getX()+"@"+temp.getImage().getY()+"@"+temp.getImage().getScaleX()+"@"+temp.getRotationAngle()+"@"+temp.getLampRoom()+"@"+"used";
                            String str2="@";
                            if (temp.photoPaths.size()!=0){
                                for (String str:temp.photoPaths){
                                    str2+=str;
                                    str2+="!";
                                }
                            }
                            out.println(str12+str2);
                        }
                    }
                    for (int i=0;i<tempFloor.unusedLamps.size();i++){
                            Lamp temp = tempFloor.unusedLamps.elementAt(i);
                            String str12 = "-1"+"%"+temp.getType()+"@"+temp.getPower()+"@"+temp.getTypeImage()+"@"+temp.getComments()+"@"+temp.getImage().getX()+"@"+temp.getImage().getY()+"@"+temp.getImage().getScaleX()+"@"+temp.getRotationAngle()+"@"+temp.getLampRoom()+"@"+"unused";
                             String str2="@";
                            if (temp.photoPaths.size()!=0){
                                for (String str:temp.photoPaths){
                                    str2+=str;
                                    str2+="!";
                                }
                         }
                            out.println(str12+str2);
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
