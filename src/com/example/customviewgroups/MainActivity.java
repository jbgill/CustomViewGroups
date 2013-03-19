package com.example.customviewgroups;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    MyViewGroup pv = (MyViewGroup) findViewById(R.id.MyViewGroup1);
    
    MyView mv1 = new MyView(this);
    mv1.setText("Testing 1");
    
    MyView mv2 = new MyView(this);
    mv2.setText("Testing 2");
    
    MyView mv3 = new MyView(this);
    mv3.setText("Testing 3");
    
    pv.addView(mv1);
    pv.addView(mv2);
    pv.addView(mv3);
    
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
