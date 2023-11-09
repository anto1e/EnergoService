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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Vector;

//Класс для хранения глобальных переменных
public class Variables {
    static boolean selectByClickFlag=false;
    static boolean wasUnlocked=false;
    static boolean isExpotedExcel=true;     //Флаг экспорта в эксель
    static Vector<ImageView> multipleAddTempVector = new Vector<ImageView>();
    static ListView listView=null;          //Список светильников
    static String path1;
    static TextView lampDop;
    static boolean lampDopShown=false;
    static TextView typeLampTxt;
    static TextView daysLampTxt;
    static TextView hoursLampTxt;
    static TextView hoursWeekendLampTxt;
    static TextView hoursSundayLampTxt;
    static int lampSize=7;
    static boolean loadingFlag=false;
    static Vector<Lamp> copyBuffer = new Vector<Lamp>();        //Буффер копирования

    static int typeOpening=0;           //Тип открытия нового файла
    static boolean selectZoneFlag=false;        //Флаг выбора зона
    static Vector<Lamp> copyVector = new Vector<Lamp>();        //Временный вектор
    static Vector<Float> distX = new Vector<Float>();           //Вектор расстояний от выбранного светильника по Х
    static Spinner hoursPerSunday;              //Спиннер рабочих дней по воскресеньям
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
    static Spinner montagneType;                //Спинер типа монтажа
    static boolean copyFlag=false;                  //Флаг активации функции копирования
    static boolean planLayCleared=false;            //Флаг очистки плана
    static Vector<LinearLayout> FloorPanelsVec = new Vector<LinearLayout>();        //Вектор вкладок на экране
    static Vector<String> backupBackVector = new Vector<String>();
    static boolean getAllBackUpDataFlag=false;
    static boolean allowRotationPlanFlag=false;
    static LinearLayout floorPanelLay;              //Layout вкладок этажей
    static ExcelExporter exporter;                  //Экспортер данных в эксель
    static RelativeLayout roomInfoView;             //Панель инфрмации о комнате
    static boolean notSelected=false;
    static Spinner typeOfBuilding;                  //Спиннер типа строения
    static Uri tempImage;
    static Spinner daysOfWorkDefault;               //Спиннер дней работы по умолчанию

    static RelativeLayout buildingInfoView;             //Панель инфрмации о здании
    static boolean fileSaved=true;                      //Флаг сохранен ли файл
    static RelativeLayout lampInfoView;             //Панель инфрмации о светильнике
    static EditText buildingName;             //Поле наименования здания
    static EditText buidlingFloor;             //Поле номера этажа
    static RelativeLayout multipleRowsInfo;     //Поле выбора данных для создания множества светильников по рядам и столбцам
    static boolean scalemode = false;           //Флаг активации режима изменения размера
    static boolean fileBackuping = false;
    static boolean rotateMode=false;            //Флаг активации режима поворота
    static boolean removeMode=false;            //Флаг активации режима удаления
    static boolean addMultipleRowsFlag=false;       //Флаг активации добавления множества светильников по рядам и столбцам
    static boolean firstTouch=false;                //Флаг определения первого нажатия для создания зоны выделения
    static float firstPointX=0;                     //Позиция по Х первого нажатия
    static float firstPointY=0;                     //Позиция по У первого нажатия
    static boolean disableMovingPlan=false;         //Флаг выключения функции перемещения по плану

