package com.emilianstankov.drumric;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pad1 = (Button)findViewById(R.id.pad1);
        Button pad2 = (Button)findViewById(R.id.pad2);
        Button pad3 = (Button)findViewById(R.id.pad3);
        Button pad4 = (Button)findViewById(R.id.pad4);
        Button pad5 = (Button)findViewById(R.id.pad5);
        Button pad6 = (Button)findViewById(R.id.pad6);
        Button pad7 = (Button)findViewById(R.id.pad7);
        Button pad8 = (Button)findViewById(R.id.pad8);
        ArrayList<Button> pads = new ArrayList<Button>(Arrays.asList(pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8));
        for(Button btn: pads){
	        btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(MainActivity.this, "clicked", 100).show();
				}
			});
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
