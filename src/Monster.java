import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.image.Image;

public class Monster extends  Enemy{
    protected static double hpmax = 3200;
    protected static double hp_default = 3200;//Dung de gan mau tro lai khi mo man moi
    protected int price = 55;

    protected double hp = hpmax;
    protected double velotical = 0.0135;
    public int step = (int)(1.0/velotical);
    public Monster(double x, double y) {
        super(x, y);
    }
    @Override
    double getHp() {
        return hp;
    }
    //ta can ghi de lai hai phuong thuc getHp va setHp vi hp cua hai lop cha va con khac nhau
    @Override
    public void setHp(double hp) {
        this.hp = hp;
    }

    @Override
    public void drawEnemy(){
        if(isSlowed){//Neu con quai bi lam cham, thuc hien ve mot tang bang duoi chan no
            Game.gc.setFill(Color.web("66ccff"));
            Game.gc.fillOval((this.x+0.15)*size,(this.y+0.6)*size,size/2,size/2);
        }
        Game.gc.setFill(Color.web("000000",0.4));
        Game.gc.fillOval((this.x+0.15)*size,(this.y+0.6)*size,size/2,size/2);
        //Ve anh con quai
        Game.gc.drawImage(Main.monster_images_list[currentDirrection][count%Game.FPS/(Game.FPS/4)],x*size, y*size,size,size);
        //Ve mau cua con quai

        Game.gc.setFill(Color.RED);
        Game.gc.fillRect(this.x*size,this.y*size-4, size,4);
        Game.gc.setFill(Color.GREEN);
        Game.gc.fillRect(this.x*size,this.y*size-4, hp/hpmax*size,4);
    }
    @Override
    protected void moveRight(){
        this.currentDirrection = RIGHT;
        this.x += this.velotical;
    }
    @Override
    protected void moveLeft(){
        this.currentDirrection = LEFT;
        this.x -= this.velotical;
    }
    @Override
    protected void moveUp(){
        this.currentDirrection = UP;
        this.y -= this.velotical;
    }
    @Override
    protected void moveDown(){
        this.currentDirrection=DOWN;
        this.y += this.velotical;
    }
    //Moi lan cap nhat thuc hien di chuyen cac con quai
    @Override
    public void update(){
        move();
    }
    @Override
    public void move(){
        //Xác định vị trí ô tiếp theo trong đuờng đi và xác định vị trí tương đối của nó so
        //với ô hiện tại để xác định hướng đi
        if(isSlowed){
            ++count_slow;

            if(count_slow>=count_slow_max){
                isSlowed = false;//Thoat khoi hieu ung lam cham
            }
            if(count_slow%2==0){//Quai bi lam cham 50%
                return;
            }
        }
        if(paths.isEmpty()) return;//Neu khong con duong di, co nghia quan dich da di toi dich
        next = paths.element();//buoc di ke tiep buoc di hien tai
        if(count==step){//Neu nhu quai da di toi buoc ke tiep
            //Dat lai vi tri hien tai, xoa bo sai so lam tron so thuc
            this.x = (int) Math.round(this.x);
            this.y = (int) Math.round(this.y);

            current.x = (int)x;
            current.y = (int)y;
            if(needbfs){//Khi dat mot tru moi, cac con quai can thuc hien bfs
                this.bfs((int) Math.round(x),(int) Math.round(y), Game.endPoint.x,Game.endPoint.y);
                this.needbfs = false;//Khi bfs xong, dat lai la false
                if(!paths.isEmpty()){//Khi con quai chua di toi dich
                    next = paths.element();//Tim buoc di ke tiep
                }
                count = 0;
            }
            else{//Neu khong can bfs thi don gian chi can cap nhat vi tri hien tai va chon diem tiep theo
                paths.remove();

                count = 0;

                if(paths.isEmpty()) return;
                //Game.canCreateTurret[current.x][current.y]=false;
                next = paths.element();
                //Game.canCreateTurret[next.x][next.y]=false;
            }

        }
        //So sanh vi tri tuong doi cua vi tri hien tai va vi tri tiep theo de chon huong di hop ly
        if(current.x<next.x){
            moveRight();
            ++count;
        }
        else if(current.x>next.x){
            moveLeft();
            ++count;
        }
        else if(current.y<next.y){
            moveDown();
            ++count;
        }
        else if(current.y>next.y){
            moveUp();
            ++count;
        }
        //System.out.println(this.x + " " + this.y);
    }



    //********************************//
    //Ham xu li am thanh (override tu class Enemy)
    //public void sound();
    //public void sound_death();
    //********************************//
    //bfs nay toi lay tu 28 tech, khong can phai hieu =))))
    @Override
    public boolean bfs(int x_begin, int y_begin, int x_end, int y_end){
        count = 0;//reset lai bien count
        boolean found = false;//Co kiem tra xem duong di co duoc tim thay hay khong
        this.x = current.x;
        this.y = current.y;
        this.needbfs = false;//neu khong co gi xay ra, sau lan thuc hien bfs nay khong can bfs nua
        visited = new boolean[Game.getColumns()][Game.getRows()];//Dat lai cac o tren ban co la chua duoc duyet qua moi lan thuc hien bfs
        java.util.Queue<Point> q = new LinkedList<>();
        List<Point> path_reverse = new ArrayList<>();
        paths.clear();//Reset lai duong di bang rong moi lan thuc hien bfs
        //Them vi tri hien tai vao trong hang doi
        q.add(new Point(x_begin,y_begin));
        while(!q.isEmpty()){
            //Thuc hien bfs
            Point cur = q.element(); q.remove();
            if(cur.getX() == x_end && cur.getY() == y_end){
                found = true;
                break;
            }
            temp = (int) (Math.random()*2);//Thuc hien duyet theo thu tu ngau nhien de co nhieu duong di da dang
            if(temp==0){//thuc hien loang tren do thi
                for(int i=0;i<4;++i){
                    int u = cur.x + dx[i];
                    int v = cur.y + dy[i];

                    if( u>=0 && u<Game.getColumns() && v>=0 && v<Game.getRows() &&
                            Game.movable[u][v] && !visited[u][v] ){
                        q.add(new Point(u,v));
                        parent[u][v] = new Point(cur.x,cur.y);//Dat tat ca cac o canh o hien tai co parent la o hien tai
                        visited[u][v] = true;//Dat co de khong duyet lai o nay nua
                    }
                }
            }
            else{
                for(int i=3;i>=0;--i){
                    int u = cur.x + dx[i];
                    int v = cur.y + dy[i];

                    if( u>=0 && u<Game.getColumns() && v>=0 && v<Game.getRows() &&
                            Game.movable[u][v] && !visited[u][v] ){
                        q.add(new Point(u,v));
                        parent[u][v] = new Point(cur.x,cur.y);
                        visited[u][v] = true;
                    }
                }
            }
        }
        if(!found) return false;//Neu khong tim thay dich
        Point curr = new Point(x_end,y_end);
        //thuc hien truy nguoc duong di
        while(curr.x!=x_begin||curr.y!=y_begin){
            path_reverse.add(curr);
            curr = parent[curr.x][curr.y];
        }
        for(int i=path_reverse.size()-1;i>=0;--i){
            paths.add(path_reverse.get(i));
        }
        return found;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public void set_Hp(double hp) {
        this.hp = hp;
    }

    @Override
    public double get_Hp() {
        return this.hp;
    }

    @Override
    public boolean isGround() {
        return true;
    }
}
