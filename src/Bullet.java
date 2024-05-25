import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class Bullet {
    //(x,y) la toa do goc tren ben trai cua vien dan
    protected   double x;
    protected  double y;
    //(x_center,y_center) la vi tri chinh giua cua vien dan
    protected double x_center, y_center;
    public double target_x, target_y;
    protected double velotical = 0.05;//Toc do bay cua vien dan
    protected double angle = 0;//Goc quay cua dan, dam bao cho no huong vao ke dich
    public Enemy target = null;
    int level = 1;
    protected static Image bulletImage = new Image("imgbgr/Bullet/DanLua/Đạn lửa 3.png");
    protected double size = (double)Game.getSquareSize()/3;
    public Bullet(double x, double y, Enemy target, int level){
        this.x = x-size/Game.getSquareSize()/2;//Trong tam cua dan la o tam cua tru
        this.y = y-size/Game.getSquareSize()/2;//Gia tri size/Game.getSquareSize()/2 tra ve ti le
        // kich thuoc vien dan so voi kich thuoc o vuong
        this.x_center = x;
        this.y_center = y;
        this.level = level;
        this.target = target;
    }
    public abstract void update();

    public abstract void draw();
    public abstract boolean isCollision();
    public Rectangle getBounds(){//Tra ve hinh chu nhat bao quanh vien dan
        return new Rectangle(this.x*Game.getSquareSize(), this.y*Game.getSquareSize(),size,size);
    }

    public double getX_center() {
        return x_center;
    }

    public double getY_center() {
        return y_center;
    }

}
