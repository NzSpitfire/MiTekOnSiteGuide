package nz.co.mitek.main;

import com.example.mitekmobileguide.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TermsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms);
		Button button = (Button) findViewById(R.id.button_continue);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				showAgain(i);

			}
		});
	}
	
	
	public void showAgain(final Intent i) {
		final boolean result = false;
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Show Terms")
	        .setMessage("Would you like to see these terms next time?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	SharedPreferences settings = getSharedPreferences("terms", 0);
				    SharedPreferences.Editor editor = settings.edit();
				    editor.putBoolean("enable", true);
				    editor.commit();
				    finish();
		        	startActivity(i);
		        }
	        })
	    .setNegativeButton("No",  new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	SharedPreferences settings = getSharedPreferences("terms", 0);
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("enable", false);
			    editor.commit();
			    finish();
			    startActivity(i);
	        }

	    })
	    .show();
		
	}

}
