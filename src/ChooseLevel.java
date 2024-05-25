import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseLevel {

    ImageView level[], level_OnMouse[], Back, Back_OnMouse;

    public void showscene(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChooseLevelScene.fxml"));
        Parent choose_level = loader.load();

        level = new ImageView[10];
        level_OnMouse = new ImageView[10];
        for (int i = 1; i <= 5; i++) {
            int type = i;
            level[i] = (ImageView) loader.getNamespace().get(Integer.toString(i));
            level_OnMouse[i] = (ImageView) loader.getNamespace().get(Integer.toString(i) + Integer.toString(i));

            level_OnMouse[i].setVisible(false);

            level[i].setOnMouseEntered(event -> {
                level[type].setVisible(false);
                level_OnMouse[type].setVisible(true);
            });

            level_OnMouse[i].setOnMouseClicked(event->{
                try {
                    Game game = new Game(Main.primaryStage, type);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            level_OnMouse[i].setOnMouseExited(event -> {
                level[type].setVisible(true);
                level_OnMouse[type].setVisible(false);
            });
        }
        Back = (ImageView) loader.getNamespace().get("Back");
        Back_OnMouse = (ImageView) loader.getNamespace().get("Back_OnMouse");

        Back_OnMouse.setVisible(false);

        Back.setOnMouseEntered(event -> {
            Back.setVisible(false);
            Back_OnMouse.setVisible(true);
        });

        Back_OnMouse.setOnMouseClicked(event->{
            Main mainscene = new Main();
            try {
                mainscene.start(Main.primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Back_OnMouse.setOnMouseExited(event -> {
            Back.setVisible(true);
            Back_OnMouse.setVisible(false);
        });

        Scene scene = new Scene(choose_level);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
