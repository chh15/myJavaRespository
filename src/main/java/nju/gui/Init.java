package nju.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarFile;

import nju.biology.*;
import nju.matrix.Dimension;

import javax.swing.*;

public class Init extends Application{

    //界面大小
    final static int WIDTH = 800;
    final static int HEIGHT = 400;
    final static int TEXT = 200;
    final static int SQUARE = 40;
    //格子数
    final static int ROW = 20;
    final static int COL = 10;
    final static int TOTAL = 200;

    final static int SQURE = 40;

    //站队队列
    final static int TOTAL_OF_QUEUE = 8;
    //鹤翼
    final static int HEYI = 0;
    //雁行
    final static int YANXING = 1;
    //冲轭
    final static int CHONGE = 2;
    //长蛇
    final static int CHANGSHE = 3;
    //鱼鳞
    final static int YULIN = 4;
    //方円
    final static int FANG = 5;
    //偃月
    final static int YANYUE = 6;
    //锋矢
    final static int FENG = 7;
    //站队标志
    final static int FLAG_CALA = 0;
    final static int FLAG_SCOR = 1;

    //第一次启动
    static int first_time = 0;

    private static List<Calabash> calabash = new ArrayList<Calabash>();
    private GrandFather grandfather;
    private Scorpion scorpion;
    private Snake snake;
    private List<Frog> frog = new ArrayList<Frog>();

    //线程池
    //private ExecutorService exec = Executors.newCachedThreadPool();

    //表示整个区域的矩阵
    Dimension dimen = new Dimension();

    //管理整个界面的组
    Group root = new Group();
    //管理整个场景
    Scene scene = new Scene(root,WIDTH,HEIGHT + TEXT);
    //在组内画布
    Canvas canvas = new Canvas();
    //创建画笔
    GraphicsContext context = canvas.getGraphicsContext2D();
    //创建显示区域
    ScrollPane scrollF, scrollG;
    //文本显示
    TextArea text1, text2;

    public void start(Stage stage){

        //初始化画布
        canvas.setWidth(WIDTH);
        canvas.setHeight(HEIGHT);
        root.getChildren().add(canvas);

        //初始化文本显示框
        text1 = new TextArea();
        scrollF = new ScrollPane(text1);
        scrollF.setLayoutX(0);
        scrollF.setLayoutY(400);
        scrollF.setPrefSize(400,400);
        root.getChildren().add(scrollF);

        //初始化文本显示框
        text2 = new TextArea();

        scrollG = new ScrollPane(text2);
        scrollG.setLayoutX(400);
        scrollG.setLayoutY(400);
        scrollG.setPrefSize(400, 400);
        root.getChildren().add(scrollG);

        //显示对象
        printInit();
        //响应键盘事件
        keyEvent();
        stage.setTitle("My Stage");
        stage.setScene(scene);
        //窗口不可更改大小
        stage.setResizable(false);
        stage.show();
    }

