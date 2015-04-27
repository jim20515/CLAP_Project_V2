package com.example.plpa.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	public static String getNowFormat() {
		long currentTime = getNow();
		
		return formatter.format(new Date(currentTime));
	}
	
	public static long getNow() {
		return System.currentTimeMillis();
	}
}
