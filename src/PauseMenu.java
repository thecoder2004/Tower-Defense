import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class PauseMenu {
    private Game game;
    public PauseMenu(Game game){
        this.game = game;
    }

    public void showPauseMenu() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PauseMenu.fxml"));
        Parent pause_game = loader.load();

// load ảnh vào để coi như 1 nút có thể click chuột
        ImageView home = (ImageView)loader.getNamespace().get("homeButton");
        ImageView homeOnMouse = (ImageView)loader.getNamespace().get("homeButtonOnMouse");
        ImageView play = (ImageView)loader.getNamespace().get("playButton");
        ImageView playOnMouse = (ImageView)loader.getNamespace().get("playButtonOnMouse");
        ImageView replay = (ImageView)loader.getNamespace().get("replayButton");
        ImageView replayOnMouse = (ImageView)loader.getNamespace().get("replayButtonOnMouse");

// setup trạng thái các nút khi di chuột đến ( nút ấn phóng to lên và nhỏ lại)
        //nút home
        home.setVisible(true);
        homeOnMouse.setVisible(false);
        home.setOnMouseEntered(event -> {
            home.setVisible(false);
            homeOnMouse.setVisible(true);
        });
        homeOnMouse.setOnMouseExited(event -> {
            home.setVisible(true);
            homeOnMouse.setVisible(false);
        });

        // nút play
        play.setVisible(true);
        playOnMouse.setVisible(false);
        play.setOnMouseEntered(event -> {
            play.setVisible(false);
            playOnMouse.setVisible(true);
        });
        playOnMouse.setOnMouseExited(event -> {
            play.setVisible(true);
            playOnMouse.setVisible(false);
        });


        //nút replay
        replay.setVisible(true);
        replayOnMouse.setVisible(false);
        replay.setOnMouseEntered(event -> {
            replay.setVisible(false);
            replayOnMouse.setVisible(true);
        });
        replayOnMouse.setOnMouseExited(event -> {
            replay.setVisible(true);
            replayOnMouse.setVisible(false);
        });

        Group group = new Group(); // tạo group để chứa màn hình Game và khung menupause hiện đè lên trên
        Scene Game_scene = Main.primaryStage.getScene();

        // khi ấn nút play trong thanh menuPause để ctrinh tiếp tục chạy
        playOnMouse.setOnMouseClicked(event -> {
            group.getChildren().remove(pause_game);
            Main.primaryStage.setScene(Main.primaryStage.getScene());
            game.resumeTimeLine();
            Main.primaryStage.show();

        });
        // ấn nút home từ thanh menuPause đ tr về scene chọn level
        homeOnMouse.setOnMouseClicked(event -> {
            try {
                // Đóng Stage hiện tại
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                //currentStage.close();
                //Dừng âm thanh
                MediaPlayer mediaPlayer1 = Game.mediaPlayer1;
                mediaPlayer1.stop();

                // Tạo một Stage mới
                //currentStage = new Stage();

                // Tạo một instance mới của ChooseLevel và hiển thị scene
                ChooseLevel chooseLevel = new ChooseLevel();
                chooseLevel.showscene(currentStage);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });
        // nt replay từ thanh menuPause để chơi lại màn đó từ đầu
        replayOnMouse.setOnMouseClicked(event -> {
            try {
                // Loại bỏ scene cũ của Game khỏi group

                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                /*
                group.getChildren().remove(Game_scene.getRoot());

                // Loại bỏ pause_game khỏi group
                group.getChildren().remove(pause_game);

                // Tạo một Stage mới
                Stage primaryStage = new Stage();

                // Tạo một instance mới của Game và khởi chạy trò chơi trên Stage mới
                Game replay_game = new Game(primaryStage, 1);
                replay_game.start(primaryStage);

                // Tạo một Scene mới từ Pane root của Game và hiển thị nó trên Stage mới
                Scene replayScene = new Scene(Game.root);
                primaryStage.setScene(replayScene);
                primaryStage.show();*/
                Game new_game = new Game(currentStage,game.level+1);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        // setup vị trí, kích thước khung menuPause
        pause_game.setLayoutX((Game_scene.getWidth() - pause_game.getBoundsInLocal().getWidth()) / 2); // Đặt vị trí X
        pause_game.setLayoutY((Game_scene.getHeight() - pause_game.getBoundsInLocal().getHeight()) / 2); // Đặt vị trí Y
        group.getChildren().add(Game_scene.getRoot()); // thêm màn hình game vào group
        group.getChildren().add(pause_game); // thêm thanh menuPause vào group
        Scene overlayScene = new Scene(group);
        Main.primaryStage.setScene(overlayScene);
        Main.primaryStage.show();
        game.pauseTimeLine();
    }
}
