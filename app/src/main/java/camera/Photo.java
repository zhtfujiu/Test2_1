package camera;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 昊天 on 2016/6/26.
 */
public class Photo {
    private static final String JSON_FILENAME="filename";
    private String mFilename;

    //根据给定的文件名构造一个Photo对象
    public Photo(String mFilename){
        this.mFilename=mFilename;
    }
    //JSON序列化方法，在保存以及加载Photo类型的数据时，Crime会用到它
    public Photo(JSONObject json) throws JSONException {
        mFilename=json.getString(JSON_FILENAME);
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject json=new JSONObject();
        json.put(JSON_FILENAME,mFilename);
        return json;
    }

    public String getmFilename() {
        return mFilename;
    }
}
