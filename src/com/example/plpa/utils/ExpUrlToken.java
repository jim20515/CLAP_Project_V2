//package com.example.plpa.utils;
//
//import java.util.ArrayList;
//
//import org.json.JSONObject;
//
//import android.util.JsonReader;
//
//import com.example.pipa.item.ExpItemAttribute;
//
//import com.google.gson.*;
//
//public class ExpUrlToken {
//	public String mId;// ����ID
//	public String mName; // ����W��
//	public String mModifytime;
//	public String mDescription;
//	public String mItem;
//	public String mPolicy;
//	
//	public static final String JSON_MODIFYTIME = "ModifyTime";
//	public static final String JSON_ITEM = "Item";
//	public static final String JSON_POLICY = "Policy";
//	public static final String JSON_DESCRIPTION = "Description";
//	
//	public static final String JSON_ITEM_ITEMID = "ItemId";
//	public static final String JSON_ITEM_ITEMNAME = "ItemName";
//	public static final String JSON_ITEM_ATTRID = "AttrId";
//	public static final String JSON_ITEM_ATTRNAME = "AttrName";
//	public static final String JSON_ITEM_CONDITION = "Condition";
//	
////	public static ExpApplyJson getApplyJson(String itemJson) {
////		
////		if(itemJson == null)
////			return null;
////		
////		Gson gson = new Gson();
////		ExpApplyJson expApply = gson.fromJson(itemJson, ExpApplyJson.class);
//		
////		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
////		String[] temps1 = temps2[0].split(TOKEN_WHERE);
////
////		String itemString = ((temps1.length > 0 ? temps1[0] : "")
////				.replace("log ", "")).trim();
////		String whereConditionString = (temps1.length > 1 ? temps1[1]
////				: "").trim();
////		
////		ArrayList<ExpItemAttribute> attris = new ArrayList<ExpItemAttribute>();
////
////		for (String item : itemString.split(",")) {
////			String[] token = item.trim().split(" ");
////			
////			ExpItemAttribute itemAttr = new ExpItemAttribute();
////			itemAttr.mName = token[0];
////			
////			if (token.length == 2) {
////				
////				try {
////					itemAttr.mNumber = Integer.valueOf(token[1]);
////
////				} catch (Exception e) {
////
////					// don't need interval
////					itemAttr.mNumber = -1;
////				}
////
////			}
////
////			attris.add(itemAttr);
////		}
////
////		for (String where : whereConditionString.split(",")) {
////			String[] token = where.split(" ");
////			if (token.length == 3) {
////				for (ExpItemAttribute name : attris) {
////					if (name.mName.equals(token[0])) {
////						name.mWhereExpress = token[1];
////						name.mWhereValue = token[2];
////					}
////				}
////			}
////		}
//		
////		return expApply;
////	}
//	
////	public static String[] getUploadPolicy(String script) {
////		if(script == null)
////			return null;
////		
////		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
////		String uploadPolicyString = (temps2.length > 1 ? temps2[1] : "")
////				.trim();
////		
////		return uploadPolicyString.split(",");
////		
////	}
//	
////	public static String getExpDescription(String script) {
////		if(script == null)
////			return null;
////		
////		String[] temps2 = script.split(TOKEN_UPLOAD_POLICY);
////		String[] temps1 = temps2[0].split(TOKEN_WHERE);
////
////		String itemString = ((temps1.length > 0 ? temps1[0] : "")
////				.replace("log ", "")).trim();
////		
////		String descriptionString = "";
////		for (String item : itemString.split(",")) {
////			String[] token = item.trim().split(" ");
////			
////			descriptionString += token[0] + " ";
////		}
////
////		return descriptionString;
////	}
//}
