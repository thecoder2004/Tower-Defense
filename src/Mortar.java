import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mortar extends Turret {
    public static Image[] base_turret = {
            new Image("/imgbgr/Turret/Mortal/Mortar 1.png"),
            new Image("/imgbgr/Turret/Mortal/Mortar 2.png"),
            new Image("/imgbgr/Turret/Mortal/Mortar 3.png")
    };
    protected double coolDownMax = 3*Game.FPS;


    //Xem o class TurretIce
    public Image image = image_list[1];//image_list lay tu class Turret
    protected double range = 6.5;//Tam ban cua tru
    protected double range_min = 3;
    protected double range_per_level = 1;
    protected double range_min_per_level = -0.5;
    protected double damage = 500;
    public static int price = 700;

    protected double damagePerLevel = 100;
    protected double area = 1.25;
    protected double area_per_level = 0.25;
    protected MediaPlayer mediaplayer;
    public Mortar(double x, double y) {
        super(x, y);
    }
    private List<BulletMotal> bullets = new ArrayList<>();
    private List<BulletMotal> bullets_collision = new ArrayList<>();

    @Override
    public void drawTurret() {
        try{
            Game.gc.drawImage(base_turret[level-1],x*size,y*size,size,size);


            for(Bullet bullet:bullets){//Ve cac vien dan do sung ban ra
                bullet.draw();
            }
            for(int i = bullets_collision.size()-1;i>=0;--i){
                bullets_collision.get(i).drawExplosion();
                ++bullets_collision.get(i).count_collision;
                if(bullets_collision.get(i).count_collision>=bullets_collision.get(i).count_collision_max){
                    bullets_collision.remove(i);
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
            Game.gc.setFill(Color.web("00ffff",0.28));
            Game.gc.fillOval(x_center*size-range_min*size,y_center*size-range_min*size,
                    range_min*size*2,range_min*size*2);
        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }
    }

    @Override
    public void drawRange() {
        try{
            Game.gc.setFill(Color.web("00ff00",0.28));
            Game.gc.fillOval(x_center*size-range*size,y_center*size-range*size,
                    range*size*2,range*size*2);
            Game.gc.setFill(Color.web("ff00ff",0.28));
            Game.gc.fillOval(x_center*size-range_min*size,y_center*size-range_min*size,
                    range_min*size*2,range_min*size*2);

        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }
    }

    @Override
    protected void setTarget(EnemyManager enemyManager) {
        for (int i = 0, n = enemyManager.enemies.size(); i < n; ++i) {
            Enemy enemyTemp = enemyManager.enemies.get(i);

            if(!enemyTemp.invisible) {
                double dx = this.x - enemyTemp.getX();
                double dy = this.y - enemyTemp.getY();
                if ((dx * dx + dy * dy) <= range * range && (dx*dx + dy*dy)>=range_min*range_min && !enemyTemp.invisible) {
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
                            if ((dx1 * dx1 + dy1 * dy1) <= range * range && (dx1 * dx1 + dy1 * dy1)>=range_min*range_min) {
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

        if (!target.isEmpty()) {
            ++coolDown;


            if (coolDown >= coolDownMax) {//Khi cho hoi chieu du lau thi ban dan vao quan dich

                Iterator<Enemy> iterator1 = target.iterator();
                while (iterator1.hasNext()) {
                    Enemy enemyTemp = iterator1.next();//Duyet qua tung ke dich
                    if (enemyTemp.getHp() <= 0) {//Neu ke dich het mau, xoa no
                        iterator1.remove();
                    }
                    else if(enemyTemp.getX()== Game.endPoint.x && enemyTemp.getY()== Game.endPoint.y){
                        iterator1.remove();//Neu ke dich toi dich thi ta xoa no
                    }
                    else{
                        bullets.add(new BulletMotal(x_center,y_center-0.3,enemyTemp,level,this));//Tao ra vien dan nham vao con quai xuat phat tu vi tri cua tru
                    }
                }
                coolDown = 0;//dat lai thoi gian hoi chieu
            }

        }
        for(int i=bullets.size()-1;i>=0;--i){
            //Neu muc tieu ma dan nham toi het mau hoac no toi dich
            //thi xoa no di

            //Neu dan con quai cham vao ke dich, gay dame cho ke dich va xoa vien dan
            //O day ta kiem tra bang cach kiem tra xem hinh chu nhat bao quanh vien dan
            //va hinh chu nhat bao quanh con quai co giao nhau hay khong
            if(bullets.get(i).isCollision()){
                for(Enemy enemy:enemyManager.enemies){
                    double dx = bullets.get(i).target_x-enemy.getX_Center();
                    double dy = bullets.get(i).target_y-enemy.getY_Center();
                    if(dx*dx + dy*dy <= area*area){
                        enemy.setHp(enemy.getHp()-this.damage);
                    }
                }

                bullets_collision.add(bullets.get(i));
                bullets.remove(i);
            }
            else{
                bullets.get(i).update();
            }
        }
    }

    @Override
    public void setEnemyAffected(EnemyManager enemyManager) {//Cap nhat cac con quai bi anh huong
    }

    @Override
    public void uplevel() {
        this.damage *= 2*level + 1;
        this.range += this.range_per_level;
        this.range_min += this.range_min_per_level;
        this.area += this.area_per_level;
        ++level;
    }



    @Override
    public int getPriceUpdate() {
        return price*(4*level - 1);
    }


}
