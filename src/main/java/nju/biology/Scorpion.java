package nju.biology;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

import nju.base.*;

public class Scorpion extends Live implements Runnable{

    private String name;
    public Scorpion(String name, int index, int hp, int x, int y, boolean good, Canvas cvs, GraphicsContext gc, TextArea te1, TextArea te2){
        super(name, hp,x,y,index,good, cvs, gc, te1, te2);
        this.name = name;
    }

    @Override
    public void onHit(int x, int y){
        synchronized (matrix){
            if (matrix.isNotEmpty(x - 1, y) && matrix.getXY(x - 1, y) < 9){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x, y - 1) && matrix.getXY(x, y - 1) < 9){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x + 1, y) && matrix.getXY(x + 1, y)  < 9){
                if (!reduceHp(20)){
                    return;
                }
            }
            if (matrix.isNotEmpty(x, y + 1) && matrix.getXY(x, y + 1) < 9){
                if (!reduceHp(20)){
                    return;
                }
            }
        }
    }

}
