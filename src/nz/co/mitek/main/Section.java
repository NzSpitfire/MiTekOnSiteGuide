package nz.co.mitek.main;

import java.util.ArrayList;

public class Section {

	private String name;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private boolean isOpen;

	public Section(String name, ArrayList<Item> itemList, boolean isOpen) {
		super();
		this.name = name;
		this.itemList = itemList;
		this.isOpen = isOpen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	

	

}