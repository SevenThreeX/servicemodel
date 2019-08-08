/**
 * Created by hxgqh on 2016/11/14.
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestGrammer {
    public static void main(String[] args) throws Exception{
        JSONObject obj = JSON.parseObject("{\"a\":\"b\"}");
        for(String k: obj.keySet()){
            System.out.println(k + ": " + obj.getString(k));
        }
        List<String> a= (List<String>)obj.get("a");
        System.out.println(JSON.toJSON(new HashMap<String, Object>(){{put("a", "b");put("c", "d");}}));
        List<String> b = new ArrayList<String>();
    }
}
