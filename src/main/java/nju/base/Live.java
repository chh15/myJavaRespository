package nju.base;

import java.lang.Runnable;
import javafx.application.Platform;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

import javafx.scene.canvas.Canvas;

import nju.matrix.Dimension;

public class Live implements Creature, Runnable{

    final static int HP = 50;
    final static int COOR_X = 0;
    final static int COOR_Y = 0;
    final static int MIDDLE_X = 10;
    final static int MIDDLE_Y = 5;

    private static Canvas cvs;
    private static GraphicsContext gc;

    private static TextArea te1, te2;

    private Image image1, image2;

    private String name;
    private int hp;
    private int coordinateX;
    private int coordinateY;
    private int preX;
    private int preY;
    private int index;
    //本回合已执行动作
    private boolean is_move;
    //本回合移动方向（0-未移动，1-x，2-y）
    private int move_to;
    //阵营
    private boolean is_good;

    //优先级
    private int value;

    public Dimension matrix;

    public Live(){
        this.name = "";
        this.hp = HP;
        this.coordinateX = COOR_X;
        this.coordinateY = COOR_Y;
        this.preX = COOR_X;
        this.preY = COOR_Y;
        this.index = 0;
        this.is_move = false;
        this.move_to = 0;
        this.is_good = false;
        this.value = 1;
        matrix = new Dimension();
    }

    public Live(String name, int hp, int x, int y, int index, boolean goodbad, Canvas cvs, GraphicsContext gc, TextArea te1,TextArea te2){
        this.name = name;
        this.hp = hp;
        this.coordinateX = x;
        this.coordinateY = y;
        this.preX = x;
        this.preY = y;
        this.index = index;
        this.is_move = false;
        this.move_to = 0;
        this.is_good = goodbad;
        this.value = index % 8 + 1;
        matrix = new Dimension();

        this.cvs = cvs;
        this.gc = gc;
        this.te1 = te1;
        this.te2 = te2;

    }

