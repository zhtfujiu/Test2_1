package pack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.test2_1.R;

/**
 * Created by 昊天 on 2016/5/20.
 */
public abstract class SingleFragmentActivity extends FragmentActivity{
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment==null){
            /**
             * 如果为空，就用抽象函数createFragment()获取一个Fragment
             * 然后把这个新的Fragment对应到容器fragmentContainer，即activity_main.xml的frameLayout上
             */
            fragment=createFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment)
                    .commit();
        }
    }
}
