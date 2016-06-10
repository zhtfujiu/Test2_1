package com.test2_1;

import android.support.v4.app.Fragment;

import pack.CrimeListFragment;
import pack.SingleFragmentActivity;

/**
 * Created by 昊天 on 2016/5/20.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
