package com.example.superapp;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import com.soundcloud.api.Params;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Http;

public class DisplayMessageActivity extends Activity {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Here I am.");
		Intent intent = getIntent();
	    String username = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	    String password = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE3);
	    String fileName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE4);
		
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
	   try{ NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    System.out.println("Connunction function.");
	    if (networkInfo != null && networkInfo.isConnected()) {
	    	new FirstSCTask().execute(username, password, message, fileName);
	        
	        } 
	    else {
	            System.out.println("No network connection available.");
	        }
	   }catch(Exception e){System.out.println(e);}
		
		
		
//		HttpResponse resp = wrapper.get(Request.to("/me"));
//		HttpResponse resp =
//			      wrapper.put(Request.to("/me")
//			             .with("user[full_name]", "Che Flute",
//			                   "user[website]",   "http://cheflute.com")
//			             .withFile("user[avatar_data]", new File("flute.jpg")));
		
		
		 // Get the message from the intent
	    

	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(message);
	    
		
		setContentView(textView);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private class FirstSCTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... userpasstitle) {
              
        	ApiWrapper wrapper = new ApiWrapper("d1b633a79dd0972a1f1798c1096e3a6c", "4c0596d73b0389fd9df63723fb9b1e82", null, null);
	    	Token token = new Token("test", "fail");
		
	    	try{
	    		if (userpasstitle[0].equals("") || userpasstitle[1].equals(""))
	    			token = wrapper.login("climbz", "derekmonkey87");
	    		else
	    			token = wrapper.login(userpasstitle[0],  userpasstitle[1]);
	    	}catch (Exception e){System.out.println(e);}
	    	
	    	System.out.println("got token from server: " + token);
	    	
	    	final File file = new File(userpasstitle[3]);
            if (!file.exists()) System.out.println("Bad file: " + file); //throw new IOException("The file `"+file+"` does not exist");
            System.out.println("Uploading " + file);
            try {
                HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
                        .add(Params.Track.TITLE,    userpasstitle[2])
                        .add(Params.Track.TAG_LIST, "demo upload")
                        .withFile(Params.Track.ASSET_DATA, file)
                        // you can add more parameters here, e.g.
                        // .withFile(Params.Track.ARTWORK_DATA, file)) /* to add artwork */

                        // set a progress listener (optional)
                        .setProgressListener(new Request.TransferProgressListener() {
                            @Override public void transferred(long amount) {
                                System.err.print(".");
                            }
                        }));

                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                    System.out.println("\n201 Created "+resp.getFirstHeader("Location").getValue());

                    // dump the representation of the new track
                    System.out.println("\n" + Http.getJSON(resp).toString(4));
                } else {
                    System.err.println("Invalid status received: " + resp.getStatusLine());
                }
            } catch (Exception e){
            	System.out.println(e);
            }finally {
            
                System.out.println("AHHH!");
            }
	    	return "wtf";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
       }
        
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
