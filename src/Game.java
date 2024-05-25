
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game extends Application {
    private boolean gameRunning = false;
    private int count_start = 0;
    private int count_start_max = 5 *Game.FPS;
    //Moi mot level chung ta se load mot map tu file text
    private static String[] map = {
            "/maps/map1.txt",
            "/maps/map2.txt",
            "/maps/map3.txt",
            "/maps/map4.txt",
            "/maps/map5.txt"
    };
     WallAndBackGround MAP[] = {
            new WallAndBackGround(this,"/maps/map1.txt"),
             new WallAndBackGround(this,"/maps/map2.txt"),
             new WallAndBackGround(this,"/maps/map3.txt"),
             new WallAndBackGround(this,"/maps/map4.txt"),
             new WallAndBackGround(this,"/maps/map5.txt"),
    };
    public static Image[] bg = {
            new Image("/imgbgr/Map/map1_ground.png"),
            new Image("/imgbgr/Map/map2_ground.png"),
            new Image("/imgbgr/Map/map3_ground.png"),
            new Image("/imgbgr/Map/map4_ground.png"),
            new Image("/imgbgr/Map/map5_ground.png")
    };
    //Background for a map, need to make versatile
    public static Image[] obs = {
            new Image("/imgbgr/Map/map1_obstacles.png"),
            new Image("/imgbgr/Map/map2_obstacles.png"),
            new Image("/imgbgr/Map/map3_obstacles.png"),
            new Image("/imgbgr/Map/map4_obstacles.png"),
            new Image("/imgbgr/Map/map5_obstacles.png")
    };
    public static MediaPlayer mediaPlayer1;


    public int level = 0;//Level hien tai
    public static int health = 0;//Mau cua nha chinh cua nguoi choi
    public static long gold = 0;

    //Đặt kích thước cửa sổ và kích thước mỗi ô vuông
    //Cùng với định nghĩa  số hàng và số cột
    public static final int SQUARE_SIZE =(int) Math.round(Math.min(Screen.getPrimary().getVisualBounds().getHeight()*1.0/18,
            Screen.getPrimary().getVisualBounds().getWidth()*1.0/32));//Kich thuoc cua moi mot o vuong trong ban co
    public static final int ROWS = 18;//So hang
    public static final int COLUMNS = 32;//So cot
    private static final int WIDTH = COLUMNS*SQUARE_SIZE;//chieu dai cua scene
    private static final int HEIGHT = ROWS*SQUARE_SIZE;//chieu cao cua scene


    //Điểm bắt đầu di chuyển của địch và điểm kết thúc di chuyển(căn cứ)
    public static List<Point> startPoint = new ArrayList<>();

    public static Point endPoint = new Point(0,0);

    //Mảng hai chiều kiểm tra xem một vị trí mà quân địch đi vào đc hay ko
    public static boolean[][] movable = new boolean[COLUMNS][ROWS];
    public static boolean[][] canCreateTurret = new boolean[COLUMNS][ROWS];
    //Window của chương trình
    public static Stage primaryStage;
    public static Scene gameScene;
    public static int FPS = 60;//So buc anh tuong ung voi moi giay, o day FPS gan bang 60 de dam bao man hinh khong bi lag
    //public static boolean drawGround = false;//Cờ để xác định khi nào vẽ xong background và tường thì mới vẽ trụ đè lên
    public static GraphicsContext gc;//Noi lưu hoạt ảnh của chương trình
    public static Canvas canvas;//Noi ve background, tru, quai, ket hop voi gc
    TurretManager turretManager;//Tac nhan quản lý trụ
    private List<ButtonAddTurret> buttonAddTurrets = new ArrayList<>();//cac nut keo tha tao mot tru moi
    EnemyManager enemyManager;//Tac nhan quan ly quai
    WaveManager waveManager;
    public static VBox item = new VBox();//VBox chua nhung nut them tru
    public static Pane root = new Pane();//chua tat ca nhung gi tren scene
    //Getter
    public static int getSquareSize(){
        return SQUARE_SIZE;
    }
    public static int getWidth(){
        return WIDTH;
    }
    public static int getHeight(){
        return HEIGHT;
    }
    public static int getRows(){
        return ROWS;
    }
    public static int getColumns(){
        return COLUMNS;
    }
    private boolean timelinePaused = false;
    private Timeline mainTimeline;
    //Chạy chương trình chính
    @Override
    public void start(Stage primaryStage) throws Exception {
        startPoint.clear();
        memset();
        begin();
        Turret turret = new Cannon(0,0);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Tower Defense");
        this.primaryStage.setResizable(false);
        enemyManager = new EnemyManager(waveManager);
        turretManager = new TurretManager(enemyManager);
        canvas = new Canvas(WIDTH,HEIGHT);
        addButtonTurret();
        item.setLayoutX(SQUARE_SIZE*(COLUMNS-1));
        //setup nút Pause
        ImageView pauseButton = new ImageView("imgbgr/Menu/Scene/pauseButton.png");
        pauseButton.setFitHeight(50);
        pauseButton.setFitWidth(50);
        pauseButton.setX(1080);
        pauseButton.setY(0);
        ImageView pauseButton_on_Mouse = new ImageView("imgbgr/Menu/Scene/pauseButton.png");
        pauseButton_on_Mouse.setFitWidth(60);
        pauseButton_on_Mouse.setFitHeight(60);
        pauseButton_on_Mouse.setX(1070);
        pauseButton_on_Mouse.setY(0);
        // khi trỏ chuột đến nút Pause
        pauseButton_on_Mouse.setVisible(false);
        pauseButton.setOnMouseEntered(event -> {
            pauseButton.setVisible(false);
            pauseButton_on_Mouse.setVisible(true);
        });
        pauseButton_on_Mouse.setOnMouseExited(event -> {
            pauseButton.setVisible(true);
            pauseButton_on_Mouse.setVisible(false);
        });
        // khi ấn nút pause
        pauseButton_on_Mouse.setOnMouseClicked(event -> {
            try {
                PauseMenu pause_menu = new PauseMenu(this);
                pause_menu.showPauseMenu();
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });
        //Them man hinh game va nut vao scene
        root.getChildren().addAll(canvas,item,pauseButton,pauseButton_on_Mouse); // mới thêm pauseButton và pauseButtonOnMouse
        gc = canvas.getGraphicsContext2D();
        gameScene = new Scene(root);
        this.primaryStage.setScene(gameScene);
        //Dat man hinh game o goc tren ben trai
        this.primaryStage.setX(0);
        this.primaryStage.setY(0);
        this.primaryStage.show();
        Thread.sleep(500);//Nghi 0.5s truoc khi vao game
        displayTurretRange();
        MAP[level].loadmap(map[level]);
        createTimeLine();
    }
    private void begin(){
        //Khoi tao khi moi vao game

        item = new VBox();
        root = new Pane();
        waveManager = new WaveManager(this);//Tac nhan quan ly wave linh
        health = 20;//Mau
        gold = 550;//Vang
        count_start = 0;
        Monster.hpmax = Monster.hp_default;
        Death.hpmax = Death.hp_default;
        Dragon.hpmax = Dragon.hp_default;
        Assasin.hpmax = Assasin.hp_default;
        gameRunning = false;
    }
    private void createTimeLine(){
        //Moi lan cap nhat man hinh danh ra 6s de thuc hien logic game nhu di chuyen quai, tru ban vao quai
        //Va 11s de ve
        Timeline logic = new Timeline(new KeyFrame(Duration.millis(5),e->{
            logic();
        }));
        logic.setCycleCount(Animation.INDEFINITE);

        Timeline draw = new Timeline(new KeyFrame(Duration.millis(10),e->{
            draw(gc);
        }));
        draw.setCycleCount(Animation.INDEFINITE);
        //Sau nay co the them mot timeline am thanh o day
        Timeline soundtimeline=new Timeline(new KeyFrame(Duration.millis(1000.0/60-10-5),e->{
            sound();
        }));


        //Khi timeline am thanh duoc them, sua logic = 5ms va draw = 10ms va sound = 2ms



        //Timeline nay gop lai cong viec cua hai timeline ben tren, 6+11=17 nen FPS = 1000/17=60, dam bao trai
        //nghiem choi game muot ma
        mainTimeline = new Timeline();
        mainTimeline.getKeyFrames().addAll(logic.getKeyFrames());
        mainTimeline.getKeyFrames().addAll(draw.getKeyFrames());
        mainTimeline.getKeyFrames().addAll(soundtimeline.getKeyFrames());
        mainTimeline.setCycleCount(Animation.INDEFINITE);//cai nay toi cx ko biet tai sao, AI ganh het =))))
        mainTimeline.play();
    }
    private void logic(){
        updateButton();
        if(gameRunning) {//Khi moi vao game phai cho 5 giay
            //Cap nhat buoc di chuyen cua quai, thuc hien bfs neu can
            enemyManager.updateEnemy();
            //thuc hien tan cong quai, truoc khi tan cong quai thi dat lai muc tieu tan cong
            turretManager.update(enemyManager);
            gold += (long) (Math.random() * 1.03);
            if ((waveManager.index_wave >= waveManager.waves.size()) && health > 0) {
                gameRunning = false;  // Dừng game
                if(level == 4){
                    EndGame endGame = new EndGame(this);
                    try {
                        endGame.showEndMenu();
                    } catch (Exception ex){
                        throw new RuntimeException(ex);
                    }
                } else {
                    NextLevel nextLevel = new NextLevel(this);
                    try {
                        nextLevel.showNextMenu();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            if(health <= 0){
                gameRunning = false;  // Dừng game
                LostGame lostMenu = new LostGame(this);
                try {
                    lostMenu.showLoseMenu();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        else if(count_start<count_start_max){
            ++count_start;
            if(count_start>=count_start_max) gameRunning=true;
        }
    }
    private void draw(GraphicsContext gc){

        //MAP[level].drawMAP(gc);//Ham nay se duoc loai bo sau khi ve xong background
        //Thuc hien goi ham ve background o day  ************//////

        drawBackground();

        //*********************/
        enemyManager.draw();
        turretManager.draw();
        //Thuc hien goi ham ve tuong o day **************////

        drawWall();
        for(Enemy enemy: enemyManager.enemies){
            if(!enemy.isGround()) enemy.drawEnemy();
        }

        //*********************/

        //Thuc hien hien thi mau cua nguoi choi
        gc.setFill(Color.WHITE);

        gc.setFont(javafx.scene.text.Font.font("Consolas", 30));
        gc.fillText("Your health: " + health,0,30);
        gc.fillText("Your gold: " + gold,0,60);
        gc.fillText("Wave: " + Math.min((waveManager.index_wave + 1),waveManager.waves.size()) +'/' + waveManager.waves.size(),0,90);
     /*   if(!gameRunning && health == 0){
            gc.setFill(Color.RED);
            gc.setFont(javafx.scene.text.Font.font("Arial", 70));
            //Neu nguoi choi thua thuc hien ve chu "Game over!"
            // Vẽ đoạn văn bản
            String text = "Game Over!";
            double textWidth = gc.getFont().getSize() * text.length();
            double x = (WIDTH - textWidth) / 2;
            double y = HEIGHT / 2;
            gc.fillText(text, x, y);
        }*/
        //Ve tru khi duoc keo tha boi cac button keo tha tao tru
        for(ButtonAddTurret buttonAddTurret:buttonAddTurrets){
            buttonAddTurret.drawTempTurret();
        }
    }
    private void updateButton(){
        for(ButtonAddTurret buttonAddTurret:buttonAddTurrets){
            if(gold<buttonAddTurret.price)
                buttonAddTurret.setDisable(true);
            else
                buttonAddTurret.setDisable(false);
        }
    }
    //Dat tat ca cac o ban co de quai co the di chuyen vao va co the dat tru
    private void memset(){
        for(int i=0;i<COLUMNS;++i){
            for(int j=0;j<ROWS;++j){
                movable[i][j]=true;
                canCreateTurret[i][j] = true;
            }
        }
    }
    private void displayTurretRange(){
        //Neu co mot tru duoc chon tren ban co thuc hien hien thi tam ban va nut de xoa tru va nang cap tru
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,(e->{
            turretManager.displayTurretRange(e);

        }));

    }
    private void addButtonTurret(){//them nut keo tha tru vao game
        ButtonAddTurret addTurret = new ButtonAddTurret(0,enemyManager,turretManager);
        addTurret.setLayoutX(COLUMNS-1);
        addTurret.setLayoutY(0);
        buttonAddTurrets.add(addTurret);

        ButtonAddTurret addTurret1 = new ButtonAddTurret(1,enemyManager,turretManager);
        addTurret1.setLayoutX(COLUMNS);
        addTurret1.setLayoutY(1);
        buttonAddTurrets.add(addTurret1);

        ButtonAddTurret addTurret2 = new ButtonAddTurret(2,enemyManager,turretManager);
        addTurret2.setLayoutX((COLUMNS-1));
        addTurret2.setLayoutY(2);
        buttonAddTurrets.add(addTurret2);

        ButtonAddTurret addTurret3 = new ButtonAddTurret(3,enemyManager,turretManager);
        addTurret2.setLayoutX((COLUMNS-1));
        addTurret2.setLayoutY(3);
        buttonAddTurrets.add(addTurret3);

        ButtonAddTurret addTurret4 = new ButtonAddTurret(4,enemyManager,turretManager);
        addTurret2.setLayoutX((COLUMNS-1));
        addTurret2.setLayoutY(4);
        buttonAddTurrets.add(addTurret4);


        for(Button buttonAddTurret: buttonAddTurrets){
            item.getChildren().add(buttonAddTurret);
            //buttonAddTurret.setLayoutX(item.getLayoutX());
            //buttonAddTurret.setLayoutY(item.getLayoutY());
        }
    }
    //Thuc hien them ham cap nhat am thanh + ve background + ve tuong o khu vuc nay

    //**************************************************
    public void sound(){
        enemyManager.updateSound();
        try {

            mediaPlayer1=Main.mediaPlayer[level];
            mediaPlayer1.setOnEndOfMedia(() -> {
                mediaPlayer1.stop();
                mediaPlayer1.play();
            });
            mediaPlayer1.play();
        } catch (Exception e) {
            System.out.println("Error loading sound: " + e.getMessage());
        }

    }



    public void drawBackground(){
        Game.gc.drawImage(bg[level],0,0,WIDTH,HEIGHT);
    };
    public void drawWall(){
        Game.gc.drawImage(obs[level],0,0,WIDTH,HEIGHT);
    };
// hàm dừng chương trình khi n nút pause
    public void pauseTimeLine(){
        if(!timelinePaused){
            mainTimeline.pause();
            timelinePaused= true;
        }
    }
    //hàm tiếp tục chạy chương trình khi ấn nút play từ menuPause
    public  void resumeTimeLine(){
        if(timelinePaused){
            mainTimeline.play();
            timelinePaused = false;
        }
    }

    //**************************************************

    //primaryStage de chi man hinh Window, level duoc chon de tai map tuong ung
    public Game(Stage primaryStage, int level) throws Exception {
        this.level = level-1;
        this.start(primaryStage);
    }
}