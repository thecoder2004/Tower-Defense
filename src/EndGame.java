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

public class EndGame {
    private Game game;
    public EndGame(Game game){
        this.game = game;
    }

    public void showEndMenu() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EndGame.fxml"));
        Parent end_game = loader.load();

// load ảnh vào để coi như 1 nút có thể click chuột
        ImageView home = (ImageView)loader.getNamespace().get("homeButton");
        ImageView homeOnMouse = (ImageView)loader.getNamespace().get("homeButtonOnMouse");
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

        Group group = new Group(); // tạo group để chứa màn hình Game và khung menupause hiện đè lên trên
        Scene Game_scene = Main.primaryStage.getScene();

        // ấn nút home từ thanh menuPause đ tr về scene chọn level
        homeOnMouse.setOnMouseClicked(event -> {
            try {
                //Dừng âm thanh
                MediaPlayer mediaPlayer1 = Game.mediaPlayer1;
                mediaPlayer1.stop();

                Main main = new Main();
                main.start(Main.primaryStage);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        // setup vị trí, kích thước khung menuPause
        end_game.setLayoutX((Game_scene.getWidth() - end_game.getBoundsInLocal().getWidth()) / 2); // Đặt vị trí X
        end_game.setLayoutY((Game_scene.getHeight() - end_game.getBoundsInLocal().getHeight()) / 2); // Đặt vị trí Y
        group.getChildren().add(Game_scene.getRoot()); // thêm màn hình game vào group
        group.getChildren().add(end_game); // thêm thanh menuPause vào group
        Scene overlayScene = new Scene(group);
        Main.primaryStage.setScene(overlayScene);
        Main.primaryStage.show();
        game.pauseTimeLine();
    }
}
