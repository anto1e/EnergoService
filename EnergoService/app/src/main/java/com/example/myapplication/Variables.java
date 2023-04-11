package com.example.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

//Класс для хранения глобальных переменных
public class Variables {
    static boolean isExpotedExcel=true;     //Флаг экспорта в эксель
    static ListView listView=null;          //Список светильников
    static Vector<Lamp> copyBuffer = new Vector<Lamp>();        //Буффер копирования

    static int typeOpening=0;           //Тип открытия нового файла
    static boolean selectZoneFlag=false;        //Флаг выбора зона
    static Vector<Lamp> copyVector = new Vector<Lamp>();        //Временный вектор
    static Vector<Float> distX = new Vector<Float>();           //Вектор расстояний от выбранного светильника по Х
    static Vector<Float> distY = new Vector<Float>();           //Вектор расстояний от выбранного светильника по У
    static Lamp tempCopiedLamp;         //Временный скопированный светильник
    static Lamp tempCopiedBufLamp;      //Временный скопированный светильник в буфер
    static Vector<Float> distBufX = new Vector<Float>();            //Вектор расстояний от сохраненного светильника в буффер по Х
    static Vector<Float> distBufY = new Vector<Float>();            //Вектор расстояний от сохраненного светильника в буффер по У
    static Vector<Float> lastMovePosX = new Vector<Float>();            //Вектор предыдущих позиций выбранных светильников по Х
    static Vector<Float> lastMovePosY = new Vector<Float>();            //Вектор предыдущих позиций выбранных светильников по У
    static int copyType=0;                  //Тип копирования светильников
    static boolean confirmBtnActive=false;      //Флаг активации кнопки подтверждения
    static boolean moveOnlySelectedZone=false;      //Флаг активации перемещения выбранной зоны
    static boolean cancelBtnActive=false;           //Флаг активации кнопки отмены
    static boolean copyFlag=false;                  //Флаг активации функции копирования
    static boolean planLayCleared=false;            //Флаг очистки плана
    static Vector<LinearLayout> FloorPanelsVec = new Vector<LinearLayout>();        //Вектор вкладок на экране
    static ExcelExporter exporter;                  //Экспортер данных в эксель
    static RelativeLayout roomInfoView;             //Панель инфрмации о комнате

    static RelativeLayout buildingInfoView;             //Панель инфрмации о здании
    static boolean fileSaved=true;                      //Флаг сохранен ли файл
    static RelativeLayout lampInfoView;             //Панель инфрмации о светильнике
    static EditText buildingName;             //Поле наименования здания
    static EditText buidlingFloor;             //Поле номера этажа
    static RelativeLayout multipleRowsInfo;     //Поле выбора данных для создания множества светильников по рядам и столбцам
    static boolean scalemode = false;           //Флаг активации режима изменения размера
    static boolean rotateMode=false;            //Флаг активации режима поворота
    static boolean removeMode=false;            //Флаг активации режима удаления
    static boolean addMultipleRowsFlag=false;       //Флаг активации добавления множества светильников по рядам и столбцам
    static boolean firstTouch=false;                //Флаг определения первого нажатия для создания зоны выделения
    static float firstPointX=0;                     //Позиция по Х первого нажатия
    static float firstPointY=0;                     //Позиция по У первого нажатия
    static boolean disableMovingPlan=false;         //Флаг выключения функции перемещения по плану

    static float lastScaletype=1.5f;            //Значение последнего значения масштабирования
    static EditText buildingAdress;             //Поле адреса здания
    static String filePath="";             //Путь к текущему открытому файлу
    static Uri selectedfile;             //Выбранный текущий файл
    static Activity activity=null;          //Главное activity
    static boolean opened = false;          //Если файл был открыт
    static BikExtensionParser parser = new BikExtensionParser();        //Парсер данных из .bik
    private static boolean addFlag=false;       //Флаг активации режима добавления светильника
    static boolean addMultiple_flag=false;       //Флаг активации режима добавления светильника
    static Integer multipleType=-1;             //Тип светильника
    static int multiplepos=-1;                  //Позиция в массиве светильнков
    static int multiplelampType=-1;             //Тип ламп
    private static boolean moveFlag=false;      //Флаг активации режима перемещния светильника
    static ImageView loadingImage;              //Колесо вращения при сохранении
    static LinearLayout floorsPanels;           //Layout для хранения вкладок
    static Plan plan = new Plan();          //План этажа
    static Spinner spinRows;                //Спиннер столбцов(создание множества светильников по рядам и столбцам)
    static Spinner spinLines;               //Спиннер строк(создание множества светильников по рядам и столбцам)
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText lampRoom;                   //Поле информации о привязке светильника к комнате
    static GridLayout roomGrid;                 //Grid layout для отображения фотографий комнаты
    static EditText roomNumber=null;            //Поле для номера помещения
    static EditText roomHeight;            //Поле для высоты помещения
    static Spinner daysPerWeek;            //Поле для дней работы помещения
    static Spinner hoursPerDay;            //Поле для часов работы в день помещения
    static Spinner hoursPerWeekend;            //Поле для часов работы в выходные помещения
    static Spinner roofType;                //Поле типа потолка