    public void run(){
        Thread.currentThread().setPriority(value);
        while (true){
            synchronized(matrix){
                if (matrix.getIsOver() == 1){
                    break;
                }
                else if (matrix.getIsOver() == 0){
                    if (matrix.getGood() == 0){
                        winBad();
                        matrix.setIsOver(1);
                        break;
                    }
                    else if (matrix.getBad() == 0){
                        winGood();
                        matrix.setIsOver(1);
                        break;
                    }
                    else{
                        if (is_good){
                            Random r = new Random();
                            int next_move = r.nextInt(10);
                            if (next_move >= 4){
                                if ( (coordinateX + 1) <= (MIDDLE_X + 1) && isDimensionEmpty(coordinateX+1,coordinateY)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preX = coordinateX;
                                    move_to = 1;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateX++;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to x + 1 " + coordinateX + " " + coordinateY);
                                }
                                else if ((coordinateY + 1) <= MIDDLE_Y && isDimensionEmpty(coordinateX,coordinateY+1)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preY = coordinateY;
                                    move_to = 2;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateY++;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to y + 1 " + coordinateX + " " + coordinateY);
                                }
                                else if (isDimensionEmpty(coordinateX,coordinateY-1)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preY = coordinateY;
                                    move_to = 2;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateY--;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to y - 1 " + coordinateX + " " + coordinateY);
                                }
                                else if (isDimensionEmpty(coordinateX-1, coordinateY)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preX = coordinateX;
                                    move_to = 1;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateX--;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to x - 1 " + coordinateX + " " + coordinateY);
                                }
                                else{
                                    move_to = 0;
                                    System.err.println(index + " not move " + coordinateX + " " + coordinateY);
                                }
                            }
                        }
                        else{
                            Random r = new Random();
                            int next_move = r.nextInt(10);
                            if (next_move >= 4){
                                if ((coordinateX - 1) >= (MIDDLE_X) && isDimensionEmpty(coordinateX-1,coordinateY)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preX = coordinateX;
                                    move_to = 1;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateX--;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to x - 1 " + coordinateX + " " + coordinateY);
                                }
                                else if ((coordinateY - 1) >= (MIDDLE_Y - 1) && isDimensionEmpty(coordinateX,coordinateY-1)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preY = coordinateY;
                                    move_to = 2;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateY--;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to y - 1 " + coordinateX + " " + coordinateY);
                                }
                                else if (isDimensionEmpty(coordinateX,coordinateY+1)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preY = coordinateY;
                                    move_to = 2;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateY++;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to y + 1 " + coordinateX + " " + coordinateY);
                                }
                                else if (isDimensionEmpty(coordinateX+1, coordinateY)){
                                    setDimension(coordinateX, coordinateY, -1);
                                    preX = coordinateX;
                                    move_to = 1;
                                    drawXY(coordinateX,coordinateY,0);
                                    coordinateX++;
                                    setDimension(coordinateX,coordinateY,index);
                                    drawXY(coordinateX,coordinateY,index);
                                    //writeText(coordinateX,coordinateY);
                                    //System.err.println(index + " move to x + 1 " + coordinateX + " " + coordinateY);
                                }
                                else{
                                    move_to = 0;
                                    System.err.println(index + " not move " + coordinateX + " " + coordinateY);
                                }
                            }
                        }
                        if (is_move){
                            System.err.println("Error " + index + " is_move is true before move");
                        }
                        //is_move = true;
                    }
                }
            }

            onHit(coordinateX,coordinateY);

            if (isDead()){
                writeDead(coordinateX,coordinateY);
                try{
                    Thread.sleep(1500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }

            try{
                Thread.sleep(1500);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onHit(int x, int y){
        synchronized(matrix){
            ;
        }
    }

    public void writeFile(int right){
        synchronized (Live.class){
            try{
                String name = "src/files/dimen" + matrix.getFile();
                RandomAccessFile dimenFile = new RandomAccessFile(name,"rw");
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

                dimenFile.seek(0);
                dimenFile.seek(dimenFile.length());
                if (right == 0){
                    dimenFile.writeInt(index);
                    dimenFile.writeInt(preX);
                    dimenFile.writeInt(preY);
                    dimenFile.writeInt(coordinateX);
                    dimenFile.writeInt(coordinateY);
                    //System.err.println("WriteFile: " + index + " " + coordinateX + " " + coordinateY);
                }
                else{
                    dimenFile.writeInt(250);
                    System.err.println("WriteFile: " + "End " + 250);
                }

                flock.release();
                fchannel.close();
                dimenFile.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public boolean reduceHp(int num){
        synchronized (matrix){
            if  (hp <= num){
                hp = 0;

                matrix.setXY(coordinateX,coordinateY, -1);

                if (index < 9){
                    matrix.reduceGood();
                }
                else {
                    matrix.reduceBad();
                }
                return false;
            }
            else{
                hp -= num;
                return true;
            }
        }
    }

    public boolean isDead(){
        if (hp <= 0)
            return true;
        else
            return false;
    }

    public void winGood(){
        te2.appendText("\n" + "葫芦娃胜利！");
        matrix.add_file();
        writeFile(10);
    }

    public void winBad(){
        te2.appendText("\n" + "妖怪胜利！");
        matrix.add_file();
        writeFile(10);
    }

    public void setXY(int x, int y){
        //setDimension(x,y, -1);
        this.coordinateX = x;
        this.coordinateY = y;
        this.preX = x;
        this.preY = y;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void drawXY(int x, int y, int index){

        if (!Thread.interrupted()){
            if (preX != coordinateX || preY != coordinateY){
                writeFile(0);
            }
        }

        if (index > 11)
            index = 11;
        Image image = new Image("file:src/picture/" + index + ".png");
        //Platform.runLater(()->{
        //    gc.clearRect(x * 40, y * 40, 40, 40);
        //});
        Platform.runLater(()->{
            gc.drawImage(image, x * 40, y * 40, 40, 40);
        });
}

    public void writeText(int x, int y) {
        te1.appendText("\n" + name + "移动到(" + x + "," + y + ")");
    }

    public void writeDead(int x, int y){
        te2.appendText("\n" + name + "被杀死");
        Image image = new Image("file:src/picture/0.png");
        //Platform.runLater(()->{
        //    gc.clearRect(x * 40, y * 40, 40, 40);
        //});
        Platform.runLater(()->{
            gc.drawImage(image, x * 40, y * 40, 40, 40);
        });
    }

    public int getX(){
        return this.coordinateX;
    }

    public int getY(){
        return this.coordinateY;
    }

    public int getPreX(){
        return this.preX;
    }

    public int getPreY(){
        return this.preY;
    }

    public int getIndex(){
        return this.index;
    }
    public int getMoveTo(){
        return this.move_to;
    }

    public void setMoveFlag(boolean f){
        this.is_move = f;
    }

    public boolean getMoveFlag(){
        return this.is_move;
    }

    public void setDimension(int x, int y, int num){
        matrix.setXY(x, y, num);
    }

    public void setClear(){
        matrix.setXY(coordinateX,coordinateY,-1);
    }

    public int getDimension(int x, int y){
        return matrix.getXY(x, y);
    }

    public boolean isDimensionEmpty(int x, int y){
        return matrix.isEmpty(x, y);
    }

}

