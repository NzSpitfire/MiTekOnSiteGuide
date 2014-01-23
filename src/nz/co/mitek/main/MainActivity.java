package nz.co.mitek.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.mitekmobileguide.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class MainActivity extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private SearchView search;
	private CheckBox checkBox;
	private MyListAdapter listAdapter;
	private ExpandableListView myList;
	
	private ArrayList<Section> sectionList = new ArrayList<Section>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.search);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		
		
		checkBox = (CheckBox) findViewById(R.id.search_box);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					listAdapter.filterData("", false);
					collapseAll();
					//expandAll();
					Toast.makeText(getApplicationContext(), 
                            "Open sections you wish to search in", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		myList = (ExpandableListView) findViewById(R.id.expandableList);
		myList.setOnChildClickListener(new OnChildClickListener() {
		
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            	Item item = listAdapter.getGroup(groupPosition).getItemList().get(childPosition);
            	
                

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ item.getPath() + ".pdf");
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  

                Intent intent = Intent.createChooser(target, "Open " + item.getName() + " with..." );
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                } 
            	
                return false;
            }
        });
		
		myList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				sectionList.get(groupPosition).setOpen(false);
				
			}
		});
		
		myList.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				sectionList.get(groupPosition).setOpen(true);
				
			}
		});
		// display the list
		displayList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	// method to expand all groups
	public void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			myList.expandGroup(i);
		}	

	}
	
	// method to collapse all groups
	public void collapseAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			myList.collapseGroup(i);
		}	

	}
	
	public void hideKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
	}

	// method to expand all groups
	private void displayList() {

		// display the list
		loadSomeData();

		// get reference to the ExpandableListView
		myList = (ExpandableListView) findViewById(R.id.expandableList);
		// create the adapter by passing your ArrayList data
		listAdapter = new MyListAdapter(MainActivity.this, sectionList);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);
	}

	private void loadSomeData() {
		
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/Index File.txt");

		try {
			Scanner scanner = new Scanner(new FileReader(file));
			while (scanner.hasNextLine())  
			{  
				String line = scanner.nextLine();
			   String[] array = line.split(" ~~ ");
			   for (int i = 0; i < array.length; i++) {
				   //removes all unknown characters 
				   array[i] = array[i].replaceAll("\uFFFD", "");
			   }
			   if(array.length == 3){
				   String selectedSection = array[0];
				   String selectedItem = array[1];
				   String correspondingFile = array[2];
				   boolean listContainsSection = false;
				   Section currentSection = null;
				   for (Section section : sectionList) {
					   if(section.getName().equals(selectedSection)){
						   listContainsSection = true;
						   currentSection = section;
					   }
				   }
				   if(!listContainsSection){
					   ArrayList<Item> itemList = new ArrayList<Item>();
					   Item item = new Item(selectedItem, correspondingFile, null);
					   itemList.add(item);
					   Section section = new Section(selectedSection, itemList, false);
					   sectionList.add(section);
//					   ParsePDF parsePDF = new ParsePDF();
//					   parsePDF.convertPDFToText(Environment.getExternalStorageDirectory().getAbsolutePath() +"/" + correspondingFile);
				   }
				   else{
					   ArrayList<Item> itemList = currentSection.getItemList();
					   Item item = new Item(selectedItem, correspondingFile, null);
					   itemList.add(item);
					   Section section = new Section(selectedSection, itemList, false);
					   sectionList.set(sectionList.indexOf(currentSection), section);
//					   ParsePDF parsePDF = new ParsePDF();
//					   parsePDF.convertPDFToText(Environment.getExternalStorageDirectory().getAbsolutePath() +"/" + correspondingFile);
				   }
 
			   }
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Activity")
	        .setMessage("Are you sure you want to close the guide?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	}

	@Override
	public boolean onClose() {
		listAdapter.filterData("", false);
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String query) {
		if(!checkBox.isChecked() || query.equals("")){
			listAdapter.filterData(query, false);
			expandAll();
			for (Section section : sectionList) {
				for (Item item : section.getItemList()) {
					item.setText(null);
				}
			}
		}

		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		
		hideKeyboard();
		if(checkBox.isChecked()){
			listAdapter.updatedInterface(query,true,this);
		}
		return false;
	}

	public ExpandableListView getMyList() {
		return myList;
	}

	public void setMyList(ExpandableListView myList) {
		this.myList = myList;
	}
	
	
}