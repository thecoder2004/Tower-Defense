import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.media.MediaPlayer;

public class ButtonAddTurret extends  Button {
    private EnemyManager enemyManager;
    private TurretManager turretManager;
    private boolean dragging;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private Turret temp;
    private int type = 0;
    private double offsetX,offsetY;
    public int price;
    public ButtonAddTurret(int type, EnemyManager enemyManager, TurretManager turretManager){
        this.type = type;
        this.turretManager = turretManager;
        this.enemyManager = enemyManager;
        setDragEvent();
        imageView = new ImageView(Turret.image_list[type]);
        imageView.setFitWidth(Game.getSquareSize());
        imageView.setFitHeight(Game.getSquareSize());
        this.setGraphic(imageView);
        this.setStyle("-fx-background-color: transparent;");
        this.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        if(type==0){
            price = TurretLight.price;

        }
        else if(type==1){
            price = Cannon.price;
        }
        else if(type == 2){
            price=TurretIce.price;
        }
        else if(type == 3){
            price = Mortar.price;
        }
        else if(type == 4){
            price = Air_Defense.price;
        }
        this.setText(String.valueOf(price));
    }
    private void setDragEvent(){
        //Su kien keo tha chuot gom 3 qua trinh: nhan chuot, keo chuot va tha chuot
        this.setOnMousePressed(e -> {
            // Xử lý khi người dùng nhấn chuột
            dragging = true;
            offsetX = e.getX()+this.localToScene(this.getBoundsInLocal()).getMinX();
            offsetY = e.getY()+this.localToScene(this.getBoundsInLocal()).getMinY();
            if(type==0){
                temp = new TurretLight(offsetX/Game.getSquareSize(),offsetY/Game.getSquareSize());
            }
            else if(type==1){
                temp = new Cannon(offsetX/Game.getSquareSize(),offsetY/Game.getSquareSize());
            }
            else if(type == 2){
                temp = new TurretIce(offsetX/Game.getSquareSize(),offsetY/Game.getSquareSize());
            }
            else if(type == 3){
                temp = new Mortar(offsetX/Game.getSquareSize(),offsetY/Game.getSquareSize());
            }
            else if(type == 4){
                temp = new Air_Defense(offsetX/Game.getSquareSize(),offsetY/Game.getSquareSize());
            }


            // Lấy thông tin về vị trí ban đầu của chuột

            //System.out.println(e.getX()+ " " + e.getY());
        });

        this.setOnMouseDragged(e -> {
            // Xử lý khi người dùng di chuyển chuột
            if(dragging){
                // Lấy thông tin về tọa độ mới của chuột
                double newX = e.getX()+this.localToScene(this.getBoundsInLocal()).getMinX();
                double newY = e.getY()+this.localToScene(this.getBoundsInLocal()).getMinY();

                // Thực hiện các hành động di chuyển tương ứng

                temp.setX(newX/Game.getSquareSize());
                temp.setY(newY/Game.getSquareSize());
                temp.drawTurret();
                temp.drawRange();
            }

        });

        this.setOnMouseReleased(e -> {
            // Xử lý khi người dùng nhả nút chuột
            // Lấy thông tin về tọa độ mới của chuột
            double finalX = e.getX()+this.localToScene(this.getBoundsInLocal()).getMinX();
            double finalY = e.getY()+this.localToScene(this.getBoundsInLocal()).getMinY();

            // Thực hiện các hành động di chuyển tương ứng
            int column = (int) (finalX/Game.SQUARE_SIZE);
            int row = (int) (finalY/Game.SQUARE_SIZE);
            if(dragging){
                dragging = false;//Co danh dau ket thuc su kien keo chuot = false
                if(column>=0&&column<Game.COLUMNS&&row>=0&&row<Game.ROWS&&canCreateTurret(column,row)){
                    //Neu co the dat tru, thuc hien cac hanh dong tuong ung
                    temp.setX(column);
                    temp.setY(row);
                    Game.movable[column][row] = false;//Khong cho quan linh di qua tai vi tri dat tru
                    Game.canCreateTurret[column][row]=false;//Khong cho hai tru stack len nhau
                    turretManager.addTurret(temp);
                    Game.gold-=this.price;
                    for(int i = 0, n = enemyManager.enemies.size();i<n;++i){
                        Enemy temp = enemyManager.enemies.get(i);
                        if(temp.getHp()<=0) continue;

                        temp.needbfs = true;//Co yeu cau cac quan linh tren ban co thuc hien tim lai duong di
                    }
                }
                System.out.println(column+ " " + row);
                temp = null;
            }

            // Thực hiện các hành động cuối cùng
            // ...

            // Định nghĩa MediaPlayer ở mức độ lớp

// Trong phương thức xử lý sự kiện


        });
    }
    private boolean canCreateTurret(int x, int y){
        //Ham tra ve true neu nhu co the dat tru tai vi tri x y tren ban do
        if(x<0||x>=Game.COLUMNS||y<0||y>=Game.ROWS) return false;//Khong duoc dat tru ngoai map
        if(!Game.canCreateTurret[x][y]) return false;//Lay thong tin vi tri chac chan khong cho dat tru
        for(Enemy enemy: enemyManager.enemies){
            //Tru khong duoc dat tai vi tri co quan dich dang toi hay chuan bi di toi
            if(enemy.current.x==x&&enemy.current.y==y&&enemy.isGround()){
                return false;
            }
            else if(enemy.next.x==x&&enemy.next.y==y&&enemy.isGround()){
                return false;
            }
        }
        //Thuat toan kiem tra xem tru moi duoc dat co chan duong cua quan linh hay khong

        for(int i=0,n=Game.startPoint.size();i<n;++i) {
            Game.movable[x][y] = false;
            Enemy temp = new Monster(Game.startPoint.get(i).x, Game.startPoint.get(i).y);
            if (!temp.bfs((int) temp.getX(), (int) temp.getY(), Game.endPoint.x, Game.endPoint.y)) {
                Game.movable[x][y] = true;
                return false;
            }
            Game.movable[x][y] = true;
        }
        return true;
    }
    public void drawTempTurret(){
        //Ve tru khi co su kien keo chuot
        if(dragging){
            temp.drawTurret();
            //temp.drawRange();
            if(!canCreateTurret((int)temp.getX(),(int)temp.getY())){
                temp.drawTurretFail();
            }
            else{
                temp.drawRange();
            }
        }
    }
}
