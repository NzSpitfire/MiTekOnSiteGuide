package nz.co.mitek.main;

import com.example.mitekmobileguide.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class StartUpActivity extends Activity {

	private boolean touched;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		touched = false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!touched){
			
			SharedPreferences settings = getSharedPreferences("terms", 0);
		    boolean enable = settings.getBoolean("enable", true);
		    Intent i;
		    if(enable){
		    	i = new Intent(getApplicationContext(), TermsActivity.class);
		    }
		    else{
		    	i = new Intent(getApplicationContext(), MainActivity.class);
		    }
			startActivity(i);
			touched = true;
			
		}
		else{
			finish();
		}
		return super.onTouchEvent(event);
	}
	
}
