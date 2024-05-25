import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TurretManager {
    //Lớp quản lý trụ bao gồm một vector các trụ và một Thread quản lý
    public List<Turret> turrets= new ArrayList<>();
    public boolean turretSelected = false;//co kiem tra xem co tru nao duoc chon khong
    public Turret clickedTurret = null;//tru duoc chon

    private EventHandler<MouseEvent> mouseClickedHandler;
    private static ImageView removeImageView = new ImageView("/imgbgr/Menu/Scene/remove.png");//hinh anh xoa bo tru
    private Button removeTurret = new Button("",removeImageView);//nut xoa tru
    private static ImageView levelUpImageView = new ImageView("/imgbgr/Turret/level_up.png");//hinh anh nang cap tru
    private Button levelUpTurret = new Button("",levelUpImageView);//nut nang cap tru
    private EnemyManager enemyManager;

    public TurretManager(EnemyManager enemyManager){
        try{
            removeImageView.setFitHeight(Game.SQUARE_SIZE);
            removeImageView.setFitWidth(Game.SQUARE_SIZE);
            levelUpImageView.setFitHeight(Game.SQUARE_SIZE);
            levelUpImageView.setFitWidth(Game.SQUARE_SIZE);
            //levelUpTurret.setText("hello");
            this.enemyManager = enemyManager;
        }
        catch (Exception e){
            System.out.println("x<20 and y<15");
        }

    }

    public void draw() {
        try {
            //Ve tat ca cac tru
            for (Turret turret : turrets) {
                turret.drawTurret();
            }
            //Ve tam ban cua tru duoc chon
            if (turretSelected&&clickedTurret!=null) {
                clickedTurret.drawRange();
                Game.gc.setFill(Color.YELLOW);

                Game.gc.setFont(javafx.scene.text.Font.font("Consolas", 30));
                Game.gc.fillText(String.valueOf(clickedTurret.getPriceUpdate()),clickedTurret.x*Game.SQUARE_SIZE,clickedTurret.y*Game.SQUARE_SIZE);
            }


        } catch (Exception e) {
            System.out.println("Loi o ham draw in TurretManager");
        }
    }
    public void addTurret(double x, double y){
        //Khi tạo trụ mới thì đặt bàn movable trong class Game là false để bfs không đi qua tọa độ này
        turrets.add(new TurretLight(x,y));
        if(x>=0&&x<Game.getColumns()&&y>=0&&y<Game.getRows()){
            Game.movable[(int)(x)][(int)(y)] = false;
            Game.canCreateTurret[(int) x][(int) y] = false;
        }
    }
    public void addTurret(Turret temp){
        turrets.add(temp);
        if(temp.x>=0&&temp.x<Game.getColumns()&&temp.y>=0&&temp.y<Game.getRows()) {
            //Khi tạo trụ mới thì đặt bàn movable trong class Game là false để bfs không đi qua tọa độ này
            //Va cac tru khong xep len nhau
            Game.movable[(int)(temp.x)][(int)(temp.y)] = false;
            Game.canCreateTurret[(int) temp.x][(int) temp.y] = false;
        }
    }

    public void attack(EnemyManager enemyManager){//Tan cong ke dich
        for(Turret turret: turrets){
            turret.attack(enemyManager);
        }

    }
    public void update(EnemyManager enemyManager){
        attack(enemyManager);
        if(clickedTurret!=null){
            //Tao nut xoa va nang cap khi mot tru duoc chon
            removeTurret.setLayoutX(this.clickedTurret.getX()*Game.SQUARE_SIZE);
            removeTurret.setLayoutY((this.clickedTurret.getY()+1)*Game.SQUARE_SIZE);
            levelUpTurret.setLayoutX(this.clickedTurret.getX()*Game.SQUARE_SIZE);
            levelUpTurret.setLayoutY((this.clickedTurret.getY()-1)*Game.SQUARE_SIZE);

            //levelUpTurret.setText("hello");
            if(clickedTurret.level>=3||Game.gold < clickedTurret.getPriceUpdate()){
                levelUpTurret.setDisable(true);
            }
            else{
                if(Game.gold < clickedTurret.getPriceUpdate()){
                    levelUpTurret.setDisable(true);
                }
                else{
                    levelUpTurret.setDisable(false);
                }

            }
        }
    }
    public void displayTurretRange(MouseEvent mouseEvent){//Ham xu li su kien khi an vao mot tru se hien ra tam ban cua no
        clickedTurret = null;
        turretSelected = false;
        double mouseX = mouseEvent.getX()/Game.getSquareSize();
        double mouseY = mouseEvent.getY()/Game.getSquareSize();
        for(Turret turret: turrets){
            if((int)turret.getX()==(int)mouseX&&(int)turret.getY()==(int)mouseY){
                clickedTurret = turret;
                turretSelected = true;
            }
        }
        if(this.clickedTurret!=null){
            if(!Game.root.getChildren().contains(removeTurret)){
                Game.root.getChildren().add(removeTurret);
                //Chinh sua cac thong so can thiet
                removeTurret.setLayoutX(this.clickedTurret.getX()*Game.SQUARE_SIZE);
                removeTurret.setLayoutY((this.clickedTurret.getY()+1)*Game.SQUARE_SIZE);
                removeTurret.setMaxWidth(Game.SQUARE_SIZE);
                removeTurret.setMaxHeight(Game.SQUARE_SIZE);
                removeImageView.setStyle("-fx-background-color: black;");

                removeTurret.setStyle("-fx-background-color: transparent;");

                removeTurret.setGraphic(removeImageView);
                removeTurret.setOnAction(ex->{
                    //Khi xoa tru, cap nhat lai o ma tru duoc dat co the di qua va thuc hien bfs cho cac con quai
                    Game.movable[(int)clickedTurret.getX()][(int)clickedTurret.getY()] = true;
                    Game.canCreateTurret[(int)clickedTurret.getX()][(int)clickedTurret.getY()] = true;
                    turrets.remove(clickedTurret);
                    for(Enemy enemy: enemyManager.enemies){
                        enemy.needbfs = true;
                    }
                    clickedTurret = null;
                    turretSelected = false;
                    //Xoa bo nut xoa tru va nang cap tru khi no duoc nhan vao
                    Game.root.getChildren().remove(removeTurret);
                    Game.root.getChildren().remove(levelUpTurret);
                });
            }
            if(!Game.root.getChildren().contains(levelUpTurret)){
                //Gan giong nut xoa tru
                Game.root.getChildren().add(levelUpTurret);
                levelUpTurret.setLayoutX(this.clickedTurret.getX()*Game.SQUARE_SIZE);
                levelUpTurret.setLayoutY((this.clickedTurret.getY()-1)*Game.SQUARE_SIZE);
                levelUpTurret.setMaxWidth(Game.SQUARE_SIZE);
                levelUpTurret.setMaxHeight(Game.SQUARE_SIZE);
                //levelUpImageView.setStyle("-fx-background-color: black;");
                levelUpTurret.setGraphic(levelUpImageView);
                levelUpTurret.setStyle("-fx-background-color: transparent;");



                //levelUpTurret.setText("hello");
                levelUpTurret.setOnAction(ex->{

                    Game.gold-=clickedTurret.getPriceUpdate();
                    clickedTurret.uplevel();
                    clickedTurret = null;
                    turretSelected = false;
                    Game.root.getChildren().remove(removeTurret);
                    Game.root.getChildren().remove(levelUpTurret);
                });
            }

        }
        else{
            //Neu khong co tru nao duoc chon, khong hien thi hai nut an nay
            if(Game.root.getChildren().contains(removeTurret)){
                Game.root.getChildren().remove(removeTurret);
            }
            if(Game.root.getChildren().contains(levelUpTurret)){
                Game.root.getChildren().remove(levelUpTurret);
            }
        }

    }


}




