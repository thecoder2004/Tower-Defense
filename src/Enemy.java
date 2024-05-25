import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Enemy {
    //Cac thong so cua ke dich gom toa do, van toc, mau
    protected double x;

    protected double y;
    protected double velotical = 0.02;
    //dx va dy dung de chi vi tri tuong doi cua o hien tai va o ben canh no
    //o ben trai la (-1,0), o ben phai la (1,0),..., chi can khi thuc hien
    //truy cap dx va dy, ta dung cung mot chi so, vd 0,1,2,3
    protected static int[] dx = {-1,1,0,0};
    protected static int[] dy = {0,0,-1,1};
    //Mục đích của biến count và step để giảm sai số thập phân, cho quá trình di chuyển không bị si đa =))))
    public int count = 0;
    public int step = (int)(1.0/velotical);//so buoc can thiet de di tu vi tri bat dau cua o hien tai den o tiep theo
    public boolean needbfs = true;
    public Point current = new Point((int)x,(int)y);//Diem the hien vi tri hien tai
    public Point next = new Point((int)x, (int)y);//diem the hien vi tri ke tiep
    public Point[][] parent = new Point[Game.getColumns()][Game.getRows()];//Mang nay de thuc hien truy vet duong di moi lan thuc hien bfs
    public boolean[][] visited = new boolean[Game.getColumns()][Game.getRows()];//Mang nay thuc hien danh dau o ma con quai da duyet qua bfs
    protected double size = Game.getSquareSize();//Kich thuoc cua moi con quai
    int type;
    //Mang static de luu nhung hinh anh cua con quai
    public static Image[] image = {
            new Image("/imgbgr/Monster/Yone/yone_up.png"),//Buc anh nay duoc tai duy nhat khi class moi duoc khoi tao
            new Image("/imgbgr/Monster/Yone/yone_down.png"),
            new Image("/imgbgr/Monster/Yone/yone_right.png"),
            new Image("/imgbgr/Monster/Yone/yone_left.png"),
    };

    //*****************************************//
    //Them bien hoac mang static luu am thanh o day

    protected boolean invisible=false;


    //*****************************************//

    protected static int UP = 0,DOWN = 1, RIGHT = 2, LEFT = 3;//Ma hoa 4 huong bang 4 so nguyen
    protected int currentDirrection = 0;//Huong di hien tai
    protected static int temp;//Thuc hien qua trinh chon duong di ngau nhien neu co nhieu ket qua bfs

    public Queue<Point> paths = new LinkedList<>();//Hang doi luu tru duong di


    protected static double hpmax = 10.0;
    protected double hp = hpmax;
    public boolean isSlowed = false;//Co kiem tra xem con quai co bi lam cham khong
    public int count_slow = 0;//count_slow va count_slow_max de dam bao rang con quai bi lam cham trong x giay sau khi bi chiu hieu ung lam cham

    public int count_slow_max = Game.FPS*2;//Lam cham trong 2 giay
    //Getter and setter
    double getHp() {
        return hp;
    }
    public double getX(){
        return this.x;
    }
    public double getX_Center(){
        return this.x + 0.5;
    }

    public double getY(){
        return this.y;
    }
    public double getY_Center(){
        return this.y + 0.5;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public Enemy(double x, double y){
        this.x = (int)(x);
        this.y = (int)(y);
        current.x = (int)x;
        current.y = (int)y;
        next.x = (int)x;
        next.y = (int)y;
        bfs((int)x,(int)y,Game.endPoint.x,Game.endPoint.y);
    }
    public abstract void drawEnemy();

    //Thuc hien tao 4 ham di chuyen theo 4 huong khac nhau
    //Chu y rang truc x huong sang phai va truc y huong xuong
    protected abstract void moveRight();
    protected abstract void moveLeft();
    protected abstract void moveUp();
    protected abstract void moveDown();
    //Moi lan cap nhat thuc hien di chuyen cac con quai
    public abstract void update();
    public abstract void move();
    //********************************//
    //Ham xu li am thanh

    //********************************//

    //bfs nay toi lay tu 28 tech, khong can phai hieu =))))
    public abstract boolean bfs(int x_begin, int y_begin, int x_end, int y_end);
    public Rectangle getBounds(){//Tra ve mot hinh chu nhat la hinh bao cua con quai
        return new Rectangle(this.x*size, this.y*size, size, size);
    }
    public abstract int getPrice();
    public double distance_square(){
        return (Game.endPoint.x - x)*(Game.endPoint.x - x) + (Game.endPoint.y - y)*(Game.endPoint.y - y);
    }
    public abstract void set_Hp(double hp);
    public abstract double get_Hp();
    public abstract boolean isGround();
}
