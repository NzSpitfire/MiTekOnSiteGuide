package nz.co.mitek.main;



import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.example.mitekmobileguide.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Section> sectionList;
	private ArrayList<Section> originalList;
	private ProgressDialog progDailog;

	public MyListAdapter(Context context, ArrayList<Section> sectionList) {
		this.context = context;
		this.sectionList = new ArrayList<Section>();
		this.sectionList.addAll(sectionList);
		this.originalList = new ArrayList<Section>();
		this.originalList.addAll(sectionList);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<Item> itemList = null;
		if(sectionList.size() > groupPosition){
			itemList = sectionList.get(groupPosition)
					.getItemList();
		}
		else{
			itemList = sectionList.get(0).getItemList();
		}
		
		if(itemList.size() > childPosition){
			return itemList.get(childPosition);
		}
		else{
			return itemList.get(0);
		}
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {

		Item item = (Item) getChild(groupPosition, childPosition); 
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.child_row, null);
		}

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView resultText = (TextView) view.findViewById(R.id.text_results);
		
		name.setText(item.getName().trim());
		Spanned text = null;
		if(sectionList.size() > groupPosition){
			if(sectionList.get(groupPosition).getItemList().size() > childPosition){
				text = sectionList.get(groupPosition).getItemList().get(childPosition).getText();
			}
		}


		
		if(text== null){
			resultText.setVisibility(View.GONE);
		}
		else{
			resultText.setText(text);
			resultText.setVisibility(View.VISIBLE);
		}

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		ArrayList<Item> countryList = sectionList.get(groupPosition)
				.getItemList();
		return countryList.size();

	}

	@Override
	public Section getGroup(int groupPosition) {
		if(sectionList.size() > groupPosition){
			return sectionList.get(groupPosition);
		}
		else{
			return sectionList.get(0);
		}
	}

	@Override
	public int getGroupCount() {
		return sectionList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {

		Section section = (Section) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.group_row, null);
		}

		TextView heading = (TextView) view.findViewById(R.id.heading);
		heading.setText(section.getName().trim());

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void filterData(String query, boolean searchPDF) {

		query = query.toLowerCase();
		Log.v("MyListAdapter", String.valueOf(sectionList.size()));
		sectionList.clear();

		if (query.isEmpty()) {
			sectionList.addAll(originalList);
		} else {

			for (Section section : originalList) {

				ArrayList<Item> itemList = section.getItemList();
				ArrayList<Item> newList = new ArrayList<Item>();
				for (Item item : itemList) {
					if (!searchPDF) {
						if (item.getName().toLowerCase().contains(query)) {
							newList.add(item);
						}
					} else {
						if(section.isOpen()){
						
							try {
								ParsePDF parsePDF = new ParsePDF();
								ParseText parseText = new ParseText();
//							    String result = parsePDF.containsPhrase(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + item.getPath(), query);
								Spanned result = parseText.containsPhrase(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + item.getPath() + ".txt", query);
								if (result != null) {
									item.setText(result);
									newList.add(item);
									System.out.println(item.getPath());
								}
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						}
					}
				}
				if (newList.size() > 0) {
					Section nContinent = new Section(section.getName(), newList, true);
					sectionList.add(nContinent);
				}
			}
			
		}

		Log.v("MyListAdapter", String.valueOf(sectionList.size()));
		if(!searchPDF)
			notifyDataSetChanged();
		
		

	}
	
	public void updatedInterface(String query, boolean searchPDF, MainActivity mainActivity){
		new UpdatedInterface(query, searchPDF, mainActivity).execute("");
	}

	private class UpdatedInterface extends AsyncTask<String, Void, String> {

		private String query;
		private boolean searchPDF;
		private MainActivity mainActivity;

		public UpdatedInterface(String query, boolean searchPDF, MainActivity mainActivity) {
			this.query = query;
			this.searchPDF = searchPDF;
			this.mainActivity = mainActivity;
		}

		@Override
		protected String doInBackground(String... params) {

			filterData(query, searchPDF);
			System.out.println("DONE!!!");
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
            progDailog.dismiss();
            
			mainActivity.expandAll();
			notifyDataSetChanged();
			mainActivity.hideKeyboard();
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		    progDailog = new ProgressDialog(mainActivity);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public ArrayList<Section> getSectionList() {
		return sectionList;
	}

	public void setSectionList(ArrayList<Section> sectionList) {
		this.sectionList = sectionList;
	}

	public ArrayList<Section> getOriginalList() {
		return originalList;
	}

	public void setOriginalList(ArrayList<Section> originalList) {
		this.originalList = originalList;
	}
	
	

}