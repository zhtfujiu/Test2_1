package camera;

import android.support.v4.app.Fragment;

import pack.SingleFragmentActivity;

/**
 * Created by 昊天 on 2016/6/5.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        setTitle("Camera");
        return new CrimeCameraFragment();
    }
}
