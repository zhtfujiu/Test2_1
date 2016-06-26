package camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;

/**
 * Created by 昊天 on 2016/6/26.
 */
public class PictureUtils {
    public static BitmapDrawable getScaledDrawable(Activity a,String path){
        Display display=a.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        //从磁盘读取文件内容
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if (srcHeight>destHeight||srcWidth>destWidth){
            if (srcWidth>srcHeight){
                inSampleSize=Math.round(srcHeight/srcWidth);
            }else {
                inSampleSize=Math.round(srcWidth/srcHeight);
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(a.getResources(),bitmap);
        //缩放至设备的默认显示屏大小
    }
}
