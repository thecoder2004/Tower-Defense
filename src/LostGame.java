import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LostGame {
    private Game game;
    public LostGame(Game game){
        this.game = game;
    }

    public void showLoseMenu() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LostGame.fxml"));
        Parent lost_game = loader.load();

// load ảnh vào để coi như 1 nút có thể click chuột
        ImageView home = (ImageView)loader.getNamespace().get("homeButton");
        ImageView homeOnMouse = (ImageView)loader.getNamespace().get("homeButtonOnMouse");
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

        // ấn nút home từ thanh menuPause đ tr về scene chọn level
        homeOnMouse.setOnMouseClicked(event -> {
            try {
                // Đóng Stage hiện tại
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
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
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                Game new_game = new Game(currentStage,game.level+1);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        // setup vị trí, kích thước khung menuPause
        lost_game.setLayoutX((Game_scene.getWidth() - lost_game.getBoundsInLocal().getWidth()) / 2); // Đặt vị trí X
        lost_game.setLayoutY((Game_scene.getHeight() - lost_game.getBoundsInLocal().getHeight()) / 2); // Đặt vị trí Y
        group.getChildren().add(Game_scene.getRoot()); // thêm màn hình game vào group
        group.getChildren().add(lost_game); // thêm thanh menuPause vào group
        Scene overlayScene = new Scene(group);
        Main.primaryStage.setScene(overlayScene);
        Main.primaryStage.show();
        game.pauseTimeLine();
    }
}
