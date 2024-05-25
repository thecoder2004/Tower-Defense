
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class BulletMotal extends Bullet {
    private Mortar mortal;
    public int count_collision = 0;
    protected double velotical = 0.035;
    public int count_collision_max = Game.FPS/3;//=20
    protected static Image[] bulletImage ={//Dan tru
            new Image("/imgbgr/Bullet/DanSungCoi/Bullet Mortar 1.png"),
            new Image("/imgbgr/Bullet/DanSungCoi/Bullet Mortar 2.png"),
            new Image("/imgbgr/Bullet/DanSungCoi/Bullet Mortar 3.png"),
    };
    protected static Image[] boom = {//Hinh anh khi dan no
            new Image(("/imgbgr/Bullet/DanSungCoi/boom1.png")),
            new Image(("/imgbgr/Bullet/DanSungCoi/boom2.png")),
    };
    public double temp_x, temp_y;
    protected double size = Game.SQUARE_SIZE/2;

    private double d1=0, d2=0;
    public BulletMotal(double x, double y, Enemy target, int level, Mortar mortal) {
        super(x, y, target, level);
        this.mortal = mortal;
        target_x = target.x+0.5;
        target_y = target.y+0.5;
        d1 = Math.sqrt((target.getX_Center()-x)*(target.getX_Center()-x) +
                (target.getY_Center()-y)*(target.getY_Center()-y));
    }
    @Override
    public void update(){
        d2 = Math.sqrt((mortal.x_center-x_center)*(mortal.x_center-x_center) +
                (mortal.y_center-y_center)*(mortal.y_center-y_center));
        if(target!=null){
            //dat goc huong vao quai
            angle = Math.atan2(target_y-y_center,target_x-x_center)*180/Math.PI;
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

        temp_x = this.x;
        temp_y = (y-(d1-d2)*d2*0.15);

        angle = Math.atan2(target_y-temp_y,target_x-temp_x)*180/Math.PI;

        //Rotate lay don vi goc la degree
        Rotate rotation = new Rotate(angle, (temp_x+size/Game.getSquareSize()/2)*Game.getSquareSize(), (temp_y+size/Game.getSquareSize()/2)*Game.getSquareSize());
        Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());

        Game.gc.drawImage(bulletImage[level-1],temp_x*Game.getSquareSize(),temp_y*Game.getSquareSize(),size,size);
        Game.gc.restore();
    }
    public boolean isCollision(){
        Rectangle temp = new Rectangle((target_x-0.5)*Game.SQUARE_SIZE,(target_y-0.5)*Game.SQUARE_SIZE,Game.SQUARE_SIZE,Game.SQUARE_SIZE);
        return  this.getBounds().intersects(temp.getBoundsInLocal());
    }
    public void drawExplosion(){
        Game.gc.drawImage(boom[count_collision*2/count_collision_max],(target_x-mortal.area)*Game.SQUARE_SIZE,
                (target_y-mortal.area)*Game.SQUARE_SIZE,2*Game.SQUARE_SIZE*mortal.area,2*Game.SQUARE_SIZE*mortal.area);
    }
}