    public void printInit(){
        calabash.add(new Calabash("red", "大娃", 1, 100,0,0,true, canvas,context, text1, text2));
        calabash.add(new Calabash("orange", "二娃", 2, 50, 0, 0, true,canvas,context, text1, text2));
        calabash.add(new Calabash("yellow", "三娃", 3, 180, 0, 0, true,canvas,context, text1, text2));
        calabash.add(new Calabash("green", "四娃", 4, 80, 0, 0, true,canvas,context, text1, text2));
        calabash.add(new Calabash("blue", "五娃", 5, 80, 0, 0, true,canvas,context, text1, text2));
        calabash.add(new Calabash("indigo", "六娃", 6, 40, 0, 0, true,canvas,context, text1, text2));
        calabash.add(new Calabash("purple", "七娃", 7, 50, 0, 0, true,canvas,context, text1, text2));
        grandfather = new GrandFather("爷爷", 8, 250, 0, 0, true, canvas,context, text1, text2);
        scorpion = new Scorpion("蝎子精", 9, 200, 0, 0, false, canvas,context, text1, text2);
        snake = new Snake("蛇精", 10, 150, 0, 0, false, canvas,context, text1, text2);
        frog.add(new Frog("小喽啰1", 11, 50, 0, 0, false, canvas,context, text1, text2));
        frog.add(new Frog("小喽啰2", 12, 50, 0, 0, false, canvas,context, text1, text2));
        frog.add(new Frog("小喽啰3", 13, 50, 0, 0, false, canvas,context, text1, text2));
        frog.add(new Frog("小喽啰4", 14, 50, 0, 0, false, canvas,context, text1, text2));
        frog.add(new Frog("小喽啰5", 15, 50, 0, 0, false, canvas,context, text1, text2));
        frog.add(new Frog("小喽啰6", 16, 50, 0, 0, false, canvas,context, text1, text2));

        Image image = new Image("file:src/picture/0.png");
        context.drawImage(image,0,0,800,400);

        //决定随机队列
        Random r = new Random();
        for (int i = 0; i < 2; ++i){
            int rand = r.nextInt(TOTAL_OF_QUEUE) % TOTAL_OF_QUEUE;

            //rand = 7;
            switch(rand){
                case HEYI:
                    heYi(i);
                    break;
                case YANXING:
                    yanXing(i);
                    break;
                case CHONGE:
                    choneE(i);
                    break;
                case CHANGSHE:
                    changShe(i);
                    break;
                case YULIN:
                    yuLin(i);
                    break;
                case FANG:
                    fang(i);
                    break;
                case YANYUE:
                    yanYue(i);
                    break;
                case FENG:
                    feng(i);
                    break;
                default:
                    System.err.println("Default queue!");
                    break;
            }
        }

        //显示对象
        printCreature();

    }

    public void printCreature(){
        for (int i = 0; i < ROW; ++i){
            for (int j = 0; j < COL; ++j){
                int tmp = dimen.getXY(i,j);
                if ( tmp> 0){
                    if (tmp > 11)
                        tmp = 11;
                    Image image1 = new Image("file:src/picture/" + tmp + ".png");
                    context.drawImage(image1,i * SQURE, j * SQURE, SQURE, SQURE);
                }
                else if (tmp == -1) {
                    //Image image1 = new Image("file:src/picture/0.png");
                }
            }
        }
    }

    public void setExec(){

        //线程池
        ExecutorService exec = Executors.newCachedThreadPool();

        System.err.println("Exec");
        for (Calabash i : calabash){
            exec.execute(i);
        }
        exec.execute(grandfather);
        exec.execute(scorpion);
        exec.execute(snake);
        for (Frog j : frog){
            exec.execute(j);
        }
        exec.shutdown();
    }

