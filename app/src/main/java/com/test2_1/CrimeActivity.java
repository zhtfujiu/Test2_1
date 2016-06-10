package com.test2_1;

import android.support.v4.app.Fragment;

import java.util.UUID;

import pack.CrimeFragment;
import pack.SingleFragmentActivity;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID crimeID= (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeID);
    }
}
