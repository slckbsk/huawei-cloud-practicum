package com.sefihuom.myhuaweiapplication.utilities;

import static com.sefihuom.myhuaweiapplication.utilities.BucketOperations.uiThread;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sefihuom.myhuaweiapplication.R;


public class AlertUtilities {


	Dialog dialog = null;

	ChangeListener changeListener;

	public void setOnNumberChangeListener(ChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	public interface ChangeListener {
		void onChange(int e);
	}


	// LOADING
	public TextView message_l ;
	public AlertUtilities(Context context, String mess, boolean bool) {


		androidx.appcompat.app.AlertDialog.Builder builder = new
				androidx.appcompat.app.AlertDialog.Builder(context);

		LayoutInflater dialogLayout = LayoutInflater.from(context);
		View alert_loading = dialogLayout.inflate(R.layout.alert_loading, null);

		message_l = alert_loading.findViewById(R.id.message_l);
		message_l.setText(mess);

		builder.setCancelable(bool);
		builder.setView(alert_loading);
		dialog = builder.create();
	}


	// EDITTEXT
	public TextView title_e;
	public TextView editText_e;
	public TextView cancel_e;
	public TextView okey_e;
	public RadioGroup radio_e;
	RadioButton radioButton;


	public AlertUtilities(Context context, String title, String action) {


		androidx.appcompat.app.AlertDialog.Builder builder = new
				androidx.appcompat.app.AlertDialog.Builder(context);

		LayoutInflater dialogLayout = LayoutInflater.from(context);
		View view1 = dialogLayout.inflate(R.layout.alert_edittext, null);

		title_e = view1.findViewById(R.id.title_e);
		editText_e = view1.findViewById(R.id.edit_text_e);
		cancel_e = view1.findViewById(R.id.cancel_e);
		okey_e = view1.findViewById(R.id.okey_e);
		radio_e = view1.findViewById(R.id.radio_e);

		title_e.setText(title);
		cancel_e.setText(R.string.cancel);
		okey_e.setText(action);


		radio_e.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				radioButton = view1.findViewById(checkedId);


				if (radioButton.getText().equals("Disk")){
					postPullNumber(changeListener, 1);
				}

				if (radioButton.getText().equals("Url")){
					postPullNumber(changeListener, 2);
				}


			}
		});


		builder.setCancelable(true);
		builder.setView(view1);
		dialog = builder.create();
	}

	private void postPullNumber (ChangeListener changeListener, int e) {
		uiThread.execute(new Runnable() {
			@Override
			public void run() {
				changeListener.onChange(e);
			}
		});
	}



	public void show()
	{
		dialog.show();
	}

	public void dismiss()
	{
		dialog.dismiss();
	}



}