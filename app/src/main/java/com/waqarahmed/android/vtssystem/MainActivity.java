package com.waqarahmed.android.vtssystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    ProgressBar spinner;

    boolean act_selector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, Form.class);
        
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        maintask task = new maintask();
        task.execute();


    }



    public class maintask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {
            try {

                Thread.sleep(1500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            spinner.setVisibility(View.GONE);
            startActivity(intent);

        }

    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
