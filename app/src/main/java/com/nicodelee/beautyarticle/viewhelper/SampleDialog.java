package com.nicodelee.beautyarticle.viewhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SampleDialog extends DialogFragment {

  public static interface OnDialogOkClick {
    public void onOkClick();
  }

  private DialogInterface.OnClickListener onOkClick;

  public SampleDialog() {
  }

  public static SampleDialog newInstance() {
    SampleDialog frag = new SampleDialog();
    Bundle args = new Bundle();
    frag.setArguments(args);
    return frag;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    //final ResultReceiver receiver = getArguments().getParcelable("receiver");

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Hi，感谢\n 侧滑可回到上级界面")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            try {
              OnDialogOkClick ok = (OnDialogOkClick) getActivity();
              if (ok != null) ok.onOkClick();
            } catch (ClassCastException e) {
            }
            //ResultReceiver receiver = getArguments().getParcelable("receiver");
            //if (receiver != null)
            //	receiver.send(Activity.RESULT_OK, null);
            dialog.dismiss();
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
          }
        });
    return builder.create();
  }

  public DialogInterface.OnClickListener getOnOkClick() {
    return onOkClick;
  }

  public SampleDialog setOnOkClick(DialogInterface.OnClickListener onOkClick) {
    this.onOkClick = onOkClick;
    return this;
  }
}
