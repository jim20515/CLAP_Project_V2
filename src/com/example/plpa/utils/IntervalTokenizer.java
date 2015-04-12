package com.example.plpa.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class IntervalTokenizer {
	
	public static String mTag = "Jim";
	
	public static long involveTokenizer(String inputString, String arg) {
		long interval = 0;
		
		String pattenString = String.format("(%s\\s*\\d+)", arg);
		Pattern p = Pattern.compile(pattenString,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		
		Matcher m = p.matcher(inputString.replace(",", "").replace(" ", ""));
		while (m.find()) {
			Pattern p1 = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE);
			Matcher m1 = p1.matcher(m.group(0).replace(",", "")
					.replace(" ", ""));
			while (m1.find()) {

				interval = (Integer.parseInt(m1.group(0)) * 1000);
				Log.v(mTag, arg + " Interval Tokenizer");

			}
			
			m1.reset();
		}
		
		m.reset();
		return interval;
	}
}
