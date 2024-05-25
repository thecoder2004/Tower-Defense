import java.util.ArrayList;
import java.util.List;

public class Wave {
    public List<Integer> wave = new ArrayList<>();//Danh sach con linh trong dot,
    //ma hoa theo loai quai
    //1 la yone
    //2 la ....
    public int index = 0;//Chi so hien tai
    public Wave(ArrayList<Integer> wave){
        this.wave = wave;
        index = 0;
    }
}
