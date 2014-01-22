package nz.co.mitek.main;

import android.text.Spanned;

public class Item {

	private String path = "";
	private String name = "";
	private Spanned text = null;

	public Item(String name, String path, Spanned text) {
		super();
		this.name = name;
		this.path = path;
		this.text = text;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Spanned getText() {
		return text;
	}


	public void setText(Spanned result) {
		this.text = result;
	}
	
	



}