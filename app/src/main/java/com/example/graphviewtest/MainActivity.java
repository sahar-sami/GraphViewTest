package com.example.graphviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements Runnable{
   public static LineGraphSeries<DataPoint> s1;
   public static LineGraphSeries<DataPoint> s2;
   public static LineGraphSeries<DataPoint> s3;
   private static final Random RANDOM = new Random();
   private static int lastX = 0;
   private GraphView tempGraph;
   private GraphView phGraph;
   private GraphView turbGraph;
   public static ArrayList<Double> tempArray;
   public static ArrayList<Double> phArray;
   public static ArrayList<Double> turbArray;
   //private final Handler handler = new Handler();
  // private Runnable runnable;
   private Thread worker;
   private final AtomicBoolean running = new AtomicBoolean(false);

  // boolean suspended = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempGraph = (GraphView) findViewById(R.id.graph);
        phGraph = (GraphView) findViewById(R.id.graph2);
        turbGraph = (GraphView) findViewById(R.id.graph3);
        s1 = new LineGraphSeries<DataPoint>();
        s2 = new LineGraphSeries<DataPoint>();
        s3 = new LineGraphSeries<DataPoint>();
        tempGraph.addSeries(s1);
        phGraph.addSeries(s2);
        turbGraph.addSeries(s3);
        tempArray = new ArrayList<Double>();
        phArray = new ArrayList<Double>();
        turbArray = new ArrayList<Double>();
        Viewport viewport1 = tempGraph.getViewport();
        viewport1.setXAxisBoundsManual(true);
        viewport1.setMinX(0);
        viewport1.setMaxX(4);
        Viewport viewport2 = phGraph.getViewport();
        viewport2.setXAxisBoundsManual(true);
        viewport2.setMinX(0);
        viewport2.setMaxX(4);
        Viewport viewport3 = turbGraph.getViewport();
        viewport3.setXAxisBoundsManual(true);
        viewport3.setMinX(0);
        viewport3.setMaxX(4);
        lastX = 0;
    }



    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        start();

       /**
        *  t = new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        });
        t.start();*/
    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        tempArray.add(RANDOM.nextDouble() * 10d);
        phArray.add(RANDOM.nextDouble() * 10d);
        turbArray.add(RANDOM.nextDouble() * 10d);
        s1.appendData(new DataPoint(lastX, tempArray.get(lastX)),true, 10000);
        s2.appendData(new DataPoint(lastX, phArray.get(lastX)), true, 10000);
        s3.appendData(new DataPoint(lastX, turbArray.get(lastX)), true, 10000);
        lastX++;
    }

    //just in case
    public void clearGraphs(){
        tempGraph.removeAllSeries();
        phGraph.removeAllSeries();
        turbGraph.removeAllSeries();
    }

    public void sendMessage(View view) {
        stop();
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
        finish();
    }

    //just in case
    private DataPoint[] blankData() {
        int count = tempArray.size();
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            DataPoint v = new DataPoint(0, 0);
            values[i] = v;
        }
        return values;
    }

    static ArrayList<Double> getTempArray(){
        return tempArray;
    }

    static ArrayList<Double> getPhArray(){
        return phArray;
    }

    static ArrayList<Double> getTurbArray(){
        return turbArray;
    }

    static LineGraphSeries<DataPoint> getS1(){
        return s1;
    }

    static LineGraphSeries<DataPoint> getS2(){
        return s2;
    }
    static LineGraphSeries<DataPoint> getS3(){
        return s3;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(
                        "Thread was interrupted, Failed to complete operation");
            }
            addEntry();
        }
    }
}

