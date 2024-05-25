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

public class NextLevel {
    private Game game;
    public NextLevel(Game game){
        this.game = game;
    }
    public void showNextMenu() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NextLevel.fxml"));
        Parent next_level = loader.load();
        // load ảnh vào để coi như 1 nút có thể click chuột
        ImageView home = (ImageView)loader.getNamespace().get("homeButton");
        ImageView homeOnMouse = (ImageView)loader.getNamespace().get("homeButtonOnMouse");
        ImageView next = (ImageView)loader.getNamespace().get("nextButton");
        ImageView nextOnMouse = (ImageView)loader.getNamespace().get("nextButtonOnMouse");
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
        next.setVisible(true);
        nextOnMouse.setVisible(false);
        next.setOnMouseEntered(event -> {
            next.setVisible(false);
            nextOnMouse.setVisible(true);
        });
        nextOnMouse.setOnMouseExited(event -> {
            next.setVisible(true);
            nextOnMouse.setVisible(false);
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

        Group group = new Group(); // tạo group để chứa màn hình Game và khung NextLevel hiện đè lên trên
        Scene Game_scene = Main.primaryStage.getScene();
// ấn nút home từ thanh nextLevel để trở về scene Main
        homeOnMouse.setOnMouseClicked(event -> {
            try {
                // Đóng Stage hiện tại
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                //Dừng âm thanh
                MediaPlayer mediaPlayer1 = Game.mediaPlayer1;
                mediaPlayer1.stop();
                // Tạo một instance mới của ChooseLevel và hiển thị scene
                ChooseLevel chooseLevel = new ChooseLevel();
                chooseLevel.showscene(currentStage);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        replayOnMouse.setOnMouseClicked(event -> {
            try {
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                Game new_game = new Game(currentStage,game.level+1);
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        nextOnMouse.setOnMouseClicked(event -> {
            try {
                Stage currentStage = (Stage) homeOnMouse.getScene().getWindow();
                Game new_game = new Game(currentStage,game.level+2);
                //Dừng âm thanh
                MediaPlayer mediaPlayer1 = Game.mediaPlayer1;
                mediaPlayer1.stop();
            }
            catch (Exception ex){
                throw  new RuntimeException(ex);
            }
        });

        next_level.setLayoutX((Game_scene.getWidth() - next_level.getBoundsInLocal().getWidth()) / 2); // Đặt vị trí X
        next_level.setLayoutY((Game_scene.getHeight() - next_level.getBoundsInLocal().getHeight()) / 2); // Đặt vị trí Y
        group.getChildren().add(Game_scene.getRoot()); // thêm màn hình game vào group
        group.getChildren().add(next_level); // thêm thanh NextLevel vào group
        Scene overlayScene = new Scene(group);
        Main.primaryStage.setScene(overlayScene);
        Main.primaryStage.show();
        game.pauseTimeLine();
    }

}
