import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class WallAndBackGround {
    Game bgr;
    int mapTileNum[][];
    Image image[];

    String filepath;
    public WallAndBackGround(Game bgr, String filepath) {
        this.bgr = bgr;
        this.filepath = filepath;
        mapTileNum = new int[bgr.COLUMNS][bgr.ROWS];
        image = new Image[10];
        loadmap(filepath);
        image[0] = new Image(getClass().getResourceAsStream("/imgbgr/Map/soil2.jpg"));
        image[1] = new Image(getClass().getResourceAsStream("/imgbgr/Map/brick.jpg"));
    }

    public void loadmap(String filepath) {
        try {

            InputStream is = getClass().getResourceAsStream(filepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < bgr.COLUMNS && row < bgr.ROWS) {

                String line = br.readLine();

                while(col < bgr.COLUMNS) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;

                }
                col = 0;
                row++;
            }
            br.close();
            drawMAP(Game.gc);

        }catch(Exception e) {

        }
    }

    public void drawMAP(GraphicsContext gc) {


        for (int i = 0; i < bgr.COLUMNS; i++) {
            for (int j = 0; j < bgr.ROWS; j++) {
                //gc.drawImage(image[(mapTileNum[i][j]==2||mapTileNum[i][j]==1)?1:0],i * bgr.SQUARE_SIZE, j * bgr.SQUARE_SIZE, bgr.SQUARE_SIZE, bgr.SQUARE_SIZE);
                if(mapTileNum[i][j] == 1){
                    Game.movable[i][j] = false;
                    Game.canCreateTurret[i][j] = false;
                }
                else if(mapTileNum[i][j]==2){
                    Game.startPoint.add(new Point(i,j));
                    Game.canCreateTurret[i][j] = false;
                }
                else if(mapTileNum[i][j]==3){
                    Game.endPoint.x = i;
                    Game.endPoint.y = j;
                }
                else if(mapTileNum[i][j]==4){
                    Game.canCreateTurret[i][j] = false;
                }
            }
        }
    }
}