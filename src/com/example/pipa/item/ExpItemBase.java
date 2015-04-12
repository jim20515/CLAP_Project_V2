package com.example.pipa.item;

import java.util.ArrayList;

public abstract class ExpItemBase {
	
	public String mExpPrefix;
	public ArrayList<ExpItemAttribute> mExpRealAttributes;
	public static String[] mExpJudgeStrings;
	
	public ExpItemBase(){
		mExpRealAttributes = new ArrayList<ExpItemAttribute>();
	};
	
	abstract public void initAttributeKey();
}
