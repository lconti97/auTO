package lucasconti.auto;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Lucas on 7/1/2016.
 */
public class AddTnmtDialogFrag extends DialogFragment {
    private AddTnmtDialogListener mListener;
    private EditText mTnmtNameText;
    public interface AddTnmtDialogListener {
        public void onPositiveClick(String name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddTnmtDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getParentFragment().getLayoutInflater(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_add_tnmt, null, false);
        mTnmtNameText = (EditText) v.findViewById(R.id.edittext_dialog_add_tnmt_name);
        return new AlertDialog.Builder(getActivity()).setTitle("Add Tournament")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPositiveClick(mTnmtNameText.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setView(v).create();
    }
}
