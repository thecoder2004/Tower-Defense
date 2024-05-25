import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.IOException;

public class Tutorial{
    private ListView TutorialList;
    public class setImage{
        ImageView A;
        public void setImage(ImageView A){
            this.A = A;
        }
        public ImageView getImage(){
            return A;
        }
    }
    setImage Back, Back_OnMouse, Tutorial, Tutorial_OnMouse, Monster, Monster_OnMouse, Turret, Turret_OnMouse;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tutorial.fxml"));
    public void setup(setImage A, String nameA, setImage A_OnMouse, String nameB){

        A.setImage((ImageView) loader.getNamespace().get(nameA));
        A_OnMouse.setImage((ImageView) loader.getNamespace().get(nameB));

        A_OnMouse.getImage().setVisible(false);

        setImage finalA = A;
        setImage finalA_OnMouse = A_OnMouse;
        A.getImage().setOnMouseEntered(event -> {
            finalA.getImage().setVisible(false);
            finalA_OnMouse.getImage().setVisible(true);
        });

        A_OnMouse.getImage().setOnMouseExited(event -> {
            finalA.getImage().setVisible(true);
            finalA_OnMouse.getImage().setVisible(false);
        });
    }
    private void setupTextArea(TextArea textArea) {
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setStyle("-fx-background-color: #FFF8C4;"
                + "-fx-control-inner-background: #FFF8C4;"
                + "-fx-background-insets: 0;"
                + "-fx-border-color: #FFF8C4;"
                + "-fx-faint-focus-color: #FFF8C4;");
    }
    public void showscene(Stage primaryStage) throws IOException {

        Parent tutorial = loader.load();
        TextArea textArea = (TextArea) loader.getNamespace().get("Tutorial_Table");
        setupTextArea(textArea);
        ////////////////////////////////////////////////////////////////////////
        ScrollPane scrollPane1 = (ScrollPane) loader.getNamespace().get("SP_Monster");
        scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane1.setVisible(false);
        AnchorPane anchorPane1 = (AnchorPane) loader.getNamespace().get("AP_Monster");
        anchorPane1.setStyle("-fx-background-color: #FFF8C4;");
        ///////////////////////////////////////////////////////////////////////
        ScrollPane scrollPane2 = (ScrollPane) loader.getNamespace().get("SP_Turret");
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane2.setVisible(false);
        AnchorPane anchorPane2 = (AnchorPane) loader.getNamespace().get("AP_Turret");
        anchorPane2.setStyle("-fx-background-color: #FFF8C4;");
        ///////////////////////////////////////////////////////////////////////
        Tutorial = new setImage();
        Tutorial_OnMouse = new setImage();
        setup(Tutorial, "Tutorial", Tutorial_OnMouse, "Tutorial_OnMouse");
        Tutorial_OnMouse.getImage().setOnMouseClicked(event->{
            textArea.setVisible(true);
            scrollPane1.setVisible(false);
            scrollPane2.setVisible(false);
        });
        ///////////////////////////////////////////////////////////////////////

        TextArea textArea1 = (TextArea) loader.getNamespace().get("InfoGai");
        setupTextArea(textArea1);

        TextArea textArea2 = (TextArea) loader.getNamespace().get("InfoSatThu");
        setupTextArea(textArea2);

        TextArea textArea3 = (TextArea) loader.getNamespace().get("InfoTuThan");
        setupTextArea(textArea3);

        TextArea textArea4 = (TextArea) loader.getNamespace().get("InfoRong");
        setupTextArea(textArea4);

        Monster = new setImage();
        Monster_OnMouse = new setImage();
        setup(Monster, "Monster", Monster_OnMouse, "Monster_OnMouse");
        Monster_OnMouse.getImage().setOnMouseClicked(event->{
            textArea.setVisible(false);
            scrollPane1.setVisible(true);
            scrollPane2.setVisible(false);
        });
        ///////////////////////////////////////////////////////////////////////
        TextArea textArea5 = (TextArea) loader.getNamespace().get("Cannon");
        setupTextArea(textArea5);

        TextArea textArea6 = (TextArea) loader.getNamespace().get("Ice");
        setupTextArea(textArea6);

        TextArea textArea7 = (TextArea) loader.getNamespace().get("Light");
        setupTextArea(textArea7);

        TextArea textArea8 = (TextArea) loader.getNamespace().get("PK");
        setupTextArea(textArea8);

        TextArea textArea9 = (TextArea) loader.getNamespace().get("PC");
        setupTextArea(textArea9);

        Turret = new setImage();
        Turret_OnMouse = new setImage();
        setup(Turret, "Turret", Turret_OnMouse, "Turret_OnMouse");
        Turret_OnMouse.getImage().setOnMouseClicked(event->{
            textArea.setVisible(false);
            scrollPane1.setVisible(false);
            scrollPane2.setVisible(true);
        });
        ///////////////////////////////////////////////////////////////////////
        Back = new setImage();
        Back_OnMouse = new setImage();
        setup(Back, "Back", Back_OnMouse, "Back_OnMouse");
        Back_OnMouse.getImage().setOnMouseClicked(event->{
            Main mainscene = new Main();
            try {
                mainscene.start(Main.primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        Scene scene = new Scene(tutorial);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
