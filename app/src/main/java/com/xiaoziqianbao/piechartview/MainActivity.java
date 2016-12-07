package com.xiaoziqianbao.piechartview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"
            ;
    private PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         pieChartView = (PieChartView) findViewById(R.id.piechart);
     findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             ArrayList<PartBean> dataMap = new ArrayList<>();

             dataMap.add(new PartBean(0.11d, Color.BLACK));
             dataMap.add(new PartBean(0.09d, Color.GREEN));
             dataMap.add(new PartBean(0.2d, Color.YELLOW));
             dataMap.add(new PartBean(0.35d, Color.RED));
             dataMap.add(new PartBean(0.25d, Color.BLUE));

             Log.d(TAG,"dataMap"+dataMap.size());
             pieChartView.initData(dataMap);
         }
     });


    }





}
