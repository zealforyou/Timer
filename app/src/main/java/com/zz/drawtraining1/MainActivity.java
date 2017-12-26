package com.zz.drawtraining1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TimeView timeView;
    private EditText et_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.timeView);
        et_time = findViewById(R.id.et_time);
    }

    public void click(View view) {
        String s = et_time.getText().toString();
        int time = 0;
        try {
            time = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (time == 0) {
            Toast.makeText(this,"时间不能为0", Toast.LENGTH_SHORT).show();
            return;
        }
        timeView.start(time);
    }
}
