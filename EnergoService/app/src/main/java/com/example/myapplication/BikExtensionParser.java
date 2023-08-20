package com.example.myapplication;

import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import java.util.Vector;

public class BikExtensionParser {
    boolean buildingInfo = false;     //Флаг начала информации о здании
    boolean lampsInfo = false;      //Флаг начала информации о светильниках
    boolean roomInfo = false;       //Флаг начала информации о комнатах
    boolean floorInfo = false;
    boolean lastThread = false;
    Vector<Thread> threads = new Vector<Thread>();
    File currentFile;

    public void parseFile(String path) throws FileNotFoundException {   //Парсинг файла
        ///Отключение кнопок/////
        Variables.loadingFlag = true;
        threads.clear();
        Variables.setAddFlag(false);
        Variables.plan.disableListenerFromPlan();
        Variables.buttons.addBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.setMoveFlag(false);
        Variables.buttons.moveBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.scalemode = false;
        Variables.buttons.scaleBtn.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.rotateMode = false;
        Variables.buttons.rotateLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.removeMode = false;
        Variables.buttons.removeLamp.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
        Variables.plan.touchedRoom = null;
        Variables.plan.lastRoom = null;
        /////////////////////////////////
        if (Variables.exportingJpg) {       //Отображение текущего этажа если это скриншот плана
            Variables.tempImage = Variables.current_floor.getImage();
        }
        Floor floor = new Floor();          //Создание нового этажа
        floor.fillVector();                 //Заполнение вектора высотой по умолчанию(0.0)
        for (String type : Variables.roofTypes) {
            floor.roofHeightDefault.add("0.0");
        }
        Variables.roofTypeDefaultText.setText("");      //Очищаем поле высоты по умолчанию
        Variables.roomHeightDefaultCheck.setChecked(false);     //Отключение чекбокса высоты по умолчанию
        if (Variables.current_floor != null) {        //Если есть предыдущий этаж - записываем в него текущие позицию и приближение
            Variables.current_floor.cordX = Variables.planLay.getX();
            Variables.current_floor.cordY = Variables.planLay.getY();
            Variables.current_floor.scale = Variables.planLay.getScaleX();
        }
        Variables.planLay.setX(floor.cordX);        //Задаем этажу позицию Х
        Variables.planLay.setY(floor.cordY);        //Задаем этажу позицию У
        Variables.planLay.setScaleX(floor.scale);   //Задаем этажу приближение
        Variables.planLay.setScaleY(floor.scale);   //Задаем этажу приближение
        if (Variables.typeOpening == 0 && Variables.current_floor != null) {        //Если открываем в текущей вкладке - переназначаем текущий этаж
            Variables.floors.set(Variables.floors.indexOf(Variables.current_floor), floor);
        } else {        //Иначе - добавляем новый этаж
            Variables.floors.add(floor);
        }
        currentFile = new File(path);       //Файл для чтения
        BufferedReader reader;
        BufferedWriter writer;
        try {           //Если открываем в текущей вкладке - очищаем все комнаты текущео этажа
            if ((Variables.current_floor != null && Variables.floors.size() > 0 && Variables.typeOpening == 0)) {
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
                } else if (line.equals("///INFORMATION ABOUT ROOMS///")) {                //Если информация о комнатах
                    buildingInfo = false;
                    roomInfo = true;
                } else if (line.equals("///INFORMATION ABOUT LAMPS///")) {             //Если информация о светильниках
                    buildingInfo = false;
                    roomInfo = false;
                    lampsInfo = true;
                } else if (line.equals("///INFORMATION ABOUT FLOOR///")) {             //Если информация о светильниках
                    buildingInfo = false;
                    roomInfo = false;
                    lampsInfo = false;
                    floorInfo = true;
                }
                if (buildingInfo) {      //Если информация о здании
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
                            try {
                                Room room = new Room(roomObj.getString("number"), tempX, tempY);
                                room.buildPoligon();
                                floor.rooms.add(room);
                            } catch (RuntimeException ex) {
                            }
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
                } else if (roomInfo) {         //Если информация о комнатах
                    if (line.length() > 10 && line.charAt(0) != '/') {
                        String[] split_number = line.split("%");
                        if (split_number.length > 1) {
                            String number = split_number[0];        //Номер помещения
                            String[] split_room_info = split_number[1].split("@");
                            if (split_room_info.length > 9) {
                                String height = split_room_info[0];
                                int typeRoom = Integer.parseInt(split_room_info[1]);    //Тип помещения
                                int days = Integer.parseInt(split_room_info[2]);         //Дни в неделю
                                int hours = Integer.parseInt(split_room_info[3]);        //Часы работы в неделю
                                int hoursPerWeekend = Integer.parseInt(split_room_info[4]);     //Часы работы в субботу
                                int hoursPerSunday = Integer.parseInt(split_room_info[5]);      //Часы работы в выходные
                                int roofType = Integer.parseInt(split_room_info[6]);            //Тип крыши
                                String comments;                //Комментарии
                                if (split_room_info.length == 7) {
                                    comments = "";
                                } else {
                                    comments = split_room_info[7];

                                    if (Objects.equals(comments, "null"))
                                        comments = "";
                                }
                                double cordX = Double.parseDouble(split_room_info[8]);          //Первая координата комнаты по X
                                double cordY = Double.parseDouble(split_room_info[9]);          //Первая координата комнаты по Y
                                Room room = Variables.getRoomByNumberAndCoords(number, cordX, cordY, Variables.current_floor);     //Находим нужную комнату
                                if (room != null) {           //Если комната найдена
                                    if (split_room_info.length > 10) {      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                        String paths = split_room_info[10];
                                        String[] split_room_photos = paths.split("!");
                                        for (int i = 0; i < split_room_photos.length; i++) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                if (Files.exists(Paths.get(split_room_photos[i]))) {
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
                                    room.setHoursPerSunday(hoursPerSunday);
                                }
                            }
                        }
                    }
                } else if (lampsInfo) {        //Если информация о светильниках
                    if (line.length() > 20 && line.charAt(0) != '/') {
                        String[] split_number = line.split("%");
                        if (split_number.length > 1) {
                            String number = split_number[0];        //номер комнаты, к которому привязан
                            String[] split_room_info = split_number[1].split("@");
                            if (split_room_info.length > 18) {
                                String type = split_room_info[0];       //Тип светильника
                                String power = split_room_info[1];      //Мощность светильника
                                String type_image = split_room_info[2];     //Тип изображения
                                String comments = split_room_info[3];       //Комментарии
                                if (Objects.equals(comments, "null"))
                                    comments = "";
                                float cordX = Float.parseFloat(split_room_info[4]);     //Координата X
                                float cordY = Float.parseFloat(split_room_info[5]);     //Координата Y
                                float scale = Float.parseFloat(split_room_info[6]);     //Коэффициент размера
                                float rotationAngle = Float.parseFloat(split_room_info[7]);     //Угол поворота
                                String lampRoom = split_room_info[8];       //Комната, к которой привязан светильник
                                String usedOrNot = split_room_info[9];      //Поле используется или нет
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
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(7, 7);
                                imageView.setLayoutParams(params);
                                imageView.setScaleX(scale);
                                imageView.setScaleY(scale);
                                Variables.plan.setListener(imageView);
                                lamp.setImage(imageView);
                                Variables.plan.rotateImg(rotationAngle, imageView, type_image, -1);
                                int montagneType = Integer.parseInt(split_room_info[10]);       //Тип монтажа светильника
                                int placeType = Integer.parseInt(split_room_info[11]);          //Местонахождения светильника
                                int groupIndex = Integer.parseInt(split_room_info[12]);         //Индекс группы светильника
                                int lampsAmount = Integer.parseInt(split_room_info[13]);        //Количество ламп в светильнике(для люстр)
                                int positionOutside = Integer.parseInt(split_room_info[14]);    //Место светильнка снаружи
                                boolean isStolb = Boolean.parseBoolean(split_room_info[15]);    //Находится ли светильник на столбе(для наружного освещения)
                                int typeRoom = Integer.parseInt(split_room_info[16]);
                                int daysOfWork = Integer.parseInt(split_room_info[17]);
                                int hoursOfWork = Integer.parseInt(split_room_info[18]);
                                int hoursOfWorkWeekend = Integer.parseInt(split_room_info[19]);
                                int hoursOfWorkSunday = Integer.parseInt(split_room_info[20]);
                                float piv1 = Float.parseFloat(split_room_info[21]);
                                float piv2 = Float.parseFloat(split_room_info[22]);
                                imageView.setPivotX(piv1);
                                imageView.setPivotY(piv2);
                                imageView.setX(cordX);
                                imageView.setY(cordY);
                                if (split_room_info.length > 23) {      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                    String paths = split_room_info[23];
                                    String[] split_room_photos = paths.split("!");
                                    for (int i = 0; i < split_room_photos.length; i++) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            if (Files.exists(Paths.get(split_room_photos[i]))) {
                                                lamp.photoPaths.add(split_room_photos[i]);
                                            }
                                        }
                                    }
                                    //room.photoPaths.addAll(Arrays.asList(split_room_photos));
                                }
                                lamp.setMontagneType(montagneType);
                                lamp.setPlaceType(placeType);
                                lamp.setGroupIndex(groupIndex);
                                lamp.setLampsAmount(lampsAmount);
                                lamp.setPositionOutside(positionOutside);
                                lamp.setStolb(isStolb);
                                lamp.setTypeRoom(typeRoom);
                                lamp.setDaysWork(daysOfWork);
                                lamp.setHoursWork(hoursOfWork);
                                lamp.setHoursWeekendWork(hoursOfWorkWeekend);
                                lamp.setHoursSundayWork(hoursOfWorkSunday);
                                Runnable myThread = () ->           //После загрузки картинки светильника - проверяем куда его привязать
                                {

                                    lamp.setView();
                                    lamp.getImage().post(new Runnable() {

                                        @Override
                                        public void run() {
                                            //imageView.setX(cordX-imageView.getWidth());
                                            //imageView.setY(cordY-imageView.getHeight());
                                            Room room = null;             //Комната, к которой привязан
                                            if (!Objects.equals(number, "-1")) {            //Если светильник привязан к какой-то комнате
                                                room = Variables.getRoomByNumber(number, cordX + imageView.getWidth(), cordY + imageView.getHeight(), scale, Variables.current_floor);         //Поиск комнтаты к которой привязан светильник
                                            }
                                            if (Objects.equals(lampRoom, "-1") && !Objects.equals(usedOrNot, "used")) {  //Если светильник не привязан никуда, пытаемся привязать по координатам, если не выходит - не привязываем
                                                Room detectedRoom = null;
                                                for (Room temp : Variables.current_floor.rooms) {
                                                    if (temp.detectTouch(cordX + imageView.getWidth(), cordY + imageView.getHeight())) {
                                                        detectedRoom = temp;
                                                    }
                                                }
                                                if (detectedRoom == null) {             //Если комната, куда можно привязать не найдена - добавляем в неиспользуемые
                                                    if (!Variables.ifUnusedContainsLamp(cordX, cordY) && !Variables.ifSomeRoomContainsLamp(cordX, cordY, floor)) {      //Если такой светильник уже есть(произошел баг) - удаляем его, иначе все ок
                                                        Variables.current_floor.unusedLamps.add(lamp);
                                                        imageView.setBackgroundResource(R.color.blue);
                                                    } else {
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                } else {        //Если комната найдена, смотрим чтобы не было в ней такого же светильника - после чего добавляем его
                                                    if (!Variables.ifRoomContainsLamp(cordX, cordY, detectedRoom)) {
                                                        detectedRoom.lamps.add(lamp);
                                                    } else {
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                }
                                            } else {        //Иначе - если комната куда привязать изначально была задана
                                                if (room != null) {     //Если комната такая есть
                                                    if (!Variables.ifRoomContainsLamp(cordX, cordY, room)) {        //Если в ней нет такого светильника
                                                        room.lamps.add(lamp);
                                                    } else {            //Иначе удаляем дублированный светильник
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                } else {        //Иначе - неиспользуемый
                                                    if (!Variables.ifUnusedContainsLamp(cordX, cordY) && !Variables.ifSomeRoomContainsLamp(cordX, cordY, floor)) {
                                                        Variables.current_floor.unusedLamps.add(lamp);
                                                        if (Objects.equals(lampRoom,"-1")) {
                                                            lamp.getImage().setBackgroundResource(R.color.blue);
                                                        }
                                                    } else {        //Если есть дублирующий - удаляем
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                }
                                            }
                                        }
                                    });
                                };

                                // Instantiating Thread class by passing Runnable
                                // reference to Thread constructor
                                //if (!lastThread) {
                                Thread run = new Thread(myThread);
                                threads.add(run);       //Запускаем этот поток для каждого светильника

                                // Starting the thread
                                run.start();
                                //}else{
                                //t.start();
                                //Thread runLast = new Thread(myThread);

                                // Starting the thread
                                //runLast.start();
                                /*try{
                                    runLast.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }*/
                                //t.start();
                                // }
                            }
                        }
                    }
                } else if (floorInfo) {         //Информауия об этаже
                    if (line.length() > 0 && line.charAt(0) != '/') {
                        try {
                            String[] split_floor_info = line.split("@");
                            int type = Integer.parseInt(String.valueOf(split_floor_info[0]));
                            if (type > 3) {
                                type = 0;
                            }
                            floor.setTypeFloor(type);
                            int days = Integer.parseInt(String.valueOf(split_floor_info[1]));
                            if (days > 8) {
                                days = 0;
                            }
                            floor.setHoursWordDefault(days);
                            ///Высота потолков по умолчанию/////
                            floor.roofHeightDefault.set(0, split_floor_info[2]);
                            floor.roofHeightDefault.set(1, split_floor_info[3]);
                            floor.roofHeightDefault.set(3, split_floor_info[4]);
                            floor.roofHeightDefault.set(4, split_floor_info[5]);
                            ///////////////////////////
                            Variables.typeOfBuilding.setSelection(floor.getTypeFloor());        //Тип здания
                            Variables.roomHeight.setText(floor.roofHeightDefault.elementAt(0));     //Высота по умолчанию
                            Variables.daysOfWorkDefault.setSelection(floor.getHoursWordDefault());      //Дни работы
                        } catch (Exception ex) {

                        }
                    }
                }
                line = reader.readLine();
            }
            ///Сброс флагов///
            buildingInfo = false;
            roomInfo = false;
            lampsInfo = false;
            floorInfo = false;
            //////
            if (Variables.exportingJpg) {
                floor.setImage(Variables.tempImage);
                Variables.image.setImageURI(Variables.tempImage);
            } else {
                floor.setImage(Variables.selectedfile);     //Передаем картинку этажа в созданный этаж
            }
            if (Variables.typeOpening == 0) {      //Если открываем в текущей вкладке - меняем вкладку
                if (!Variables.exportingJpg) {
                    if (Buttons.active == null) {      //Если нужно добавить новую вкладку
                        Variables.buttons.addPanel(floor.getFloor());
                    } else {      //Иначе заменяем текущую
                        Variables.FloorPanelsVec.remove(Buttons.active);
                        Variables.floorsPanels.removeView(Buttons.active);
                        Variables.buttons.addPanel(floor.getFloor());
                    }
                }
            } else {              //Если создаем новую вкладку - добавляем новую вкладку
                Variables.buttons.addPanel(floor.getFloor());
            }
            reader.close();
            if (Variables.current_floor != null) {
                for (Room room : Variables.current_floor.rooms) {
                    if (room.getDays() == 0) {
                        room.setDays(Integer.parseInt(String.valueOf(Variables.daysOfWorkDefault.getSelectedItemPosition())));
                    }
                }
            }
            ///Сброс флагов////
            Variables.exportingJpg = false;
            Variables.isExportingToJpg = false;
            Variables.loadingFlag = false;
            ///////////////////
            Variables.setInfoEmpty(floor);
            Variables.planLay.setRotation(0);
            Variables.roofTypeDefaultText.setText(floor.roofHeightDefault.elementAt(Variables.roofTypeDefault.getSelectedItemPosition()));
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile(String pathFile) throws IOException {      //Сохранение файла
        String filename = pathFile;

        File oldFile = new File(pathFile);
        File newFile = new File(pathFile);

        try {       //Находим информацию о комнатах и стираем все после нее
            String str1 = "\n///INFORMATION ABOUT ROOMS///";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte[] b1 = Files.readAllBytes(Paths.get(pathFile));
                byte[] b2 = str1.getBytes();
                long bytesCount = 0;
                long countMatches = 0;
                for (int i = 0; i < b1.length; i++) {
                    if (b1[i] == b2[0]) {
                        if (i + b2.length <= b1.length) {
                            for (int j = 0; j < b2.length; j++) {
                                if (b1[i + j] == b2[j])
                                    countMatches++;
                            }
                        }
                    }
                    if (countMatches == b2.length)
                        break;
                    bytesCount++;
                    countMatches = 0;
                }
                byte[] temp_ar = new byte[(int) bytesCount];
                for (int i = 0; i < temp_ar.length; i++) {
                    temp_ar[i] = b1[i];
                }
                FileOutputStream fos = new FileOutputStream(newFile);

                fos.write(temp_ar);
                fos.close();
                //str1 = Base64.encodeBase64URLSafeString(b);
                try (FileWriter fw = new FileWriter(pathFile, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {       //Начинаем записывать информацию о комнатах
                    Floor tempFloor = Variables.current_floor;
                    out.println();
                    out.println("///INFORMATION ABOUT ROOMS///");
                    for (int i = 0; i < tempFloor.rooms.size(); i++) {
                        String str12 = tempFloor.rooms.elementAt(i).getNumber() + "%" + tempFloor.rooms.elementAt(i).getHeight() + "@" + tempFloor.rooms.elementAt(i).getType_pos() + "@" + tempFloor.rooms.elementAt(i).getDays() + "@" + tempFloor.rooms.elementAt(i).getHoursPerDay() + "@" + tempFloor.rooms.elementAt(i).getHoursPerWeekend() + "@" + tempFloor.rooms.elementAt(i).getHoursPerSunday() + "@" + tempFloor.rooms.elementAt(i).getRoofType() + "@" + tempFloor.rooms.elementAt(i).getComments() + "@" + tempFloor.rooms.elementAt(i).arrayX[0] + "@" + tempFloor.rooms.elementAt(i).arrayY[0];
                        String str2 = "@";
                        if (tempFloor.rooms.elementAt(i).photoPaths.size() != 0) {
                            for (String str : tempFloor.rooms.elementAt(i).photoPaths) {
                                str2 += str;
                                str2 += "!";
                            }
                        }
                        out.println(str12 + str2);

                    }


                    out.println("///INFORMATION ABOUT LAMPS///");   //Начинаем записывать информацию о светильниках
                    for (int i = 0; i < tempFloor.rooms.size(); i++) {
                        for (int j = 0; j < tempFloor.rooms.elementAt(i).lamps.size(); j++) {
                            Lamp temp = tempFloor.rooms.elementAt(i).lamps.elementAt(j);
                            float piv1 = temp.getImage().getPivotX();
                            float piv2 = temp.getImage().getPivotY();
                            //emp.getImage().setPivotX(piv1);
                            //temp.getImage().setPivotY(piv2);
                            //temp.getImage().setX(temp.getImage().getX()-temp.getImage().getWidth());
                            //temp.getImage().setY(temp.getImage().getY()-temp.getImage().getHeight());
                            String str12 = tempFloor.rooms.elementAt(i).getNumber() + "%" + temp.getType() + "@" + temp.getPower() + "@" + temp.getTypeImage() + "@" + temp.getComments() + "@" + (temp.getImage().getX()) + "@" + (temp.getImage().getY()) + "@" + temp.getImage().getScaleX() + "@" + temp.getRotationAngle() + "@" + temp.getLampRoom() + "@" + "used" + "@" + temp.getMontagneType() + "@" + temp.getPlaceType() + "@" + temp.getGroupIndex() + "@" + temp.getLampsAmount() + "@" + temp.getPositionOutside() + "@" + temp.isStolb() + "@" + temp.getTypeRoom() + "@" + temp.getDaysWork() + "@" + temp.getHoursWork() + "@" + temp.getHoursWeekendWork() + "@" + temp.getHoursSundayWork() + "@" + piv1 + "@" + piv2;
                            String str2 = "@";
                            if (temp.photoPaths.size() != 0) {
                                for (String str : temp.photoPaths) {
                                    str2 += str;
                                    str2 += "!";
                                }
                            }
                            out.println(str12 + str2);
                        }
                    }
                    for (int i = 0; i < tempFloor.unusedLamps.size(); i++) {
                        Lamp temp = tempFloor.unusedLamps.elementAt(i);
                        float piv1 = temp.getImage().getPivotX();
                        float piv2 = temp.getImage().getPivotY();
                        piv1 = 0;
                        piv2 = 0;
                        temp.getImage().setPivotX(piv1);
                        temp.getImage().setPivotY(piv2);
                        String str12 = "-1" + "%" + temp.getType() + "@" + temp.getPower() + "@" + temp.getTypeImage() + "@" + temp.getComments() + "@" + temp.getImage().getX() + "@" + temp.getImage().getY() + "@" + temp.getImage().getScaleX() + "@" + temp.getRotationAngle() + "@" + temp.getLampRoom() + "@" + "unused" + "@" + temp.getMontagneType() + "@" + temp.getPlaceType() + "@" + temp.getGroupIndex() + "@" + temp.getLampsAmount() + "@" + temp.getPositionOutside() + "@" + temp.isStolb() + "@" + temp.getTypeRoom() + "@" + temp.getDaysWork() + "@" + temp.getHoursWork() + "@" + temp.getHoursWeekendWork() + "@" + temp.getHoursSundayWork() + "@" + piv1 + "@" + piv2;
                        String str2 = "@";
                        if (temp.photoPaths.size() != 0) {
                            for (String str : temp.photoPaths) {
                                str2 += str;
                                str2 += "!";
                            }
                        }
                        out.println(str12 + str2);
                    }
                    out.println("///INFORMATION ABOUT FLOOR///");   //Начинаем записывать информацию об этаже
                    String str = Variables.current_floor.getTypeFloor() + "@" + Variables.current_floor.getHoursWordDefault() + "@" + Variables.current_floor.roofHeightDefault.elementAt(0) + "@" + Variables.current_floor.roofHeightDefault.elementAt(1) + "@" + Variables.current_floor.roofHeightDefault.elementAt(2) + "@" + Variables.current_floor.roofHeightDefault.elementAt(3);
                    out.println(str);
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

    public void getAllBackUpBackData() {        //Получение данных из последнего бэкапа
        Variables.backupBackVector.clear();     //Очистка вектора с данными бэкапа
        String[] split_path = Variables.filePath.split("/");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split_path.length - 1; i++) {
            builder.append("/");
            builder.append(split_path[i]);
        }
        String folderPath = builder.toString();         //Путь к текущей папке
        BufferedReader reader;
        BufferedWriter writer;
        try {           //Производим чтение
            reader = new BufferedReader(new FileReader(folderPath + "/" + Variables.current_floor.getName() + ";" + Variables.current_floor.getFloor() + "_log.txt"));
            String line = reader.readLine();
            String info = "";
            boolean startedInfo = false;
            while (line != null) {  //Пока файл не закончился считываем строка за строкой
                if (startedInfo) {
                    while (!line.equals("#END#")) {     //Пока не дошли до конца - считываем
                        info+=line;
                        info+="/n";
                        line = reader.readLine();
                    }
                }

                if (line.equals("#START#")) {       //Если старт - считываем информацию из бэкапа
                    startedInfo = true;
                }
                if (line.equals("#END#")){          //Если дошли до конца - сброс флага, читаем файл дальше до следующего старта
                    Variables.backupBackVector.add(info);
                    info = "";
                    startedInfo = false;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void applyLoadBackupBack(String date) throws JSONException, InterruptedException {
        for (int i=0;i<Variables.planLay.getChildCount();i++){
            if (Variables.planLay.getChildAt(i)!=Variables.image){
                Variables.planLay.removeView(Variables.planLay.getChildAt(i));
            }
        }
        Variables.roofTypeDefaultText.setText("");      //Очищаем поле высоты по умолчанию
        Variables.roomHeightDefaultCheck.setChecked(false);     //Отключение чекбокса высоты по умолчанию
        String[] split_backup_data = date.split("/n");
        int countLine=1;
        while (countLine<split_backup_data.length){
                if (split_backup_data[countLine].equals("///INFORMATION ABOUT BUILDING AND ROOMS///")) {        //Если информация о разметке
                    buildingInfo = true;
                } else if (split_backup_data[countLine].equals("///INFORMATION ABOUT ROOMS///")) {                //Если информация о комнатах
                    buildingInfo = false;
                    roomInfo = true;
                } else if (split_backup_data[countLine].equals("///INFORMATION ABOUT LAMPS///")) {             //Если информация о светильниках
                    buildingInfo = false;
                    roomInfo = false;
                    lampsInfo = true;
                } else if (split_backup_data[countLine].equals("///INFORMATION ABOUT FLOOR///")) {             //Если информация о светильниках
                    buildingInfo = false;
                    roomInfo = false;
                    lampsInfo = false;
                    floorInfo = true;
                }
                if (buildingInfo) {      //Если информация о здании
                    if (split_backup_data[countLine].length() > 2 && (split_backup_data[countLine].charAt(0) == 'H' || split_backup_data[countLine].charAt(0) == 'S' || split_backup_data[countLine].charAt(0) == 'R') && split_backup_data[countLine].charAt(1) == '@') {
                        if (split_backup_data[countLine].charAt(0) == 'S') {  //Если это информация о размерах изображения
                            String temp = split_backup_data[countLine].substring(2);
                            String[] subStr = temp.split("/");
                            Variables.lastHeight = Double.parseDouble(subStr[0]);
                            Variables.lastWidth = Double.parseDouble(subStr[1]);
                        } else if (split_backup_data[countLine].charAt(0) == 'R') {     //Иначе это информация о комнатах
                            String temp = split_backup_data[countLine].substring(2);
                            JSONObject roomObj = new JSONObject(temp.substring(temp.indexOf("{"), temp.lastIndexOf("}") + 1));
                            JSONArray arrX = roomObj.getJSONArray("arrayX");
                            JSONArray arrY = roomObj.getJSONArray("arrayY");
                            double[] tempX = new double[arrX.length()];
                            double[] tempY = new double[arrY.length()];
                            Variables.current_floor.resizeCoeffs();
                            for (int i = 0; i < arrX.length(); i++) {       //Изменяем координаты точек разметки в зависимости от размера экрана
                                tempX[i] = arrX.getDouble(i) / Variables.current_floor.resizeCoeffX;
                                tempY[i] = arrY.getDouble(i) / Variables.current_floor.resizeCoeffY;
                            }
                            try {
                                Room room = new Room(roomObj.getString("number"), tempX, tempY);
                                room.buildPoligon();
                                Variables.current_floor.rooms.add(room);
                            } catch (RuntimeException ex) {
                            }
                        } else if (split_backup_data[countLine].charAt(0) == 'H') {     //Информация о зданиии
                            String temp = split_backup_data[countLine].substring(2);
                            String[] subStr = temp.split("@");
                            Variables.current_floor.setName(subStr[0]);
                            Variables.current_floor.setFloor(subStr[1]);
                            Variables.current_floor.setAdress(subStr[2]);
                            EditText buildingName = Variables.activity.findViewById(R.id.buildingName);
                            EditText floor1 = Variables.activity.findViewById(R.id.floorNumber);
                            EditText adress = Variables.activity.findViewById(R.id.adress);
                            buildingName.setText(Variables.current_floor.getName());
                            floor1.setText(Variables.current_floor.getFloor());
                            adress.setText(Variables.current_floor.getAdress());
                        }
                    }
                } else if (roomInfo) {         //Если информация о комнатах
                    if (split_backup_data[countLine].length() > 10 && split_backup_data[countLine].charAt(0) != '/') {
                        String[] split_number = split_backup_data[countLine].split("%");
                        if (split_number.length > 1) {
                            String number = split_number[0];        //Номер помещения
                            String[] split_room_info = split_number[1].split("@");
                            if (split_room_info.length > 9) {
                                String height = split_room_info[0];
                                int typeRoom = Integer.parseInt(split_room_info[1]);    //Тип помещения
                                int days = Integer.parseInt(split_room_info[2]);         //Дни в неделю
                                int hours = Integer.parseInt(split_room_info[3]);        //Часы работы в неделю
                                int hoursPerWeekend = Integer.parseInt(split_room_info[4]);     //Часы работы в субботу
                                int hoursPerSunday = Integer.parseInt(split_room_info[5]);      //Часы работы в выходные
                                int roofType = Integer.parseInt(split_room_info[6]);            //Тип крыши
                                String comments;                //Комментарии
                                if (split_room_info.length == 7) {
                                    comments = "";
                                } else {
                                    comments = split_room_info[7];

                                    if (Objects.equals(comments, "null"))
                                        comments = "";
                                }
                                double cordX = Double.parseDouble(split_room_info[8]);          //Первая координата комнаты по X
                                double cordY = Double.parseDouble(split_room_info[9]);          //Первая координата комнаты по Y
                                Room room = Variables.getRoomByNumberAndCoords(number, cordX, cordY, Variables.current_floor);     //Находим нужную комнату
                                if (room != null) {           //Если комната найдена
                                    if (split_room_info.length > 10) {      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                        String paths = split_room_info[10];
                                        String[] split_room_photos = paths.split("!");
                                        for (int i = 0; i < split_room_photos.length; i++) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                if (Files.exists(Paths.get(split_room_photos[i]))) {
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
                                    room.setHoursPerSunday(hoursPerSunday);
                                }
                            }
                        }
                    }
                } else if (lampsInfo) {        //Если информация о светильниках
                    if (split_backup_data[countLine].length() > 20 && split_backup_data[countLine].charAt(0) != '/') {
                        String[] split_number = split_backup_data[countLine].split("%");
                        if (split_number.length > 1) {
                            String number = split_number[0];        //номер комнаты, к которому привязан
                            String[] split_room_info = split_number[1].split("@");
                            if (split_room_info.length > 18) {
                                String type = split_room_info[0];       //Тип светильника
                                String power = split_room_info[1];      //Мощность светильника
                                String type_image = split_room_info[2];     //Тип изображения
                                String comments = split_room_info[3];       //Комментарии
                                if (Objects.equals(comments, "null"))
                                    comments = "";
                                float cordX = Float.parseFloat(split_room_info[4]);     //Координата X
                                float cordY = Float.parseFloat(split_room_info[5]);     //Координата Y
                                float scale = Float.parseFloat(split_room_info[6]);     //Коэффициент размера
                                float rotationAngle = Float.parseFloat(split_room_info[7]);     //Угол поворота
                                String lampRoom = split_room_info[8];       //Комната, к которой привязан светильник
                                String usedOrNot = split_room_info[9];      //Поле используется или нет
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
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(7, 7);
                                imageView.setLayoutParams(params);
                                imageView.setScaleX(scale);
                                imageView.setScaleY(scale);
                                Variables.plan.setListener(imageView);
                                lamp.setImage(imageView);
                                Variables.plan.rotateImg(rotationAngle, imageView, type_image, -1);
                                int montagneType = Integer.parseInt(split_room_info[10]);       //Тип монтажа светильника
                                int placeType = Integer.parseInt(split_room_info[11]);          //Местонахождения светильника
                                int groupIndex = Integer.parseInt(split_room_info[12]);         //Индекс группы светильника
                                int lampsAmount = Integer.parseInt(split_room_info[13]);        //Количество ламп в светильнике(для люстр)
                                int positionOutside = Integer.parseInt(split_room_info[14]);    //Место светильнка снаружи
                                boolean isStolb = Boolean.parseBoolean(split_room_info[15]);    //Находится ли светильник на столбе(для наружного освещения)
                                int typeRoom = Integer.parseInt(split_room_info[16]);
                                int daysOfWork = Integer.parseInt(split_room_info[17]);
                                int hoursOfWork = Integer.parseInt(split_room_info[18]);
                                int hoursOfWorkWeekend = Integer.parseInt(split_room_info[19]);
                                int hoursOfWorkSunday = Integer.parseInt(split_room_info[20]);
                                float piv1 = Float.parseFloat(split_room_info[21]);
                                float piv2 = Float.parseFloat(split_room_info[22]);
                                imageView.setPivotX(piv1);
                                imageView.setPivotY(piv2);
                                imageView.setX(cordX);
                                imageView.setY(cordY);
                                if (split_room_info.length > 23) {      //Если есть пути к фотографиям и сами файлы существуют - добавляем
                                    String paths = split_room_info[23];
                                    String[] split_room_photos = paths.split("!");
                                    for (int i = 0; i < split_room_photos.length; i++) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            if (Files.exists(Paths.get(split_room_photos[i]))) {
                                                lamp.photoPaths.add(split_room_photos[i]);
                                            }
                                        }
                                    }
                                    //room.photoPaths.addAll(Arrays.asList(split_room_photos));
                                }
                                lamp.setMontagneType(montagneType);
                                lamp.setPlaceType(placeType);
                                lamp.setGroupIndex(groupIndex);
                                lamp.setLampsAmount(lampsAmount);
                                lamp.setPositionOutside(positionOutside);
                                lamp.setStolb(isStolb);
                                lamp.setTypeRoom(typeRoom);
                                lamp.setDaysWork(daysOfWork);
                                lamp.setHoursWork(hoursOfWork);
                                lamp.setHoursWeekendWork(hoursOfWorkWeekend);
                                lamp.setHoursSundayWork(hoursOfWorkSunday);
                                Runnable myThread = () ->           //После загрузки картинки светильника - проверяем куда его привязать
                                {

                                    lamp.setView();
                                    lamp.getImage().post(new Runnable() {

                                        @Override
                                        public void run() {
                                            //imageView.setX(cordX-imageView.getWidth());
                                            //imageView.setY(cordY-imageView.getHeight());
                                            Room room = null;             //Комната, к которой привязан
                                            if (!Objects.equals(number, "-1")) {            //Если светильник привязан к какой-то комнате
                                                room = Variables.getRoomByNumber(number, cordX + imageView.getWidth(), cordY + imageView.getHeight(), scale, Variables.current_floor);         //Поиск комнтаты к которой привязан светильник
                                            }
                                            if (Objects.equals(lampRoom, "-1") && !Objects.equals(usedOrNot, "used")) {  //Если светильник не привязан никуда, пытаемся привязать по координатам, если не выходит - не привязываем
                                                Room detectedRoom = null;
                                                for (Room temp : Variables.current_floor.rooms) {
                                                    if (temp.detectTouch(cordX + imageView.getWidth(), cordY + imageView.getHeight())) {
                                                        detectedRoom = temp;
                                                    }
                                                }
                                                if (detectedRoom == null) {             //Если комната, куда можно привязать не найдена - добавляем в неиспользуемые
                                                    if (!Variables.ifUnusedContainsLamp(cordX, cordY) && !Variables.ifSomeRoomContainsLamp(cordX, cordY, Variables.current_floor)) {      //Если такой светильник уже есть(произошел баг) - удаляем его, иначе все ок
                                                        Variables.current_floor.unusedLamps.add(lamp);
                                                        imageView.setBackgroundResource(R.color.blue);
                                                    } else {
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                } else {        //Если комната найдена, смотрим чтобы не было в ней такого же светильника - после чего добавляем его
                                                    if (!Variables.ifRoomContainsLamp(cordX, cordY, detectedRoom)) {
                                                        detectedRoom.lamps.add(lamp);
                                                    } else {
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                }
                                            } else {        //Иначе - если комната куда привязать изначально была задана
                                                if (room != null) {     //Если комната такая есть
                                                    if (!Variables.ifRoomContainsLamp(cordX, cordY, room)) {        //Если в ней нет такого светильника
                                                        room.lamps.add(lamp);
                                                    } else {            //Иначе удаляем дублированный светильник
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                } else {        //Иначе - неиспользуемый
                                                    if (!Variables.ifUnusedContainsLamp(cordX, cordY) && !Variables.ifSomeRoomContainsLamp(cordX, cordY, Variables.current_floor)) {
                                                        Variables.current_floor.unusedLamps.add(lamp);
                                                        if (Objects.equals(lampRoom,"-1")) {
                                                            lamp.getImage().setBackgroundResource(R.color.blue);
                                                        }
                                                    } else {        //Если есть дублирующий - удаляем
                                                        Variables.planLay.removeView(lamp.getImage());
                                                    }
                                                }
                                            }
                                        }
                                    });
                                };

                                // Instantiating Thread class by passing Runnable
                                // reference to Thread constructor
                                //if (!lastThread) {
                                Thread run = new Thread(myThread);
                                threads.add(run);       //Запускаем этот поток для каждого светильника

                                // Starting the thread
                                run.start();
                                //}else{
                                //t.start();
                                //Thread runLast = new Thread(myThread);

                                // Starting the thread
                                //runLast.start();
                                /*try{
                                    runLast.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }*/
                                //t.start();
                                // }
                            }
                        }
                    }
                } else if (floorInfo) {         //Информауия об этаже
                    if (split_backup_data[countLine].length() > 0 && split_backup_data[countLine].charAt(0) != '/') {
                        try {
                            String[] split_floor_info = split_backup_data[countLine].split("@");
                            int type = Integer.parseInt(String.valueOf(split_floor_info[0]));
                            if (type > 3) {
                                type = 0;
                            }
                            Variables.current_floor.setTypeFloor(type);
                            int days = Integer.parseInt(String.valueOf(split_floor_info[1]));
                            if (days > 8) {
                                days = 0;
                            }
                            Variables.current_floor.setHoursWordDefault(days);
                            ///Высота потолков по умолчанию/////
                            Variables.current_floor.roofHeightDefault.set(0, split_floor_info[2]);
                            Variables.current_floor.roofHeightDefault.set(1, split_floor_info[3]);
                            Variables.current_floor.roofHeightDefault.set(3, split_floor_info[4]);
                            Variables.current_floor.roofHeightDefault.set(4, split_floor_info[5]);
                            ///////////////////////////
                            Variables.typeOfBuilding.setSelection(Variables.current_floor.getTypeFloor());        //Тип здания
                            Variables.roomHeight.setText(Variables.current_floor.roofHeightDefault.elementAt(0));     //Высота по умолчанию
                            Variables.daysOfWorkDefault.setSelection(Variables.current_floor.getHoursWordDefault());      //Дни работы
                        } catch (Exception ex) {

                        }
                    }
                }
                countLine++;
            }
            ///Сброс флагов///
            buildingInfo = false;
            roomInfo = false;
            lampsInfo = false;
            floorInfo = false;
            //////
            if (Variables.current_floor != null) {
                for (Room room : Variables.current_floor.rooms) {
                    if (room.getDays() == 0) {
                        room.setDays(Integer.parseInt(String.valueOf(Variables.daysOfWorkDefault.getSelectedItemPosition())));
                    }
                }
            }
            ///Сброс флагов////
            Variables.exportingJpg = false;
            Variables.isExportingToJpg = false;
            Variables.loadingFlag = false;
            ///////////////////
            Variables.setInfoEmpty(Variables.current_floor);
            Variables.planLay.setRotation(0);
            Variables.roofTypeDefaultText.setText(Variables.current_floor.roofHeightDefault.elementAt(Variables.roofTypeDefault.getSelectedItemPosition()));
            for (Thread thread : threads) {
                try {
                    thread.join();
                }catch (Exception ex){

                }
            }
    }
}