    static Buttons buttons = new Buttons();        //Создание класса с кнопками
    static Spinner type;            //Выпадающий список(спинер) с типами помещений
    static RelativeLayout RoomInfo;            //Макет, где хранится информация о помещении
    static EditText lampType;                  //Поле типа светильника
    static EditText lampPower;                  //Поле мощности светильника
    static EditText lampComments;              //Поле комментариев к светильнику

    static EditText roomComments;               //Поле комментариев к комнате

    static Vector<Floor> floors= new Vector<Floor>();       //Вектор, хранящий открытые этаж

    static Floor current_floor=null;            //Текущий открытый этаж

    static double lastWidth;                //Ширина плана, при разметке на сайте
    static double lastHeight;                //Высота плана, при разметке на сайте
    static double currentWidth;                //Ширина плана в приложении
    static double currentHeight;                //Высота плана в приложении
    public static final String[] lampNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт"
    };

    static String[] roofTypes = {"Бетон","Армстронг","ПВХ"};        //Типы потолков
    static String[] typesOfRooms = { "Игровая", "Спальная", "Санузел", "Коридор", "Тамбур","Лестница","Кабинет","Пищеблок"};            //Типы помещений
    static String[] daysPerWeekArr = {"0","1","2","3","4","5","6","7"};         //Дней работы в неделю
    static String[] hoursPerDayArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по будням
    static String[] hoursPerWeekendArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по выходным

    static String[] spinRowsArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в столбцах
    static String[] spinLinesArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в рядах



    public static void init(){                //Инициализация переменных
        roomGrid = activity.findViewById(R.id.roomGrid);
        multipleRowsInfo = activity.findViewById(R.id.multipleRowsInfo);
        lampRoom = activity.findViewById(R.id.lampRoom);
        floorsPanels = activity.findViewById(R.id.floorPanelsLay);
        loadingImage = activity.findViewById(R.id.LoadingImage);
        loadingImage.bringToFront();
        roomInfoView = activity.findViewById(R.id.RoomInfoView);
        buildingInfoView = activity.findViewById(R.id.BuildingInfoView);
        lampInfoView = activity.findViewById(R.id.lampInfoView);
        lampType = activity.findViewById(R.id.lampType);
        roomComments = activity.findViewById(R.id.roomComments);
        lampPower = activity.findViewById(R.id.lampPower);
        lampComments = activity.findViewById(R.id.lampComments);
        image = activity.findViewById(R.id.imageView);
        roomNumber = activity.findViewById(R.id.roomNumber);
        type = activity.findViewById(R.id.spinTypes);
        roomHeight = activity.findViewById(R.id.roomHigh);
        daysPerWeek = activity.findViewById(R.id.roomDays);
        hoursPerDay = activity.findViewById(R.id.roomHours);
        hoursPerWeekend = activity.findViewById(R.id.roomHoursWeekends);
        roofType = activity.findViewById(R.id.roofType);
        RoomInfo = activity.findViewById(R.id.RoomInfoView);
        buildingName = activity.findViewById(R.id.buildingName);
        buidlingFloor = activity.findViewById(R.id.floorNumber);
        buildingAdress = activity.findViewById(R.id.adress);
        spinRows=activity.findViewById(R.id.spinColumns);
        spinLines=activity.findViewById(R.id.spinRows);
        setSpinners();
        exporter = new ExcelExporter();
    }

    public static void resetListColor(){            //Сброс цвето в списке светильников
        if (Variables.listView!=null) {
            int count = Variables.listView.getCount();
            for (int i = 0; i < count; i++) {
                ViewGroup row = (ViewGroup) Variables.listView.getChildAt(i);
                row.setBackgroundResource(0);
                //  Get your controls from this ViewGroup and perform your task on them =)

            }
        }
    }
    public static void setSpinners(){       //Инициализация спиннеров
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, typesOfRooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,daysPerWeekArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysPerWeek.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerWeekend.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerDayArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerDay.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,roofTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roofType.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,spinRowsArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRows.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,spinLinesArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLines.setAdapter(adapter);
    }

    public static void clearFields(){           //Очистка полей при переключении плана
        roomInfoView.setVisibility(View.GONE);
        lampType.setText("");
        lampPower.setText("");
        lampComments.setText("");
    }



    //Геттеры, сеттеры, инверторы флагов
    public static boolean getAddFlag(){
        return addFlag;
    }
    public static void setAddFlag(boolean addFlagNew){
        addFlag = addFlagNew;
    }
    public static void invertAddFlag(){
        if (addFlag)
            addFlag=false;
        else
            addFlag=true;
    }
    public static boolean getMoveFlag(){
        return moveFlag;
    }
    public static void setMoveFlag(boolean moveFlagNew){
        moveFlag = moveFlagNew;
    }
    public static void invertMoveFlag(){
        if (moveFlag)
            moveFlag=false;
        else
            moveFlag=true;
    }
    public static Room getRoomByNumber(String number){
        for (int i=0;i<current_floor.rooms.size();i++){
            if (Objects.equals(current_floor.rooms.elementAt(i).getNumber(), number))
                return current_floor.rooms.elementAt(i);
        }
        return null;
    }
    public static void moveCopiedVector(float cordX,float cordY){
        tempCopiedLamp.getImage().setX(cordX);
        tempCopiedLamp.getImage().setY(cordY);
        for (int i=0;i<copyVector.size();i++){
            if (copyVector.elementAt(i)!=tempCopiedLamp){
                copyVector.elementAt(i).getImage().setX(cordX+distX.elementAt(i));
                copyVector.elementAt(i).getImage().setY(cordY+distY.elementAt(i));
            }
        }
    }
    public static void moveCopiedBufVector(float cordX,float cordY){
        tempCopiedBufLamp.getImage().setX(cordX);
        tempCopiedBufLamp.getImage().setY(cordY);
        for (int i=0;i<copyBuffer.size();i++){
            if (copyBuffer.elementAt(i)!=tempCopiedBufLamp){
                copyBuffer.elementAt(i).getImage().setX(cordX+distBufX.elementAt(i));
                copyBuffer.elementAt(i).getImage().setY(cordY+distBufY.elementAt(i));
            }
        }
    }
    public static void resetCordsCopiedVector(){
        for (int i=0;i<copyVector.size();i++){
            copyVector.elementAt(i).getImage().setX(lastMovePosX.elementAt(i));
            copyVector.elementAt(i).getImage().setY(lastMovePosY.elementAt(i));
        }
        lastMovePosX.clear();
        lastMovePosY.clear();
    }
    public static void showAllPhotos(Room room){
        for (String str:room.photoPaths){
            File f = new File(str);
            Buttons.createNewPhotoRoom(f);
        }
    }
    public static void refreshLampsToRooms(Floor floor){
        for (Room room: floor.rooms){
            Vector<Lamp> tempVec= new Vector<Lamp>(room.lamps);
            for (Lamp lamp: tempVec){
                Room temp = getRoomByNumber(lamp.getLampRoom());
                if (temp!=null && !temp.lamps.contains(lamp)){
                    temp.lamps.add(lamp);
                    if (current_floor.unusedLamps.contains(lamp)){
                        current_floor.unusedLamps.remove(lamp);
                    }
                }
            }
        }
        Vector<Lamp> tempVec = new Vector<Lamp>(floor.unusedLamps);
         for (Lamp lamp: tempVec){
            Room temp = getRoomByNumber(lamp.getLampRoom());
            if (temp!=null && !temp.lamps.contains(lamp)){
                temp.lamps.add(lamp);
                if (current_floor.unusedLamps.contains(lamp)){
                    current_floor.unusedLamps.remove(lamp);
                }
            }
        }
    }
}
