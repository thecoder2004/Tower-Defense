import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.util.Iterator;

public class TurretLight extends Turret{
    //Gan giong TurretIce, tuy nhien co mot vai su khac biet
    public static Image base_turret = new Image("/imgbgr/Turret/Light/Đế Turret Light.png");
    public static Image[] shaft_turret = {
            new Image("/imgbgr/Turret/Light/Turret Light Level 1.png"),
            new Image("/imgbgr/Turret/Light/Turret Light Level 2.png"),
            new Image("/imgbgr/Turret/Light/Turret Light Level 3.png"),
    };
    protected double range = 4.5;
    protected double range_per_level = 1.5;
    protected double area = 2;
    protected double area_per_level = 0.5;

    protected double damage = 600;
    protected double damagePerLevel = 60;
    protected int coolDownMax = Game.FPS;
    public static ImageView[] image1 = {
            new ImageView("/imgbgr/Bullet/DanSet/Đạn tím 1.png"),
            new ImageView("/imgbgr/Bullet/DanSet/Đạn tím 2.png"),
            new ImageView("/imgbgr/Bullet/DanSet/Đạn tím 5.png"),
    };
    protected MediaPlayer mediaplayer;
    protected MediaPlayer mediaPlayer;
    public static int price = 700;
    public TurretLight(double x, double y){
        super(x,y);
    }
    @Override
    public void drawTurret(){
        try{
            Game.gc.drawImage(base_turret,x*size,y*size,size,size);
            Game.gc.save();
            if(!target.isEmpty()){
                temp = target.get(0);
                angle = Math.atan2(temp.getY()-y,temp.getX()-x)*180/3.1415;
            }
            else{
                angle = 120;
            }
            //Xoay anh cua tru huong theo muc tieu cua no
            Rotate rotation = new Rotate(angle, x_center*size, y_center*size);
            Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
            Game.gc.drawImage(shaft_turret[level-1],x*size,y*size,size,size);
            Game.gc.restore();
            if(coolDown<=coolDownMax/5&&!enemyAffected.isEmpty()){
                //Ve tia laze huong vao quan dich
                if(enemyAffected.isEmpty()||target.isEmpty()) return;
                temp = enemyAffected.get(0);

                Game.gc.save();
                //Ve hinh chu nhat co chieu dai la khoang cach tu tru den muc tieu, sau do xoay anh mot goc tuong ung
                //La goc tao boi nong cua tru so voi truc Ox
                Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
                //Tinh khoang cach giua tru va quai
                double dx = this.x - target.get(0).getX();
                double dy = this.y - target.get(0).getY();
                Game.gc.drawImage(image1[level-1].getImage(), x_center*size-size*0.2, y_center*size - size*0.2,Math.sqrt(dx*dx + dy*dy)*size,size*0.4);
                //Khoi phuc lai trang thai cua gc truoc khi xoay
                Game.gc.restore();


                for(int i=1,n=enemyAffected.size();i<n;++i){
                    //Luu lai trang thai cua gc
                    Game.gc.save();
                    //Xoay anh, lay trong tam la con quai thu i-1
                    double angle1 = Math.atan2(enemyAffected.get(i).getY_Center()-enemyAffected.get(i-1).getY_Center(),
                            enemyAffected.get(i).getX_Center()-enemyAffected.get(i-1).getX_Center())*180/Math.PI;
                    Rotate rotate1 = new Rotate(angle1,enemyAffected.get(i-1).getX_Center()*size,enemyAffected.get(i-1).getY_Center()*size);

                    Game.gc.setTransform(rotate1.getMxx(), rotate1.getMyx(), rotate1.getMxy(), rotate1.getMyy(), rotate1.getTx(), rotate1.getTy());
                    //Tinh khoang cach giua hai con quai gan nhau nhat
                    double dx1 = enemyAffected.get(i).getX() - enemyAffected.get(i-1).getX();
                    double dy1 = enemyAffected.get(i).getY() - enemyAffected.get(i-1).getY();
                    //Ve anh la hinh chu nhat co chieu rong la khoang cach giua hai con quai
                    //Chieu cao la 0.4 o vuong
                    //Nho co buoc xoay anh ben tren nen ta co dan tu con nay lan sang con kia
                    Game.gc.drawImage(image1[level-1].getImage(), enemyAffected.get(i-1).getX_Center()*size, enemyAffected.get(i-1).getY_Center()*size-size*0.2,Math.sqrt(dx1*dx1 + dy1*dy1)*size,size*0.4);
                    Game.gc.restore();
                }
            }

        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }

    }

    @Override
    public void drawTurretFail() {
        try{
            Game.gc.setFill(Color.web("ff0000",0.28));
            Game.gc.fillOval(x_center*size-range*size,y_center*size-range*size,
                    range*size*2,range*size*2);
        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }
    }

