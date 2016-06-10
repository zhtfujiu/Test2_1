package pack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.test2_1.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by 昊天 on 2016/5/27.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE="com.pack.DatePickerFragment.date";
    private Date mDate;
    private int Year,month,day,hour,min;
    public static DatePickerFragment newInstance(Date date){
        Bundle bundle=new Bundle();
        bundle.putSerializable(EXTRA_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    //当屏幕显示DialogFragment时，托管Activity的FragmentManager会调用此方法
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate= (Date) getArguments().getSerializable(EXTRA_DATE);
        //创建Calendar对象来获取年月日
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        Year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);
        DatePicker datePicker= (DatePicker) view.findViewById(R.id.dialog_date_datepicker);
        TimePicker timePicker= (TimePicker) view.findViewById(R.id.dialog_date_timepicker);
        datePicker.init(Year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate=new GregorianCalendar(year,monthOfYear,dayOfMonth,hour,min).getTime();
                Year=year;
                month=monthOfYear;
                day=dayOfMonth;
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate=new GregorianCalendar(Year,month,day,hourOfDay,minute).getTime();
                hour=hourOfDay;
                min=minute;
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        getArguments().putSerializable(EXTRA_DATE,mDate);
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
    private void sendResult(int resultCode){
        if (getTargetFragment()==null)return;
        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
