import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.util.Iterator;

public class TurretIce extends  Turret{
    public static Image base_turret = new Image("/imgbgr/Turret/Ice/Đế Ice.png");
    public static Image[] shaft_turret = {
            new Image("/imgbgr/Turret/Ice/Turret Ice Level 1.png"),
            new Image("/imgbgr/Turret/Ice/Turret Ice Level 2.png"),
            new Image("/imgbgr/Turret/Ice/Turret Ice Level 3.png"),
    };
    //Ghi de cac thong so can thiet
    protected double range = 5;

    protected double damage = 75;
    protected int duration = 4;
    protected int durationPerLevel = 2;
    protected double damagePerLevel = 0;
    protected double rangePerLevel = 1.5;
    public static int price = 350;

    protected double coolDownMax = 1*Game.FPS;
    protected Image image = image_list[2];
    protected static Image[] imageBullet ={
            new Image("/imgbgr/Bullet/DanBang/Đạn băng 4.png"),
            new Image("/imgbgr/Bullet/DanBang/Đạn băng 2.png"),
            new Image("/imgbgr/Bullet/DanBang/Đạn băng 1.png"),
    };
    protected MediaPlayer mediaplayer;
    private EnemyManager enemyManager;
    public TurretIce(double x, double y) {
        super(x, y);
    }
    private Line laze = new Line();//Doan thang the hien tia dan chinh
    private Line diagonal_line1 = new Line();//Duong cheo thu nhat cua con quai, coi con quai dung trong mot hinh chu nhat
    private Line diagonal_line2 = new Line();//Duong cheo thu hai cua con quai
    @Override
    public void drawTurret() {
        try{
            Game.gc.drawImage(base_turret,x*size,y*size,size,size);
            Game.gc.save();

            //Xoay anh cua tru huong theo muc tieu cua no
            Rotate rotation = new Rotate(angle, x_center*size, y_center*size);
            Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
            Game.gc.drawImage(shaft_turret[level-1],x*size,y*size,size,size);
            Game.gc.restore();//Khoi phuc lai trang thai
            if(coolDown<=coolDownMax/5&&!target.isEmpty()){
                //Ve tia laze huong vao quan dich
                /*temp = target.get(0);
                Game.gc.setStroke(Color.BLUE);
                Game.gc.setLineWidth(1.0);
                if(laze!=null){
                    Game.gc.strokeLine(laze.getStartX(),laze.getStartY(), laze.getEndX(),laze.getEndY());
                }*/
                Game.gc.save();

                Game.gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
                Game.canvas.setOpacity(0.1);
                Game.gc.drawImage(imageBullet[level-1], x*size + 0.5*size, y*size + 0.4*size,range*size,size*0.2);
                Game.canvas.setOpacity(1);
                Game.gc.restore();


            }else{
                if(laze!=null) laze = null;//Sau khi ve xong xoa tia laze di
            }

        }
        catch(Exception e){
            System.out.println("Drown turret failed!");
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

    @Override
    public void drawRange() {
        try{
            Game.gc.setFill(Color.web("00ff00",0.28));
            Game.gc.fillOval(x_center*size-range*size,y_center*size-range*size,
                    range*size*2,range*size*2);
        }
        catch(Exception e){
            System.out.println("Drown failed!");
        }
    }

    @Override
    protected void setTarget(EnemyManager enemyManager) {//Cap nhat muc tieu
        for (int i = 0, n = enemyManager.enemies.size(); i < n; ++i) {
            Enemy enemyTemp = enemyManager.enemies.get(i);
            if(!enemyTemp.invisible) {
                double dx = this.x - enemyTemp.getX();
                double dy = this.y - enemyTemp.getY();
                if ((dx * dx + dy * dy) <= range * range && !enemyTemp.invisible) {//Neu ke dich o trong tam ban cua tru
                    //thi ngam vao chung, uu tien ke dich gan can cu nhat
                    if (target.size() < numberOfTarget && enemyTemp.getHp() > 0 && !target.contains(enemyTemp)) {
                        target.add(enemyTemp);
                    }
                } else {
                    //Neu ke dich duoc chon di ra ngoai tam, thuc hien cap nhat lai muc tieu
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

                if (!target.isEmpty()) {//Neu tim duoc muc tieu, xoay nong sung theo muc tieu
                    temp = target.get(0);
                    angle = Math.atan2(temp.getY() - y, temp.getX() - x) * 180 / Math.PI;
                } else {
                    angle = 120;
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
        //Co vai loai tru khac thuc hien cap nhat muc tieu bi anh huong
        if (!target.isEmpty()) {
            ++coolDown;
            if (coolDown >= coolDownMax) {//Khi cho hoi chieu du lau thi ban dan vao quan dich

                Iterator<Enemy> iterator1 = target.iterator();
                laze = new Line();
                //Ban ra tia laze huong vao ke dich
                laze.setStartX(this.x_center*size);
                laze.setStartY(this.y_center*size);
                laze.setEndX((x_center+range*Math.cos(angle*Math.PI/180))*size);//Do Math.cos nhan dau vao la mot goc
                //don vi radian, ta can thuc hien viec chuyen doi
                laze.setEndY((y_center+range*Math.sin(angle*Math.PI/180))*size);

                for(Enemy enemy: enemyManager.enemies){
                    if(intersect(enemy)){//Neu tia dan cat ngang ke dich
                        //thuc hien lam cham chung
                        if(enemy.isSlowed){
                            enemy.count_slow_max = Math.max(enemy.count_slow_max,enemy.count_slow+duration*Game.FPS);//Neu ke dich da bi lam cham, reset lai thoi gian
                            if(enemy.count_slow>=10000000){//Tranh tran so
                                enemy.count_slow_max-=enemy.count_slow;
                                enemy.count_slow=0;
                            }
                        }
                        else{//Neu chua bi lam cham, dat co lam cham la true
                            enemy.isSlowed = true;
                            enemy.count_slow_max = enemy.count_slow+duration*Game.FPS;
                            if(enemy.count_slow>=10000000) {
                                enemy.count_slow_max-=enemy.count_slow;
                                enemy.count_slow=0;
                            }
                        }
                        enemy.setHp(enemy.getHp() - damage);
                    }

                }

                //Gay ra sat thuong cho ke dich
                while (iterator1.hasNext()) {
                    Enemy enemyTemp = iterator1.next();


                    //Neu mau ke dich da het, xoa chung khoi muc tieu

                    if (enemyTemp.getHp() <= 0) {
                        iterator1.remove();
                    }
                    else if(enemyTemp.getX()== Game.endPoint.x && enemyTemp.getY()== Game.endPoint.y){
                        iterator1.remove();
                    }
                }
                //dat lai thoi gian hoi chieu
                coolDown = 0;
            }
        }

    }

    @Override
    public void setEnemyAffected(EnemyManager enemyManager) {

    }

    @Override
    public void uplevel() {
        this.damage *= 2*level + 1;
        this.range += this.rangePerLevel;
        this.duration += this.durationPerLevel;
        ++level;
    }



    public boolean intersect(Enemy enemy){
        //Ve duong cheo thu nhat cua con quai
        diagonal_line1.setStartX(enemy.getX()*size);
        diagonal_line1.setStartY(enemy.getY()*size);
        diagonal_line1.setEndX((enemy.getX()+1)*size);
        diagonal_line1.setEndY((enemy.getY()+1)*size);
        //Ve duong cheo thu hai cua con quai
        diagonal_line2.setStartX((enemy.getX()+1)*size);
        diagonal_line2.setStartY(enemy.getY()*size);
        diagonal_line2.setEndX((enemy.getX())*size);
        diagonal_line2.setEndY((enemy.getY()+1)*size);
        //Kiem tra xem tia lazer co cat qua mot trong hai duong cheo hay khong, neu co tra ve true,
        //Co nghia la con quai bi ban trung
        return areLinesIntersecting(laze,diagonal_line1)
                ||areLinesIntersecting(laze,diagonal_line2);
    }
    public static boolean areLinesIntersecting(Line line1, Line line2) {
        //Ham kiem tra hai doan thang co giao nhau hay khong
        double x1 = line1.getStartX();
        double y1 = line1.getStartY();
        double x2 = line1.getEndX();
        double y2 = line1.getEndY();

        double x3 = line2.getStartX();
        double y3 = line2.getStartY();
        double x4 = line2.getEndX();
        double y4 = line2.getEndY();

        // Tính các hệ số để kiểm tra xem hai đường thẳng có cắt nhau hay không
        double denominator = ((x4 - x3) * (y2 - y1)) - ((x2 - x1) * (y4 - y3));
        double numerator1 = ((x1 - x3) * (y4 - y3)) - ((y1 - y3) * (x4 - x3));
        double numerator2 = ((x1 - x3) * (y2 - y1)) - ((y1 - y3) * (x2 - x1));

        // Kiểm tra xem hai đường thẳng có cắt nhau hay không
        if (denominator == 0) {
            return numerator1 == 0 && numerator2 == 0; // Hai đường thẳng trùng nhau
        }

        double ua = numerator1 / denominator;
        double ub = numerator2 / denominator;

        return ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1;
    }

    @Override
    public int getPriceUpdate() {
        return price*(4*level - 1);
    }

}
