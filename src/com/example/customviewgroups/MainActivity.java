package com.example.customviewgroups;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    //PedigreeView pv = (PedigreeView) findViewById(R.id.LinearLayout1);
    MyViewGroup pv = (MyViewGroup) findViewById(R.id.MyViewGroup1);
    
//    Button b1 = new Button(this);
//    b1.setText("test1");
//    b1.setLayoutParams(new LayoutParams(
//        LayoutParams.WRAP_CONTENT,
//        LayoutParams.WRAP_CONTENT));
//    //b1.setWidth(100);
//    //b1.setHeight(50);
//    //b1.setId(4444);
//    Button b2 = new Button(this);
//    b2.setText("test2");
//    //b2.setWidth(100);
//    //b2.setHeight(50);
//    b2.setLayoutParams(new LayoutParams(
//        LayoutParams.WRAP_CONTENT,
//        LayoutParams.WRAP_CONTENT));
//    //b2.setId(5555);
    
    MyView mv1 = new MyView(this);
    mv1.setText("Testing 1");
    
    MyView mv2 = new MyView(this);
    mv2.setText("Testing 2");
    
    MyView mv3 = new MyView(this);
    mv3.setText("Testing 3");
    
    pv.addView(mv1);
    pv.addView(mv2);
    pv.addView(mv3);
    
    //pv.invalidate();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
