package com.test2_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

import pack.Crime;
import pack.CrimeFragment;
import pack.CrimeLab;

/**
 * Created by 昊天 on 2016/5/21.
 */
public class CrimePagerActivity extends FragmentActivity {
    private ViewPager viewPager;

    private ArrayList<Crime> mCrimes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager=new ViewPager(this);
        viewPager.setId(R.id.viewpager);
        setContentView(viewPager);

        //从CrimeLab中获取数据集
        mCrimes= CrimeLab.get(this).getCrimes();
        //获取FragmentManager实例
        FragmentManager fragmentManager=getSupportFragmentManager();
        //FragmentStatePagerAdapter是代理，负责管理与ViewPager的对话协同工作
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            //FragmentStatePagerAdapter相较于FragmentPagerAdapter更省内存
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                //代理首先将Fragment添加给activity
                return CrimeFragment.newInstance(crime.getmID());
            }
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        /**
         * 循环检查ID找到crime表项设置为Crime在数组中的索引位置
         * getSerializableExtra实现接口，用于Activity之间传递对象，而非简单的基本类型或String
         */
        final UUID crimeID= (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
//        final Crime crime= (Crime) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i=0;i<mCrimes.size();i++){
            if (mCrimes.get(i).getmID().equals(crimeID)){
                viewPager.setCurrentItem(i);
                setTitle(mCrimes.get(i).getmTitle());
                break;
            }
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Crime crime=mCrimes.get(position);
                if (crime.getmTitle()!=null){
                    setTitle(crime.getmTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
}
