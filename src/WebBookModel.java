import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebBookModel {
    public BookModel book;
    //public List<Map<String, Integer>> values = new ArrayList<Map<String, Integer>>();
    public Map<String, Integer> values = new HashMap<>();
    public Integer weight = 0;

    public int getInt(){
        return  weight;
    }
}
