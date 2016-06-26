package camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test2_1.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 昊天 on 2016/6/5.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG="CrimeCameraFragment";
    private Camera camera;
    private SurfaceView surfaceView;
    private View mProgressBarContainer;
    private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    };
    private Camera.PictureCallback mJPEGCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            String fileName= UUID.randomUUID().toString()+".jpg";
            String fileName= CurrentDateAndTime.getCurrentDateAndTime()+".jpg";
            FileOutputStream fos=null;
            boolean isSuccess=true;
            try {
                fos=getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                isSuccess=false;
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess=false;
            }finally {
                if (fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        isSuccess=false;
                    }
                }
            }

            if (isSuccess){
                Log.i(TAG,"JPEG saved at "+fileName);
            }
            getActivity().finish();
        }
    };


    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_camera,container,false);
        Button button= (Button) view.findViewById(R.id.fragment_crime_camera_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera!=null){
                    camera.takePicture(mShutterCallback,null,mJPEGCallback);
                }
            }
        });
        surfaceView= (SurfaceView) view.findViewById(R.id.fragment_crime_camera_surface);
        SurfaceHolder holder=surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (camera!=null){
                    try {
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (camera==null)return;

                Camera.Parameters parameters=camera.getParameters();
                Camera.Size size=getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(size.width,size.height);
                //
                size=getBestSupportedSize(parameters.getSupportedPictureSizes(),width,height);
                parameters.setPictureSize(size.width,size.height);
                //
                camera.setParameters(parameters);
                try{
                    camera.startPreview();
                }catch (Exception e){
                    Log.e(TAG,"Could not start preview",e);
                    camera.release();
                    camera=null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera!=null){
                    camera.startPreview();
                }
            }
        });

        mProgressBarContainer=view.findViewById(R.id.fragment_crime_camera_progressContainer);
        mProgressBarContainer.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            camera= android.hardware.Camera.open(0);
//            camera.setDisplayOrientation(90);//顺时针旋转90度
        }else {
            camera= android.hardware.Camera.open();
//            camera.setDisplayOrientation(90);//顺时针旋转90度
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera!=null){
            camera.release();
            camera=null;
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes,int width,int height){
        Camera.Size bestSize=sizes.get(0);
        int largeArea=bestSize.width * bestSize.height;
        for (Camera.Size s:sizes){
            int area=s.width * s.height;
            if (area>largeArea){
                bestSize=s;
                largeArea=area;
            }
        }
        return bestSize;
    }
}
