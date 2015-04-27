package com.example.plpa.utils;

import java.util.ArrayList;

import com.example.pipa.item.ExpItemAttribute;

public class ExpUrlToken {
	public String mId;// 實驗ID
	public String mName; // 實驗名稱
	public String mDescription; // 實驗描述
	public String mScript; // 實驗設定檔列表

	public static final String TOKEN_WHERE = "where";
	public static final String TOKEN_UPLOAD_POLICY = "uploadpolicy";

	public static ArrayList<ExpItemAttribute> getExpAttribute(String script) {
		
		if(script == null)
			return null;
		
		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
		String[] temps1 = temps2[0].split(TOKEN_WHERE);

		String itemString = ((temps1.length > 0 ? temps1[0] : "")
				.replace("log ", "")).trim();
		String whereConditionString = (temps1.length > 1 ? temps1[1]
				: "").trim();
		
		ArrayList<ExpItemAttribute> attris = new ArrayList<ExpItemAttribute>();

		for (String item : itemString.split(",")) {
			String[] token = item.trim().split(" ");
			
			ExpItemAttribute itemAttr = new ExpItemAttribute();
			itemAttr.mName = token[0];
			
			if (token.length == 2) {
				
				try {
					itemAttr.mNumber = Integer.valueOf(token[1]);

				} catch (Exception e) {

					// don't need interval
					itemAttr.mNumber = -1;
				}

			}

			attris.add(itemAttr);
		}

		for (String where : whereConditionString.split(",")) {
			String[] token = where.split(" ");
			if (token.length == 3) {
				for (ExpItemAttribute name : attris) {
					if (name.mName.equals(token[0])) {
						name.mWhereExpress = token[1];
						name.mWhereValue = token[2];
					}
				}
			}
		}
		
		return attris;
	}
	
	public static String[] getUploadPolicy(String script) {
		if(script == null)
			return null;
		
		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
		String uploadPolicyString = (temps2.length > 1 ? temps2[1] : "")
				.trim();
		
		return uploadPolicyString.split(",");
		
	}
	
	public static String getExpDescription(String script) {
		if(script == null)
			return null;
		
		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
		String[] temps1 = temps2[0].split(TOKEN_WHERE);

		String itemString = ((temps1.length > 0 ? temps1[0] : "")
				.replace("log ", "")).trim();
		
		String descriptionString = "";
		for (String item : itemString.split(",")) {
			String[] token = item.trim().split(" ");
			
			descriptionString += token[0] + " ";
		}

		return descriptionString;
	}
}
