import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main extends Application {
    public static Stage primaryStage;
    public static Scene scene_main;

    public static Image[][] assasin_images_list = {
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + nhấc chân phải).png")}
    };
    public static Image[][] assasin_tang_hinh={
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + nhấc chân trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (sau lưng + nhấc chân phải + tàng hình).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân phải + tàng hình).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (phải + nhấc chân phải + tàng hình).png")},
            {new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + nhấc chân trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + tàng hình).png"), new Image("/imgbgr/Monster/QuaiSatThu/Quái Sát Thủ (trái + nhấc chân phải + tàng hình).png")}

    };
    public static Image[][] dragon_images_list = {
            {new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Sau + Low).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Sau + Medium).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Sau + High).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Sau + Medium).png")},
            {new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trước + Low).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trước + Medium).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trước + High).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trước + Medium).png")},
            {new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Phải + Low).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Phải + Medium).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Phải + High).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Phải + Medium).png")},
            {new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trái + Low).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trái + Medium).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trái + High).png"), new Image("/imgbgr/Monster/QuaiRong/Quái Rồng (Trái + Medium).png")},

    };

    public static Image[][] monster_images_list = {
            {new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (Sau lưng).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (Sau lưng + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (Sau lưng).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (Sau lưng + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (trái + nhấc chân phải).png")},
            {new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (phải).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (phải + nhấc chân trái).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (phải).png"), new Image("/imgbgr/Monster/QuaiGai/Quái_Gai (phải + nhấc chân phải).png")}

    };
    public static Image[][] death_images_list = {
            {new Image("/imgbgr/Monster/QuaiTuThan/Quái Tử Thần (Sau).png")},
            {new Image("/imgbgr/Monster/QuaiTuThan/Quái Tử Thần (Trước).png")},
            {new Image("/imgbgr/Monster/QuaiTuThan/Quái Tử Thần (Phải).png")},
            {new Image("/imgbgr/Monster/QuaiTuThan/Quái Tử Thần (Trái).png")},
    };
    public static Media[] media={
            new Media(new File("src/sound/divineko13.mp3").toURI().toString()),
            new Media(new File("src/sound/divineko13.mp3").toURI().toString()),
            new Media(new File("src/sound/divineko20.mp3").toURI().toString()),
            new Media(new File("src/sound/divineko20.mp3").toURI().toString()),
            new Media(new File("src/sound/outbreak.mp3").toURI().toString())
    };
    public static MediaPlayer[] mediaPlayer ={
            new MediaPlayer(media[0]),
            new MediaPlayer(media[1]),
            new MediaPlayer(media[2]),
            new MediaPlayer(media[3]),
            new MediaPlayer(media[4])
    };
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
        Parent root = loader.load();
        scene_main = new Scene(root);
        //load id ảnh vào đối tượng coi như là 1 button
        ImageView play = (ImageView)loader.getNamespace().get("playButton");
        ImageView playOnMouse = (ImageView)loader.getNamespace().get("playOnMouseButton");
        ImageView guide = (ImageView)loader.getNamespace().get("guideButton");
        ImageView guideOnMouse = (ImageView)loader.getNamespace().get("guideOnMouseButton");

        // setup nút Play
        //trạng thái ban đầu của nút play
        play.setVisible(true);
        playOnMouse.setVisible(false);
        // sau khi con trỏ chuột trỏ đến
        play.setOnMouseEntered(event -> {
            play.setVisible(false);
            playOnMouse.setVisible(true);
        });
        // khi click chuột
        playOnMouse.setOnMouseClicked(event -> {
            try {
                ChooseLevel choose_Level = new ChooseLevel();
                choose_Level.showscene(primaryStage);
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });
        // khi di chuyển con trỏ chuột rời khỏi nút
        playOnMouse.setOnMouseExited(event -> {
            play.setVisible(true);
            playOnMouse.setVisible(false);
        });

        // Setup nút hướng dẫn
        guide.setVisible(true);
        guideOnMouse.setVisible(false);
        // sau khi con trỏ chuột trỏ đến
        guide.setOnMouseEntered(event -> {
            guide.setVisible(false);
            guideOnMouse.setVisible(true);
        });

        guideOnMouse.setOnMouseClicked(event -> {
            try {
                Tutorial tutorial = new Tutorial();
                tutorial.showscene(primaryStage);
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });

        // khi di chuyển con trỏ chuột rời khỏi nút
        guideOnMouse.setOnMouseExited(event -> {
            guide.setVisible(true);
            guideOnMouse.setVisible(false);
        });

        // in ra main
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tower Defense");
        primaryStage.setScene(scene_main);
        primaryStage.show();
    }
}