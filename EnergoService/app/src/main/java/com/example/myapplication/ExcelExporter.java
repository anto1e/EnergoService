package com.example.myapplication;

import android.os.Environment;


import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.ChartCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class ExcelExporter {

    String path = String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
    Workbook workbook;
    WorksheetCollection worksheets;
    Worksheet sheet;
    Cells cells;
    int rowCount = 4;
    public void init() throws Exception {       //Инициализация Эксель файла
        rowCount=4;
        workbook = new Workbook(path + "/bdr.xlsx");
        worksheets = workbook.getWorksheets();
        sheet = worksheets.get(1);
        cells = sheet.getCells();
    }
    public void exportToExel() throws Exception {           //Функция экспорта в Эксель(дорабатывается)
        init();
        /*for (int i = 0; i < Variables.building.rooms.size(); i++) {
            int count=0;
            Vector<String> types = new Vector<String>();
            Room room = Variables.building.rooms.elementAt(i);
            Vector<Lamp> lamps = room.getLamps();
            for (int j = 0; j < lamps.size(); j++) {
                if (!types.contains(lamps.elementAt(j).getType() + " " + lamps.elementAt(j).getPower())){
                    types.add(lamps.elementAt(j).getType() + " " + lamps.elementAt(j).getPower());
                }
            }
            for (int j=0;j<types.size();j++){
                for (int z=0;z<lamps.size();z++){
                    if (Objects.equals(types.elementAt(j), lamps.elementAt(z).getType() + " " + lamps.elementAt(z).getPower())){
                        if (lamps.elementAt(z).getComments()==null){
                            count++;
                        }else{
                            writeToFile(room,types.elementAt(j),1,lamps.elementAt(z).getComments());
                        }
                    }
                }
                if (count>0) {
                    writeToFile(room, types.elementAt(j), count, "");
                }
                count=0;
            }
        }*/
        /*for (int i = 0; i < Variables.building.rooms.size(); i++) {
            Vector<String> types = new Vector<String>();
            int count=0;
            Room room = Variables.building.rooms.elementAt(i);
            Vector<Lamp> lamps = room.getLamps();
            for (int j=0;j<lamps.size();j++){
                if (!types.contains(lamps.elementAt(j).getType()+"/"+lamps.elementAt(j).getPower()+"/"+lamps.elementAt(j).getComments())){
                    count++;
                    for (int z=1;z<lamps.size()-1;z++){
                        if ((lamps.elementAt(z).getType() + "/" + lamps.elementAt(z).getPower() + "/" + lamps.elementAt(z).getComments()).equals(lamps.elementAt(j).getType() + "/" + lamps.elementAt(j).getPower() + "/" + lamps.elementAt(j).getComments())){
                            count++;
                        }
                    }
                    types.add(lamps.elementAt(j).getType()+"/"+lamps.elementAt(j).getPower()+"/"+lamps.elementAt(j).getComments());
                    if (count>0){
                        writeToFile(room, lamps.elementAt(j).getType()+" "+lamps.elementAt(j).getPower(),count,lamps.elementAt(j).getComments());
                    }
                    count=0;
                }
            }
        }*/
        for (int i = 0; i < Variables.current_floor.rooms.size(); i++) {
            int count=0;
            Vector<String> types = new Vector<String>();
            Room room = Variables.current_floor.rooms.elementAt(i);
            Vector<Lamp> lamps = room.getLamps();
            for (int j = 0; j < lamps.size(); j++) {        //Типы светильников
                if (!types.contains(lamps.elementAt(j).getType() + " " + lamps.elementAt(j).getPower()+" "+ lamps.elementAt(j).getComments())){
                    types.add(lamps.elementAt(j).getType() + " " + lamps.elementAt(j).getPower()+" "+ lamps.elementAt(j).getComments());
                }
            }
            for (int j=0;j<types.size();j++){       //Находим и считаем светильники, чьи типы есть в Векторе
                String type="";
                String comm="";
                for (int z=0;z<lamps.size();z++){
                    if (Objects.equals(types.elementAt(j), lamps.elementAt(z).getType() + " " + lamps.elementAt(z).getPower()+" "+ lamps.elementAt(z).getComments())){
                        //if (lamps.elementAt(z).getComments()==null){
                            count++;
                            type = lamps.elementAt(z).getType()+" "+lamps.elementAt(z).getPower();
                            comm=lamps.elementAt(z).getComments();
                        //}else{
                            //writeToFile(room,types.elementAt(j),1,lamps.elementAt(z).getComments());
                        //}
                    }
                }if (count>0) {
                    writeToFile(room, type, count, comm);       //Запись данных в файл
                }
                count=0;
            }
        }
        save();
    }

    public void writeToFile(Room room,String type, int amount,String comments) throws Exception {       //Запись в файл по ячейкам

// Obtaining the reference of the first worksheet
// Adding some sample value to cells
        Cell cell = cells.get("A"+Integer.toString(rowCount));
        cell.setValue(Variables.current_floor.getFloor());
        cell = cells.get("B"+Integer.toString(rowCount));
        cell.setValue(room.getNumber());
        cell = cells.get("C"+Integer.toString(rowCount));
        cell.setValue(room.getHeight());
        cell = cells.get("D"+Integer.toString(rowCount));
        cell.setValue(Variables.current_floor.getAdress());
        cell = cells.get("F"+Integer.toString(rowCount));
        cell.setValue(Variables.type.getItemAtPosition(room.getType_pos()));
        cell = cells.get("G"+Integer.toString(rowCount));
        cell.setValue(Integer.valueOf((String) Variables.daysPerWeek.getItemAtPosition(room.getDays())));
        cell = cells.get("H"+Integer.toString(rowCount));
        cell.setValue(Float.valueOf((String) Variables.hoursPerDay.getItemAtPosition(room.getHoursPerDay())));
        cell = cells.get("I"+Integer.toString(rowCount));
        cell.setValue(Float.valueOf((String) Variables.hoursPerWeekend.getItemAtPosition(room.getHoursPerWeekend())));
        cell = cells.get("J"+Integer.toString(rowCount));
        cell.setValue(Float.valueOf((String) Variables.hoursPerWeekend.getItemAtPosition(room.getHoursPerWeekend())));
        cell = cells.get("K"+Integer.toString(rowCount));
        cell.setValue(type);
        cell = cells.get("M"+Integer.toString(rowCount));
        cell.setValue(amount);
        cell = cells.get("P"+Integer.toString(rowCount));
        cell.setValue(Variables.roofType.getItemAtPosition(room.getRoofType()));
        cell = cells.get("Q"+Integer.toString(rowCount));
        cell.setValue(comments);
        cell = cells.get("L"+Integer.toString(rowCount));
        String value = cell.getFormula();
        cell.setFormula(value);
        cell = cells.get("R"+Integer.toString(rowCount));
        value = cell.getFormula();
        cell.setFormula(value);
        cell = cells.get("AA"+Integer.toString(rowCount));
        value = cell.getFormula();
        cell.setFormula(value);
        cell = cells.get("AD"+Integer.toString(rowCount));
        value = cell.getFormula();
        cell.setFormula(value);
        cell = cells.get("Z"+Integer.toString(rowCount));
        value = cell.getFormula();
        cell.setFormula(value);
        rowCount++;

// Write the Excel file

    }
    public void save() throws Exception {       //Сохранение в новый файл(Aspose Cells)
        workbook.save(path + "/"+Variables.current_floor.getName()+".xlsx");
        //Пересохранение файла, с удалением страницы о пробной лицензии(Apache POI Excel)
        FileInputStream inputStream = new FileInputStream(new File(path + "/"+Variables.current_floor.getName()+".xlsx"));
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);

//Delete Sheet
        workBook.removeSheetAt(7);

//Save the file
        FileOutputStream outFile = new FileOutputStream(new File(path + "/"+Variables.current_floor.getName()+".xlsx"));
        workBook.write(outFile);
        outFile.close();
    }
}