    static float lastScaletype=3f;            //Значение последнего значения масштабирования
    static EditText buildingAdress;             //Поле адреса здания
    static String filePath="";             //Путь к текущему открытому файлу
    static Uri selectedfile;             //Выбранный текущий файл
    static Activity activity=null;          //Главное activity
    static ImageView photoImage;       //Поле для отображения фотографии
    static int currentLampsPanelIndex = 0;      //Текущий индекс открытой панели с типами светильников
    static boolean opened = false;          //Если файл был открыт
    static LampsList lampsList=null;        //Список, отображающий светильники текущей открытой вкладки
    static BikExtensionParser parser = new BikExtensionParser();        //Парсер данных из .bik
    private static boolean addFlag=false;       //Флаг активации режима добавления светильника
    static boolean addMultiple_flag=false;       //Флаг активации режима добавления светильника
    static Integer multipleType=-1;             //Тип светильника
    static LinearLayout imageWrap;          //Оболочка картинки
    static int multiplepos=-1;                  //Позиция в массиве светильнков
    static int multiplelampType=-1;             //Тип ламп
    private static boolean moveFlag=false;      //Флаг активации режима перемещния светильника
    static ImageView loadingImage;              //Колесо вращения при сохранении
    static LinearLayout floorsPanels;           //Layout для хранения вкладок
    static Plan plan = new Plan();          //План этажа
    static Spinner spinRows;                //Спиннер столбцов(создание множества светильников по рядам и столбцам)
    static Spinner spinLines;               //Спиннер строк(создание множества светильников по рядам и столбцам)
    static Spinner roofTypeDefault;         //Спинер типа потолка по умолчанию
    static String fileName="";
    static EditText roofTypeDefaultText;        //Поле для ввода высоты по умолчанию
    static Vector<VerticalTextView> lampsPanels = new Vector<VerticalTextView>();       //Вектор панелей с типами светильников
    static RelativeLayout planLay;          //Layout плана
    static ImageView image;                     //Изображение(план)
    static EditText lampRoom;                   //Поле информации о привязке светильника к комнате
    static GridLayout roomGrid;                 //Grid layout для отображения фотографий комнаты
    static GridLayout lampGrid;                  //Grid layout для отображения фотографий светильника
    static EditText roomNumber=null;            //Поле для номера помещения
    static EditText roomHeight;            //Поле для высоты помещения
    static Spinner daysPerWeek;            //Поле для дней работы помещения
    static boolean showPhotoFlag=false;     //Флаг просмотра фотографии комнаты
    static boolean showPhotoLampFlag=false; //Флаг просмотра фотографии светильника
    static CheckBox roomHeightDefaultCheck; //Чекбокс использования высоты по умолчанию
    static boolean takePhotoFlag=false;     //Флаг съемки фотографии
    static TextView montagneOutsideTypeTxt;     //Текст наружного монтажа
    static Spinner montagneOutsideType;         //Спинер наружного монтажа свеитльника
    static TextView positionOutsideTxt;         //Текст позиции снаружи светильника
    static boolean exportingJpg=false;
    static TextView montagneTypeTxtTwo;
    static Spinner montagneTypeTwo;
    static boolean switchFlag=false;
    static Spinner positionOutside;             //Спинер позиции снаружи светильника
    static TextView isStolbTxt;                 //Текст находится ли светильник на столбе
    static CheckBox isStolbCheck;               //Чекбокс находится ли светильник на столбе
    static Spinner hoursPerDay;            //Поле для часов работы в день помещения
    static Spinner hoursPerWeekend;            //Поле для часов работы в выходные помещения
    static Spinner roofType;                //Поле типа потолка

    static Buttons buttons = new Buttons();        //Создание класса с кнопками
    static Spinner type;            //Выпадающий список(спинер) с типами помещений
    static RelativeLayout RoomInfo;            //Макет, где хранится информация о помещении
    static EditText lampType;                  //Поле типа светильника
    static EditText lampPower;                  //Поле мощности светильника
    static Spinner typeLamp;
    static Spinner daysLamp;
    static Spinner hoursLamp;
    static Spinner hoursPerWeekendLamp;
    static Spinner hoursPerSundayLamp;
    static EditText lampComments;              //Поле комментариев к светильнику
    static FrameLayout photoFrame;              //Layout для отображения выбранной фотографии
    static FrameLayout bacupBackFrame;
    static TextView lampAmountText;             //Текст количества ламп(для люстр)
    static EditText lampAmountEdit;             //Поле ввода количества ламп(для люстр)
    static Spinner placeType;                   //Спинер месторасположения светильнка(для наружного освещения)
    static TextView montagneTypeTxt;            //Текст типа монтажа

    static EditText roomComments;               //Поле комментариев к комнате
    static int indexOfPhoto=-1;                 //Индекс текущей просматриваемой фотографии

    static Vector<Floor> floors= new Vector<Floor>();       //Вектор, хранящий открытые этаж

    static Floor current_floor=null;            //Текущий открытый этаж

    static double lastWidth;                //Ширина плана, при разметке на сайте
    static double lastHeight;                //Высота плана, при разметке на сайте
    static boolean isExportingToJpg=false;
    static double currentWidth;                //Ширина плана в приложении
    static double currentHeight;                //Высота плана в приложении
    static String[] montagneOutsideTypeArr = {"Консоль","Кронштейн","Шар"};           //Типы монтажа наружного освещения
    static String [] positionOutsideArr = {"Фасад","Крыльцо","Территория","Крыша","Футбольное поле","Игровая площадка"};        //Типы расположения светильника снаружи
    static String[] roofTypes = {"Бетон","Армстронг","ПВХ","Гипрок"};        //Типы потолков
    static String[] montagneTypeArr = {"Накладной","Встраиваемый","Настенный","Подвесной"};        //Типы монтажа светильника
    static String[] montagneTypeTwoArr = {"Не указано","Накладной","Встраиваемый","Настенный","Подвесной"};        //Типы монтажа светильника
    static String[] placeTypeArr = {"В здании","Наружный"};        //Типы нахождения светильника(снаружи/в здании)
    static String[] typeOfBuildingArr = {"Детский сад","Школа","Больница","Другое"};         //Тип здания

