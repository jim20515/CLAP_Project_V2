package com.example.plpa.utils;

import java.util.ArrayList;

public class ExpApplyJson {
	public int ExpId;
	public String ModifyTime;
	public String Description;
	public ArrayList<Item> Items;
	public ArrayList<Policy> Policies;
	
	public class Item{
		public int ItemId;
		public String ItemName;
		public int AttrId;
		public String AttrName;
		public String Condition;
	}
	
	public class Policy {
		public Boolean Checked;
		public int Id;
		public String Name;
	}
	
	
}
