package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class loggingThread extends Thread {
    @SuppressLint("SimpleDateFormat")
    public void run() {
        try {
                    //Получаем путь к текущей папке
                    String[] split_path = Variables.filePath.split("/");
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < split_path.length - 1; i++) {
                        builder.append("/");
                        builder.append(split_path[i]);
                    }
                    String folderPath = builder.toString();
                    File file = new File(folderPath + "/" + Variables.current_floor.getName() + ";" + Variables.current_floor.getFloor() + "_log.txt");

// Get length of file in bytes
                    //Размер файла в мегабайтах
                    long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

            if (fileSizeInMB > 10) {    //Если больше 10Мб файл - пересоздаем
                file.delete();
                file.createNewFile();
            }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    //Производим запись в файл бэкапа
                    try (FileWriter fw = new FileWriter(folderPath + "/" + Variables.current_floor.getName() + ";" + Variables.current_floor.getFloor() + "_log.txt", true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.println();
                        out.println();
                        out.println("#START#");
                        out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
                                .getInstance().getTime()));
                        Floor tempFloor = Variables.current_floor;
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
                        out.println("#END#");
                        //more code
                    } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }
        } catch (Exception e) {

        }
    }
}