    static int[] defaultHoursDetSad = {0,5,4,6,1,6,6,2,5,3,6,5,6,6,4,2,3,4,4,1,0};
    static int[] defaultHoursSchool5Days = {0,5,6,6,4,1,6,6,6,2,5,3,6,6,4,6,6,4,1,6,0};
    static int[][] defaultHoursSchool6Days = {{0,0},{5,4},{6,5},{6,0},{4,4},{1,0},{6,4},{6,0},{6,4},{2,1},{5,4},{3,3},{6,0},{6,5},{4,3},{6,5},{6,4},{4,3},{1,0},{6,4},{0,0}};

    static String[] typesOfRoomsDetSad = {"Служебное помещение","Актовый зал","Гардероб","Групповая","ГРЩ","Кабинет", "Коридор","Кладовая","Лестница","Моечная","Медкабинет","Музыкальный зал","Пищеблок","Прачечная","Раздевалка","Спальная", "Санузел","Спортзал",  "Тамбур","Техническое помещение","Другое"};            //Типы помещений(детские сады)
    static String[] typesOfRoomsSchools = {"Служебное помещение","Актовый зал","Бассейн","Библиотека","Гардероб","ГРЩ","Учебный кабинет", "Кабинет", "Коридор","Кладовая","Лестница","Санузел", "Медкабинет","Пищеблок","Раздевалка","Столовая","Спортзал","Тамбур","Техническое помещение","Цех","Другое"};            //Типы помещений(школы)
    static String[] typesOfRoomsHospitals = {"Служебное помещение","Актовый зал","Бассейн","Гардероб","ГРЩ","Кабинет врача", "Кабинет", "Санузел", "Коридор","Кладовая","Лестница", "Медкабинет","Палата","Пищеблок","Процедурная","Спортзал","Столовая","Тамбур","Другое"};            //Типы помещений(больницы)
    static String[] typesOfRoomsOthers = {"Служебное помещение","Коридор", "Кабинет", "Санузел", "Служебное помещение", "Тамбур","Лестница","Спортзал","Пищеблок","Актовый зал","Медкабинет","Кладовая","Столовая","Процедурный кабинет","Палата","Комната","Учебный кабинет","Душевая","Гардероб","Другое"};            //Типы помещений(больницы)

    static String[] daysPerWeekArr = {"0","1/мес","1","2","3","4","5","6","7"};         //Дней работы в неделю
    static String[] hoursPerDayArr = {"0","0.5","1","2","4","6","8","10","12","16","20","24"};       //Часов работы по будням
    static String[] hoursPerWeekendArr = {"0","0.5","1","2","4","6","8","10","12","16","20","24"};       //Часов работы по выходным

    static String[] spinRowsArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в столбцах
    static String[] spinLinesArr = {"2","3","4","5","6","7","8","9","10"};       //Количество светильников в рядах


    public static  final Integer[] VstraivaemieImageId = {              //Изображения встраиваемых светильников
            R.drawable.lum4_18,R.drawable.lum2_36vstr,R.drawable.lampnakal60spot,R.drawable.lampkll15spot,R.drawable.lampgalogen35,R.drawable.lum2_18vstr,R.drawable.lum1_18vstr,R.drawable.lampnakal40spot,R.drawable.lampkll18spot,R.drawable.lampkll20spot,R.drawable.lampkll25spot,R.drawable.lampkll30spot,R.drawable.lum1_36vstr,R.drawable.lum2_58vstr,R.drawable.lum2_80vstr,R.drawable.lum4_36vstr,R.drawable.lampnakal75spot,R.drawable.lampnakal95spot,R.drawable.lum4_14
    };
    public static  final Integer[] NakladnieImageId = {              //Изображения накладных светильников
            R.drawable.lum4_18nakl,R.drawable.lum2_36,R.drawable.lampnakal60,R.drawable.lampkll15,R.drawable.lum2_18,R.drawable.lum1_36,R.drawable.lum1_18,R.drawable.lampnakal40,R.drawable.lum2_58,R.drawable.lum2_80,R.drawable.lum4_36,R.drawable.lum4_58,R.drawable.lum4_80,R.drawable.lum1_58,R.drawable.lum1_80,R.drawable.lampnakal75,R.drawable.lampnakal95,R.drawable.lum2_14,R.drawable.lum2_28,R.drawable.lum4_14nakl
    };
    public static  final Integer[] LampsImageId = {              //Изображения ламп
            R.drawable.lampdiod12,R.drawable.lampdiod10,R.drawable.lampkll18,R.drawable.lampkll20,R.drawable.lampkll25,R.drawable.lampkll30,R.drawable.lampdiod15,R.drawable.lampdiod50
    };
    public static  final Integer[] DiodsImageId = {              //Изображения светодиодных светильников
            R.drawable.diod36vstr,R.drawable.diod36nakl,R.drawable.diod36long,R.drawable.diod18nakl,R.drawable.lampdiod12,R.drawable.diod24long,R.drawable.lampdiodspot12,R.drawable.lustradiod,R.drawable.diod40vstr,R.drawable.diod40nakl,R.drawable.diod40long,R.drawable.lampdiodspot10,R.drawable.lampdiodspot15
    };
    public static  final Integer[] DoskiImageId = {              //Изображения светодиодных светильников
            R.drawable.lum1_36dosk,R.drawable.diod18dosk,R.drawable.lum1_58dosk,R.drawable.diod24dosk,R.drawable.lum1_28dosk,R.drawable.lum1_52dosk,R.drawable.lum2_36dosk,R.drawable.lum2_58dosk,R.drawable.diod20dosk
    };
    public static  final Integer[] PodvesImageId = {              //Изображения светодиодных светильников
            R.drawable.lampnakal60podves,R.drawable.lampkll15podves,R.drawable.lampdiod12podves,R.drawable.lampnakal40podves,R.drawable.lampkll18podves,R.drawable.lampkll20podves,R.drawable.lampkll25podves,R.drawable.lampkll30podves,R.drawable.lampdiod10podves,R.drawable.lampdiod15podves,R.drawable.lampnakal75podves,R.drawable.lampnakal95podves
    };

