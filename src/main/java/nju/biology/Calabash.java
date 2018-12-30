package nju.biology;

import javafx.scene.canvas.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import nju.base.Live;

public class Calabash extends Live implements Runnable{

    private String color;
    private String name;
    //定义优先级
    //private int value;

    public Calabash(String color, String name, int index, int hp, int x, int y, boolean good, Canvas cvs, GraphicsContext gc, TextArea te1, TextArea te2) {
        super(name, hp, x, y, index, good, cvs, gc, te1, te2);
        this.color = color;
        this.name = name;
    }

    @Override
    public void onHit(int x, int y){
        synchronized (matrix){
            if (matrix.isNotEmpty(x + 1, y) && matrix.getXY(x + 1, y) > 8){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x, y + 1) && matrix.getXY(x, y + 1) > 8){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x - 1, y) && matrix.getXY(x - 1, y)  > 8){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x, y - 1) && matrix.getXY(x, y - 1) > 8){
                if (!reduceHp(20)){
                    return;
                }
            }
        }

    }

}
