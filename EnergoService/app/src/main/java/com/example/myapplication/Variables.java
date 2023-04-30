package com.example.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    static Spinner hoursPerSunday;
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
    static Spinner montagneType;
    static boolean copyFlag=false;                  //Флаг активации функции копирования
    static boolean planLayCleared=false;            //Флаг очистки плана
    static Vector<LinearLayout> FloorPanelsVec = new Vector<LinearLayout>();        //Вектор вкладок на экране
    static LinearLayout floorPanelLay;
    static ExcelExporter exporter;                  //Экспортер данных в эксель
    static RelativeLayout roomInfoView;             //Панель инфрмации о комнате
    static Spinner typeOfBuilding;
    static Spinner daysOfWorkDefault;

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
    static ImageView photoImage;       //Поле для отображения фотографии
    static int currentLampsPanelIndex = 0;
    static boolean opened = false;          //Если файл был открыт
    static LampsList lampsList=null;
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
    static Spinner roofTypeDefault;
    static EditText roofTypeDefaultText;
    static Vector<VerticalTextView> lampsPanels = new Vector<VerticalTextView>();
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText lampRoom;                   //Поле информации о привязке светильника к комнате
    static GridLayout roomGrid;                 //Grid layout для отображения фотографий комнаты
    static GridLayout lampGrid;
    static EditText roomNumber=null;            //Поле для номера помещения
    static EditText roomHeight;            //Поле для высоты помещения
    static Spinner daysPerWeek;            //Поле для дней работы помещения
    static boolean showPhotoFlag=false;
    static boolean showPhotoLampFlag=false;
    static CheckBox roomHeightDefaultCheck;
    static boolean takePhotoFlag=false;
    static TextView montagneOutsideTypeTxt;
    static Spinner montagneOutsideType;
    static TextView positionOutsideTxt;
    static Spinner positionOutside;
    static TextView isStolbTxt;
    static CheckBox isStolbCheck;
    static Spinner hoursPerDay;            //Поле для часов работы в день помещения
    static Spinner hoursPerWeekend;            //Поле для часов работы в выходные помещения
    static Spinner roofType;                //Поле типа потолка

    static Buttons buttons = new Buttons();        //Создание класса с кнопками
    static Spinner type;            //Выпадающий список(спинер) с типами помещений
    static RelativeLayout RoomInfo;            //Макет, где хранится информация о помещении
    static EditText lampType;                  //Поле типа светильника
    static EditText lampPower;                  //Поле мощности светильника
    static EditText lampComments;              //Поле комментариев к светильнику
    static FrameLayout photoFrame;              //Layout для отображения выбранной фотографии
    static TextView lampAmountText;
    static EditText lampAmountEdit;
    static Spinner placeType;
    static TextView montagneTypeTxt;

    static EditText roomComments;               //Поле комментариев к комнате
    static int indexOfPhoto=-1;

    static Vector<Floor> floors= new Vector<Floor>();       //Вектор, хранящий открытые этаж

    static Floor current_floor=null;            //Текущий открытый этаж

    static double lastWidth;                //Ширина плана, при разметке на сайте
    static double lastHeight;                //Высота плана, при разметке на сайте
    static double currentWidth;                //Ширина плана в приложении
    static double currentHeight;                //Высота плана в приложении
    static String[] montagneOutsideTypeArr = {"Консоль","Кронштейн"};
    static String [] positionOutsideArr = {"Крыльцо","Фасад","Территория","Футбольное поле","Игровая площадка"};
    static String[] roofTypes = {"Бетон","Армстронг","ПВХ","Гипрок"};        //Типы потолков
    static String[] montagneTypeArr = {"Накладной","Встраиваемый","Подвесной"};        //Типы потолков
    static String[] placeTypeArr = {"В здании","Наружный"};        //Типы потолков
    static String[] typeOfBuildingArr = {"Детский сад","Школа","Больница"};
    static String[] typesOfRoomsDetSad = { "Игровая","Гардероб", "Спальная", "Санузел", "Коридор", "Тамбур","Лестница","Кабинет","Пищеблок","Прачечная","Моечная","Кладовая","Служебное помещение","Спортзал","Актовый зал","Медкабинет"};            //Типы помещений(детские сады)
    static String[] typesOfRoomsSchools = { "Учебный кабинет", "Кабинет", "Санузел", "Коридор", "Тамбур","Лестница","Спортзал","Пищеблок","Актовый зал","Медкабинет","Кладовая","Служебное помещение"};            //Типы помещений(школы)
    static String[] typesOfRoomsHospitals = { "Кабинет врача", "Кабинет", "Санузел", "Коридор", "Тамбур","Лестница","Спортзал","Пищеблок","Актовый зал","Медкабинет","Кладовая","Служебное помещение","Палата","Процедурная"};            //Типы помещений(больницы)
    static String[] daysPerWeekArr = {"0","1","2","3","4","5","6","7"};         //Дней работы в неделю
    static String[] hoursPerDayArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по будням
    static String[] hoursPerWeekendArr = {"0","0.5","1","2","4","6","8","12","16","20","24"};       //Часов работы по выходным

    static String[] spinRowsArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в столбцах
    static String[] spinLinesArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в рядах


    public static  final Integer[] VstraivaemieImageId = {              //Изображения светильников
            R.drawable.lum4_18,R.drawable.lum2_36vstr,R.drawable.lampnakalspot,R.drawable.lampkll15spot,R.drawable.lampgalogen35
    };
    public static  final Integer[] NakladnieImageId = {              //Изображения светильников
            R.drawable.lum4_18nakl,R.drawable.lum2_36,R.drawable.lum2_18,R.drawable.lum1_36,R.drawable.lum1_18,R.drawable.lum2_58,R.drawable.lum2_80,R.drawable.lum4_36,R.drawable.lum4_58,R.drawable.lum4_80
    };
    public static  final Integer[] LampsImageId = {              //Изображения светильников
            R.drawable.lampnakal,R.drawable.lampdiod,R.drawable.lampkll15
    };
    public static  final Integer[] DiodsImageId = {              //Изображения светильников
            R.drawable.diod4_18,R.drawable.diod4_18nakl,R.drawable.diod2_36,R.drawable.lampdiodspot,R.drawable.lustradiod
    };
    public static  final Integer[] OthersImageId = {              //Изображения светильников
            R.drawable.diod4_18lampsvstr,R.drawable.diod4_18lampsnakl,R.drawable.diod2_36lampsnakl,R.drawable.diod2_18lampsnakl,R.drawable.diod1_36lampsnakl,R.drawable.diod1_18lampsnakl,R.drawable.lustranakal,R.drawable.lustrakll
    };
    public static  final Integer[] OutsideImageId = {              //Изображения светильников
            R.drawable.drlfasad,R.drawable.dnatfasad,R.drawable.mglfasad,R.drawable.diodfasad
    };



    public static  final Integer[] VstraivaemieImageIdBold = {              //Изображения светильников
            R.drawable.lum4_18bold,R.drawable.lum2_36vstrbold,R.drawable.lampnakalspotbold,R.drawable.lampkll15spotbold,R.drawable.lampgalogen35bold
    };

    public static  final Integer[] NakladnieImageIdBold = {              //Изображения светильников
            R.drawable.lum4_18naklbold,R.drawable.lum2_36bold,R.drawable.lum2_18bold,R.drawable.lum1_36bold,R.drawable.lum1_18bold,R.drawable.lum2_58bold,R.drawable.lum2_80bold,R.drawable.lum4_36bold,R.drawable.lum4_58bold,R.drawable.lum4_80bold
    };

    public static  final Integer[] LampsImageIdBold = {              //Изображения светильников
            R.drawable.lampnakalbold,R.drawable.lampdiodbold,R.drawable.lampkll15bold
    };
    public static  final Integer[] DiodsImageIdBold = {              //Изображения светильников
            R.drawable.diod4_18bold,R.drawable.diod4_18naklbold,R.drawable.diod2_36bold,R.drawable.lampdiodspotbold,R.drawable.lustradiodbold
    };
    public static  final Integer[] OthersImageIdBold = {              //Изображения светильников
            R.drawable.diod4_18lampsvstrbold,R.drawable.diod4_18lampsnaklbold,R.drawable.diod2_36lampsnaklbold,R.drawable.diod2_18lampsnaklbold,R.drawable.diod1_36lampsnaklbold,R.drawable.diod1_18lampsnaklbold,R.drawable.lustranakalbold,R.drawable.lustrakllbold
    };
    public static  final Integer[] OutsideImageIdBold = {              //Изображения светильников
            R.drawable.drlfasadbold,R.drawable.dnatfasadbold,R.drawable.mglfasadbold,R.drawable.diodfasadbold
    };



    public static final String[] lampVstraivaemieNames = {             //Названия светильников
            "4*18Вт","2*36Вт","ЛН 60Вт","КЛЛ 15Вт","Гал. 35Вт"
    };
    public static final String[] lampNakladnieNames = {             //Названия светильников
            "4*18Вт","2*36Вт","2*18Вт","1*36Вт","1*18Вт","2*58Вт","2*80Вт","4*36Вт","4*58Вт","4*80Вт"
    };
    public static final String[] lampLampsNames = {             //Названия светильников
            "ЛН 60Вт","СД 12Вт","КЛЛ 15Вт"
    };
    public static final String[] lampLampsNamesForOutput = {             //Названия светильников
            "накаливания 60Вт","светодиодная 12Вт","КЛЛ 15Вт"
    };
    public static final String[] lampDiodsNames = {             //Названия светильников
            "36Вт","36Вт","36Вт","12Вт","12Вт"
    };
    public static final String[] lampOthersNames = {             //Названия светильников
            "36Вт","36Вт","36Вт","18Вт","18Вт","9Вт","ЛН 60Вт","КЛЛ 15Вт"
    };
    public static final String[] lampOutsideNames = {             //Названия светильников
            "ДРЛ-250Вт","ДНаТ-250Вт","МГЛ-250Вт","СД-50Вт"
    };


    public static  final String[] lampsVstraivaemieName = {"lum4_18","lum2_36vstr","lampnakalspot","lampkll15spot","lampgalogen35"};
    public static  final String[] lampsNakladnieName = {"lum4_18nakl","lum2_36","lum2_18","lum1_36","lum1_18","lum2_58","lum2_80","lum4_36","lum4_58","lum4_80"};
    public static  final String[] lampsLampsName = {"lampnakal","lampdiod","lampkll15"};
    public static  final String[] lampsDiodsName = {"diod4_18","diod4_18nakl","diod2_36","lampdiodspot","lustradiod"};
    public static  final String[] lampsOthersName = {"diod4_18lampsvstr","diod4_18lampsnakl","diod2_36lampsnakl","diod2_18lampsnakl","diod1_36lampsnakl","diod1_18lampsnakl","lustranakal","lustrakll"};
    public static  final String[] lampsOutsideName = {"drlfasad","dnatfasad","mglfasad","diodfasad"};



    public static void init(){                //Инициализация переменных
        montagneTypeTxt = activity.findViewById(R.id.montagneTypeTxt);
        isStolbCheck = activity.findViewById(R.id.isStolbCheck);
        isStolbTxt = activity.findViewById(R.id.isStolbTxt);
        positionOutside = activity.findViewById(R.id.positionOutside);
        positionOutsideTxt = activity.findViewById(R.id.positionOutsideTxt);
        montagneOutsideType = activity.findViewById(R.id.montagneOutsideType);
        montagneOutsideTypeTxt = activity.findViewById(R.id.montagneOutsideTypeTxt);
        lampAmountText = activity.findViewById(R.id.lampAmountText);
        lampAmountEdit = activity.findViewById(R.id.lampAmountEdit);
        placeType = activity.findViewById(R.id.placeType);
        montagneType=activity.findViewById(R.id.montagneType);
        initLampsPanels();
        hoursPerSunday = activity.findViewById(R.id.roomHoursSunday);
        daysOfWorkDefault = activity.findViewById(R.id.daysOfWorkDefault);
        typeOfBuilding = activity.findViewById(R.id.typeOfBuilding);
        roomHeightDefaultCheck = activity.findViewById(R.id.roomHeightDefaultCheck);
        roofTypeDefaultText = activity.findViewById(R.id.roofTypeDefaultText);
        roofTypeDefault = activity.findViewById(R.id.roofTypeDefault);
        lampGrid = activity.findViewById(R.id.lampGrid);
        floorPanelLay = activity.findViewById(R.id.floorPanelLay);
        photoImage = activity.findViewById(R.id.photoImage);
        photoFrame = activity.findViewById(R.id.photoFrame);
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
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, typesOfRoomsDetSad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,daysPerWeekArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysPerWeek.setAdapter(adapter);
        daysOfWorkDefault.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerWeekend.setAdapter(adapter);
        hoursPerSunday.setAdapter(adapter);
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
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,roofTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roofTypeDefault.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,typeOfBuildingArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfBuilding.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,montagneTypeArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        montagneType.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,placeTypeArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeType.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,montagneOutsideTypeArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        montagneOutsideType.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,positionOutsideArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionOutside.setAdapter(adapter);
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
    public static Room getRoomByNumber(String number,Floor floor){
        for (int i=0;i<floor.rooms.size();i++){
            if (Objects.equals(floor.rooms.elementAt(i).getNumber(), number))
                return floor.rooms.elementAt(i);
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
        Variables.roomGrid.removeAllViews();
        for (String str:room.photoPaths){
            File f = new File(str);
            Buttons.createNewPhotoRoom(f,true);
        }
    }
    public static void showLampsAllPhotos(Lamp lamp){
        Variables.lampGrid.removeAllViews();
        for (String str:lamp.photoPaths){
            File f = new File(str);
            Buttons.createNewPhotoRoom(f,false);
        }
    }

    public static void resetRoomHeight(Floor floor){
        for (Room room: floor.rooms) {
            if (Variables.roomHeightDefaultCheck.isChecked()) {
                if (Objects.equals(room.getHeight(), "0.0")) {
                    if (!Objects.equals(floor.roofHeightDefault.elementAt(room.getRoofType()), "0.0")) {
                        room.setHeight(floor.roofHeightDefault.elementAt(room.getRoofType()));
                    }
                }
            }
        }
    }
    public static void refreshLampsToRooms(Floor floor){
        resetRoomHeight(floor);

        for (Room room: floor.rooms){
            Vector<Lamp> tempVec= new Vector<Lamp>(room.lamps);
            for (Lamp lamp: tempVec){
                Room temp = getRoomByNumber(lamp.getLampRoom(),floor);
                if (temp!=null && !temp.lamps.contains(lamp)){
                    temp.lamps.add(lamp);
                    if (floor.unusedLamps.contains(lamp)){
                        floor.unusedLamps.remove(lamp);
                    }
                }
            }
        }
        Vector<Lamp> tempVec = new Vector<Lamp>(floor.unusedLamps);
         for (Lamp lamp: tempVec){
            Room temp = getRoomByNumber(lamp.getLampRoom(),floor);
            if (temp!=null && !temp.lamps.contains(lamp)){
                temp.lamps.add(lamp);
                if (floor.unusedLamps.contains(lamp)){
                    floor.unusedLamps.remove(lamp);
                }
            }
        }
         for (Room room:floor.rooms){
             if (room.getHeight().equals("0.0")){
                 if (Variables.roomHeightDefaultCheck.isChecked()){
                     int index = room.getRoofType();
                     room.setHeight(Variables.current_floor.roofHeightDefault.elementAt(index));
                 }
             }
         }
    }
    public static void clearLampGrid(){
        if (!Variables.getMoveFlag()) {
            lampGrid.removeAllViews();
        }
    }

    public static void copyFile(Uri pathOld, String pathNew){
        String pathOldFile = FileHelper.getRealPathFromURI(activity, pathOld);
        String[] pathOldSplitted =  pathOldFile.split("/");
        String name = pathOldSplitted[pathOldSplitted.length-1];
        File oldFile = new File(pathOldFile);
        File newFile = new File( pathNew, name );
        try
        {
            FileInputStream fis = new FileInputStream( oldFile );
            FileOutputStream fos = new FileOutputStream( newFile );
            try
            {
                int currentByte = fis.read();
                while( currentByte != -1 )
                {
                    fos.write( currentByte );
                    currentByte = fis.read();
                }
            }
            catch( IOException exception )
            {
                System.err.println( "IOException occurred!" );
                exception.printStackTrace();
            }
            finally
            {
                fis.close();
                fos.close();
                System.out.println( "Copied file!" );
            }
        }
        catch( IOException exception )
        {
            System.err.println( "Problems with files!" );
            exception.printStackTrace();
        }
    }

    public static int findIndexOfLamp(String type,int index){
        switch (index){
            case 0:
                for (int i=0;i<lampsVstraivaemieName.length;i++){
                    if (lampsVstraivaemieName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 1:
                for (int i=0;i<lampsNakladnieName.length;i++){
                    if (lampsNakladnieName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 2:
                for (int i=0;i<lampsLampsName.length;i++){
                    if (lampsLampsName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 3:
                for (int i=0;i<lampsDiodsName.length;i++){
                    if (lampsDiodsName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 4:
                for (int i=0;i<lampsOthersName.length;i++){
                    if (lampsOthersName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 5:
                for (int i=0;i<lampsOutsideName.length;i++){
                    if (lampsOutsideName[i].equals(type)){
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public static LinearLayout getCurrentPanelLayout(){
        VerticalTextView txt = lampsPanels.elementAt(currentLampsPanelIndex);
        LinearLayout temp = (LinearLayout) txt.getParent();
        return temp;
    }
    private static void initLampsPanels(){
        LinearLayout lampsLayoutsWrapper = activity.findViewById(R.id.lampTypesPanelsLayout);
        lampsLayoutsWrapper.post(new Runnable() {
            @Override
            public void run() {
                lampsList = new LampsList(activity, lampVstraivaemieNames, VstraivaemieImageId);
                listView.setAdapter(lampsList);
                lampsPanels.add(activity.findViewById(R.id.VstraivaemiePanel));
                lampsPanels.add(activity.findViewById(R.id.NakladniePanel));
                lampsPanels.add(activity.findViewById(R.id.LampsPanel));
                lampsPanels.add(activity.findViewById(R.id.DiodsPanel));
                lampsPanels.add(activity.findViewById(R.id.OthersPanel));
                lampsPanels.add(activity.findViewById(R.id.OutsidePanel));
                LinearLayout lampsLayoutsWrapper = activity.findViewById(R.id.lampTypesPanelsLayout);
                int heightOfParentView = lampsLayoutsWrapper.getHeight();
                int heightOfEachElem = heightOfParentView/lampsPanels.size();
                LinearLayout.LayoutParams paramsForWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) heightOfEachElem);
                LinearLayout.LayoutParams paramsForText = new LinearLayout.LayoutParams((int) heightOfEachElem,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, Variables.activity.getResources().getDisplayMetrics()));
                for (VerticalTextView lay:lampsPanels){
                    LinearLayout temp = (LinearLayout) lay.getParent();
                    temp.setLayoutParams(paramsForWrap);
                    temp.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            for (int i=0;i<lampsPanels.size();i++){
                                LinearLayout temp1 = (LinearLayout) lampsPanels.elementAt(i).getParent();
                                if (temp1==v){
                                    if (i!=currentLampsPanelIndex) {
                                        for (VerticalTextView lay1:lampsPanels){
                                            LinearLayout temp2 = (LinearLayout) lay1.getParent();
                                            temp2.setBackgroundColor(Variables.activity.getResources().getColor(R.color.grey));
                                        }
                                        currentLampsPanelIndex = i;
                                        temp1.setBackgroundColor(Variables.activity.getResources().getColor(R.color.white));
                                        switch (currentLampsPanelIndex){
                                            case 0:
                                                lampsList = new LampsList(activity, lampVstraivaemieNames, VstraivaemieImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 1:
                                                lampsList = new LampsList(activity, lampNakladnieNames, NakladnieImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 2:
                                                lampsList = new LampsList(activity, lampLampsNames, LampsImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 3:
                                                lampsList = new LampsList(activity, lampDiodsNames, DiodsImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 4:
                                                lampsList = new LampsList(activity, lampOthersNames, OthersImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 5:
                                                lampsList = new LampsList(activity, lampOutsideNames, OutsideImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    lay.setLayoutParams(paramsForText);
                }
            }
        });
    }

    public static void removeLampByView(ImageView imageView){
        for (Room room:Variables.current_floor.rooms){
            for (Lamp lamp:room.lamps){
                if (lamp.getImage()==imageView){
                    room.lamps.remove(lamp);
                    return;
                }
            }
        }
            for (Lamp lamp:Variables.current_floor.unusedLamps){
                if (lamp.getImage()==imageView){
                    Variables.current_floor.unusedLamps.remove(lamp);
                    return;
                }
        }
    }
    /*public static int getTypeOfLampByPower(){

    }*/
}
