package com.example.plpa.utils;

import com.example.plpa_clap_v2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CommonAlertDialog {

	private static ProgressDialog mProgressDialog;
	
	public static void showAlertDialog(Context context, String title,
			final String message, boolean showCancel, DialogInterface.OnClickListener OKClickListener) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		if(title != null) dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButton("OK", OKClickListener);
		if(showCancel) dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public static void showOKAlertDialog(Context context, String message) {
		
		showAlertDialog(context, null, message, false, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				dialog.dismiss();
			}
		});
	}
	
	public static void showAlertDialog(Activity activity, 
			String message, DialogInterface.OnClickListener clickListener) {
		
		showAlertDialog(activity, null, message, true, clickListener);
	}
	
	public static void showAlertDialog(Context context, String title,
			String message, DialogInterface.OnClickListener clickListener) {
		
		showAlertDialog(context, title, message, true, clickListener);
	}
	
	public static void showWaitingDialog(Context context){

		if(mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		
		mProgressDialog = ProgressDialog.show(context, 
				context.getResources().getText(R.string.loading), 
				context.getResources().getText(R.string.wait), true);
	}
	
	public static void dismissWaitingDialog(Context context){
		if(mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}
	
}
