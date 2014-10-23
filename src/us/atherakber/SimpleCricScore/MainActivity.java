package us.atherakber.SimpleCricScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	String key_items = "item";
	String key_title = "title";
	String key_description = "description";
	//String key_link = "link";
	//String key_date = "pubDate";
	ListView lstPost = null;
	List<HashMap<String, Object>> post_lists = new ArrayList<HashMap<String, Object>>();
	List<String> lists = new ArrayList<String>();
	ArrayAdapter<String> adapter = null;
	RSSReader rssfeed = new RSSReader();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		lstPost = (ListView) findViewById(R.id.lstPosts);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_2, android.R.id.text1, lists) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView txt1 = (TextView) view
						.findViewById(android.R.id.text1);
				TextView txt2 = (TextView) view
						.findViewById(android.R.id.text2);
				HashMap<String, Object> data = post_lists.get(position);
				txt1.setText(data.get(key_title).toString());
				txt2.setText(data.get(key_description).toString());
				return view;
			}

		};
		
		Document xmlFeed = rssfeed
				.getRSSFromServer("http://static.cricinfo.com/rss/livescores.xml");
		NodeList nodes = xmlFeed.getElementsByTagName("item");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element item = (Element) nodes.item(i);
			HashMap<String, Object> feed = new HashMap<String, Object>();
			feed.put(key_title, rssfeed.getValue(item, key_title));
			feed.put(key_description, rssfeed.getValue(item, key_description));

			post_lists.add(feed);
			lists.add(feed.get(key_title).toString());
		}
		lstPost.setAdapter(adapter);
		
		
    }

   

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.exit) {
        	finish();
            System.exit(0);
            return true;
        }
        if (id == R.id.share) {
        	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
            sharingIntent.setType("text/plain");
            String shareBody = "Take a look at \"Simple Cric Score\" - https://play.google.com/store/apps/details?id=us.atherakber.SimpleCricScore";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }
        
        if (id == R.id.about) {
      	  android.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Simple Cric Score");
            alert.setMessage("No Need to open up your browser to check the Cricket Scores, just load up the app and view the score!\nDeveloped by Ather Akber");
           
            alert.setPositiveButton("Close", null);
            alert.show();
          return true;
      }
        
        return super.onOptionsItemSelected(item);
    }
}