    public static  final Integer[] OthersImageId = {              //Изображения других светильников
            R.drawable.diod4_18lampsvstr,R.drawable.diod4_18lampsnakl,R.drawable.diod2_36lampsnakl,R.drawable.diod2_18lampsnakl,R.drawable.diod1_36lampsnakl,R.drawable.diod1_18lampsnakl,R.drawable.lustranakal,R.drawable.lustrakll,R.drawable.unknowntype,R.drawable.unknowntypediod
    };

    public static  final Integer[] OutsideImageId = {              //Изображения наружных светильников
            R.drawable.drl250,R.drawable.dnat250,R.drawable.mgl250,R.drawable.diod50,R.drawable.lampnakal60,R.drawable.lampkll15,R.drawable.lampdiod12,R.drawable.shar125,R.drawable.shar250,R.drawable.drl400,R.drawable.dnat400,R.drawable.mgl400,R.drawable.drl125,R.drawable.drl700,R.drawable.drl1000,R.drawable.dnat70,R.drawable.dnat100,R.drawable.dnat150,R.drawable.mgl500,R.drawable.mgl1000,R.drawable.diod30,R.drawable.diod70,R.drawable.diod100
    };



    public static  final Integer[] VstraivaemieImageIdBold = {              //Изображения встраиваемх светильников
            R.drawable.lum4_18bold,R.drawable.lum2_36vstrbold,R.drawable.lampnakalspotbold,R.drawable.lampkll15spotbold,R.drawable.lampgalogen35bold
    };

    public static  final Integer[] NakladnieImageIdBold = {              //Изображения накладных светильников
            R.drawable.lum4_18naklbold,R.drawable.lum2_36bold,R.drawable.lum2_18bold,R.drawable.lum1_36bold,R.drawable.lum1_18bold,R.drawable.lum2_58bold,R.drawable.lum2_80bold,R.drawable.lum4_36bold,R.drawable.lum4_58bold,R.drawable.lum4_80bold
    };

    public static  final Integer[] LampsImageIdBold = {              //Изображения ламп

    };
    public static  final Integer[] DiodsImageIdBold = {              //Изображения светодиодных светильников
            R.drawable.diod4_18bold,R.drawable.diod4_18naklbold,R.drawable.diod2_36bold,R.drawable.lampdiodspotbold,R.drawable.lustradiodbold
    };
    public static  final Integer[] DoskiImageIdBold = {              //Изображения светодиодных светильников
    };
    public static  final Integer[] PodvesImageIdBold = {              //Изображения светодиодных светильников
    };
    public static  final Integer[] OthersImageIdBold = {              //Изображения других светильников
            R.drawable.diod4_18lampsvstrbold,R.drawable.diod4_18lampsnaklbold,R.drawable.diod2_36lampsnaklbold,R.drawable.diod2_18lampsnaklbold,R.drawable.diod1_36lampsnaklbold,R.drawable.diod1_18lampsnaklbold,R.drawable.lustranakalbold,R.drawable.lustrakllbold
    };
    public static  final Integer[] OutsideImageIdBold = {              //Изображения наружных светильников
            R.drawable.drlfasadbold,R.drawable.dnatfasadbold,R.drawable.mglfasadbold,R.drawable.diodfasadbold
    };



