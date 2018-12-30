package nju.matrix;

public class Dimension {

    final static int ROW = 20;
    final static int COL = 10;
    //静态数组存储界面区域
    private static int dimension[][] = new int[ROW][COL];

    private static int total = 0;
    private static int num_good = 0;
    private static int num_bad = 0;
    private static int is_over = 0;
    private static int num_of_file = 0;

    public Dimension(){
        for (int i = 0; i < ROW; ++i){
            for (int j = 0; j < COL; ++j)
                dimension[i][j] = -1;
        }
        total = 0;
        num_good = 0;
        num_bad = 0;
        is_over = 0;
    }

    public void outputTotal(){
        System.err.println("total " + total);
    }

    public int getTotal(){
        return total;
    }

    public void clear(){
        for (int i = 0; i < ROW; ++i){
            for (int j = 0; j < COL; ++j)
                dimension[i][j] = -1;
        }
        total = 0;
        num_good = 8;
        num_bad = 8;
        is_over = 0;
        num_of_file++;
    }

    public int getFile(){
        return num_of_file;
    }

    public void add_file(){
        num_of_file++;
    }

    public void reduceGood(){
        synchronized (Dimension.class){
            if (num_good > 0){
                num_good--;
            }
            else{
                System.err.println("num_good is all dead!");
            }
        }
    }

    public void reduceBad(){
        synchronized (Dimension.class){
            if (num_bad > 0){
                num_bad--;
            }
            else{
                System.err.println("num_bad is all dead!");
            }
        }
    }

    public int getIsOver(){
        return is_over;
    }

    public void setIsOver(int f){
        is_over = f;
    }

    public void setGood(int num){
        num_good = num;
    }

    public void sestBad(int num){
        num_bad = num;
    }

    public int getGood(){
        return num_good;
    }

    public int getBad(){
        return num_bad;
    }

    public void setXY(int x, int y, int num){
        if (x < 0 || x >= ROW || y < 0 || y >= COL){
            System.err.println("Dimension setXY " + x + " " + y + " is error.");
            return;
        }
        dimension[x][y] = num;
        if (num == -1)
            total --;
        else
            total++;
    }

    public int getXY(int x, int y){
        if (x < 0 || x >= ROW || y < 0 || y >= COL){
            System.err.println("Dimension getXY " + x + " " + y + " is error.");
            return -1;
        }
        return dimension[x][y];
    }

    public int getCol(){
        return ROW;
    }

    public int getRow(){
        return COL;
    }

    public boolean isEmpty(int x, int y){
        if (x < 0 || x >= ROW || y < 0 || y >= COL){
            System.err.println("Dimension isEmpty " + x + " " + y + " is error.");
            return false;
        }
        if (dimension[x][y] == -1)
            return true;
        else
            return false;
    }

    public boolean isNotEmpty(int x, int y){
        if (x < 0 || x >=ROW || y < 0 || y >= COL){
            System.err.println("Dimension isNotEmpty " + x + " " + y + " is error.");
            return false;
        }
        if (dimension[x][y] > 0)
            return true;
        else
            return false;
    }

}