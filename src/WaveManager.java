import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaveManager {
    public List<Wave> waves = new ArrayList<>();//Danh sach cac wave linh
    public Game game;
    public int index_wave = 0;//Chi so wave linh hien tai
    public int index_enemy = 0;//Chi so quan linh hien tai trong wave linh
    public int level;
    public String[] filepath = {//Doc file
            "/wave/map1.txt",
            "/wave/map2.txt",
            "/wave/map3.txt",
            "/wave/map1.txt",
            "/wave/map1.txt"
    };
    public WaveManager(Game game){
        this.game = game;
        this.level = game.level;
        loadWave();
    }
    public void addWave(Wave wave){//Them wave linh
        this.waves.add(wave);
    }
    public void loadWave()  {//Ham doc wave linh tu file txt
        InputStream is = getClass().getResourceAsStream(filepath[level]);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try{
            while((line = br.readLine())!= null){
                String[] numbers = line.trim().split("\\s+");//Doc cac so theo tung dong, cac so tren dong ngan cach boi dau " "
                ArrayList<Integer> wave = new ArrayList<>();//Mang tam thoi luu cac so doc duoc
                for(String number: numbers){
                    wave.add(Integer.parseInt(number));//Luu cac so vao mang tam thoi
                }
                addWave(new Wave(wave));//Them mang tam thoi vao day
            }
        } catch (IOException e) {
            System.out.println("File read fail");;
        }

    }
}