    public double getRange() {
        return range;
    }
    @Override
    public void drawRange(){//Ve tam ban cua tru
        try{
            Game.gc.setFill(Color.web("00ff00",0.28));
            Game.gc.fillOval(x_center*size-range*size,y_center*size-range*size,
                    range*size*2,range*size*2);
        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }

    }

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
    @Override
    protected void setTarget(EnemyManager enemyManager) {
        for (int i = 0, n = enemyManager.enemies.size(); i < n; ++i) {
            Enemy enemyTemp = enemyManager.enemies.get(i);
            if (!enemyTemp.invisible) {
                double dx = this.x - enemyTemp.getX();
                double dy = this.y - enemyTemp.getY();
                if ((dx * dx + dy * dy) <= range * range) {
                    if (target.size() < numberOfTarget && enemyTemp.getHp() > 0 && !target.contains(enemyTemp)) {
                        target.add(enemyTemp);
                    }
                } else {
                    if (target.contains(enemyTemp)) {
                        target.remove(enemyTemp);
                        for (int j = 0, m = enemyManager.enemies.size(); j < m; ++j) {
                            Enemy enemyTemp1 = enemyManager.enemies.get(j);
                            double dx1 = this.x - enemyTemp1.getX();
                            double dy1 = this.y - enemyTemp1.getY();
                            if ((dx1 * dx1 + dy1 * dy1) <= range * range) {
                                if (target.size() < numberOfTarget && enemyTemp1.getHp() > 0 && !target.contains(enemyTemp1)) {
                                    target.add(enemyTemp1);
                                }
                            }
                        }
                    }
                }

            }
            else{
                if(target.contains(enemyTemp)){
                    target.remove(enemyTemp);
                }
            }
        }

    }
    @Override
    public void attack(EnemyManager enemyManager) {
        setTarget(enemyManager);//Khi tan cong, thuc tien cong viec tim muc tieu
        setEnemyAffected(enemyManager);//Va tim tat ca cac con quai bi anh huong
        if (!target.isEmpty()) {
            ++coolDown;
            if (coolDown >= coolDownMax) {//Khi cho hoi chieu du lau thi ban dan vao quan dich

                Iterator<Enemy> iterator1 = enemyAffected.iterator();
                while (iterator1.hasNext()) {
                    Enemy enemyTemp = iterator1.next();
                    //Gay dame co ke dich chinh nhieu hon ke dich xung quanh
                    if(enemyTemp==target.get(0)) {
                        enemyTemp.setHp(enemyTemp.getHp() - damage);
                    }
                    else{
                        enemyTemp.setHp(enemyTemp.getHp() - damage*0.3);
                    }

                    if (enemyTemp.getHp() <= 0) {
                        iterator1.remove();
                    }
                    else if(enemyTemp.getX()== Game.endPoint.x && enemyTemp.getY()== Game.endPoint.y){
                        iterator1.remove();
                    }
                }
                Iterator<Enemy> iterator = target.iterator();
                while (iterator.hasNext()) {
                    Enemy enemyTemp = iterator.next();

                    if (enemyTemp.getHp() <= 0) {
                        iterator.remove();
                    }
                    else if(enemyTemp.getX()== Game.endPoint.x && enemyTemp.getY()== Game.endPoint.y){
                        iterator.remove();
                    }
                }
                coolDown = 0;
            }
        }
    }
    @Override
    public void setEnemyAffected(EnemyManager enemyManager){
        enemyAffected.clear();
        if(!target.isEmpty()){
            enemyAffected.add(target.get(0));
            //Khi tim duoc muc tieu chinh, thuc hien tim cac con dich gan nhat lam muc tieu bi lan
            for(int i=1;i<numberOfEnemyAffected;++i){//Duyet cho den khi co the tim duoc so luong toi da
                Enemy temp = enemyAffected.get(i-1);
                Enemy newEnemy = null;
                double nearestSquare = 9;
                //Thuc hien tim con quai gan nhat con quai cuoi cung bi lan tim thay
                for(Enemy enemy:enemyManager.enemies){
                    if(!enemyAffected.contains(enemy)){
                        double dx = enemy.getX() - temp.getX();
                        double dy = enemy.getY() - temp.getY();
                        double distanceSquare = dx*dx + dy*dy;
                        if(distanceSquare<nearestSquare&&distanceSquare<=area*area){
                            newEnemy = enemy;
                            nearestSquare = distanceSquare;
                        }

                    }
                }
                if(newEnemy!=null){
                    enemyAffected.add(newEnemy);
                }
                else{
                    break;
                }
            }
        }
    }

    @Override
    public void uplevel() {
        this.damage *= 2*level + 1;
        this.range += this.range_per_level;
        this.area += this.area_per_level;
        ++level;
    }


    @Override
    public int getPriceUpdate() {
        return price*(4*level - 1);
    }

}