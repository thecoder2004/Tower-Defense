import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class BulletAirDefense extends Bullet {
    private Air_Defense airDefense;
    protected double size = (double)Game.getSquareSize()*0.5;
    protected static Image[] bulletImage ={
            new Image("imgbgr/Bullet/DanPhongKhong/Bullet Air Defense 1.png"),
            new Image("imgbgr/Bullet/DanPhongKhong/Bullet Air Defense 2.png"),
            new Image("imgbgr/Bullet/DanPhongKhong/Bullet Air Defense 3.png"),
    };
    public BulletAirDefense(double x, double y, Enemy target, int level, Air_Defense airDefense) {
        super(x, y, target, level);
        this.airDefense = airDefense;
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
        double d1 = Math.sqrt((airDefense.x_center-target.getX_Center())*(airDefense.x_center-target.getX_Center()) + (airDefense.y_center-target.getY_Center())*(airDefense.y_center-target.getY_Center()));
        double d2 = Math.sqrt((airDefense.x_center-x_center)*(airDefense.x_center-x_center) + (airDefense.y_center-y_center)*(airDefense.y_center-y_center));
        double temp_x = x;
        double temp_y = (y-(d1-d2)*d2*0.25);
        angle = Math.atan2(target.getY()-temp_y,target.getX()-temp_x)*180/Math.PI;

        //Rotate lay don vi goc la degree
        Rotate rotation = new Rotate(angle, (temp_x+size/Game.getSquareSize()/2)*Game.getSquareSize(), (temp_y+size/Game.getSquareSize()/2)*Game.getSquareSize());
        Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());

        Game.gc.drawImage(bulletImage[level-1],temp_x*Game.getSquareSize(),temp_y*Game.getSquareSize(),size,size);
        Game.gc.restore();
    }

    @Override
    public boolean isCollision() {
        return false;
    }
}
