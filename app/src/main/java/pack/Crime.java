package pack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

import camera.Photo;

/**
 * Created by 昊天 on 2016/5/10.
 */
public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private Photo mPhoto;
    private boolean mSolved;
    //
    private static final String JSON_ID="id";
    private static final String JSON_TITLE="titel";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";

    public Crime() {
        mID = UUID.randomUUID();
        mDate = new Date();
    }
    public Crime(JSONObject jsonObject){
        try {
            mID=UUID.fromString(jsonObject.getString(JSON_ID));
            if (jsonObject.has(JSON_TITLE)){
                mTitle=jsonObject.getString(JSON_TITLE);
            }
            mSolved=jsonObject.getBoolean(JSON_SOLVED);
            mDate=new Date(jsonObject.getLong(JSON_DATE));
            if (jsonObject.has(JSON_PHOTO)){
                //获取一个JSONObject对象，以此为参数，新建一个Photo对象
                mPhoto=new Photo(jsonObject.getJSONObject(JSON_PHOTO));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public UUID getmID() {
        return mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    //实现Crime对象的JSON序列化
    public JSONObject toJSON() {
        JSONObject json=new JSONObject();
        try {
            json.put(JSON_ID,mID.toString());
            json.put(JSON_TITLE,mTitle);
            json.put(JSON_SOLVED,mSolved);
            json.put(JSON_DATE,mDate.getTime());
            if (mPhoto!=null){
                json.put(JSON_PHOTO,mPhoto.toJSON());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