    public static final String[] lampVstraivaemieNames = {             //Названия встраиваемых светильников
            "4*18Вт","2*36Вт","ЛН 60Вт","КЛЛ 15Вт","Гал. 35Вт","2*18Вт","1*18Вт","ЛН 40Вт","КЛЛ 18Вт","КЛЛ 20Вт","КЛЛ 25Вт","КЛЛ 30Вт","1*36Вт","2*58Вт","2*80Вт","4*36Вт","ЛН 75Вт","ЛН 95Вт","4*14Вт"
    };
    public static final String[] lampNakladnieNames = {             //Названия накладных светильников
            "4*18Вт","2*36Вт","ЛН 60Вт","КЛЛ 15Вт","2*18Вт","1*36Вт","1*18Вт","ЛН 40Вт","2*58Вт","2*80Вт","4*36Вт","4*58Вт","4*80Вт","1*58Вт","1*80Вт","ЛН 75Вт","ЛН 95Вт","2*14Вт","2*28Вт","4*14Вт"
    };
    public static final String[] lampLampsNames = {             //Названия ламп
            "СД 12Вт","СД 10Вт","КЛЛ 18Вт","КЛЛ 20Вт","КЛЛ 25Вт","КЛЛ 30Вт","СД 15Вт","СД 50Вт"
    };
    public static final String[] lampDiodsNames = {             //Названия светодиодных светильников
            "36Вт","36Вт","36Вт","18Вт","12Вт","24Вт","12Вт","12Вт","40Вт","40Вт","40Вт","10Вт","15Вт"
    };
    public static final String[] lampDoskiNames = {             //Названия светодиодных светильников
            "1*36Вт","1*18Вт","1*58Вт","1*24Вт","1*28Вт","1*52Вт","2*36Вт","2*58Вт","2*10Вт"
    };
    public static final String[] lampPodvesNames = {             //Названия светодиодных светильников
            "ЛН 60Вт","КЛЛ 15Вт","СД 12Вт","ЛН 40Вт","КЛЛ 18Вт","КЛЛ 20Вт","КЛЛ 25Вт","КЛЛ 30Вт","СД 10Вт","СД 15Вт","ЛН 75Вт","ЛН 95Вт"
    };
    public static final String[] lampOthersNames = {             //Названия остальных светильников
            "4*9Вт","4*9Вт","2*18Вт","2*9Вт","1*18Вт","1*9Вт","ЛН 60Вт","КЛЛ 15Вт","Неизв.","Неизв. СД"
    };
    public static final String[] lampOutsideNames = {             //Названия наружных светильников
            "ДРЛ-250Вт","ДНаТ-250Вт","МГЛ-250Вт","СД-50Вт","ЛН 60Вт","КЛЛ 15Вт","СД 12Вт","Шар 125Вт","Шар 250Вт","ДРЛ-400Вт","ДНаТ-400Вт","МГЛ-400Вт","ДРЛ-125Вт","ДРЛ-700Вт","ДРЛ-1000Вт","ДНаТ-70Вт","ДНаТ-100Вт","ДНаТ-150Вт","МГЛ-500Вт","МГЛ-1000Вт","СД-30Вт","СД-70Вт","СД-100Вт"
    };


    public static  final String[] lampsVstraivaemieName = {"lum4_18","lum2_36vstr","lampnakal60spot","lampkll15spot","lampgalogen35","lum2_18vstr","lum1_18vstr","lampnakal40spot","lampkll18spot","lampkll20spot","lampkll25spot","lampkll30spot","lum1_36vstr","lum2_58vstr","lum2_80vstr","lum4_36vstr","lampnakal75spot","lampnakal95spot","lum4_14"};        //Название ресурса встраиваемых светильников
    public static  final String[] lampsNakladnieName = {"lum4_18nakl","lum2_36","lampnakal60","lampkll15","lum2_18","lum1_36","lum1_18","lampnakal40","lum2_58","lum2_80","lum4_36","lum4_58","lum4_80","lum1_58","lum1_80","lampnakal75","lampnakal95","lum2_14","lum2_28","lum4_14nakl"};       //Название ресурса накладных светильников
    public static  final String[] lampsLampsName = {"lampdiod12","lampdiod10","lampkll18","lampkll20","lampkll25","lampkll30","lampdiod15","lampdiod50"};        //Название ресурса ламп
    public static  final String[] lampsDiodsName = {"diod36vstr","diod36nakl","diod36long","diod18nakl","lampdiod12","diod24long","lampdiodspot12","lustradiod","diod40vstr","diod40nakl","diod40long","lampdiodspot10","lampdiodspot15"};      //Название ресурса светодиодных светильников
    public static  final String[] lampsDoskiName = {"lum1_36dosk","diod18dosk","lum1_58dosk","diod24dosk","lum1_28dosk","lum1_52dosk","lum2_36dosk","lum2_58dosk","diod20dosk"};      //Название ресурса светодиодных светильников
    public static  final String[] lampsPodvesName = {"lampnakal60podves","lampkll15podves","lampdiod12podves","lampnakal40podves","lampkll18podves","lampkll20podves","lampkll25podves","lampkll30podves","lampdiod10podves","lampdiod15podves","lampnakal75podves","lampnakal95podves"};      //Название ресурса светодиодных светильников
    public static  final String[] lampsOthersName = {"diod4_18lampsvstr","diod4_18lampsnakl","diod2_36lampsnakl","diod2_18lampsnakl","diod1_36lampsnakl","diod1_18lampsnakl","lustranakal","lustrakll","unknowntype","unknowntypediod"};        //Название ресурса других светильников
    public static  final String[] lampsOutsideName = {"drl250","dnat250","mgl250","diod50","lampnakal60","lampkll15","lampdiod12","shar125","shar250","drl400","dnat400","mgl400","drl125","drl700","drl1000","dnat70","dnat100","dnat150","mgl500","mgl1000","diod30","diod70","diod100"};       //Название ресурса наружных светильников



