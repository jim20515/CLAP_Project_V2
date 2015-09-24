package com.example.plpa.utils;

import java.util.ArrayList;

public class ExpApplyJson {
	public int Id;
	public String Title;
	public String Description;
	public Detail Detail;
	
	public class Detail{
		public ArrayList<Items> Items;
		public ArrayList<Policy> Policy;
	}
	
	public class Items{
		public int ItemId;
		public String ItemName;
		public int AttrId;
		public String AttrName;
//		public double Condition;
	}
	
	public class Policy {
//		public Boolean Checked;
		public int Id;
		public String Name;
	}
	
	
}
