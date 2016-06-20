package com.nicodelee.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicodelee.R;

public class LoadingDialog extends AlertDialog {
	private TextView loginpay_text = null;
	private String msg;
	private android.view.View.OnClickListener mListener;
	private Context context;
	private RelativeLayout relativeLayout;
	private int backgroundColor, textColor, backgroundResource = 0;
	private Drawable drawable = null;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpaydialog);
		relativeLayout = (RelativeLayout) this.findViewById(R.id.mypaydialog);
		loginpay_text = (TextView) this.findViewById(R.id.loginpay_text);
		loginpay_text.setText(msg);

		if (textColor != 0) {
			loginpay_text.setTextColor(textColor);
		}
		if (backgroundResource != 0) {
			relativeLayout.setBackgroundResource(backgroundResource);
		}
		if (backgroundColor != 0) {
			relativeLayout.setBackgroundColor(backgroundColor);
		}
		if (drawable != null) {
			relativeLayout.setBackgroundDrawable(drawable);
		}

	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		// loginpay_text.setText(message);
		// super.setMessage(message);
	}

	public void setBackgroundResource(int resid) {
		this.backgroundResource = resid;
	}

	public void setBackgroundColor(int color) {
		this.backgroundColor = color;

	}

	public void setBackgroundDrawable(Drawable d) {
		this.drawable = d;
	}

	public void setTextSize(int color) {
		this.textColor = color;

	}

	public void setListener(android.view.View.OnClickListener listener) {
		this.mListener = listener;
	}

	public void setMessage(String msg) {
		this.msg = msg;
		if (loginpay_text != null)
			loginpay_text.setText(msg);

		show();
	}

	public void setMessage(int msg) {
		this.msg = context.getString(msg);
	}

	public void showDialog() {

		this.show();

	}

	public void closeDialog() {
		this.cancel();
	}

}