    public static void init(){                //Инициализация переменных
        typeLampTxt = activity.findViewById(R.id.typeLampTxt);
        daysLampTxt = activity.findViewById(R.id.daysLampTxt);
        hoursLampTxt = activity.findViewById(R.id.hoursLampTxt);
        hoursWeekendLampTxt = activity.findViewById(R.id.hoursWeekendLampTxt);
        hoursSundayLampTxt = activity.findViewById(R.id.hoursSundayLampTxt);
        lampDop = activity.findViewById(R.id.lampDop);
        hoursPerSundayLamp = activity.findViewById(R.id.roomHoursSundayLamp);
        hoursPerWeekendLamp = activity.findViewById(R.id.roomHoursWeekendsLamp);
        hoursLamp = activity.findViewById(R.id.roomHoursLamp);
        daysLamp = activity.findViewById(R.id.roomDaysLamp);
        typeLamp = activity.findViewById(R.id.spinTypesLamp);
        File directory = new File("/storage/emulated/0/Documents/EnergoserviceApp");
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        path1 = "/storage/emulated/0/Documents/EnergoserviceApp";
        //path1 = String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        montagneTypeTwo = activity.findViewById(R.id.montagneTypeTwo);
        montagneTypeTxtTwo = activity.findViewById(R.id.montagneTypeTxtTwo);
        imageWrap = activity.findViewById(R.id.imageWrap);
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
        bacupBackFrame = activity.findViewById(R.id.photoFrame);
        setSpinners();
        exporter = new ExcelExporter();
    }

