葫芦兄弟
===
![葫芦兄弟](https://github.com/chh15/myJavaRespository/blob/master/src/picture/333.gif)  
![葫芦兄弟](https://github.com/chh15/myJavaRespository/blob/master/src/picture/cala_4.gif)  
## 类结构  
![类](https://github.com/chh15/myJavaRespository/blob/master/src/picture/ClassDiagram1.png)  
**所有生物体的基类**  
**Live类**  
```java
public class Live implements Creature, Runnable{
    //属性
    private String name;
    private int hp;
    private int coordinateX;
    private int coordinateY;
    private int index;
    //方法（实现Runnable接口中的run方法，作为单个线程执行方法）
    public void run(){}
    ... ...
}
```
**其余生物体类**  
```java
public class Calabash extends Live implements Runnable{  
    //重写一些方法，针对不同个体，受击后，损失血量不同
    @Override
    public void onHit(int x, int y){}
    ... ...
}
```
其余类均是继承自**Live**类，实现类似**Calabash**类  
**Dimension类**  
```java
public class Dimension {
    final static int ROW = 20;
    final static int COL = 10；
    //静态数组存储界面区域
    private static int dimension[][] = new int[ROW][COL];
    //方法，实现存储生物体，空格子为-1，有生物体的格子则存储生物体的编号
    ... ...
}
```
绘制战场（20 *10的矩阵，一个格子可以存在一个生物体）  
**Init类**  
```java
public class Init extends Application{
   
    //界面大小，及一些其他全局变量
    final static int WIDTH = 800;
    final static int HEIGHT = 400;
    final static int TEXT = 200;
    final static int SQUARE = 40;
    ... ...   
    //方法，start、keyEvent、setExec等等
    public void start(Stage stage){}
    public void keyEvent(){}
    public void setExec(){}
    ... ...
}
```
Init类继承自Application类，创建整个图形界面，其他所有生物体在其上创建  
**线程的实现**  
```java
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
```
创建一个线程池，每个生物体实现一个线程  
**Live类方法中run方法**  
```java
public void run(){
        Thread.currentThread().setPriority(value);
        while (true){
            synchronized(matrix){
            ... ...
            }
        }
}
```
**记**  
一开始思路出了问题，将循环设置在setExec方法中，企图用每一轮都依次启动所有生物的线程来实  
现同步，但是后来发现多轮之后，线程增长特别快，运行崩溃。后来才意识到问题，将每个线程启动  
后，在各自的线程里面进行循环运动（直到被杀死），最后正确执行  
**同步**  
对整个地图（matrix）上锁，来实现同步，每个个体访问时，其他个体不能访问  
## 面向对象思想  
**封装**  
```java   
public interface Creature {
    public void setDimension(int x, int y, int num);
    public int getDimension(int x, int  y);
    public boolean isDimensionEmpty(int x, int y);
    ... ...   
}
```  
将所有生物公有的一些方法封装成接口，通过继承该接口实现方法，另外，所有的生物类和地图类，均是  
对数据和方法的封装  
**继承**  
将所有生物的公共属性抽象成**Live基类**，同过继承此基类，实现葫芦娃、爷爷和蛇精、蝎子精、小喽啰  
**多态**  
通过在基类中实现基本的方法，在葫芦娃等各个子类中重写这些方法，来达到父类引用指向子类方法，来实现  
不同的操作  
## 运行  
![初始](https://github.com/chh15/myJavaRespository/blob/master/src/picture/Begin.PNG)  
初始界面如上。葫芦娃和妖怪各自按随机队列在战场的随机位置进行初始生成  
**SPACE**按下后，开始运动，双方相互进攻，并逐步损失血量，直至一方全部死去，另一方获胜  
![葫芦娃胜](https://github.com/chh15/myJavaRespository/blob/master/src/picture/Cala_Win.PNG)  
![妖怪胜](https://github.com/chh15/myJavaRespository/blob/master/src/picture/Scor_Win.PNG)  


