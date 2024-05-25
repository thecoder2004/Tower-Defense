import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Turret {
    protected double x,y;
    public double x_center, y_center;
    protected double range = 4;

    protected int coolDown = 0;
    protected int coolDownMax = Game.FPS;

    public static Image[] image_list = {//Tai anh duy nhat mot lam khi khoi tao lop
            new Image("/imgbgr/Turret/Light/Đế Turret Light.png"),
            new Image("/imgbgr/Turret/Cannon/Đế Cannon.png"),
            new Image("/imgbgr/Turret/Ice/Đế Ice.png"),
            new Image("/imgbgr/Turret/Mortal/Mortar 1.png"),
            new Image("/imgbgr/Turret/Air Defense/Air Defense 1.png"),
    };
    protected Image image;
    public int level = 1;
    protected static int size = Game.getSquareSize();//Kich thuoc cua tru, dat no la mot o vuong
    protected List<Enemy> target = new ArrayList<>();
    Enemy temp;
    protected double angle;//Goc quay cua tru. dung de ve sung huong vao muc tieu
    protected int numberOfTarget = 1;//So muc tieu toi da cua tru

    List<Enemy> enemyAffected = new ArrayList<>();
    protected int numberOfEnemyAffected = 5;//so ke dich chiu anh huong, su dung cho sung ban tia set
    protected int damage = 120;
    protected int damagePerLevel = 100000;
    public Turret(double x, double y){
        this.x = (int)(x);
        this.y = (int)(y);
        //Xác định vị trí trung tâm của cái trụ(mot cai tru chiem 1 o)
        this.x_center = x + 0.5;
        this.y_center = y + 0.5;
    }
    public abstract void drawTurret();
    public abstract void drawTurretFail();
    public double getRange() {
        return range;
    }

    public abstract void drawRange();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public void setX(double x){
        this.x = (int)(x);
        this.x_center = (int)x + 0.5;
    }
    public void setY(double y){
        this.y = (int)(y);
        this.y_center = (int)y + 0.5;
    }
    protected abstract void setTarget(EnemyManager enemyManager);
    public abstract void attack(EnemyManager enemyManager);
    public abstract void setEnemyAffected(EnemyManager enemyManager);
    public abstract void uplevel();
    public abstract int getPriceUpdate();
}