    public static void resetListColor(){            //Сброс цвето в списке светильников
        if (Variables.listView!=null) {
            int count = Variables.listView.getCount();
            for (int i = 0; i < count; i++) {
                ViewGroup row = (ViewGroup) Variables.listView.getChildAt(i);
                if (row!=null) {
                    row.setBackgroundResource(0);
                }
                //  Get your controls from this ViewGroup and perform your task on them =)

            }
        }
    }
    public static void setSpinners(){       //Инициализация спиннеров
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, typesOfRoomsDetSad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        typeLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,daysPerWeekArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysPerWeek.setAdapter(adapter);
        daysLamp.setAdapter(adapter);
        daysOfWorkDefault.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerWeekendArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerWeekend.setAdapter(adapter);
        hoursPerWeekendLamp.setAdapter(adapter);
        hoursPerSunday.setAdapter(adapter);
        hoursPerSundayLamp.setAdapter(adapter);
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,hoursPerDayArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursPerDay.setAdapter(adapter);
        hoursLamp.setAdapter(adapter);
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
        adapter = new ArrayAdapter<>(activity,R.layout.spinner_item,montagneTypeTwoArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        montagneTypeTwo.setAdapter(adapter);
    }

    public static void clearFields(){           //Очистка полей при переключении плана
        roomInfoView.setVisibility(View.GONE);
        Variables.plan.touchedLamp=null;
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

    public static Room getRoomByNumberAndCoords(String number,double cordX,double cordY,Floor floor){        //Поиск комнаты по номеру, к которому привязан светильник, и по координатам левой верхней точки комнаты
        for (int i=0;i<floor.rooms.size();i++){
            if (Objects.equals(floor.rooms.elementAt(i).getNumber(),number) && floor.rooms.elementAt(i).arrayX[0]==cordX && floor.rooms.elementAt(i).arrayY[0]==cordY)
                return floor.rooms.elementAt(i);
        }
        return null;
    }
    public static Room getRoomOnlyByNumber(String number,Floor floor){              //Поиск комнату только по номеру
        for (int i=0;i<floor.rooms.size();i++){
            if (Objects.equals(floor.rooms.elementAt(i).getNumber(),number))
                return floor.rooms.elementAt(i);
        }
        return null;
    }
    public static Room getRoomByNumber(String number,float cordX,float cordY,float scale,Floor floor){      //Поиск комнаты по номеру, к которому привязан светильник, и по его координатам
        for (int i=0;i<floor.rooms.size();i++){
            if (Objects.equals(floor.rooms.elementAt(i).getNumber(), number) && floor.rooms.elementAt(i).detectTouch(cordX,(cordY)))
                return floor.rooms.elementAt(i);
        }
        return null;
    }

    public static void moveCopiedVector(float cordX,float cordY){           //Передвижение светильников в векторе копирования
        tempCopiedLamp.getImage().setX(cordX);
        tempCopiedLamp.getImage().setY(cordY);
        for (int i=0;i<copyVector.size();i++){
            if (copyVector.elementAt(i)!=tempCopiedLamp){
                copyVector.elementAt(i).getImage().setX(cordX+distX.elementAt(i));
                copyVector.elementAt(i).getImage().setY(cordY+distY.elementAt(i));
            }
        }
    }
    public static void moveCopiedBufVector(float cordX,float cordY){        //Передвижение светильников в буфере копирования
        tempCopiedBufLamp.getImage().setX(cordX);
        tempCopiedBufLamp.getImage().setY(cordY);
        for (int i=0;i<copyBuffer.size();i++){
            if (copyBuffer.elementAt(i)!=tempCopiedBufLamp){
                copyBuffer.elementAt(i).getImage().setX(cordX+distBufX.elementAt(i));
                copyBuffer.elementAt(i).getImage().setY(cordY+distBufY.elementAt(i));
            }
        }
    }
    public static void resetCordsCopiedVector(){            //Перезадание координат скопированного вектора
        for (int i=0;i<copyVector.size();i++){
            copyVector.elementAt(i).getImage().setX(lastMovePosX.elementAt(i));
            copyVector.elementAt(i).getImage().setY(lastMovePosY.elementAt(i));
        }
        lastMovePosX.clear();
        lastMovePosY.clear();
    }
    public static void showAllPhotos(Room room){            //Отображение всех фотографий помещения
        Variables.roomGrid.removeAllViews();
        for (String str:room.photoPaths){
            File f = new File(str);
            Buttons.createNewPhotoRoom(f,true);
        }
    }
    public static void showLampsAllPhotos(Lamp lamp){       //Отображение всех фотографий светильника
        Variables.lampGrid.removeAllViews();
        for (String str:lamp.photoPaths){
            File f = new File(str);
            Buttons.createNewPhotoRoom(f,false);
        }
    }

    public static void resetRoomHeight(Floor floor){        //Перезадание высоты
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
    public static void refreshLampsToRooms(Floor floor){            //Перепривязка светильников к комнатам
        resetRoomHeight(floor);

        for (Room room: floor.rooms){
            Vector<Lamp> tempVec= new Vector<Lamp>(room.lamps);
            for (Lamp lamp: tempVec){
                Room temp = getRoomByNumber(lamp.getLampRoom(),lamp.getImage().getX()+(lamp.getImage().getWidth()/2),lamp.getImage().getY()+(lamp.getImage().getHeight()/2),lamp.getImage().getScaleX(),floor);
                if (temp!=null && !temp.lamps.contains(lamp)){
                    temp.lamps.add(lamp);
                    if (floor.unusedLamps.contains(lamp)){
                        floor.unusedLamps.remove(lamp);
                    }else
                        Variables.plan.removeFromEveryWhere(lamp);
                }
            }
        }
        Vector<Lamp> tempVec = new Vector<Lamp>(floor.unusedLamps);
         for (Lamp lamp: tempVec){
            Room temp = getRoomByNumber(lamp.getLampRoom(),lamp.getImage().getX()+(lamp.getImage().getWidth()/2),lamp.getImage().getY()+(lamp.getImage().getHeight()/2),lamp.getImage().getScaleX(),floor);
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
    public static void clearLampGrid(){     //Очистка полей под фотографию
        if (!Variables.getMoveFlag()) {
            lampGrid.removeAllViews();
        }
    }

    public static void copyFile(Uri pathOld, String pathNew){           //Копирование файла в папку
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

    public static int findIndexOfLamp(String type,int index){       //Функция поиска индекса светильника
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
                for (int i=0;i<lampsDoskiName.length;i++){
                    if (lampsDoskiName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 5:
                for (int i=0;i<lampsPodvesName.length;i++){
                    if (lampsPodvesName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 6:
                for (int i=0;i<lampsOthersName.length;i++){
                    if (lampsOthersName[i].equals(type)){
                        return i;
                    }
                }
                break;
            case 7:
                for (int i=0;i<lampsOutsideName.length;i++){
                    if (lampsOutsideName[i].equals(type)){
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public static LinearLayout getCurrentPanelLayout(){     //Получить текущую активную панель
        VerticalTextView txt = lampsPanels.elementAt(currentLampsPanelIndex);
        LinearLayout temp = (LinearLayout) txt.getParent();
        return temp;
    }
    private static void initLampsPanels(){          //Инициализация панелей с типами светильников
        LinearLayout lampsLayoutsWrapper = activity.findViewById(R.id.lampTypesPanelsLayout);
        lampsLayoutsWrapper.post(new Runnable() {       //После полной загрузки Layout отрисовываем вкладки типов светильников и светильники
            @Override
            public void run() {
                lampsList = new LampsList(activity, lampVstraivaemieNames, VstraivaemieImageId);
                listView.setAdapter(lampsList);
                //Добавляем панели
                lampsPanels.add(activity.findViewById(R.id.VstraivaemiePanel));
                lampsPanels.add(activity.findViewById(R.id.NakladniePanel));
                lampsPanels.add(activity.findViewById(R.id.LampsPanel));
                lampsPanels.add(activity.findViewById(R.id.DiodsPanel));
                lampsPanels.add(activity.findViewById(R.id.DoskiPanel));
                lampsPanels.add(activity.findViewById(R.id.PodvesPanel));
                lampsPanels.add(activity.findViewById(R.id.OthersPanel));
                lampsPanels.add(activity.findViewById(R.id.OutsidePanel));
                LinearLayout lampsLayoutsWrapper = activity.findViewById(R.id.lampTypesPanelsLayout);
                //Высчитываем высоты,требуемые для каждой панели
                int heightOfParentView = lampsLayoutsWrapper.getHeight();
                int heightOfEachElem = heightOfParentView/lampsPanels.size();
                //Создаем параметры
                LinearLayout.LayoutParams paramsForWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) heightOfEachElem);
                LinearLayout.LayoutParams paramsForText = new LinearLayout.LayoutParams((int) heightOfEachElem,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, Variables.activity.getResources().getDisplayMetrics()));
                for (VerticalTextView lay:lampsPanels){
                    //Задаем параметры и слушатель нажатий на панели
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
                                                lampsList = new LampsList(activity, lampDoskiNames, DoskiImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 5:
                                                lampsList = new LampsList(activity, lampPodvesNames, PodvesImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 6:
                                                lampsList = new LampsList(activity, lampOthersNames, OthersImageId);
                                                listView.setAdapter(lampsList);
                                                break;
                                            case 7:
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

    public static void removeLampByView(ImageView imageView){       //Удалить лампу по изображению
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

    public static boolean ifUnusedContainsLamp(float cordX,float cordY){
        for (Lamp lamp:Variables.current_floor.unusedLamps){
            if (lamp.getImage().getX()==cordX && lamp.getImage().getY()==cordY){
                return true;
            }
        }
        return false;
    }

    public static boolean ifRoomContainsLamp(float cordX,float cordY,Room room){
        if (room.getNumber().equals("4")){
            System.out.println("");
        }
        for (Lamp lamp:room.lamps){
            if (lamp.getImage().getX()==cordX && lamp.getImage().getY()==cordY){
                return true;
            }
        }
        return false;
    }


    public static boolean ifSomeRoomContainsLamp(float cordX,float cordY,Floor floor){
        for (Room room:floor.rooms) {
            for (Lamp lamp : room.lamps) {
                lamp.getImage().setPivotX(0);
                lamp.getImage().setPivotY(0);
                if (lamp.getImage().getX() == cordX && lamp.getImage().getY() == cordY) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setInfoEmpty(Floor floor){
        for (Room room:floor.rooms){
            for (Lamp lamp:room.lamps){
                if (lamp.getTypeRoom()==0 && lamp.getDaysWork()==0 && lamp.getHoursWork()==0){
                    lamp.setTypeRoom(room.getType_pos());
                    lamp.setDaysWork(room.getDays());
                    lamp.setHoursWork(room.getHoursPerDay());
                    lamp.setHoursWeekendWork(room.getHoursPerWeekend());
                    lamp.setHoursSundayWork(room.getHoursPerSunday());
                }
            }
        }
    }

    public static void removeDuppleLamps(Floor floor){
        for (Room room:floor.rooms){
            for (Lamp lamp:room.lamps){
                findAndRemoveLamp(floor,room,lamp);
            }
        }
    }

    public static void findAndRemoveLamp(Floor floor,Room room, Lamp lamp){
        for (Lamp lamp1:room.lamps){
            if (lamp1!=lamp && lamp1.getImage().getX() == lamp.getImage().getX() && lamp1.getImage().getY() == lamp.getImage().getY()){
                room.lampRemove(lamp1);
            }
        }
        //RelativeLayout r = (RelativeLayout) ((ViewGroup) lamp.getImage().getParent()).getParent();
        //lamp.setView();
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

 /*   public static boolean planLayIfChild(ImageView img){
        for (int i=0;i<Variables.planLay.getChildCount();i++){
            if (Variables.planLay.getChildAt(i)==img)
                return true;
        }
        return false;
    }
    public static void clearEmptyLamps(Floor floor){
        for (Room room:floor.rooms){
            for (Lamp lamp:room.lamps){
            }
        }
    }*/

    /*public static int getTypeOfLampByPower(){

    }*/

}
