package camera;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 昊天 on 2016/6/26.
 */
public class CurrentDateAndTime {
    public static String getCurrentDateAndTime(){
        String str;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        str=simpleDateFormat.format(new Date()).toString();
        return str;
    }
}
