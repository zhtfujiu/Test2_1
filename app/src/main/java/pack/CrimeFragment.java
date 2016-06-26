package pack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.test2_1.R;

import java.util.Date;
import java.util.UUID;

import camera.CrimeCameraActivity;
import camera.CrimeCameraFragment;

/**
 * Created by 昊天 on 2016/5/18.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton, mChooseDiaBtn, mPhotoButton;
    private CheckBox mSolvedCheckBox;
    public static final String EXTRA_CRIME_ID = "com.test2_1.Crime.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final String TAG="CrimeFragment";

    public static CrimeFragment newInstance(UUID crimeID) {
        /**
         * 每一个Fragment实例都可以携带一个bundle对象
         * 作为内同提要argument来添加进去
         * bundle对应一个key-value键值对
         * ↓看下面的onCreate方法
         */
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, crimeID);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=Activity.RESULT_OK){
            return;
        }
        //下面原来是判断的Activity.RESULT_OK
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            UpDateButtonText();
        }else if (requestCode == REQUEST_PHOTO){
            String filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename!=null){
                Log.i(TAG,"filename:"+filename);
            }
        }
        //下面这一段目前肯定是错误的，过一会再改
        if (requestCode == Activity.RESULT_CANCELED) {
            mCrime.setmTitle(data.getStringExtra("CHOOSE1"));
            mTitleField.setText(mCrime.getmTitle());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setHasOptionsMenu(true);
        /**
         * 这个getArguments().getSerializable（）方法
         * 就是利用上面的argument携带bundle来传递数据
         */
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        //通知FragmentManager：CrimeFragment将代表其托管Activity实现选项菜单相关的回调方法
        setHasOptionsMenu(true);
    }

    //覆盖onOptionsItemSelected方法，响应用户对该菜单项的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //判断是否存在父Activity
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            }
            case R.id.menu_delete_crime: {
                for (Crime c : CrimeLab.get(getActivity()).getCrimes()) {
                    if (c.getmID().equals(mCrime.getmID())) {
                        CrimeLab.get(getActivity()).deleteCrime(c);
                        CrimeListFragment.reflashAdapter();
                        if (NavUtils.getParentActivityName(getActivity()) != null) {
                            NavUtils.navigateUpFromSameTask(getActivity());
                        }
                        return true;
                    }
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        /**
         * 第三个参数告知布局生成器是否将生成的视图添加给父视图；
         * 传入false，因为我们将通过activity代码方式添加视图。
         */
        //将应用图标转换为按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //判断是否存在父Activity，不存在就不改图标了
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        init_View(view);
        return view;
    }

    @Override
    public void onPause() {
        CrimeLab.get(getActivity()).saveCrimes();
        super.onPause();
    }

    public void init_View(View view) {
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        UpDateButtonText();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });
        mChooseDiaBtn = (Button) view.findViewById(R.id.fragment_crime_choose);
        mChooseDiaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogChooseFragment dialogChooseFragment = new DialogChooseFragment();
                dialogChooseFragment.setTargetFragment(CrimeFragment.this, 0);
                dialogChooseFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        mPhotoButton = (Button) view.findViewById(R.id.fragment_crime_startcamera_button);
        if (!hasCamera()) {
            mPhotoButton.setEnabled(false);
            mPhotoButton.setText("此设备相机不可用");
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
    }

    private boolean hasCamera() {
        boolean flag = false;
        PackageManager pm = getActivity().getPackageManager();
        flag = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
                Camera.getNumberOfCameras() > 0;
        //flag为真，则可用
        return flag;
    }

    private void UpDateButtonText() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }
}
