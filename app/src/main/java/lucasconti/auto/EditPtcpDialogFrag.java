package lucasconti.auto;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Lucas on 6/26/2016.
 */
public class EditPtcpDialogFrag extends DialogFragment {

    private EditText mNameText;
    private EditText mPhoneText;
    public interface EditPtcpDialogListener {
        public void onEditPtcpDialogPositiveClick(String name, String phoneNumber);
    }
    private EditPtcpDialogListener mListener;

    public EditPtcpDialogFrag() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditPtcpDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getParentFragment().getLayoutInflater(savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_add_edit_ptcp, null, false);
        mNameText = (EditText) v.findViewById(R.id.dialog_name_edit_text);
        mPhoneText = (EditText) v.findViewById(R.id.dialog_phone_edit_text);
        Bundle b = getArguments();
        if (b != null) {
            mNameText.setText(b.getString(RegistrationFragment.TAG_NAME, ""));
            mPhoneText.setText(b.getString(RegistrationFragment.TAG_NUMBER, ""));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Edit participant")
                .setView(v)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onEditPtcpDialogPositiveClick(
                                mNameText.getText().toString(), mPhoneText.getText().toString());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
        return builder.create();
    }
}
