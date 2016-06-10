package pack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.test2_1.R;

/**
 * Created by 昊天 on 2016/5/31.
 */
public class DialogChooseFragment extends DialogFragment {
    private Button button;
    public static String str_text="";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_choose,null);
        button= (Button) view.findViewById(R.id.dialog_choose_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                Dialog_1_Fragment dialog_1_fragment=new Dialog_1_Fragment();
                dialog_1_fragment.setTargetFragment(DialogChooseFragment.this,0);
                dialog_1_fragment.show(fragmentManager,"TAG");
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Choose A Method:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.putExtra("CHOOSE1",str_text);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED,intent);
                    }
                })
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==200){
            str_text=data.getStringExtra("CHOOSE1");
        }
    }
}
