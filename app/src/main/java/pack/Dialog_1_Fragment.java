package pack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.test2_1.R;

/**
 * Created by 昊天 on 2016/5/31.
 */
public class Dialog_1_Fragment extends DialogFragment {
    private EditText editText;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_1,null);
        editText= (EditText) view.findViewById(R.id.dialog_1_edittext);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Dialog_1")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendText();
                    }
                })
                .create();
    }
    private void sendText(){
        Intent intent=new Intent();
        String str=editText.getText().toString().trim();
        intent.putExtra("CHOOSE1",str);
        getTargetFragment().onActivityResult(getTargetRequestCode(),200,intent);
    }
}