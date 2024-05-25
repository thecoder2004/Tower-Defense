import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class BulletCannon extends Bullet {
    protected static Image[] bulletImage ={
            new Image("imgbgr/Bullet/DanLua/Đạn lửa 4.png"),
            new Image("imgbgr/Bullet/DanLua/Đạn lửa 2.png"),
            new Image("imgbgr/Bullet/DanLua/Đạn lửa 1.png"),
    };
    protected double size = (double)Game.getSquareSize()*0.4;
    protected double velotical = 0.075;//Toc do bay cua vien dan
    public BulletCannon(double x, double y, Enemy target, int level) {
        super(x, y, target, level);
    }
    @Override
    public void update(){
        if(target!=null){
            //dat goc huong vao quai
            angle = Math.atan2(target.getY_Center()-y_center,target.getX_Center()-x_center)*180/Math.PI;
            //Chuyen doi don vi goc cho phu hop
        }
        else{
            angle = 0;
        }
        //Cap nhat vi tri vien dan
        this.x+=velotical*Math.cos(angle*Math.PI/180);//Math lay don vi goc la radian
        this.y+=velotical*Math.sin(angle*Math.PI/180);
        this.x_center = this.x + size/Game.getSquareSize()/2;
        this.y_center = this.y + size/Game.getSquareSize()/2;
    }
    @Override
    public  void draw(){
        Game.gc.save();
        //Rotate lay don vi goc la degree
        Rotate rotation = new Rotate(angle, x_center*Game.getSquareSize(), y_center*Game.getSquareSize());
        Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
        Game.gc.drawImage(bulletImage[level-1],(x)*Game.getSquareSize(),(y)*Game.getSquareSize(),size,size);
        Game.gc.restore();
    }

    @Override
    public boolean isCollision() {
        return false;
    }
}