    public void keyEvent(){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE){
                    System.err.println("SPACE");
                    setExec();
                }
                else if (event.getCode() == KeyCode.L){
                    keyL();
                }
                else if (event.getCode() == KeyCode.H){
                    battleInit();
                }
            }
        });
        text1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE){
                    System.err.println("SPACE");
                    setExec();
                }
                else if (event.getCode() == KeyCode.L){
                    keyL();
                }
                else if (event.getCode() == KeyCode.H){
                    battleInit();
                }
            }
        });
        text2.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE){
                    System.err.println("SPACE");
                    setExec();
                }
                else if (event.getCode() == KeyCode.L){
                    keyL();
                }
                else if (event.getCode() == KeyCode.H){
                    battleInit();
                }
            }
        });
    }

    public void battleInit(){
        if (first_time == 0){
            first_time++;
            return;
        }

        //context.clearRect(0, 0, 800, 400);
        Image image = new Image("file:src/picture/0.png");
        context.drawImage(image,0,0,800,400);

        dimen.clear();
        //text1.clear();
        //text2.clear();
        text1.appendText("\n\n\n\n\n\n\n");
        text2.appendText("\n\n\n\n\n\n\n");

        creatureReInit();
        //决定随机队列
        Random r = new Random();
        for (int i = 0; i < 2; ++i){
            int rand = r.nextInt(TOTAL_OF_QUEUE) % TOTAL_OF_QUEUE;

            //rand = 7;
            switch(rand){
                case HEYI:
                    heYi(i);
                    break;
                case YANXING:
                    yanXing(i);
                    break;
                case CHONGE:
                    choneE(i);
                    break;
                case CHANGSHE:
                    changShe(i);
                    break;
                case YULIN:
                    yuLin(i);
                    break;
                case FANG:
                    fang(i);
                    break;
                case YANYUE:
                    yanYue(i);
                    break;
                case FENG:
                    feng(i);
                    break;
                default:
                    System.err.println("Default queue!");
                    break;
            }
        }

        printCreature();
    }

    public void creatureReInit(){
        int j1 = 1;
        for (Calabash i1 : calabash){
            if (j1 == 1)
                i1.setIndex(100);
            else if (j1 == 2)
                i1.setIndex(50);
            else if (j1 == 3)
                i1.setIndex(180);
            else if (j1 == 4)
                i1.setIndex(80);
            else if (j1 == 5)
                i1.setIndex(80);
            else if (j1 == 6)
                i1.setIndex(40);
            else if (j1 == 7)
                i1.setIndex(50);
            j1++;
        }
        grandfather.setIndex(250);
        scorpion.setIndex(200);
        snake.setIndex(150);
        int j2 = 11;
        for (Frog i2 : frog){
            if (j2 == 11)
                i2.setIndex(50);
            else if (j2 == 12)
                i2.setIndex(50);
            else if (j2 == 13)
                i2.setIndex(50);
            else if (j2 == 14)
                i2.setIndex(50);
            else if (j2 == 15)
                i2.setIndex(50);
            else if (j2 == 16)
                i2.setIndex(50);
            j2++;
        }

        for (Calabash x : calabash){
            x.setClear();
        }
        grandfather.setClear();
        scorpion.setClear();
        snake.setClear();
        for (Frog fr : frog){
            fr.setClear();
        }
    }

    public void keyL(){
        File directory = new File("");
        try{
            String currentPath = directory.getCanonicalPath();
            System.err.println("currentPath: " + currentPath);

            JFileChooser fileChooser = new JFileChooser(currentPath);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returenVal = fileChooser.showOpenDialog(fileChooser);
            if (returenVal == JFileChooser.APPROVE_OPTION){
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                System.err.println("filePath: " + filePath);

                try{
                    RandomAccessFile dimenFile = new RandomAccessFile(filePath, "rw");
                    FileChannel fchannel = dimenFile.getChannel();
                    FileLock flock = null;
                    while (true){
                        try{
                            flock = fchannel.lock();
                            break;
                        }catch (Exception e){
                            //System.out.println("The file is used.");
                            Thread.sleep(100);
                        }
                    }
                    System.err.println("Open battle file.");

                    dimenFile.seek(0);
                    int fir = dimenFile.readInt();
                    int index,px,py,x,y;
                    do{
                        index = fir;
                        px = dimenFile.readInt();
                        py = dimenFile.readInt();
                        x = dimenFile.readInt();
                        y = dimenFile.readInt();

                        drawPxPyXY(index,px,py,x,y);
                        try{
                            Thread.sleep(200);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        fir = dimenFile.readInt();
                    }while(fir != 250);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void drawPxPyXY(int index, int px, int py, int x, int y){
        if (index > 11)
            index = 11;
        Image image = new Image("file:src/picture/" + index + ".png");
        Platform.runLater(()->{
            context.clearRect(x * 40, y * 40, 40, 40);
        });
        Platform.runLater(()->{
            context.drawImage(image, x * 40, y * 40, 40, 40);
        });
    }

    public void heYi(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 3);
            if (x < 4)
                x = 4;
            if (y < 4)
                y = 4;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y - 1, 1);
            dimen.setXY( x - 1, y + 1, 2);
            dimen.setXY(x - 2, y - 2, 3);
            dimen.setXY(x - 2, y + 2, 4);
            dimen.setXY(x - 3, y - 3, 5);
            dimen.setXY(x - 3, y + 3, 6);
            dimen.setXY(x - 4, y - 4, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 2)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 3)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 4)
                    i1.setXY(x - 2, y + 2);
                else if (j1 == 5)
                    i1.setXY(x - 3, y - 3);
                else if (j1 == 6)
                    i1.setXY(x - 3, y + 3);
                else if (j1 == 7)
                    i1.setXY(x - 4, y - 4);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("鹤翼");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt(ROW / 2);
            int y = r.nextInt(COL - 3);
            x += ROW / 2;
            if (x > 15)
                x = 15;
            if (y < 4)
                y = 4;
            dimen.setXY(x, y, 9);
            dimen.setXY(x + 1, y - 1, 10);
            dimen.setXY(x + 1, y + 1, 11);
            dimen.setXY(x + 2, y - 2, 12);
            dimen.setXY(x + 2, y + 2, 13);
            dimen.setXY(x + 3, y - 3, 14);
            dimen.setXY(x + 3, y + 3, 15);
            dimen.setXY(x + 4, y - 4, 16);
            scorpion.setXY(x, y);
            snake.setXY(x + 1, y - 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x + 1, y + 1);
                else if (j2 == 12)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 13)
                    i2.setXY(x + 2, y + 2);
                else if (j2 == 14)
                    i2.setXY(x + 3, y - 3);
                else if (j2 == 15)
                    i2.setXY(x + 3, y + 3);
                else if (j2 == 16)
                    i2.setXY(x + 4, y - 4);
                j2++;
            }
            textPrintScor("鹤翼");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void yanXing(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            if (x < 7)
                x = 7;
            if (y < 7)
                y = 7;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y - 1, 1);
            dimen.setXY( x - 2, y - 2, 2);
            dimen.setXY(x - 3, y - 3, 3);
            dimen.setXY(x - 4, y - 4, 4);
            dimen.setXY(x - 5, y - 5, 5);
            dimen.setXY(x - 6, y - 6, 6);
            dimen.setXY(x - 7, y - 7, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 2)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 3)
                    i1.setXY(x - 3, y - 3);
                else if (j1 == 4)
                    i1.setXY(x - 4, y - 4);
                else if (j1 == 5)
                    i1.setXY(x - 5, y - 5);
                else if (j1 == 6)
                    i1.setXY(x - 6, y - 6);
                else if (j1 == 7)
                    i1.setXY(x - 7, y - 7);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("雁行");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            x += ROW / 2;
            if (x > 12)
                x = 12;
            if (y < 7)
                y = 7;
            dimen.setXY(x, y, 9);
            dimen.setXY(x + 1, y - 1, 10);
            dimen.setXY(x + 2, y - 2, 11);
            dimen.setXY(x + 3, y - 3, 12);
            dimen.setXY(x + 4, y - 4, 13);
            dimen.setXY(x + 5, y - 5, 14);
            dimen.setXY(x + 6, y - 6, 15);
            dimen.setXY(x + 7, y - 7, 16);
            scorpion.setXY(x, y);
            snake.setXY(x + 1, y - 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 12)
                    i2.setXY(x + 3, y - 3);
                else if (j2 == 13)
                    i2.setXY(x + 4, y - 4);
                else if (j2 == 14)
                    i2.setXY(x + 5, y - 5);
                else if (j2 == 15)
                    i2.setXY(x + 6, y - 6);
                else if (j2 == 16)
                    i2.setXY(x + 7, y - 7);
                j2++;
            }
            textPrintScor("雁行");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void choneE(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            if (x < 1)
                x = 1;
            if (y > 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y + 1, 1);
            dimen.setXY( x, y + 2, 2);
            dimen.setXY(x - 1, y + 3, 3);
            dimen.setXY(x, y + 4, 4);
            dimen.setXY(x - 1, y + 5, 5);
            dimen.setXY(x, y + 6, 6);
            dimen.setXY(x - 1, y + 7, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 2)
                    i1.setXY(x, y + 2);
                else if (j1 == 3)
                    i1.setXY(x - 1, y + 3);
                else if (j1 == 4)
                    i1.setXY(x, y + 4);
                else if (j1 == 5)
                    i1.setXY(x - 1, y + 5);
                else if (j1 == 6)
                    i1.setXY(x, y + 6);
                else if (j1 == 7)
                    i1.setXY(x - 1, y + 7);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("冲轭");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            x += ROW / 2;
            if (x > 18)
                x = 18;
            if (y > 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x - 1, y + 1, 10);
            dimen.setXY(x, y + 2, 11);
            dimen.setXY(x - 1, y + 3, 12);
            dimen.setXY(x, y + 4, 13);
            dimen.setXY(x - 1, y + 5, 14);
            dimen.setXY(x, y + 6, 15);
            dimen.setXY(x - 1, y + 7, 16);
            scorpion.setXY(x, y);
            snake.setXY(x - 1, y + 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x, y + 2);
                else if (j2 == 12)
                    i2.setXY(x - 1, y + 3);
                else if (j2 == 13)
                    i2.setXY(x, y + 4);
                else if (j2 == 14)
                    i2.setXY(x - 1, y + 5);
                else if (j2 == 15)
                    i2.setXY(x, y + 6);
                else if (j2 == 16)
                    i2.setXY(x - 1, y + 7);
                j2++;
            }
            textPrintScor("冲轭");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void changShe(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            if (y > 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x, y + 1, 1);
            dimen.setXY(x, y + 2, 2);
            dimen.setXY(x, y + 3, 3);
            dimen.setXY(x, y + 4, 4);
            dimen.setXY(x, y + 5, 5);
            dimen.setXY(x, y + 6, 6);
            dimen.setXY(x, y + 7, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x, y + 1);
                else if (j1 == 2)
                    i1.setXY(x, y + 2);
                else if (j1 == 3)
                    i1.setXY(x, y + 3);
                else if (j1 == 4)
                    i1.setXY(x, y + 4);
                else if (j1 == 5)
                    i1.setXY(x, y + 5);
                else if (j1 == 6)
                    i1.setXY(x, y + 6);
                else if (j1 == 7)
                    i1.setXY(x, y + 7);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("长蛇");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL);
            x += ROW / 2;
            if (y > 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x, y + 1, 10);
            dimen.setXY(x, y + 2, 11);
            dimen.setXY(x, y + 3, 12);
            dimen.setXY(x, y + 4, 13);
            dimen.setXY(x, y + 5, 14);
            dimen.setXY(x, y + 6, 15);
            dimen.setXY(x, y + 7, 16);
            scorpion.setXY(x, y);
            snake.setXY(x, y + 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x, y + 2);
                else if (j2 == 12)
                    i2.setXY(x, y + 3);
                else if (j2 == 13)
                    i2.setXY(x, y + 4);
                else if (j2 == 14)
                    i2.setXY(x, y + 5);
                else if (j2 == 15)
                    i2.setXY(x, y + 6);
                else if (j2 == 16)
                    i2.setXY(x, y + 7);
                j2++;
            }
            textPrintScor("长蛇");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void yuLin(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            if (x < 3)
                x = 3;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y - 1, 1);
            dimen.setXY( x - 1, y + 1, 2);
            dimen.setXY(x - 2, y - 2, 3);
            dimen.setXY(x - 2, y + 2, 4);
            dimen.setXY(x - 2, y - 1, 5);
            dimen.setXY(x - 2, y + 1, 6);
            dimen.setXY(x - 3, y, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 2)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 3)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 4)
                    i1.setXY(x - 2, y + 2);
                else if (j1 == 5)
                    i1.setXY(x - 2, y - 1);
                else if (j1 == 6)
                    i1.setXY(x - 2, y + 1);
                else if (j1 == 7)
                    i1.setXY(x - 3, y);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("鱼鳞");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            x += ROW / 2;
            if (x > 16)
                x = 16;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x + 1, y - 1, 10);
            dimen.setXY(x + 1, y + 1, 11);
            dimen.setXY(x + 2, y - 2, 12);
            dimen.setXY(x + 2, y + 2, 13);
            dimen.setXY(x + 2, y - 1, 14);
            dimen.setXY(x + 2, y + 1, 15);
            dimen.setXY(x + 3, y, 16);
            scorpion.setXY(x, y);
            snake.setXY(x + 1, y - 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x + 1, y + 1);
                else if (j2 == 12)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 13)
                    i2.setXY(x + 2, y + 2);
                else if (j2 == 14)
                    i2.setXY(x + 2, y - 1);
                else if (j2 == 15)
                    i2.setXY(x + 2, y + 1);
                else if (j2 == 16)
                    i2.setXY(x + 3, y);
                j2++;
            }
            textPrintScor("鱼鳞");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void fang(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            if (x < 4)
                x = 4;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y - 1, 1);
            dimen.setXY( x - 1, y + 1, 2);
            dimen.setXY(x - 2, y - 2, 3);
            dimen.setXY(x - 2, y + 2, 4);
            dimen.setXY(x - 3, y - 1, 5);
            dimen.setXY(x - 3, y + 1, 6);
            dimen.setXY(x - 4, y, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 2)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 3)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 4)
                    i1.setXY(x - 2, y + 2);
                else if (j1 == 5)
                    i1.setXY(x - 3, y - 1);
                else if (j1 == 6)
                    i1.setXY(x - 3, y + 1);
                else if (j1 == 7)
                    i1.setXY(x - 4, y);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("方円");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            x += ROW / 2;
            if (x > 15)
                x = 15;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x + 1, y - 1, 10);
            dimen.setXY(x + 1, y + 1, 11);
            dimen.setXY(x + 2, y - 2, 12);
            dimen.setXY(x + 2, y + 2, 13);
            dimen.setXY(x + 3, y - 1, 14);
            dimen.setXY(x + 3, y + 1, 15);
            dimen.setXY(x + 4, y, 16);
            scorpion.setXY(x, y);
            snake.setXY(x + 1, y - 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x + 1, y + 1);
                else if (j2 == 12)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 13)
                    i2.setXY(x + 2, y + 2);
                else if (j2 == 14)
                    i2.setXY(x + 3, y - 1);
                else if (j2 == 15)
                    i2.setXY(x + 3, y + 1);
                else if (j2 == 16)
                    i2.setXY(x + 4, y);
                j2++;
            }
            textPrintScor("方円");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void yanYue(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            if (x < 2)
                x = 2;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x, y - 1, 1);
            dimen.setXY(x, y + 1, 2);
            dimen.setXY(x - 1, y, 3);
            dimen.setXY(x - 1, y - 1, 4);
            dimen.setXY(x - 1, y + 1, 5);
            dimen.setXY(x - 2, y - 2, 6);
            dimen.setXY(x - 2, y + 2, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x, y - 1);
                else if (j1 == 2)
                    i1.setXY(x, y + 1);
                else if (j1 == 3)
                    i1.setXY(x - 1, y);
                else if (j1 == 4)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 5)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 6)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 7)
                    i1.setXY(x - 2, y + 2);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("偃月");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            x += ROW / 2;
            if (x > 17)
                x = 17;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x, y - 1, 10);
            dimen.setXY(x, y + 1, 11);
            dimen.setXY(x + 1, y, 12);
            dimen.setXY(x + 1, y - 1, 13);
            dimen.setXY(x + 1, y + 1, 14);
            dimen.setXY(x + 2, y - 2, 15);
            dimen.setXY(x + 2, y + 2, 16);
            scorpion.setXY(x, y);
            snake.setXY(x, y - 1);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x, y + 1);
                else if (j2 == 12)
                    i2.setXY(x + 1, y);
                else if (j2 == 13)
                    i2.setXY(x + 1, y - 1);
                else if (j2 == 14)
                    i2.setXY(x + 1, y + 1);
                else if (j2 == 15)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 16)
                    i2.setXY(x + 2, y + 2);
                j2++;
            }
            textPrintScor("偃月");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void feng(int flag){
        Random r = new Random();
        if (flag == FLAG_CALA){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            if (x < 3)
                x = 3;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 8);
            dimen.setXY(x - 1, y, 1);
            dimen.setXY(x - 1, y - 1, 2);
            dimen.setXY(x - 1, y + 1, 3);
            dimen.setXY(x - 2, y, 4);
            dimen.setXY(x - 2, y - 2, 5);
            dimen.setXY(x - 2, y + 2, 6);
            dimen.setXY(x - 3, y, 7);
            grandfather.setXY(x, y);

            dimen.setGood(8);

            int j1 = 1;
            for (Calabash i1 : calabash){
                if (j1 == 1)
                    i1.setXY(x - 1, y);
                else if (j1 == 2)
                    i1.setXY(x - 1, y - 1);
                else if (j1 == 3)
                    i1.setXY(x - 1, y + 1);
                else if (j1 == 4)
                    i1.setXY(x - 2, y);
                else if (j1 == 5)
                    i1.setXY(x - 2, y - 2);
                else if (j1 == 6)
                    i1.setXY(x - 2, y + 2);
                else if (j1 == 7)
                    i1.setXY(x - 3, y);
                else if (j1 == 8)
                    i1.setXY(x, y);
                j1++;
            }
            textPrintCala("锋矢");
        }
        else if (flag == FLAG_SCOR){
            int x = r.nextInt( ROW / 2);
            int y = r.nextInt(COL - 2);
            x += ROW / 2;
            if (x > 16)
                x = 16;
            if (y < 2)
                y = 2;
            dimen.setXY(x, y, 9);
            dimen.setXY(x + 1, y, 10);
            dimen.setXY(x + 1, y - 1, 11);
            dimen.setXY(x + 1, y + 1, 12);
            dimen.setXY(x + 2, y, 13);
            dimen.setXY(x + 2, y - 2, 14);
            dimen.setXY(x + 2, y + 2, 15);
            dimen.setXY(x + 3, y, 16);
            scorpion.setXY(x, y);
            snake.setXY(x + 1, y);

            dimen.sestBad(8);

            int j2 = 11;
            for (Frog i2 : frog){
                if (j2 == 11)
                    i2.setXY(x + 1, y - 1);
                else if (j2 == 12)
                    i2.setXY(x + 1, y + 1);
                else if (j2 == 13)
                    i2.setXY(x + 2, y);
                else if (j2 == 14)
                    i2.setXY(x + 2, y - 2);
                else if (j2 == 15)
                    i2.setXY(x + 2, y + 2);
                else if (j2 == 16)
                    i2.setXY(x + 3, y);
                j2++;
            }
            textPrintScor("锋矢");
        }
        else{
            System.err.println("FLAG_CALA or FLAG_SCOR is error.");
        }
    }

    public void textPrintCala(String str){
        text1.appendText("大娃生成");
        text1.appendText("\n二娃生成");
        text1.appendText("\n三娃生成");
        text1.appendText("\n四娃生成");
        text1.appendText("\n五娃生成");
        text1.appendText("\n六娃生成");
        text1.appendText("\n七娃生成");
        text1.appendText("\n爷爷生成");
        text2.appendText("葫芦娃生成：" + str + "\n");
    }

    public void textPrintScor(String str){
        text1.appendText("\n蝎子精生成");
        text1.appendText("\n蛇精生成");
        text1.appendText("\n小喽啰1生成");
        text1.appendText("\n小喽啰2生成");
        text1.appendText("\n小喽啰3生成");
        text1.appendText("\n小喽啰4生成");
        text1.appendText("\n小喽啰5生成");
        text1.appendText("\n小喽啰6生成");
        text2.appendText("妖怪生成：" + str + "\n");
    }

    public static void launchInit(String[]  args){
        Application.launch(args);
    }

}
