import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EnemyManager {
    public List<Enemy> enemies = new ArrayList<>();
    //Them list luu lai quai chet o day
    public List<Enemy> enemies_death = new ArrayList<>();
    private int create = 0;
    private int waitMax = 5;
    private int wait = 0;
    private boolean needWait = false;
    protected MediaPlayer mediaplayer;
    private WaveManager waveManager;

    private int createMax = 1 * Game.FPS;//moi 2 giay ta them mot con quai

    public EnemyManager(WaveManager waveManager) {
        this.waveManager = waveManager;

        /*bfs();*/
        for (Enemy enemy : enemies) {
            enemy.bfs((int) Math.round(enemy.getX()), (int) Math.round(enemy.getY()),
                    Game.endPoint.x, Game.endPoint.y);
        }
    }


    public void draw() {
        try {
            for (Enemy enemy : enemies) {
                enemy.drawEnemy();//Ve tat ca cac con quai
            }

        } catch (Exception e) {
            System.out.println("Lỗi ở hàm draw trong class EnemyManager");
        }
    }

    public void addEnemy() {
        if (waveManager.index_wave >= waveManager.waves.size()) return;
        if (!needWait) {
            if (waveManager.index_enemy < waveManager.waves.get(waveManager.index_wave).wave.size()) {
                int i = (int) (Math.random() * Game.startPoint.size());
                if (waveManager.waves.get(waveManager.index_wave).wave.get(waveManager.index_enemy) == 1)
                    enemies.add(new Monster(Game.startPoint.get(i).x, Game.startPoint.get(i).y));
                else if (waveManager.waves.get(waveManager.index_wave).wave.get(waveManager.index_enemy) == 2)
                    enemies.add(new Assasin(Game.startPoint.get(i).x, Game.startPoint.get(i).y));
                else if (waveManager.waves.get(waveManager.index_wave).wave.get(waveManager.index_enemy) == 3)
                    enemies.add(new Dragon(Game.startPoint.get(i).x, Game.startPoint.get(i).y));
                else if (waveManager.waves.get(waveManager.index_wave).wave.get(waveManager.index_enemy) == 4)
                    enemies.add(new Death(Game.startPoint.get(i).x, Game.startPoint.get(i).y));
            }
            ++waveManager.index_enemy;

            if (waveManager.index_enemy >= waveManager.waves.get(waveManager.index_wave).wave.size()) {
                needWait = true;
            }

        } else {

            if (wait < waitMax) {
                ++wait;
            } else if (enemies.isEmpty()) {
                Monster.hpmax *= 1.125;
                Death.hpmax *= 1.125;
                Dragon.hpmax *= 1.125;
                Assasin.hpmax *= 1.125;

                needWait = false;
                wait = 0;
                waveManager.index_enemy = 0;
                ++waveManager.index_wave;
            }
        }

    }


    public void updateEnemy() {
        ++create;
        if (create >= createMax) {//Thuc hien tao con quai
            addEnemy();
            create = 0;
        }
        if (enemies.isEmpty()) {//Neu khong co con quai nao thi khong cap nhat gi
            return;
        }

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.getHp() <= 0) {//Xoa bo quan dich khi het mau
                iterator.remove();
                enemies_death.add(enemy);
                Game.gold += enemy.getPrice();
            } else if (enemy.getX() == Game.endPoint.x && enemy.getY() == Game.endPoint.y) {
                iterator.remove();
                Game.health -= 1;//Khi quan dich cham dich, xoa chung khoi ban do va tru mau nguoi choi
            }
        }

        if (enemies.isEmpty()) {
            return;
        }

        for (Enemy enemy : enemies) {
            if (enemy.paths.isEmpty()) {
                continue;
            }
            enemy.update();//Thuc hien di chuyen cac con quai moi lan cap nhat
        }
        updateSort();
    }

    public void updateSort() {
        enemies.sort(Comparator.comparing((Enemy::distance_square)));
    }

    //**************************//
    //Viet ham xu li am thanh
    public void updateSound() {
        if (create % createMax == 0) {
            //Phat am thanh khoi tao quai khi thoi gian cho de tao quai ket thuc
            int j = enemies_death.size() - 1;
            for (int i = j; i >= 0; --i)
            //Phat am thanh cua con quai khi no chet di va sau do xoa no khoi
            //danh sach con quai da chet
            {

                enemies.remove(enemies_death.get(i));
                enemies_death.remove(i);
                j--;
            }

        }


    }
}
