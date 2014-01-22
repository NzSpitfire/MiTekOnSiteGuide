package nz.co.mitek.main;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
public class ParseText {


	public Spanned containsPhrase(String file, String text) throws IOException{

			BufferedReader br = null;
	 
				String sCurrentLine;
				br = new BufferedReader(new FileReader(file));
	 
				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.toLowerCase().contains(text.toLowerCase())){
						br.close();
						Spanned result = Html.fromHtml(sCurrentLine.trim().replaceAll("\uFFFD", "").replaceAll("  ", " ").toLowerCase().replaceAll(text, "<b>" + text + "</b>"));
						
						return result;
					}
				}
			br.close();
			return null;
		}	
	}